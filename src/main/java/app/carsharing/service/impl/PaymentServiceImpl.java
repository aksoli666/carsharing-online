package app.carsharing.service.impl;

import app.carsharing.dto.PaymentDto;
import app.carsharing.dto.request.PaymentRequestDto;
import app.carsharing.dto.responce.PaymentResponseDto;
import app.carsharing.exception.EntityNotFoundException;
import app.carsharing.exception.NotificationException;
import app.carsharing.mapper.PaymentMapper;
import app.carsharing.model.Payment;
import app.carsharing.model.Rental;
import app.carsharing.notification.NotificationService;
import app.carsharing.repository.PaymentRepository;
import app.carsharing.repository.RentalRepository;
import app.carsharing.security.CustomUserDetailsService;
import app.carsharing.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final String currency = "usd";

    private final CustomUserDetailsService customUserDetailsService;
    private final NotificationService notificationService;
    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final PaymentMapper paymentMapper;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;
    @Value("${payment.success.url}")
    private String successUrl;
    @Value("${payment.cancel.url}")
    private String cancelUrl;

    @Override
    public PaymentDto getById(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Payment don`t found by id: " + id));
        return paymentMapper.toPaymentDto(payment);
    }

    @Override
    public Page<PaymentDto> getPayments(Authentication authentication, Pageable pageable) {
        Long userId = customUserDetailsService.getUserIdFromAuthentication(authentication);
        Page<Payment> payments = paymentRepository.findByRentalUserIdFetchRental(userId, pageable);
        return paymentMapper.toPaymentDtoPage(payments);
    }

    @Transactional
    @Override
    public PaymentResponseDto createPaymentSession(Authentication authentication,
                                                   PaymentRequestDto dto) {
        Long userId = customUserDetailsService.getUserIdFromAuthentication(authentication);
        Rental rental = rentalRepository.findByIdAndUserId(dto.rentalId(), userId).orElseThrow(
                () -> new EntityNotFoundException("Rental don`t found by userId: " + userId));

        BigDecimal amount = calculateAmount(rental, dto.paymentType());
        Stripe.apiKey = stripeSecretKey;
        SessionCreateParams sessionParams = createSessionParams(amount);
        Session session = null;
        try {
            session = Session.create(sessionParams);
        } catch (StripeException e) {
            throw new RuntimeException("Can`t create Stripe Session!");
        }
        Payment payment = new Payment();
        payment.setStatus(Payment.Status.PENDING);
        payment.setType(dto.paymentType());
        payment.setRental(rental);
        payment.setSessionUrl(session.getUrl());
        payment.setSessionId(session.getId());
        payment.setAmount(amount);
        return paymentMapper.toPaymentResponseDto(paymentRepository.save(payment));
    }

    @Override
    public void paymentSuccess(String sessionId) throws NotificationException {
        Payment payment = paymentRepository.findBySessionId(sessionId).orElseThrow(
                () -> new EntityNotFoundException("Payment not found by sessionId: " + sessionId));

        if (isPaymentSessionPaid(sessionId)) {
            payment.setStatus(Payment.Status.PAID);
            paymentRepository.save(payment);
            notificationService.sendPaymentSuccessNotification(payment);
        } else {
            throw new RuntimeException("Payment was not successful for sessionId: " + sessionId);
        }
    }

    @Override
    public void paymentCancel(String sessionId) throws NotificationException {
        Payment payment = paymentRepository.findBySessionId(sessionId).orElseThrow(
                () -> new EntityNotFoundException("Payment not found by sessionId: " + sessionId));

        if (payment.getStatus().equals(Payment.Status.PENDING)) {
            notificationService.sendPaymentCancelNotification(payment);
        }
    }

    private BigDecimal calculateAmount(Rental rental, Payment.Type paymentType) {
        long days = ChronoUnit.DAYS.between(rental.getRentalDate(), rental.getActualReturnDate());
        BigDecimal amount = rental.getCar().getDaileFee().multiply(BigDecimal.valueOf(days));
        if (paymentType == Payment.Type.FINE) {
            long overdue = ChronoUnit.DAYS.between(
                    rental.getReturnDate(), rental.getActualReturnDate());
            amount = amount.multiply(BigDecimal.valueOf(overdue));
        }
        return amount;
    }

    private SessionCreateParams createSessionParams(BigDecimal amount) {
        return SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(currency)
                                                .setUnitAmount(amount
                                                        .multiply(BigDecimal.valueOf(100))
                                                        .longValue())
                                                .setProductData(
                                                        SessionCreateParams
                                                                .LineItem
                                                                .PriceData
                                                                .ProductData
                                                                .builder()
                                                                .setName("Car Rental Payment")
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();
    }

    private boolean isPaymentSessionPaid(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            return "paid".equals(session.getPaymentStatus());
        } catch (StripeException e) {
            throw new RuntimeException("Error retrieving Stripe session: " + sessionId, e);
        }
    }
}

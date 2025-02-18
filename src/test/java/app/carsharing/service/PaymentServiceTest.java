package app.carsharing.service;

import static app.carsharing.util.ConstantUtil.COUNT_CONTENT_1;
import static app.carsharing.util.ConstantUtil.ID_1L_CORRECT;
import static app.carsharing.util.ConstantUtil.INCORRECT_ID;
import static app.carsharing.util.ConstantUtil.pageable;
import static app.carsharing.util.EntityAndDtoMaker.createCar1L;
import static app.carsharing.util.EntityAndDtoMaker.createPayment;
import static app.carsharing.util.EntityAndDtoMaker.createPaymentDto;
import static app.carsharing.util.EntityAndDtoMaker.createPaymentRequestDto;
import static app.carsharing.util.EntityAndDtoMaker.createPaymentResponseDto;
import static app.carsharing.util.EntityAndDtoMaker.createRental;
import static app.carsharing.util.EntityAndDtoMaker.createUser1L;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import app.carsharing.dto.PaymentDto;
import app.carsharing.dto.request.PaymentRequestDto;
import app.carsharing.dto.responce.PaymentResponseDto;
import app.carsharing.exception.EntityNotFoundException;
import app.carsharing.mapper.PaymentMapper;
import app.carsharing.model.Car;
import app.carsharing.model.Payment;
import app.carsharing.model.Rental;
import app.carsharing.model.User;
import app.carsharing.repository.PaymentRepository;
import app.carsharing.repository.RentalRepository;
import app.carsharing.security.CustomUserDetailsService;
import app.carsharing.service.impl.PaymentServiceImpl;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @InjectMocks
    private PaymentServiceImpl paymentService;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private Authentication authentication;

    @Test
    @DisplayName("""
            Verify #getById(), return PaymentDto
            """)
    public void getById_validId_returnPaymentDto() {
        Payment payment = createPayment();
        PaymentDto expected = createPaymentDto();

        when(paymentRepository.findById(ID_1L_CORRECT)).thenReturn(Optional.of(payment));
        when(paymentMapper.toPaymentDto(payment)).thenReturn(expected);

        PaymentDto actual = paymentService.getById(ID_1L_CORRECT);
        assertEquals(expected, actual);
        verify(paymentRepository).findById(ID_1L_CORRECT);
        verify(paymentMapper).toPaymentDto(payment);
    }

    @Test
    @DisplayName("""
            Verify #findById(), throw EntityNotFoundException
            """)
    public void findById_invalidId_throwEntityNotFoundException() {
        when(paymentRepository.findById(INCORRECT_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> paymentService.getById(INCORRECT_ID));

        assertEquals("Payment don`t found by id: " + INCORRECT_ID, exception.getMessage());
    }

    @Test
    @DisplayName("""
            Verify #getPayments(), return Page<PaymentDto>
            """)
    public void getPayments_validAuthentication_returnPagePaymentDto() {
        User user = createUser1L();
        Payment payment = createPayment();
        PaymentDto dto = createPaymentDto();
        Page<Payment> payments = new PageImpl<>(List.of(payment), pageable, COUNT_CONTENT_1);
        Page<PaymentDto> expected = new PageImpl<>(List.of(dto), pageable, COUNT_CONTENT_1);

        when(customUserDetailsService.getUserIdFromAuthentication(authentication)).thenReturn(user.getId());
        when(paymentRepository.findByRentalUserIdFetchRental(ID_1L_CORRECT, pageable)).thenReturn(payments);
        when(paymentMapper.toPaymentDtoPage(payments)).thenReturn(expected);

        Page<PaymentDto> actual = paymentService.getPayments(authentication, pageable);
        assertEquals(expected, actual);
        verify(customUserDetailsService).getUserIdFromAuthentication(authentication);
        verify(paymentRepository).findByRentalUserIdFetchRental(ID_1L_CORRECT, pageable);
        verify(paymentMapper).toPaymentDtoPage(payments);
    }

    @Test
    @DisplayName("""
        Verify #createPaymentSession(), return PaymentResponseDto
        """)
    void createPaymentSession_validAuthentication_success() throws StripeException {
        PaymentRequestDto requestDto = createPaymentRequestDto();
        User user = createUser1L();
        Rental rental = createRental();
        Car car = createCar1L();
        rental.setUser(user);
        rental.setCar(car);
        BigDecimal amount = BigDecimal.valueOf(100.00);
        PaymentResponseDto expectedResponse = createPaymentResponseDto();
        Payment payment = new Payment();
        payment.setStatus(Payment.Status.PENDING);
        payment.setType(requestDto.paymentType());
        payment.setRental(rental);
        payment.setAmount(amount);
        payment.setSessionUrl("https://example.com/success");
        payment.setSessionId("session_id");

        Session mockSession = mock(Session.class);
        when(mockSession.getUrl()).thenReturn("https://example.com/success");
        when(mockSession.getId()).thenReturn("session_id");
        mockStatic(Session.class);
        when(Session.create(any(SessionCreateParams.class))).thenReturn(mockSession);
        when(customUserDetailsService.getUserIdFromAuthentication(authentication)).thenReturn(user.getId());
        when(rentalRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(rental));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentMapper.toPaymentResponseDto(payment)).thenReturn(expectedResponse);

        PaymentResponseDto actualResponse = paymentService.createPaymentSession(authentication, requestDto);
        assertEquals(expectedResponse, actualResponse);
        verify(rentalRepository).findByIdAndUserId(1L, 1L);
        verify(paymentRepository).save(any(Payment.class));
        verify(paymentMapper).toPaymentResponseDto(payment);
    }
}

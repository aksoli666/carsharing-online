package app.carsharing.service;

import app.carsharing.dto.PaymentDto;
import app.carsharing.dto.request.PaymentRequestDto;
import app.carsharing.dto.responce.PaymentResponseDto;
import app.carsharing.exception.NotificationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface PaymentService {
    PaymentDto getById(Long id);

    Page<PaymentDto> getPayments(Authentication authentication,
                                 Pageable pageable);

    PaymentResponseDto createPaymentSession(Authentication authentication, PaymentRequestDto dto);

    void paymentSuccess(String sessionId) throws NotificationException;

    void paymentCancel(String sessionId) throws NotificationException;
}

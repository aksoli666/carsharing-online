package app.carsharing.controller;

import app.carsharing.dto.PaymentDto;
import app.carsharing.dto.request.PaymentRequestDto;
import app.carsharing.dto.responce.PaymentResponseDto;
import app.carsharing.exception.NotificationException;
import app.carsharing.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payment Controller", description = "Controller to manage payment-related operations.")
@RestController
@RequestMapping(value = "/payments", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(
            summary = "Retrieve payment by ID",
            description = "Fetches the details of a payment using its unique identifier. "
                    + "Accessible only to users with the 'ROLE_MANAGER' role."
    )
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/{id}")
    public PaymentDto getById(@Positive @PathVariable Long id) {
        return paymentService.getById(id);
    }

    @Operation(
            summary = "Get all payments for a customer",
            description = "Retrieves a paginated list of payments for the authenticated customer. "
                    + "Accessible only to users with the 'ROLE_CUSTOMER' role."
    )
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping
    public Page<PaymentDto> getPayments(Authentication authentication,
                                        Pageable pageable) {
        return paymentService.getPayments(authentication, pageable);
    }

    @Operation(
            summary = "Create a new payment session",
            description = "Initiates a new payment session for the authenticated customer. "
                    + "Accessible only to users with the 'ROLE_CUSTOMER' role."
    )
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PaymentResponseDto createPaymentSession(Authentication authentication,
                                                   @Valid @RequestBody PaymentRequestDto dto) {
        return paymentService.createPaymentSession(authentication, dto);
    }

    @Operation(
            summary = "Handle payment success",
            description = "Processes a successful payment session using the session ID. "
                    + "Accessible only to users with the 'ROLE_CUSTOMER' role."
    )
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/success")
    public void paymentSuccess(@RequestParam("session_id") String sessionId)
            throws NotificationException {
        paymentService.paymentSuccess(sessionId);
    }

    @Operation(
            summary = "Handle payment success",
            description = "Processes a successful payment session using the session ID. "
                    + "Accessible only to users with the 'ROLE_CUSTOMER' role."
    )
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/cancel")
    public void paymentCancel(@RequestParam("session_id") String sessionId)
            throws NotificationException {
        paymentService.paymentCancel(sessionId);
    }
}

package app.carsharing.service;

import app.carsharing.dto.RentalDto;
import app.carsharing.dto.request.RentalAddRequestDto;
import app.carsharing.dto.responce.RentalAddResponseDto;
import app.carsharing.exception.NotificationException;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface RentalService {
    RentalAddResponseDto addRental(Authentication authentication,
                                   RentalAddRequestDto dto) throws NotificationException;

    Page<RentalDto> rentalHistory(Authentication authentication,
                                  boolean isActive, Pageable pageable);

    RentalDto getById(Authentication authentication, Long id);

    RentalDto setActualReturnDate(Long id, LocalDate actualReturnDate) throws NotificationException;
}

package app.carsharing.controller;

import app.carsharing.dto.RentalDto;
import app.carsharing.dto.request.RentalAddRequestDto;
import app.carsharing.dto.responce.RentalAddResponseDto;
import app.carsharing.exception.NotificationException;
import app.carsharing.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Rental Management",
        description = "Handles all operations related to car rentals, "
                + "including rental creation, rental history, and return date updates")
@RestController
@RequestMapping(value = "/rentals", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @Operation(
            summary = "Create a new rental",
            description = "This endpoint allows a customer to create a new rental "
                    + "for a car by specifying rental details such as the "
                    + "car ID, start date, and end date."
    )
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public RentalAddResponseDto addRental(Authentication authentication,
                                          @Valid @RequestBody RentalAddRequestDto dto)
            throws NotificationException {
        return rentalService.addRental(authentication, dto);
    }

    @Operation(
            summary = "Get rental history",
            description = "This endpoint allows a customer to fetch their rental history, "
                    + "filtered by whether the rental is active or not, and paginated."
    )
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping
    public Page<RentalDto> rentalHistory(Authentication authentication,
                                         @RequestParam("is_active") boolean isActive,
                                         Pageable pageable) {
        return rentalService.rentalHistory(authentication, isActive, pageable);
    }

    @Operation(
            summary = "Get rental details by ID",
            description = "This endpoint allows a customer "
                    + "to retrieve detailed information about a specific rental by its ID."
    )
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/{id}")
    public RentalDto getById(Authentication authentication,
                             @Positive @PathVariable Long id) {
        return rentalService.getById(authentication, id);
    }

    @Operation(
            summary = "Set actual return date for a rental",
            description = "This endpoint allows a manager to set the actual return date "
                    + "for a rental, marking the car as returned and updating its availability."
    )
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping("/{id}")
    public RentalDto setActualReturnDate(@Positive @PathVariable Long id,
                                         @RequestParam("actual_return_date")
                                         LocalDate actualReturnDate)
            throws NotificationException {
        return rentalService.setActualReturnDate(id, actualReturnDate);
    }
}

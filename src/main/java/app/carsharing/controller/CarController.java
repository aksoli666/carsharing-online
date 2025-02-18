package app.carsharing.controller;

import app.carsharing.dto.CarDto;
import app.carsharing.service.CarService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Car management", description = "Endpoints for managing cars")
@RestController
@RequestMapping(value = "/cars", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @Operation(
            summary = "Create a new car",
            description = "Allows a manager to create a new car entry. "
                    + "The car details (e.g., model, daily fee, availability status) "
                    + "must be provided in the request body. "
                    + "This endpoint is restricted to users with the ROLE_MANAGER authority. "
                    + "Returns the created car's details."
    )
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CarDto create(@Valid @RequestBody CarDto dto) {
        return carService.create(dto);
    }

    @Operation(
            summary = "Retrieve car details by ID",
            description = "Fetches the details of a car by its unique identifier (ID). "
                    + "This endpoint is accessible to customers and provides the car's details, "
                    + "including model, daily fee, and availability."
    )
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/{id}")
    public CarDto getById(@Positive @PathVariable Long id) {
        return carService.getById(id);
    }

    @Operation(
            summary = "Retrieve all cars with pagination",
            description = "Retrieves a paginated list of all cars in the system. "
                    + "Allows customers to browse available cars. "
                    + "Supports pagination via query parameters (e.g., page, size, sort)."
    )
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping
    public Page<CarDto> getAll(Pageable pageable) {
        return carService.getAll(pageable);
    }

    @Operation(
            summary = "Update car details",
            description = "Allows a manager to update the details of an existing car. "
                    + "The car's unique identifier (ID) and updated details must be provided. "
                    + "Only users with the ROLE_MANAGER authority can access this endpoint."
    )
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping("/{id}")
    public CarDto update(@Positive @PathVariable Long id,
                         @Valid @RequestBody CarDto dto) {
        return carService.update(id, dto);
    }

    @Operation(
            summary = "Delete a car by ID",
            description = "Allows a manager to delete a car by its unique identifier (ID). "
                    + "The car is removed from the system. "
                    + "This operation is restricted to users with the ROLE_MANAGER authority."
    )
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@Positive @PathVariable Long id) {
        carService.delete(id);
    }
}

package app.carsharing.service.impl;

import app.carsharing.dto.RentalDto;
import app.carsharing.dto.request.RentalAddRequestDto;
import app.carsharing.dto.responce.RentalAddResponseDto;
import app.carsharing.exception.CarOutOfStockException;
import app.carsharing.exception.EntityNotFoundException;
import app.carsharing.exception.NotificationException;
import app.carsharing.exception.RentalAlreadyReturnedException;
import app.carsharing.mapper.RentalMapper;
import app.carsharing.model.Car;
import app.carsharing.model.Rental;
import app.carsharing.model.User;
import app.carsharing.notification.NotificationService;
import app.carsharing.repository.CarRepository;
import app.carsharing.repository.RentalRepository;
import app.carsharing.security.CustomUserDetailsService;
import app.carsharing.service.RentalService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final CustomUserDetailsService userDetailsService;
    private final NotificationService notificationService;
    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final RentalMapper rentalMapper;

    @Transactional
    @Override
    public RentalAddResponseDto addRental(Authentication authentication,
                                          RentalAddRequestDto dto) throws NotificationException {
        Long carId = dto.getCarId();
        Car car = carRepository.findById(carId).orElseThrow(
                () -> new EntityNotFoundException("Car not found by id: " + carId));
        User current = userDetailsService.getUserFromAuthentication(authentication);
        Rental rental = rentalMapper.toRental(dto);
        rental.setUser(current);
        rental.setCar(car);
        updateCarInventory(car, -1);
        Rental saved = rentalRepository.save(rental);
        notificationService.sendRentalCreationNotification(saved);
        return rentalMapper.toRentalAddResponseDto(saved);
    }

    @Override
    public Page<RentalDto> rentalHistory(Authentication authentication,
                                         boolean isActive,
                                         Pageable pageable) {
        Long userId = userDetailsService.getUserIdFromAuthentication(authentication);
        Page<Rental> rentals = isActive
                ? rentalRepository.findByUserIdAndActualReturnDateIsNull(userId, pageable)
                : rentalRepository.findByUserIdAndActualReturnDateIsNotNull(userId, pageable);
        return rentalMapper.toRentalDtoPage(rentals);
    }

    @Override
    public RentalDto getById(Authentication authentication, Long id) {
        Long userId = userDetailsService.getUserIdFromAuthentication(authentication);
        Rental rental = rentalRepository.findByIdAndUserId(id, userId).orElseThrow(
                () -> new EntityNotFoundException("Rental not found by id: " + id));
        return rentalMapper.toRentalDto(rental);
    }

    @Transactional
    @Override
    public RentalDto setActualReturnDate(Long id, LocalDate actualReturnDate)
            throws NotificationException {
        Rental rental = rentalRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Rental not found by id: " + id));
        if (rental.getActualReturnDate() != null) {
            throw new RentalAlreadyReturnedException("Rental already returned");
        }
        Car car = rental.getCar();
        updateCarInventory(car, 1);
        rental.setActualReturnDate(actualReturnDate);
        Rental saved = rentalRepository.save(rental);
        notificationService.sendRentalReturnNotification(saved);
        return rentalMapper.toRentalDto(saved);
    }

    private void updateCarInventory(Car car, int adjustment) {
        int updatedInventory = car.getInventory() + adjustment;
        if (updatedInventory < 0) {
            throw new CarOutOfStockException("Car with id " + car.getId() + " is out of stock.");
        }
        car.setInventory(updatedInventory);
        carRepository.save(car);
    }
}

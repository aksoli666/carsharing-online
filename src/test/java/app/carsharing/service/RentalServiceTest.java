package app.carsharing.service;

import static app.carsharing.util.ConstantUtil.COUNT_CONTENT_1;
import static app.carsharing.util.ConstantUtil.ID_1L_CORRECT;
import static app.carsharing.util.ConstantUtil.INCORRECT_ID;
import static app.carsharing.util.ConstantUtil.IS_ACTIVE;
import static app.carsharing.util.ConstantUtil.NOT_ACTIVE;
import static app.carsharing.util.ConstantUtil.pageable;
import static app.carsharing.util.EntityAndDtoMaker.createCar1L;
import static app.carsharing.util.EntityAndDtoMaker.createRental;
import static app.carsharing.util.EntityAndDtoMaker.createRentalAddRequestDto;
import static app.carsharing.util.EntityAndDtoMaker.createRentalAddResponseDto;
import static app.carsharing.util.EntityAndDtoMaker.createRentalDtoIsActive;
import static app.carsharing.util.EntityAndDtoMaker.createRentalDtoNotActive;
import static app.carsharing.util.EntityAndDtoMaker.createUser1L;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import app.carsharing.dto.RentalDto;
import app.carsharing.dto.request.RentalAddRequestDto;
import app.carsharing.dto.responce.RentalAddResponseDto;
import app.carsharing.exception.EntityNotFoundException;
import app.carsharing.exception.NotificationException;
import app.carsharing.mapper.RentalMapper;
import app.carsharing.model.Car;
import app.carsharing.model.Rental;
import app.carsharing.model.User;
import app.carsharing.notification.TelegramNotificationService;
import app.carsharing.repository.CarRepository;
import app.carsharing.repository.RentalRepository;
import app.carsharing.security.CustomUserDetailsService;
import app.carsharing.service.impl.RentalServiceImpl;
import java.time.LocalDate;
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
public class RentalServiceTest {
    @InjectMocks
    private RentalServiceImpl rentalService;
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private RentalMapper rentalMapper;
    @Mock
    private TelegramNotificationService notificationService;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private Authentication authentication;

    @Test
    @DisplayName("""
            Verify #addRental(), return RentalAddResponseDto
            """)
    public void addRental_validAuthenticationAndDto() throws NotificationException {
        RentalAddRequestDto dto = createRentalAddRequestDto();
        Car car = createCar1L();
        User user = createUser1L();
        Rental rental = createRental();
        RentalAddResponseDto expected = createRentalAddResponseDto();

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        when(customUserDetailsService.getUserFromAuthentication(authentication)).thenReturn(user);
        when(rentalMapper.toRental(dto)).thenReturn(rental);
        when(rentalRepository.save(rental)).thenReturn(rental);
        when(rentalMapper.toRentalAddResponseDto(rental)).thenReturn(expected);

        RentalAddResponseDto actual = rentalService.addRental(authentication, dto);
        assertEquals(expected, actual);
        verify(carRepository).findById(car.getId());
        verify(customUserDetailsService).getUserFromAuthentication(authentication);
        verify(rentalMapper).toRental(dto);
        verify(rentalRepository).save(rental);
        verify(rentalMapper).toRentalAddResponseDto(rental);
    }

    @Test
    @DisplayName("""
            Verify #addRental(), return RentalAddResponseDto
            """)
    public void addRental_invalidCarIdRequestDto() {
        RentalAddRequestDto dto = createRentalAddRequestDto();
        dto.setCarId(INCORRECT_ID);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> rentalService.addRental(authentication, dto));

        assertEquals("Car not found by id: " + INCORRECT_ID, exception.getMessage());
    }

    @Test
    @DisplayName("""
            Verify #rentalHistory(), is_active = true, return Page<RentalDto>
            """)
    public void rentalHistory_validAuthenticationAndIsActive_returnPageRentalDto() {
        User user = createUser1L();
        Rental rental = createRental();
        RentalDto dto = createRentalDtoIsActive();
        Page<Rental> rentals = new PageImpl<>(List.of(rental), pageable, COUNT_CONTENT_1);
        Page<RentalDto> expected = new PageImpl<>(List.of(dto), pageable, COUNT_CONTENT_1);

        when(customUserDetailsService.getUserIdFromAuthentication(authentication)).thenReturn(user.getId());
        when(rentalRepository.findByUserIdAndActualReturnDateIsNull(ID_1L_CORRECT, pageable))
                .thenReturn(rentals);
        when(rentalMapper.toRentalDtoPage(rentals)).thenReturn(expected);

        Page<RentalDto> actual = rentalService.rentalHistory(authentication, IS_ACTIVE, pageable);
        assertEquals(expected, actual);
        verify(customUserDetailsService).getUserIdFromAuthentication(authentication);
        verify(rentalRepository).findByUserIdAndActualReturnDateIsNull(ID_1L_CORRECT, pageable);
        verify(rentalMapper).toRentalDtoPage(rentals);

    }

    @Test
    @DisplayName("""
            Verify #rentalHistory(), is_active = false, return Page<RentalDto>
            """)
    public void rentalHistory_validAuthenticationAndNotActive_returnPageRentalDto() {
        User user = createUser1L();
        Rental rental = createRental();
        RentalDto dto = createRentalDtoNotActive();
        Page<Rental> rentals = new PageImpl<>(List.of(rental), pageable, COUNT_CONTENT_1);
        Page<RentalDto> expected = new PageImpl<>(List.of(dto), pageable, COUNT_CONTENT_1);

        when(customUserDetailsService.getUserIdFromAuthentication(authentication)).thenReturn(user.getId());
        when(rentalRepository.findByUserIdAndActualReturnDateIsNotNull(ID_1L_CORRECT, pageable))
                .thenReturn(rentals);
        when(rentalMapper.toRentalDtoPage(rentals)).thenReturn(expected);

        Page<RentalDto> actual = rentalService.rentalHistory(authentication, NOT_ACTIVE, pageable);
        assertEquals(expected, actual);
        verify(customUserDetailsService).getUserIdFromAuthentication(authentication);
        verify(rentalRepository).findByUserIdAndActualReturnDateIsNotNull(ID_1L_CORRECT, pageable);
        verify(rentalMapper).toRentalDtoPage(rentals);
    }

    @Test
    @DisplayName("""
            Verify #getById(), return RentalDto
            """)
    public void getById_validAuthenticationAndId() {
        User user = createUser1L();
        Rental rental = createRental();
        RentalDto expected = createRentalDtoNotActive();

        when(customUserDetailsService.getUserIdFromAuthentication(authentication)).thenReturn(user.getId());
        when(rentalRepository.findByIdAndUserId(ID_1L_CORRECT, ID_1L_CORRECT))
                .thenReturn(Optional.of(rental));
        when(rentalMapper.toRentalDto(rental)).thenReturn(expected);

        RentalDto actual = rentalService.getById(authentication, ID_1L_CORRECT);
        assertEquals(expected, actual);
        verify(customUserDetailsService).getUserIdFromAuthentication(authentication);
        verify(rentalRepository).findByIdAndUserId(ID_1L_CORRECT, ID_1L_CORRECT);
        verify(rentalMapper).toRentalDto(rental);
    }

    @Test
    @DisplayName("""
            Verify #getById(), throw EntityNotFoundException
            """)
    public void getById_invalidId_throwEntityNotFoundException() {
        User user = createUser1L();

        when(customUserDetailsService.getUserIdFromAuthentication(authentication)).thenReturn(user.getId());
        when(rentalRepository.findByIdAndUserId(INCORRECT_ID, ID_1L_CORRECT))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> rentalService.getById(authentication, INCORRECT_ID));
        assertEquals("Rental not found by id: " + INCORRECT_ID, exception.getMessage());
    }

    @Test
    @DisplayName("""
            Verify #setActualReturnDate(), return RentalDto
            """)
    public void setActualReturnDate_validId_returnRentalDto() throws NotificationException {
        Rental rental = createRental();
        rental.setActualReturnDate(null);
        rental.setCar(createCar1L());
        RentalDto expected = createRentalDtoNotActive();

        when(rentalRepository.findById(ID_1L_CORRECT)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(rental)).thenReturn(rental);
        when(rentalMapper.toRentalDto(rental)).thenReturn(expected);

        RentalDto actual = rentalService
                .setActualReturnDate(ID_1L_CORRECT, LocalDate.now().plusDays(1));
        assertEquals(expected, actual);
        verify(rentalRepository).findById(ID_1L_CORRECT);
        verify(rentalRepository).save(rental);
        verify(rentalMapper).toRentalDto(rental);
    }

    @Test
    @DisplayName("""
            Verify #setActualReturnDate(), throw EntityNotFoundException
            """)
    public void setActualReturnDate_invalidId_throwEntityNotFoundException() {
        when(rentalRepository.findById(INCORRECT_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> rentalService.setActualReturnDate(
                        INCORRECT_ID, LocalDate.now().plusDays(1)));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> rentalService.setActualReturnDate(INCORRECT_ID, LocalDate.now().plusDays(1)));
        assertEquals("Rental not found by id: " + INCORRECT_ID, exception.getMessage());
    }
}

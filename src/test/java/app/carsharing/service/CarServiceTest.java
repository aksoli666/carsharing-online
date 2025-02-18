package app.carsharing.service;

import static app.carsharing.util.ConstantUtil.COUNT_CONTENT_1;
import static app.carsharing.util.ConstantUtil.ID_1L_CORRECT;
import static app.carsharing.util.ConstantUtil.INCORRECT_ID;
import static app.carsharing.util.ConstantUtil.pageable;
import static app.carsharing.util.EntityAndDtoMaker.createCar1L;
import static app.carsharing.util.EntityAndDtoMaker.createCarDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import app.carsharing.dto.CarDto;
import app.carsharing.exception.EntityNotFoundException;
import app.carsharing.mapper.CarMapper;
import app.carsharing.model.Car;
import app.carsharing.repository.CarRepository;
import app.carsharing.service.impl.CarServiceImpl;
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

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {
    @InjectMocks
    private CarServiceImpl carService;
    @Mock
    private CarRepository carRepository;
    @Mock
    private CarMapper carMapper;

    @Test
    @DisplayName("""
            Verify #create(), return CarDto
            """)
    public void create_validCarDto_returnCarDto() {
        CarDto expected = createCarDto();
        Car car = createCar1L();

        when(carMapper.toCar(expected)).thenReturn(car);
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toCarDto(car)).thenReturn(expected);

        CarDto actual = carService.create(expected);
        assertEquals(expected, actual);
        verify(carMapper).toCar(expected);
        verify(carRepository).save(car);
        verify(carMapper).toCarDto(car);
    }

    @Test
    @DisplayName("""
            Verify #getById(), return CarDto
            """)
    public void getById_validId_returnCarDto() {
        CarDto expected = createCarDto();
        Car car = createCar1L();

        when(carRepository.findById(ID_1L_CORRECT)).thenReturn(Optional.of(car));
        when(carMapper.toCarDto(car)).thenReturn(expected);

        CarDto actual = carService.getById(ID_1L_CORRECT);
        assertEquals(expected, actual);
        verify(carRepository).findById(ID_1L_CORRECT);
        verify(carMapper).toCarDto(car);
    }

    @Test
    @DisplayName("""
            Verify #getById(), throw EntityNotFoundException
            """)
    public void getById_invalidId_throwEntityNotFoundException() {
        when(carRepository.findById(INCORRECT_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> carService.getById(INCORRECT_ID));

        assertEquals("Car not found by id: " + INCORRECT_ID, exception.getMessage());
    }

    @Test
    @DisplayName("""
            Verify #getAll(), return Page<CarDto>
            """)
    public void getAll_validAll_returnPageCarDto() {
        Car car = createCar1L();
        CarDto dto = createCarDto();
        Page<Car> cars = new PageImpl<>(List.of(car), pageable, COUNT_CONTENT_1);
        Page<CarDto> expected = new PageImpl<>(List.of(dto), pageable, COUNT_CONTENT_1);

        when(carRepository.findAll(pageable)).thenReturn(cars);
        when(carMapper.toCarDtoPage(cars)).thenReturn(expected);

        Page<CarDto> actual = carService.getAll(pageable);
        assertEquals(expected, actual);
        verify(carRepository).findAll(pageable);
        verify(carMapper).toCarDtoPage(cars);
    }

    @Test
    @DisplayName("""
            Verify #update(), return CarDto
            """)
    public void update_validCarDto_returnCarDto() {
        Car car = createCar1L();
        CarDto expected = createCarDto();

        when(carRepository.findById(ID_1L_CORRECT)).thenReturn(Optional.of(car));
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toCarDto(car)).thenReturn(expected);

        CarDto actual = carService.update(car.getId(), expected);
        assertEquals(expected, actual);
        verify(carRepository).findById(ID_1L_CORRECT);
        verify(carRepository).save(car);
        verify(carMapper).toCarDto(car);
    }

    @Test
    @DisplayName("""
            Verify #update(), throw EntityNotFoundException
            """)
    public void update_invalidId_throwEntityNotFoundException() {
        when(carRepository.findById(INCORRECT_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> carService.update(INCORRECT_ID, null));

        assertEquals("Car not found by id: " + INCORRECT_ID, exception.getMessage());
    }

    @Test
    @DisplayName("""
            Verify #delete(), void
            """)
    public void delete_validId_returnNothing() {
        carService.delete(ID_1L_CORRECT);
        verify(carRepository).deleteById(ID_1L_CORRECT);
    }
}

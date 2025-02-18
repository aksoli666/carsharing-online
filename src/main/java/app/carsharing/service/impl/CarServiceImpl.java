package app.carsharing.service.impl;

import app.carsharing.dto.CarDto;
import app.carsharing.exception.EntityNotFoundException;
import app.carsharing.mapper.CarMapper;
import app.carsharing.model.Car;
import app.carsharing.repository.CarRepository;
import app.carsharing.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Transactional
    @Override
    public CarDto create(CarDto dto) {
        Car car = carMapper.toCar(dto);
        return carMapper.toCarDto(carRepository.save(car));
    }

    @Override
    public CarDto getById(Long id) {
        Car car = carRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Car not found by id: " + id));
        return carMapper.toCarDto(car);
    }

    @Override
    public Page<CarDto> getAll(Pageable pageable) {
        return carMapper.toCarDtoPage(carRepository.findAll(pageable));
    }

    @Transactional
    @Override
    public CarDto update(Long id, CarDto dto) {
        Car car = carRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Car not found by id: " + id));
        carMapper.updateCar(dto, car);
        return carMapper.toCarDto(carRepository.save(car));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        carRepository.deleteById(id);
    }
}

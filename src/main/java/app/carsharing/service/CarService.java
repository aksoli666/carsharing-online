package app.carsharing.service;

import app.carsharing.dto.CarDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarService {
    CarDto create(CarDto dto);

    CarDto getById(Long id);

    Page<CarDto> getAll(Pageable pageable);

    CarDto update(Long id, CarDto dto);

    void delete(Long id);
}

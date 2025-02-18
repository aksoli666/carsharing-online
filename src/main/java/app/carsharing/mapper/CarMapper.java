package app.carsharing.mapper;

import app.carsharing.config.MapperConfig;
import app.carsharing.dto.CarDto;
import app.carsharing.model.Car;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(config = MapperConfig.class)
public interface CarMapper {
    Car toCar(CarDto carDto);

    CarDto toCarDto(Car car);

    void updateCar(CarDto carDto, @MappingTarget Car car);

    List<CarDto> toCarDtoList(List<Car> cars);

    default Page<CarDto> toCarDtoPage(Page<Car> cars) {
        List<CarDto> carDtoList = toCarDtoList(cars.getContent());
        return new PageImpl<>(carDtoList, cars.getPageable(), cars.getTotalElements());
    }
}

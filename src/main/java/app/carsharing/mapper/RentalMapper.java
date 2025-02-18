package app.carsharing.mapper;

import app.carsharing.config.MapperConfig;
import app.carsharing.dto.RentalDto;
import app.carsharing.dto.request.RentalAddRequestDto;
import app.carsharing.dto.responce.RentalAddResponseDto;
import app.carsharing.model.Rental;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(config = MapperConfig.class)
public interface RentalMapper {
    @Mapping(source = "carId", target = "car.id")
    Rental toRental(RentalAddRequestDto dto);

    @Mapping(source = "car.id", target = "carId")
    @Mapping(source = "user.id", target = "userId")
    RentalDto toRentalDto(Rental rental);

    @Mapping(source = "car.id", target = "carId")
    RentalAddResponseDto toRentalAddResponseDto(Rental rental);

    @Mapping(source = "car.id", target = "carId")
    @Mapping(source = "user.id", target = "userId")
    List<RentalDto> toRentalDtoPage(List<Rental> rentals);

    default Page<RentalDto> toRentalDtoPage(Page<Rental> rentals) {
        List<RentalDto> rentalDtoList = toRentalDtoPage(rentals.getContent());
        return new PageImpl<>(rentalDtoList, rentals.getPageable(), rentals.getTotalElements());
    }
}

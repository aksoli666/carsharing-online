package app.carsharing.mapper;

import app.carsharing.config.MapperConfig;
import app.carsharing.dto.UserDto;
import app.carsharing.dto.request.UserRegisterRequestDto;
import app.carsharing.dto.request.UserUpdateProfileRequestDto;
import app.carsharing.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toUser(UserRegisterRequestDto registerDto);

    UserDto toUserDto(User user);

    UserDto toRegisterDto(User user);

    void updateUser(UserUpdateProfileRequestDto updateDto,
                    @MappingTarget User user);
}

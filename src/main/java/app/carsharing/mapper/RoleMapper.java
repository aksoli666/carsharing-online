package app.carsharing.mapper;

import app.carsharing.config.MapperConfig;
import app.carsharing.dto.RoleDto;
import app.carsharing.model.Role;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface RoleMapper {
    Role toRole(RoleDto roleDto);
}

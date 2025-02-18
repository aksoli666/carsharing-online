package app.carsharing.service;

import app.carsharing.dto.UserDto;
import app.carsharing.dto.request.UserUpdateProfileRequestDto;
import org.springframework.security.core.Authentication;

public interface UserService {
    void updateRole(Authentication authentication, String roleName);

    UserDto getProfile(Authentication authentication);

    UserDto updateProfile(Authentication authentication,
                          UserUpdateProfileRequestDto updateProfileRequestDto);
}

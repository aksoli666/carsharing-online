package app.carsharing.controller;

import app.carsharing.dto.UserDto;
import app.carsharing.dto.request.UserUpdateProfileRequestDto;
import app.carsharing.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User management", description = "Endpoints for managing user information")
@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Assign a new role to a user",
            description = "Assigns a new role (ROLE_MANAGER, ROLE_CUSTOMER) "
                    + "to an authenticated user. "
                    + "The user must not already have the specified role. "
                    + "This endpoint is protected "
                    + "and can only be accessed by users with appropriate authority."
    )
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PutMapping("/role")
    public void updateRole(Authentication authentication,
                           @RequestParam("role_name") String roleName) {
        userService.updateRole(authentication, roleName);
    }

    @Operation(
            summary = "Retrieve the authenticated user's profile",
            description = "Fetches the profile information of the authenticated user. "
                    + "This includes details such as email, first name, and last name. "
                    + "This endpoint is protected and can only be accessed by the user themselves."
    )
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/me")
    public UserDto getProfile(Authentication authentication) {
        return userService.getProfile(authentication);
    }

    @Operation(
            summary = "Update the authenticated user's profile",
            description = "Updates the profile information "
                    + "of the authenticated user with the provided details. "
                    + "The user can update their first name, last name, and password. "
                    + "This endpoint is protected and can only be accessed by the user themselves."
    )
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PutMapping("/me")
    public UserDto updateProfile(Authentication authentication,
                                 @Valid @RequestBody UserUpdateProfileRequestDto dto) {
        return userService.updateProfile(authentication, dto);
    }
}

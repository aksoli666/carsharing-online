package app.carsharing.service;

import static app.carsharing.util.ConstantUtil.ROLE_CUSTOMER;
import static app.carsharing.util.EntityAndDtoMaker.createUpdateUserDto;
import static app.carsharing.util.EntityAndDtoMaker.createUser1L;
import static app.carsharing.util.EntityAndDtoMaker.createUserDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import app.carsharing.dto.UserDto;
import app.carsharing.dto.request.UserUpdateProfileRequestDto;
import app.carsharing.exception.DuplicateRoleException;
import app.carsharing.exception.EntityNotFoundException;
import app.carsharing.mapper.UserMapper;
import app.carsharing.model.Role;
import app.carsharing.model.User;
import app.carsharing.repository.RoleRepository;
import app.carsharing.repository.UserRepository;
import app.carsharing.security.CustomUserDetailsService;
import app.carsharing.service.impl.UserServiceImpl;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private Authentication authentication;

    @Test
    @DisplayName("""
            Verify #updateRole(), void
            """)
    public void updateRole_validAuthenticationAndRoleName_void() {
        User user = createUser1L();
        user.setRoles(new HashSet<>());

        Role role = new Role();
        role.setRole(Role.RoleName.ROLE_CUSTOMER);

        when(customUserDetailsService.getUserFromAuthentication(authentication)).thenReturn(user);
        when(roleRepository.findByRole(Role.RoleName.ROLE_CUSTOMER)).thenReturn(Optional.of(role));

        userService.updateRole(authentication, ROLE_CUSTOMER);

        assertTrue(user.getRoles().contains(role));

        verify(customUserDetailsService).getUserFromAuthentication(authentication);
        verify(roleRepository).findByRole(Role.RoleName.ROLE_CUSTOMER);
    }

    @Test
    @DisplayName("""
            Verify #updateRole(), throw EntityNotFoundException
            """)
    public void updateRole_invalidRoleName_throwEntityNotFoundException() {
        User user = createUser1L();

        when(customUserDetailsService.getUserFromAuthentication(authentication)).thenReturn(user);
        when(roleRepository.findByRole(Role.RoleName.ROLE_CUSTOMER)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.updateRole(authentication, ROLE_CUSTOMER));

        assertEquals("Role not found: " + ROLE_CUSTOMER, exception.getMessage());
    }

    @Test
    @DisplayName("updateRole: Throws IllegalArgumentException for invalid role name")
    void updateRole_invalidRoleName_throwsIllegalArgumentException() {
        User user = createUser1L();

        when(customUserDetailsService.getUserFromAuthentication(authentication)).thenReturn(user);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.updateRole(authentication, "INVALID_ROLE"));
        assertEquals("Invalid role name: INVALID_ROLE", exception.getMessage());
    }


    @Test
    @DisplayName("""
            Verify #updateRole(), throw DuplicateRoleException
            """)
    public void updateRole_duplicateRoleName_throwDuplicateRoleException() {
        User user = createUser1L();

        Role role = new Role();
        role.setRole(Role.RoleName.ROLE_CUSTOMER);

        when(customUserDetailsService.getUserFromAuthentication(authentication)).thenReturn(user);
        when(roleRepository.findByRole(Role.RoleName.ROLE_CUSTOMER)).thenReturn(Optional.of(role));

        DuplicateRoleException exception = assertThrows(DuplicateRoleException.class,
                () -> userService.updateRole(authentication, ROLE_CUSTOMER));

        assertEquals("Role already exists: " + ROLE_CUSTOMER, exception.getMessage());

        verify(customUserDetailsService).getUserFromAuthentication(authentication);
        verify(roleRepository).findByRole(Role.RoleName.ROLE_CUSTOMER);
    }

    @Test
    @DisplayName("""
            Verify #getProfile(), return UserDto
            """)
    public void getProfile_validAuthentication_returnUserDto() {
        User user = createUser1L();

        UserDto expected = createUserDto();

        when(customUserDetailsService.getUserFromAuthentication(authentication)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(expected);

        UserDto actual = userService.getProfile(authentication);

        assertEquals(expected, actual);

        verify(customUserDetailsService).getUserFromAuthentication(authentication);
        verify(userMapper).toUserDto(user);
    }

    @Test
    @DisplayName("""
            Verify #updateProfile(), return UserDto
            """)
    public void updateProfile_validAuthenticationAndDto_ReturnUserDto() {
        User user = createUser1L();

        UserUpdateProfileRequestDto dto = createUpdateUserDto();

        when(customUserDetailsService.getUserFromAuthentication(authentication)).thenReturn(user);
        doNothing().when(userMapper).updateUser(dto, user);

        userService.updateProfile(authentication, dto);

        assertTrue(user.getFirstName().equals(dto.getFirstName()));

        verify(customUserDetailsService).getUserFromAuthentication(authentication);
        verify(userMapper).updateUser(dto, user);
    }
}

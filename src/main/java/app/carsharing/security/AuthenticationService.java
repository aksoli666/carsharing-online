package app.carsharing.security;

import app.carsharing.dto.UserDto;
import app.carsharing.dto.request.UserLoginRequestDto;
import app.carsharing.dto.request.UserRegisterRequestDto;
import app.carsharing.dto.responce.UserLoginResponseDto;
import app.carsharing.exception.RegistrationException;
import app.carsharing.mapper.UserMapper;
import app.carsharing.model.Role;
import app.carsharing.model.User;
import app.carsharing.repository.RoleRepository;
import app.carsharing.repository.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public UserDto register(UserRegisterRequestDto registerDto) throws RegistrationException {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new RegistrationException("Email already exists");
        }
        User user = userMapper.toUser(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(Set.of(roleRepository.findByRole(Role.RoleName.ROLE_CUSTOMER)
                .orElseThrow(() -> new RegistrationException("Role customer not found"))));
        userRepository.save(user);
        return userMapper.toRegisterDto(user);
    }

    public UserLoginResponseDto login(UserLoginRequestDto loginDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }
}

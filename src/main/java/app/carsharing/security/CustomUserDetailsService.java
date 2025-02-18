package app.carsharing.security;

import app.carsharing.exception.EntityNotFoundException;
import app.carsharing.model.User;
import app.carsharing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User " + email + " not found"));
    }

    public User getUserFromAuthentication(Authentication authentication) {
        String email = ((org.springframework.security.core.userdetails.User)
                authentication.getPrincipal()).getUsername();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User " + email + " not found"));
    }

    public Long getUserIdFromAuthentication(Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        return user.getId();
    }
}

package sk409.youtube.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import sk409.youtube.models.User;
import sk409.youtube.repositories.UserRepository;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User loadUserByUsername(final String username) throws UsernameNotFoundException {
        if (username == null || "".equals(username)) {
            throw new UsernameNotFoundException("username is empty");
        }
        final Optional<User> _user = userRepository.findByUsername(username);
        if (!_user.isPresent()) {
            throw new UsernameNotFoundException("User does not exist: " + username);
        }
        return _user.get();
    }

}
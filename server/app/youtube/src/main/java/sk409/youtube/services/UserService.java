package sk409.youtube.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk409.youtube.Authority;
import sk409.youtube.models.User;
import sk409.youtube.repositories.UserRepository;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(final PasswordEncoder passwordEncoder, final UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public Optional<User> findById(final Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User registerUser(final String username, final String nickname, final String password, final String email) {
        final User user = new User(username, nickname, passwordEncoder.encode(password), email);
        userRepository.save(user);
        final List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(Authority.ROLE_USER.toString()));
        final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(),
                user.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(token);
        return user;
    }

}
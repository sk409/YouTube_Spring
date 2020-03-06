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

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User findById(Long id) {
        final Optional<User> _user = userRepository.findById(id);
        return _user.isPresent() ? _user.get() : null;
    }

    public User findByUsername(String username) {
        final Optional<User> _user = userRepository.findByUsername(username);
        return _user.isPresent() ? _user.get() : null;
    }

    @Transactional
    public User registerUser(String username, String nickname, String password, String email) {
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
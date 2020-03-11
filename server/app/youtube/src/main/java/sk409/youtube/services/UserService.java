package sk409.youtube.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sk409.youtube.Authority;
import sk409.youtube.models.User;
import sk409.youtube.repositories.UserRepository;

@Service
public class UserService extends QueryService<User> {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(final EntityManager entityManager, final PasswordEncoder passwordEncoder,
            final UserRepository userRepository) {
        super(entityManager);
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public Class<User> classLiteral() {
        return User.class;
    }

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
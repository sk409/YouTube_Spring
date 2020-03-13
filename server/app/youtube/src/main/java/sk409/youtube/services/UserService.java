package sk409.youtube.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sk409.youtube.Authority;
import sk409.youtube.models.User;
import sk409.youtube.query.QueryComponents;
import sk409.youtube.query.specifications.UserSpecifications;
import sk409.youtube.repositories.UserRepository;

@Service
public class UserService extends QueryService<User> {

    private final PasswordEncoder passwordEncoder;
    private final PathService pathService;
    private final UserRepository userRepository;

    public UserService(final EntityManager entityManager, final PasswordEncoder passwordEncoder,
            final PathService pathService, final UserRepository userRepository) {
        super(entityManager);
        this.passwordEncoder = passwordEncoder;
        this.pathService = pathService;
        this.userRepository = userRepository;
    }

    @Override
    public Class<User> classLiteral() {
        return User.class;
    }

    public Optional<User> findByUsername(String username) {
        final UserSpecifications userSpecifications = new UserSpecifications();
        userSpecifications.setUsernameEqual(username);
        final QueryComponents<User> userQueryComponents = new QueryComponents<>();
        userQueryComponents.setSpecifications(userSpecifications);
        final Optional<User> _user = findOne(userQueryComponents);
        return _user;
    }

    public User registerUser(final String username, final String nickname, final String password, final String email) {
        return registerUser(username, nickname, password, email, pathService.getRelativeNoImagePath().toString());
    }

    public User registerUser(final String username, final String nickname, final String password, final String email,
            final String profileImagePath) {
        final User user = new User(username, nickname, passwordEncoder.encode(password), email, profileImagePath);
        userRepository.save(user);
        final List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(Authority.ROLE_USER.toString()));
        final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(),
                user.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(token);
        return user;
    }

}
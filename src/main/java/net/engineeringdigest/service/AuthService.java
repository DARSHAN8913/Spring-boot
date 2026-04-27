package net.engineeringdigest.service;

import net.engineeringdigest.dto.LoginRequest;
import net.engineeringdigest.dto.RegisterRequest;
import net.engineeringdigest.entity.User;
import net.engineeringdigest.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService (UserRepository userRepository,BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User register(RegisterRequest request) {
        User user= User.builder().
                    name(request.getName()).
                    email(request.getEmail()).
                    role(request.getRole()).
                    password(passwordEncoder.encode(request.getPassword())).build();

        return userRepository.save(user);
    }
    public Optional<User> login(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
       if(userOpt.isPresent()){
           User user = userOpt.get();
           if(passwordEncoder.matches(rawPassword,user.getPassword())) {
               return userOpt;
           }
       }
       return Optional.empty();
    }


}

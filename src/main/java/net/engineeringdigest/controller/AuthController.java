package net.engineeringdigest.controller;

import net.engineeringdigest.dto.LoginRequest;
import net.engineeringdigest.dto.RegisterRequest;
import net.engineeringdigest.entity.User;
import net.engineeringdigest.repository.UserRepository;
import net.engineeringdigest.service.AuthService;
import net.engineeringdigest.utils.JwtUtils;
import org.jboss.jandex.TypeTarget;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    public AuthController (AuthService authService,JwtUtils jwtUtils,UserRepository userRepository) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }
    @PostMapping("/regis") // skip if exsists not implemented
    public  ResponseEntity<?> register(@RequestBody RegisterRequest request){

         User user = authService.register(request);
         return ResponseEntity.ok(user);
     }
     @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        Optional<User> useropt= authService.login(request.getEmail(), request.getPassword());
        if(!useropt.isPresent()){
            return ResponseEntity.status(401).body("Invalid creds");
        }
        User user = useropt.get();
        if(!user.getRole().equals(request.getRole())){
            return ResponseEntity.status(403).body("Roler msimatcgh");
        }
        user.setLoginAt(LocalDateTime.now());
         userRepository.save(user);
        String token = jwtUtils.generateToken(user.getId(),user.getEmail(),user.getRole());

        return  ResponseEntity.ok().header("Authorization","Bearer "+token)
                .body("Login Successfull");
     }

}

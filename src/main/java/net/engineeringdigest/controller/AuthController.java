package net.engineeringdigest.controller;

import net.engineeringdigest.dto.LoginRequest;
import net.engineeringdigest.dto.RegisterRequest;
import net.engineeringdigest.entity.User;
import net.engineeringdigest.service.AuthService;
import net.engineeringdigest.utils.JwtUtils;
import org.jboss.jandex.TypeTarget;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtils jwtUtils;
    public AuthController (AuthService authService,JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }
    @PostMapping("/regis")
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
        String token = jwtUtils.generateToken(user.getId(),user.getEmail(),user.getRole());

        return  ResponseEntity.ok().header("Authorization","Bearer "+token)
                .body("Login Successfull");
     }

}

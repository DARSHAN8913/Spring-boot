package net.engineeringdigest.controller;

import net.engineeringdigest.dto.RegisterRequest;
import net.engineeringdigest.entity.User;
import net.engineeringdigest.service.AuthService;
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
    public AuthController (AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/regis")
    public  ResponseEntity<?> register(@RequestBody RegisterRequest request){

         User user = authService.register(request);
         return ResponseEntity.ok(user);
     }
}

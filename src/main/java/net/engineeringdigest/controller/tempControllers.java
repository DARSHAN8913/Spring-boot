package net.engineeringdigest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/protected")
public class tempControllers {
    @GetMapping("/me")
    public Map<String,Object> megetter(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("uid",request.getAttribute("uid"));
        map.put("email",request.getAttribute("email"));
        map.put("role",request.getAttribute("role"));
        return map;
    }
}

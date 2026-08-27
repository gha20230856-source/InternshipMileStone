package com.example.InternshipMileStone.controller;

import com.example.InternshipMileStone.model.User;
import com.example.InternshipMileStone.repo.UserRepo;
import com.example.InternshipMileStone.service.JWTService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LoginController {
    private JWTService jwtService;
    private AuthenticationManager authenticationManager;
    private UserRepo userRepo;
    @PostMapping("/login")
    String logIntoAccount(@RequestBody User user)
    {

        Authentication authentication  = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken
                        (user.getUsername(),user.getPassword()));
        User current = userRepo.findByUsername(user.getUsername()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if(authentication.isAuthenticated()&& current.getEnabled() )
        {
            return jwtService.generateToken(user.getUsername());
        }
        else
        {
            return "fail";
        }


    }

    @GetMapping("/tester")
    String tester(Authentication authentication)
    {

        return authentication.getName();
    }


}

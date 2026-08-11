package com.example.InternshipMileStone.controller;

import com.example.InternshipMileStone.model.User;
import com.example.InternshipMileStone.service.JWTService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LoginController {
    private JWTService jwtService;
    private AuthenticationManager authenticationManager;
    @PostMapping("/login")
    String logIntoAccount(@RequestBody User user)
    {
        System.out.println(user.getUsername() + " " + user.getPassword());
        Authentication authentication  = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken
                        (user.getUsername(),user.getPassword()));

        if(authentication.isAuthenticated())
        {
            return jwtService.generateToken(user.getUsername());
        }
        else
        {
            return "fail";
        }


    }

}

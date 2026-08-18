package com.example.InternshipMileStone.service;

import com.example.InternshipMileStone.model.User;
import com.example.InternshipMileStone.repo.UserRepo;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


// for registration purposes

@Service
@Data
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder encoder;

    public User registerUser(User user)
    {
        String encoded = encoder.encode(user.getPassword());
        user.setPassword(encoded);

        return  userRepo.save(user);
    }


}

package com.example.InternshipMileStone.service;

import com.example.InternshipMileStone.model.User;
import com.example.InternshipMileStone.repo.UserRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


// for regestartion purposes

@Service
@Data
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder encoder;

    public ResponseEntity<String> registerUser(User user)
    {
        String encoded = encoder.encode(user.getPassword());
        user.setPassword(encoded);
        User created = userRepo.save(user);
        return  new ResponseEntity<String> ("user created successfully", HttpStatus.CREATED);
    }


}

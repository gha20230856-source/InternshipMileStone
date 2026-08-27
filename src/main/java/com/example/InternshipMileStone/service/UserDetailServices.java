package com.example.InternshipMileStone.service;

import com.example.InternshipMileStone.model.User;
import com.example.InternshipMileStone.model.UserPrinciple;
import com.example.InternshipMileStone.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailServices implements UserDetailsService {


    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        User user =userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserPrinciple(user);
    }
}

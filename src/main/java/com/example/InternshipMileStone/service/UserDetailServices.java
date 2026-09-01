package com.example.InternshipMileStone.service;

import com.example.InternshipMileStone.model.Employee;
import com.example.InternshipMileStone.model.User;
import com.example.InternshipMileStone.model.UserPrinciple;
import com.example.InternshipMileStone.repo.EmployeeRepo;
import com.example.InternshipMileStone.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


//TODO use yoda code when using  string.equalsignorecase()
@Service
@AllArgsConstructor
public class UserDetailServices implements UserDetailsService {


    private UserRepo userRepo;
    private EmployeeRepo employeeRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


       User user =
                userRepo.findByUsername(username)
                .or(() -> userRepo.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("user with usernameOrEmail: " + username + " not found"));

       Employee employee =
               employeeRepo.findByUser(user)
               .orElseThrow(()->new UsernameNotFoundException("Employee not found"));


       return new UserPrinciple(user, "active".equalsIgnoreCase(employee.getStatus()));

    }
}

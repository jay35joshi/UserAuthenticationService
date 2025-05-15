package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.exceptions.IncorrectPassword;
import com.example.userauthenticationservice.exceptions.UserDoesNotExistsException;
import com.example.userauthenticationservice.exceptions.UserExistsException;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User signup(String email, String password) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if (optionalUser.isPresent()) {
            throw new UserExistsException("Please try with different email");
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(bCryptPasswordEncoder.encode(password));
        userRepo.save(newUser);

        return newUser;
    }

    @Override
    public User login(String email, String password) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new UserDoesNotExistsException("Please signup first");
        }

        if(!bCryptPasswordEncoder.matches(password, optionalUser.get().getPassword())){
        //if(!optionalUser.get().getPassword().equals(password)){
                  throw new IncorrectPassword("Password didn't match");
        }
        return optionalUser.get();
    }
}

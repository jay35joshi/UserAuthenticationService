package com.example.userauthenticationservice.controllers;

import com.example.userauthenticationservice.dtos.LoginRequestDto;
import com.example.userauthenticationservice.dtos.SignupRequestDto;
import com.example.userauthenticationservice.dtos.UserDto;
import com.example.userauthenticationservice.exceptions.UserExistsException;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignupRequestDto signupRequestDto) {
        try {
            User user = authService.signup(signupRequestDto.getEmail(), signupRequestDto.getPassword());
            return from(user);
        } catch (UserExistsException userExistsException) {
            throw userExistsException;
        }
    }


    @PostMapping("/login")
    public UserDto login(@RequestBody LoginRequestDto loginRequestDto){
        User user = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        return from(user);
    }

    public UserDto from(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());

        return userDto;
    }


}

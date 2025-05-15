package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.models.User;

public interface IAuthService {

    public User signup(String email, String password);
    public User login(String email, String password);
}

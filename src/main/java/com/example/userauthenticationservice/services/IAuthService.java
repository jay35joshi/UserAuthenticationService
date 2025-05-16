package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.models.User;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.util.MultiValueMap;

public interface IAuthService {

    public User signup(String email, String password);
    public Pair<User, MultiValueMap<String,String>> login(String email, String password);
}

package com.example.userauthenticationservice.services;

import com.example.userauthenticationservice.exceptions.IncorrectPassword;
import com.example.userauthenticationservice.exceptions.UserDoesNotExistsException;
import com.example.userauthenticationservice.exceptions.UserExistsException;
import com.example.userauthenticationservice.models.Status;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.models.UserSession;
import com.example.userauthenticationservice.repos.SessionRepo;
import com.example.userauthenticationservice.repos.UserRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SessionRepo sessionRepo;

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
    public Pair<User,MultiValueMap<String,String>> login(String email, String password) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new UserDoesNotExistsException("Please signup first");
        }

        if(!bCryptPasswordEncoder.matches(password, optionalUser.get().getPassword())){
        //if(!optionalUser.get().getPassword().equals(password)){
                  throw new IncorrectPassword("Password didn't match");
        }
//                        String message = "{\n" +
//                "   \"email\": \"anurag@gmail.com\",\n" +
//                "   \"roles\": [\n" +
//                "      \"instructor\",\n" +
//                "      \"ta\"\n" +
//                "   ],\n" +
//                "   \"expirationDate\": \"2ndApril2025\"\n" +
//                "}";
//
//                byte[] content = message.getBytes(StandardCharsets.UTF_8);

        Map<String,Object> userClaims = new HashMap<>();

        userClaims.put("userId", optionalUser.get().getId());
        userClaims.put("permissions",optionalUser.get().getRoles());
        Long currentTimeinMillis = System.currentTimeMillis();
        userClaims.put("iat",currentTimeinMillis);
        userClaims.put("exp",currentTimeinMillis*8640000);
        userClaims.put("issuer","Scaler");





        MacAlgorithm macAlgorithm = Jwts.SIG.HS256;
        SecretKey secretKey = macAlgorithm.key().build();

        //String token = Jwts.builder().content(content).signWith(secretKey).compact();

        String token = Jwts.builder().claims(userClaims).signWith(secretKey).compact();

        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE, token);

        //For validation purpose
        UserSession userSession = new UserSession();
        userSession.setToken(token);
        userSession.setUser(optionalUser.get());
        userSession.setStatus(Status.ACTIVE);
        sessionRepo.save(userSession);

        Pair<User,MultiValueMap<String,String>> response = new Pair<>(optionalUser.get(), headers);
        return response;
    }
}

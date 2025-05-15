package com.example.userauthenticationservice.models;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class User extends BaseModel{

    private String email;
    private String password;

    //Whenever a user is created he will have empty list in his role field. it will not be null by default
    @ManyToMany
    private List<Role> roles = new ArrayList<>();
}

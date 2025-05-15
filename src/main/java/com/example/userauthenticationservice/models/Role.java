package com.example.userauthenticationservice.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

//if you have multiple roles or lot of roles you should not use enum
//No point in keeping 25 30 enums
@Getter
@Setter
@Entity
public class Role extends BaseModel{
    private String roleName;
}

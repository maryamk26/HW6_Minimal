package com.example.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@EqualsAndHashCode
public class UserInfo {
    private long id;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String password;
}

package com.android.coffeeshop.utils;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWithRole {
    private int userId;
    private String userName;
    private String password;
    private String roleName;
    private int active;
}

package com.android.coffeeshop.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditUserProfile {
    private int userId;
    private String userName;
    private String fullName;
    private String phone;
    private String roleName;
    private String avatarUrl;
    private String email;


}

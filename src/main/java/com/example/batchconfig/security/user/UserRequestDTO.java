package com.example.batchconfig.security.user;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String username;
    private Integer userId;
    private String userRole;
}

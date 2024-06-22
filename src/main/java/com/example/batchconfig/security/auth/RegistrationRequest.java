package com.example.batchconfig.security.auth;

import com.example.batchconfig.security.user.Role;

/**
 * @author Sampson Alfred
 */

public record RegistrationRequest(
         String firstName,
         String lastName,
         String email,
         String password,
         Role role) {
}

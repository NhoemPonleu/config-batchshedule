package com.example.batchconfig.security.auditing;

import com.example.batchconfig.security.user.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorImplement implements AuditorAwareUserName<String>{
    @Override
    public Optional<String> getCurrentAuditor1() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }

        User userPrincipal = (User) authentication.getPrincipal();
        return  Optional.ofNullable(userPrincipal.getFirstname());
    }
}

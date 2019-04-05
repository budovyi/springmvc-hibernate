package com.budovyy.service;

import com.budovyy.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User getByUsername(String username);

    User register(User user);

    User emailVerification(String token);
}

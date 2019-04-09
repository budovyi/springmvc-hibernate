package com.budovyy.service;

import com.budovyy.dao.UserDao;
import com.budovyy.model.Role;
import com.budovyy.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private MailService mailService;

    public User getByUsername(String username) {
        return userDao.getByUsername(username);
    }

    @Override
    public User register(User user) {
        String hashedPassword = encoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setToken(getToken());
        user.setStatus(User.UserStatus.PENDING_VERIFICATION);
        User saved = userDao.addUser(user);
        mailService.send(saved);
        return saved;

    }

    @Override
    public User emailVerification(String token) {
        User user = userDao.getByToken(token);
        user.setStatus(User.UserStatus.ACTIVE);

        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setRoleName("USER");
        roles.add(role);

        user.setRoles(roles);
        userDao.updateUser(user);
        return user;
    }

    private String getToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getByUsername(username);
        List<SimpleGrantedAuthority> roles = user.getRoles().stream()
                .map(Role::getRoleName)
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);
    }
}
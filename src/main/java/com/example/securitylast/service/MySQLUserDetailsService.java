package com.example.securitylast.service;

import com.example.securitylast.entity.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MySQLUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("进入MySQLUserDetailsService 的 loadUserByUsername方法" + username);
        SecurityUser securityUser = new SecurityUser();
        securityUser.setId(1);
        securityUser.setUserName(username);
        securityUser.setPassword(new BCryptPasswordEncoder().encode("123456"));
        securityUser.setAccountNonLocked(true);
        securityUser.setEnabled(true);
        securityUser.setAccountNonExpired(true);
        securityUser.setCredentialsNonExpired(true);
        return securityUser;
    }
}

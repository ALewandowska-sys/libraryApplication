package com.application.library.security;

import com.application.library.account.ReaderModel;
import com.application.library.account.ReaderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("ReaderService")
public class ReaderSecurityService implements UserDetailsService {
    @Autowired
    private ReaderRepo repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.equals("admin")){
            return addStaticAdmin();
        }
        final ReaderModel customer = repo.findByName(username);
        if(customer == null){
            throw new UsernameNotFoundException(username);
        }
        return User
                .withUsername(customer.getName())
                .password(customer.getPassword())
                .roles("Reader")
                .build();
    }

    private UserDetails addStaticAdmin() {
        return User
            .withUsername("admin")
            .password(encoder.encode("admin"))
            .roles("Admin", "Reader")
            .build();
    }
}

package com.example.lab4.laba_4.service;

import com.example.lab4.laba_4.domain.UserEntity;
import com.example.lab4.laba_4.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
public class DaoUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByUsername(login);

        if (user != null) {
//            System.out.println(new BCryptPasswordEncoder().encode(user.getPassword()));

            return new User(user.getUsername(), user.getPassword(), getGrantedAuthorities(user));
        }
        throw new UsernameNotFoundException("Username not found");

    }

    private List<GrantedAuthority> getGrantedAuthorities(UserEntity userEntity) {


        List authorities = new ArrayList<GrantedAuthority>();

        authorities.add(

                new SimpleGrantedAuthority("User")
        );


        return authorities;

    }
}
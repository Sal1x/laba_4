package com.example.lab4.laba_4;

import com.example.lab4.laba_4.domain.UserEntity;
import com.example.lab4.laba_4.repo.ResultRepository;
import com.example.lab4.laba_4.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@ComponentScan({"com.example.lab4.laba_4"})
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ResultRepository resultRepository;

    @Autowired
    public DatabaseLoader(UserRepository userepository, ResultRepository resultRepository) {
        this.userRepository = userepository;
        this.resultRepository = resultRepository;
    }


    @Override
    public void run(String... strings) throws Exception {
        this.userRepository.save(new UserEntity("someName", new BCryptPasswordEncoder().encode("password"), false));
        this.userRepository.save(new UserEntity("sanya", new BCryptPasswordEncoder().encode("sanya"), false));
    }
}
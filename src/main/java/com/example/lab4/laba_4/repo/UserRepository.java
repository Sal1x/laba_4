package com.example.lab4.laba_4.repo;

import com.example.lab4.laba_4.domain.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {
    public UserEntity findByUsername(String username);
}

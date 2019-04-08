package com.example.lab4.laba_4.repo;

import com.example.lab4.laba_4.domain.ResultEntity;
import com.example.lab4.laba_4.domain.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends CrudRepository<ResultEntity, Long> {
    public Iterable<ResultEntity> findByOwner(UserEntity owner);
}

package com.application.library.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReaderRepo extends JpaRepository<ReaderModel, Integer> {
    ReaderModel findByName(@Param(value = "name") String name);
}

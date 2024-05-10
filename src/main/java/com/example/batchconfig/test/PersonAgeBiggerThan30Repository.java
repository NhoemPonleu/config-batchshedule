package com.example.batchconfig.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonAgeBiggerThan30Repository extends JpaRepository<PersonAgeBiggerThan30,Long> {
    @Query("SELECT COALESCE(MAX(p.transactionSeqNo), 0) FROM PersonAgeBiggerThan30 p WHERE p.person.id = :personId")
    Long findMaxSeqNoByPersonId(@Param("personId") Long personId);
}

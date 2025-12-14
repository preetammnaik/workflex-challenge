package com.example.workflex.repository;


import com.example.workflex.data.Workation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkationRepository extends JpaRepository<Workation, Long> {
}
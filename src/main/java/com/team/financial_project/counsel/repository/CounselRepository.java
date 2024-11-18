package com.team.financial_project.counsel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounselRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);
}

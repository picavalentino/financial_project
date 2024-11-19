package com.team.financial_project.counsel.repository;

import com.team.financial_project.counsel.entity.TbCounsel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounselRepository extends JpaRepository<TbCounsel, Long> {
    Page<TbCounsel> findAll(Pageable pageable);
}

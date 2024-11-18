package com.team.financial_project.counsel.controller;

import com.team.financial_project.counsel.repository.CounselRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CounselController {

    @Autowired
    private CounselRepository counselRepository;

    @GetMapping("/getPagedData")
    public Page<User> getPagedData(@RequestParam("page") int page, @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return counselRepository.findAll(pageable);
    }
}

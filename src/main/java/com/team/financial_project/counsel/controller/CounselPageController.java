package com.team.financial_project.counsel.controller;

import com.team.financial_project.counsel.entity.TbCounsel;
import com.team.financial_project.counsel.repository.CounselRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer/counsel")
public class CounselPageController {

    @Autowired
    private CounselRepository counselRepository;

    @GetMapping("/getPagedData")
    public Page<TbCounsel> getPagedData(@RequestParam("page") int page, @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("counselId")));
        return counselRepository.findAll(pageable);
    }
}

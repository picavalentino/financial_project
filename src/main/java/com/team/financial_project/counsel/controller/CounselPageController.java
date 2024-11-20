package com.team.financial_project.counsel.controller;

import com.team.financial_project.counsel.dto.TbCounselDTO;
import com.team.financial_project.counsel.repository.CounselRepository;
import com.team.financial_project.counsel.service.CounselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer/counsel")
public class CounselPageController {

    @Autowired
    private CounselService counselService;

//    @Autowired
//    private CounselRepository counselRepository;

//    @GetMapping("/getPagedData")
//    public Page<TbCounsel> getPagedData(@RequestParam("page") int page, @RequestParam("size") int size) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("counselId")));
//        return counselRepository.findAll(pageable);
//    }

    @GetMapping("/getPagedData")
    public List<TbCounselDTO> getPagedData(@RequestParam("page") int page, @RequestParam("size") int size) {
        List<TbCounselDTO> dtos = counselService.getPagedCounselData(page, size);
        return dtos;
    }
}

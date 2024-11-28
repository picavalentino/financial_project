package com.team.financial_project.counsel.controller;

import com.team.financial_project.counsel.dto.CodeDTO;
import com.team.financial_project.counsel.dto.TbCounselDTO;
import com.team.financial_project.counsel.service.CounselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer/counsel")
@CrossOrigin(origins = "http://localhost:4000")
public class CounselRestController {
    @Autowired
    private CounselService counselService;

/*    @GetMapping("/deleteCounsel")
    public ResponseEntity<String> deleteCounsel(@RequestParam("id") Long id) {
        counselService.deleteCounsel(id);
        return ResponseEntity.ok("상담 삭제가 완료되었습니다.");
    }*/

    @DeleteMapping("/deleteCounsel")
    public ResponseEntity<String> deleteCounsel(@RequestParam("id") Long id) {
        counselService.deleteCounsel(id);
        return ResponseEntity.ok("상담 삭제가 완료되었습니다.");
    }

    @PostMapping("/insertCounsel")
    public ResponseEntity<String> insertCounsel(TbCounselDTO dto){
        counselService.insertCounsel(dto);
        return ResponseEntity.ok("상담 작성이 완료되었습니다.");
    }

    @PostMapping("/updateCounsel")
    public ResponseEntity<TbCounselDTO> updateCounsel(@RequestParam("counsel_id") Long id,
                                @RequestParam("update_counsel_category") String category,
                                @RequestParam("update_counsel_content") String content){
        counselService.updateCounsel(id, category, content);
        // 업데이트 후 최신 데이터 가져오기
        TbCounselDTO updatedCounsel = counselService.getCounselById(id);
        return ResponseEntity.ok(updatedCounsel);
    }
}

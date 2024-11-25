package com.team.financial_project.main.controller;

import com.team.financial_project.main.service.BookmarkService;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/bookmark")
public class BookmarkController {
    private static final Logger log = LoggerFactory.getLogger(BookmarkController.class);
    //로그인 했다 치고
    String userId = "20240706002";

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addBookmark(@RequestParam("menuId") Long menuId) {
        try {
            bookmarkService.addBookmark(menuId, userId); // 하드코딩된 userId 사용
            return ResponseEntity.ok("북마크 추가 완료");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("북마크 추가 실패");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Long>> getUserBookmarks() {
        // 북마크된 menu_id 목록 가져오기
        List<Long> bookmarkedMenuIds = bookmarkService.getBookmarkedMenuIds(userId);
        return ResponseEntity.ok(bookmarkedMenuIds);
    }

    @GetMapping("/bmlist")
    public ResponseEntity<List<Map<String, Object>>> getUserBookmarkMenus() {
        List<Map<String, Object>> bookmarkedMenus = bookmarkService.getBookmarkedMenus(userId);
        log.info("bookmarkName: "+bookmarkedMenus.toString());
        return ResponseEntity.ok(bookmarkedMenus);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteBookmark(@RequestParam("menuId") Long menuId) {
        try {
            bookmarkService.deleteBookmark(userId, menuId);
            return ResponseEntity.ok("북마크 삭제 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("북마크 삭제 실패");
        }
    }



}

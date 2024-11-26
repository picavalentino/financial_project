package com.team.financial_project.main.service;

import com.team.financial_project.mapper.BookmarkMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BookmarkService {
    private final BookmarkMapper bookmarkMapper;

    public BookmarkService(BookmarkMapper bookmarkMapper) {
        this.bookmarkMapper = bookmarkMapper;
    }

    public void addBookmark(Long menuId, String userId) {
        // 중복 확인
        if (bookmarkMapper.isBookmarkExists(userId, menuId)) {
            throw new IllegalStateException("이미 북마크에 추가된 항목입니다.");
        }

        // 북마크 추가
        bookmarkMapper.insertBookmark(userId, menuId);
    }

    public List<Long> getBookmarkedMenuIds(String userId) {
        return bookmarkMapper.findBookmarkedMenuIds(userId);
    }

    public List<Map<String, Object>> getBookmarkedMenus(String userId) {
        return bookmarkMapper.findBookmarkedMenus(userId);
    }

    public void deleteBookmark(String userId, Long menuId) {
        log.info("Deleting bookmark for userId: {}, menuId: {}", userId, menuId); // 디버깅용 로그
        bookmarkMapper.deleteBookmark(userId, menuId);
    }


}

package com.team.financial_project.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface BookmarkMapper {
    void insertBookmark(@Param("userId") String userId, @Param("menuId") Long menuId);

    boolean isBookmarkExists(@Param("userId") String userId, @Param("menuId") Long menuId);

    @Select("SELECT menu_id FROM tb_bookmark WHERE user_id = #{userId}")
    List<Long> findBookmarkedMenuIds(String userId);

    @Select("""
        SELECT b.menu_id, m.menu_view_name, m.menu_url
        FROM tb_bookmark b
        JOIN tb_menu m ON b.menu_id = m.menu_id
        WHERE b.user_id = #{userId}
    """)
    List<Map<String, Object>> findBookmarkedMenus(String userId);

    void deleteBookmark(@Param("userId") String userId, @Param("menuId") Long menuId);

}

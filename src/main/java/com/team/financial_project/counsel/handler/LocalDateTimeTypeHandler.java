//package com.team.financial_project.counsel.handler;
//
//import org.apache.ibatis.type.BaseTypeHandler;
//import org.apache.ibatis.type.JdbcType;
//
//import java.sql.CallableStatement;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//public class LocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {
//    @Override
//    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
//        ps.setString(i, parameter.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)); // LocalDateTime을 문자열로 변환
//    }
//
//    @Override
//    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
//        String dateTime = rs.getString(columnName);
//        return dateTime != null ? LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
//    }
//
//    @Override
//    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
//        String dateTime = rs.getString(columnIndex);
//        return dateTime != null ? LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
//    }
//
//    @Override
//    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
//        String dateTime = cs.getString(columnIndex);
//        return dateTime != null ? LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
//    }
//}

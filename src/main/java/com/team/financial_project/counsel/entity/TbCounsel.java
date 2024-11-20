package com.team.financial_project.counsel.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class TbCounsel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long counselId;
    @Column(length = 20, nullable = false)
    private String userName;
    @Column(length = 50, nullable = false)
    private String userDeptCd;
    @Column(length = 10, nullable = false)
    private String counselCategory;
    @Column
    private LocalDateTime counselCreateAt;
    @Column
    private LocalDateTime counselUpdateAt;
    @Column(length = 10000)
    private String counselContent;
}

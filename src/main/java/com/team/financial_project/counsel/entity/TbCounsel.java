package com.team.financial_project.counsel.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
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
    private Timestamp counselCreateAt;
    @Column
    private Timestamp counselUpdateAt;
    @Column(length = 10000)
    private String counselContent;
}

package com.example.gameverse2.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;
    @Column(unique = true)
    private String loginId;
    private String password;
    private LocalDateTime  createDate;
    private LocalDateTime deleteDate;
    private LocalDateTime  updateDate;
    @Column
    private Role role;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String nickName;
    private char deleteFl;

}
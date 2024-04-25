package com.example.gameverse2.domain.board.entity;

import com.example.gameverse2.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardNo;
    @Column
    private int boardCode; //코드 롤,옵치 종류 등등
    @Column
    private int tag; //태그 자유,팁 등등
    @Column(columnDefinition = "TEXT")
    private String boardTitle; //제목
    @Column
    private String boardText; // 내용
    @Column
    private Date createDate; //작성일
    @Column
    private Date deleteDate; //삭제날짜
    @Column
    private Date updateDate; //수정날짜
    @Column
    private int readCount;  //조회수
    @Column
    private char boardDelete; //삭제여부
    @Column
    private int likeCount; // 좋아요

}

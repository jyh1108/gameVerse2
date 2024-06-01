package com.example.gameverse2.domain.board.entity;

import com.example.gameverse2.domain.member.entity.Member;
import com.example.gameverse2.domain.reply.entity.Reply;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardNo;
    @Column
    private String boardCode; //코드 롤,옵치 종류 등등
    @Column
    private String tag; //태그 자유,팁 등등
    @Column(columnDefinition = "TEXT")
    private String boardTitle; //제목
    @Column
    private String boardText; // 내용
    @Column
    private LocalDateTime createDate; //작성일
    @Column
    private LocalDateTime  deleteDate; //삭제날짜
    @Column
    private LocalDateTime  updateDate; //수정날짜
    @Column
    private int readCount;  //조회수
    @Column
    private char boardDelete; //삭제여부
    @Column
    private int likeCount; // 좋아요
    @ManyToOne
    private Member author;
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Reply> replyList;
    @ManyToMany
    Set<Member> voter;
}

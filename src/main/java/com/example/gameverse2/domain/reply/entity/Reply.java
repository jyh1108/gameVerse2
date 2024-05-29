package com.example.gameverse2.domain.reply.entity;

import com.example.gameverse2.domain.board.entity.Board;
import com.example.gameverse2.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyNo;
    @Column(columnDefinition = "TEXT")
    private String replyContent; //댓글 내용
    @Column
    private LocalDateTime  replyDate; //작성일
    @Column
    private LocalDateTime replyUpdate; //수정일
    @ManyToOne
    private Member author; //작성자
    @ManyToOne
    private Board board; //게시글


}

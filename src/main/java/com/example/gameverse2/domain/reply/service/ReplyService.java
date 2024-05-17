package com.example.gameverse2.domain.reply.service;

import com.example.gameverse2.domain.board.entity.Board;
import com.example.gameverse2.domain.member.entity.Member;
import com.example.gameverse2.domain.reply.dao.ReplyRepository;
import com.example.gameverse2.domain.reply.entity.Reply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;

    public void create (Board board, String ReplyContent, Member author){
        Reply reply = new Reply();
        reply.setReplyContent(ReplyContent);
        reply.setReplyDate(LocalDateTime.now());
        reply.setBoard(board);
        reply.setAuthor(author);
        this.replyRepository.save(reply);
    }
}

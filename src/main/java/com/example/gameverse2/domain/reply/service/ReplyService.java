package com.example.gameverse2.domain.reply.service;

import com.example.gameverse2.domain.board.entity.Board;
import com.example.gameverse2.domain.reply.dao.ReplyRepository;
import com.example.gameverse2.domain.reply.entity.Reply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;

    public void create (Board board, String ReplyContent){
        Reply reply = new Reply();
        reply.setReplyContent(ReplyContent);
        reply.setReplyDate(new Date());
        reply.setBoard(board);
        this.replyRepository.save(reply);
    }
}

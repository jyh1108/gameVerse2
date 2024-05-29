package com.example.gameverse2.domain.reply.service;

import com.example.gameverse2.domain.board.entity.Board;
import com.example.gameverse2.domain.member.entity.DataNotFoundException;
import com.example.gameverse2.domain.member.entity.Member;
import com.example.gameverse2.domain.reply.dao.ReplyRepository;
import com.example.gameverse2.domain.reply.entity.Reply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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

    public Reply getReply(Long replyNo){
        Optional<Reply> reply = this.replyRepository.findById(replyNo);
        if (reply.isPresent()) {
            return reply.get();
        } else {
            throw new DataNotFoundException("reply not found");
        }
    }

    public void modify(Reply reply, String replyContent) {
        reply.setReplyContent(replyContent);
        reply.setReplyUpdate(LocalDateTime.now());
        this.replyRepository.save(reply);
    }
    //서비스만 작성해놓은상태


    public void delete(Reply reply) {
        this.replyRepository.delete(reply);
    }
}

package com.example.gameverse2.domain.reply.dao;

import com.example.gameverse2.domain.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository  extends JpaRepository<Reply, Long> {

}

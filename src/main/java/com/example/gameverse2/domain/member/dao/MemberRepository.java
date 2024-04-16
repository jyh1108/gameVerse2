package com.example.gameverse2.domain.member.dao;

import com.example.gameverse2.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}

package com.example.gameverse2.domain.member.dao;

import com.example.gameverse2.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);

    Optional<Member> findByNickName(String nickName);
    boolean existsByLoginId(String loginId);

    Optional<Member> findByEmail(String email);
    Optional<Member> findByLoginIdAndEmail(String loginId, String email);
}

package com.example.gameverse2.domain.member.service;

import com.example.gameverse2.domain.member.dao.MemberRepository;
import com.example.gameverse2.domain.member.entity.DataNotFoundException;
import com.example.gameverse2.domain.member.entity.Member;
import com.example.gameverse2.domain.member.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private  final PasswordEncoder passwordEncoder;

    public Member create(String loginId, String password, String email, String nickName){
        Member member = new Member();
        member.setLoginId(loginId);
        member.setEmail(email);
        member.setNickName(nickName);
        member.setRole(Role.MEMBER);
        member.setDeleteFl('N');
        member.setCreateDate(LocalDateTime.now());
        member.setPassword(passwordEncoder.encode(password));
        this.memberRepository.save(member);
        return member;
    }

    public Member getMember(String loginId) {
        Optional<Member> member = this.memberRepository.findByLoginId(loginId);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new DataNotFoundException("Member not found");
        }
    }


    // 중복 확인 메서드 추가
    public boolean isLoginIdAvailable(String loginId) {
        return !memberRepository.existsByLoginId(loginId);
    }

    public String findIdByEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        return member != null ? member.getLoginId() : null;
    }
}
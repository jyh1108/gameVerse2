package com.example.gameverse2.domain.member.service;

import com.example.gameverse2.domain.member.dao.MemberRepository;
import com.example.gameverse2.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

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
        member.setRole(1);
        member.setDeleteFl('N');
        member.setCreateDate(new Date());
        member.setPassword(passwordEncoder.encode(password));
        this.memberRepository.save(member);
        return member;
    }
}
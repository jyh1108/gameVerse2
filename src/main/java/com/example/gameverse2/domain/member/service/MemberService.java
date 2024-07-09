package com.example.gameverse2.domain.member.service;

import com.example.gameverse2.domain.member.dao.MemberRepository;
import com.example.gameverse2.domain.member.entity.DataNotFoundException;
import com.example.gameverse2.domain.member.entity.Member;
import com.example.gameverse2.domain.member.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberSecurityService memberSecurityService;

    public Member create(String loginId, String password, String email, String nickName) {
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
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.map(Member::getLoginId).orElse(null);
    }

    public boolean verifyMember(String loginId, String email) {
        Optional<Member> member = memberRepository.findByLoginIdAndEmail(loginId, email);
        return member.isPresent();
    }

    @Transactional
    public void resetPassword(String loginId, String email, String newPassword) {
        // 새 비밀번호를 암호화
        String encryptedPassword = passwordEncoder.encode(newPassword);

        // 회원 정보 조회
        Optional<Member> memberOptional = memberRepository.findByLoginIdAndEmail(loginId, email);
        if (!memberOptional.isPresent()) {
            throw new DataNotFoundException("User not found with loginId: " + loginId + " and email: " + email);
        }

        // 비밀번호 업데이트
        Member member = memberOptional.get();
        member.setPassword(encryptedPassword);
    }

    // 카카오 로그인 시 회원 가입 또는 로그인 처리 메서드 추가
    @Transactional
    public Member createOrGetKakaoMember(String kakaoLoginId, String email, String nickName) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isPresent()) {
            return memberOptional.get();  // 이미 가입된 회원이면 반환
        }

        // 새 회원 생성
        Member newMember = new Member();
        newMember.setLoginId(kakaoLoginId);
        newMember.setEmail(email);
        newMember.setNickName(nickName);
        newMember.setRole(Role.MEMBER);
        newMember.setDeleteFl('N');
        newMember.setCreateDate(LocalDateTime.now());
        // 카카오 로그인 시 비밀번호는 따로 사용하지 않으므로 더미 패스워드 설정
        newMember.setPassword(passwordEncoder.encode("kakao_dummy_password"));
        memberRepository.save(newMember);
        return newMember;
    }

    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return memberSecurityService.loadUserByUsername(loginId);
    }
}

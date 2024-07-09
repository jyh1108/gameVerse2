package com.example.gameverse2.global.security;

import com.example.gameverse2.domain.member.dao.MemberRepository;
import com.example.gameverse2.domain.member.entity.Member;
import com.example.gameverse2.domain.member.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        if ("kakao".equals(registrationId)) {
            return processKakaoUser(oauth2User, userNameAttributeName);
        }
        throw new UsernameNotFoundException("해당 OAuth2 로그인은 지원되지 않습니다.");
    }

    private OAuth2User processKakaoUser(OAuth2User oauth2User, String userNameAttributeName) {
        String loginId = "kakao_" + oauth2User.getAttributes().get("id").toString();
        String email = (String) ((Map<String, Object>) oauth2User.getAttributes().get("kakao_account")).get("email");
        String nickName = (String) ((Map<String, Object>) oauth2User.getAttributes().get("properties")).get("nickname");

        Optional<Member> memberOptional = memberRepository.findByLoginId(loginId);
        Member member;
        if (memberOptional.isPresent()) {
            member = memberOptional.get();
        } else {
            member = new Member();
            member.setLoginId(loginId);
            member.setEmail(email);
            member.setNickName(nickName);
            member.setRole(Role.MEMBER);
            member.setDeleteFl('N');
            member.setCreateDate(LocalDateTime.now());
            member.setPassword("kakao_dummy_password"); // 패스워드 설정 필요 없음
            memberRepository.save(member);
        }

        return new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority(member.getRole().getValue())),
                oauth2User.getAttributes(),
                userNameAttributeName
        );
    }
}

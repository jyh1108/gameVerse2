package com.example.gameverse2.domain.member.controller;

import com.example.gameverse2.domain.member.dto.MemberCreateForm;
import com.example.gameverse2.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("memberCreateForm", new MemberCreateForm());
        return "domain/member/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "domain/member/signup";
        }
        if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "domain/member/signup";
        }
        try {
            memberService.create(memberCreateForm.getLoginId(),
                    memberCreateForm.getPassword1(), memberCreateForm.getEmail(), memberCreateForm.getNickName());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다.");
            return "domain/member/signup";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "domain/member/signup";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "domain/member/login_form";
    }

    // 중복 확인 엔드포인트 추가
    @GetMapping("/checkId")
    @ResponseBody
    public String checkId(@RequestParam String loginId) {
        boolean isAvailable = memberService.isLoginIdAvailable(loginId);
        return isAvailable ? "OK" : "DUPLICATE";
    }

    // 아이디/비밀번호 찾기 페이지
    @GetMapping("/find")
    public String find() {
        return "domain/member/member_find";
    }

    // 아이디 찾기 페이지
    @GetMapping("/findId")
    public String findId() {
        return "domain/member/findId";
    }

    // 아이디 조회 엔드포인트
    @GetMapping("/findIdByEmail")
    @ResponseBody
    public String findIdByEmail(@RequestParam String email) {
        String loginId = memberService.findIdByEmail(email);
        return loginId != null ? loginId : "";
    }

    @GetMapping("/findPassword")
    public String findPassword(){
        return "domain/member/findPassword";
    }

    @PostMapping("/findPassword")
    public String findPassword(@RequestParam String loginId, @RequestParam String email, Model model) {
        boolean isMatched = memberService.verifyMember(loginId, email);
        if (isMatched) {
            model.addAttribute("loginId", loginId);
            model.addAttribute("email", email);
            return "domain/member/reset_password";
        } else {
            model.addAttribute("errorMessage", "아이디와 이메일이 일치하지 않습니다.");
            return "domain/member/findPassword";
        }
    }
    @GetMapping("/resetPassword")
    public String resetPassword(@RequestParam String loginId, @RequestParam String email, Model model) {
        model.addAttribute("loginId", loginId);
        model.addAttribute("email", email);
        return "domain/member/reset_password";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String loginId, @RequestParam String email,
                                @RequestParam String newPassword, Model model) {
        try {
            memberService.resetPassword(loginId, email, newPassword);
                return "redirect:/member/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "비밀번호 재설정 중 오류가 발생했습니다.");
            return "domain/member/reset_password";
        }
    }
}

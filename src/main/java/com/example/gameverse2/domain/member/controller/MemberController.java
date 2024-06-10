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
            try{
                memberService.create(memberCreateForm.getLoginId(),
                        memberCreateForm.getPassword1(),memberCreateForm.getEmail(), memberCreateForm.getNickName());
            }catch (DataIntegrityViolationException e){
                e.printStackTrace();
                bindingResult.reject("signupFailed","이미 등록된 사용자 입니다.");
                return "domain/member/signup";
            }catch (Exception e){
                e.printStackTrace();
                bindingResult.reject("signupFailed",e.getMessage());
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

}

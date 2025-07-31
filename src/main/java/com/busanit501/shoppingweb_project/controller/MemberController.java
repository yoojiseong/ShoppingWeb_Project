package com.busanit501.shoppingweb_project.controller;

import com.busanit501.shoppingweb_project.dto.AddressDTO;
import com.busanit501.shoppingweb_project.dto.MemberDTO;
import com.busanit501.shoppingweb_project.security.CustomUserDetails;
import com.busanit501.shoppingweb_project.security.MemberSecurityDTO;
import com.busanit501.shoppingweb_project.service.AddressService;
import com.busanit501.shoppingweb_project.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AddressService addressService;

    @GetMapping("/signup")
    public String registerForm(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());
        return "/signup";
    }

    @PostMapping("/signup")
    public String register(@Valid @ModelAttribute MemberDTO memberDTO,
                           BindingResult bindingResult,
                           Model model) {
        // 유효성 검사 실패
        if (bindingResult.hasErrors()) {
            return "/signup";
        }

        // 아이디 중복 확인
        if (memberService.isMemberIdDuplicated(memberDTO.getMemberId())) {
            model.addAttribute("errorMessage", "이미 존재하는 아이디입니다.");
            return "/signup";
        }

        // 비밀번호 확인
        if (!memberDTO.getPassword().equals(memberDTO.getConfirmPassword())) {
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "/signup";
        }

        // 회원 등록
        memberService.register(memberDTO);
        return "redirect:/login";
    }

    @GetMapping("/userInfo-update")
    public String showUpdateForm(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        MemberDTO memberDTO = memberService.findByMemberId(userDetails.getUsername());
        model.addAttribute("user", memberDTO);
        return "userInfo-update";
    }

    @PostMapping("/userInfo-update")
    public String updateUserInfo(@ModelAttribute MemberDTO memberDTO,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 Model model) {
        // 비밀번호 확인 처리
        if (memberDTO.getPassword() != null && !memberDTO.getPassword().isEmpty()) {
            if (!memberDTO.getPassword().equals(memberDTO.getConfirmPassword())) {
                model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
                model.addAttribute("user", memberDTO);
                return "userInfo-update";
            }
        }

        memberService.updateMemberInfo(userDetails.getMemberId(), memberDTO);

        return "redirect:/mypage";
    }

    @GetMapping("/complete-profile")
    public String showCompleteProfileForm(Model model, @AuthenticationPrincipal MemberSecurityDTO userDetails) {
        model.addAttribute("member", userDetails);
        model.addAttribute("address", new AddressDTO());
        return "complete-profile";
    }

    @PostMapping("/complete-profile")
    public String completeProfile(@AuthenticationPrincipal MemberSecurityDTO userDetails,
                                  @RequestParam String phone,
                                  @ModelAttribute AddressDTO addressDTO) {

        if(userDetails == null || userDetails.getMid() == null) {
            // 인증 정보 없으면 로그인 페이지 등으로 리다이렉트 처리
            return "redirect:/login";
        }
        String memberId = userDetails.getMid(); // String 타입 아이디

        Long memberIdLong = Long.parseLong(memberId);
        memberService.updatePhone(memberIdLong, phone);
        log.info("전화번호 : ",memberIdLong, phone);
        addressService.addAddress(addressDTO, memberIdLong);
        log.info("주소 정보 : ", addressDTO, memberIdLong);

        return "redirect:/userInfo-update";
    }
}

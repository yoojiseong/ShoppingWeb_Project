package com.busanit501.shoppingweb_project.controller;
import com.busanit501.shoppingweb_project.dto.MemberDTO;
import com.busanit501.shoppingweb_project.dto.UserinfoDTO;
import com.busanit501.shoppingweb_project.security.CustomUserDetails;
import com.busanit501.shoppingweb_project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberControllerRest {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<UserinfoDTO> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails){
        Long memberId = userDetails.getMemberId();
        UserinfoDTO dto = memberService.getUserinfoDTOById(memberId);
        return ResponseEntity.ok(dto);
    }
}

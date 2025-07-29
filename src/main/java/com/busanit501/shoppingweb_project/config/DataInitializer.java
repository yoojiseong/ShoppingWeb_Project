package com.busanit501.shoppingweb_project.config;

import com.busanit501.shoppingweb_project.domain.Member;
import com.busanit501.shoppingweb_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Log4j2
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // 관리자 계정이 이미 존재하는지 확인
        if (!memberRepository.existsByMemberId("admin")) {
            Member admin = Member.builder()
                    .memberId("admin")
                    .password(passwordEncoder.encode("admin1234")) // 비밀번호는 인코딩 필수
                    .email("admin@example.com")
                    .userName("관리자")
                    .phone("010-0000-0000")
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .createdAt(LocalDateTime.now())
                    .role("ROLE_ADMIN") // 관리자 권한
                    .build();

            memberRepository.save(admin);
            log.info("관리자 계정 생성 완료: admin / admin1234");
        }

        // 일반 사용자 계정이 이미 존재하는지 확인
        if (!memberRepository.existsByMemberId("memberId")) {
            Member user = Member.builder()
                    .memberId("user")
                    .password(passwordEncoder.encode("1234"))
                    .email("user01@example.com")
                    .userName("일반회원")
                    .phone("010-1111-1111")
                    .birthDate(LocalDate.of(1995, 5, 15))
                    .createdAt(LocalDateTime.now())
                    .role("ROLE_USER") // 일반 회원 권한
                    .build();

            memberRepository.save(user);
            log.info("일반 사용자 계정 생성 완료: user / 1234");
        }
    }
}
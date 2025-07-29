package com.busanit501.shoppingweb_project.repository;

import com.busanit501.shoppingweb_project.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);

    boolean existsByMemberId(String memberId);

    @EntityGraph(attributePaths = "role")
    @Query("select m from Member m where m.memberId = :memberId and m.social = false")
    Optional<Member> getWithRoles(@Param("memberId") String memberId);


    @EntityGraph(attributePaths = "role")
    Optional<Member> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("update Member m set m.password = :password where m.memberId = :memberId")
    void updatePassword(@Param("password") String password, @Param("memberId") String memberId);
}

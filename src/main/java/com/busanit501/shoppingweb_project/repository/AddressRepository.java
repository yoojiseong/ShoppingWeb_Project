package com.busanit501.shoppingweb_project.repository;

import com.busanit501.shoppingweb_project.domain.Address;
import com.busanit501.shoppingweb_project.domain.CartItem;
import com.busanit501.shoppingweb_project.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Address findByMemberAndIsDefaultTrue(Member member);

    List<Address> findByMember(Member member);

    // 기본 배송지 초기화
    @Modifying
    @Query("update Address a set a.isDefault = false where a.member.memberId = :memberId")
    void resetDefaultAddress(String memberId);

}

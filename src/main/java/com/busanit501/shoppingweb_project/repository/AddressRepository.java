package com.busanit501.shoppingweb_project.repository;

import com.busanit501.shoppingweb_project.domain.Address;
import com.busanit501.shoppingweb_project.domain.CartItem;
import com.busanit501.shoppingweb_project.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Address findByMemberAndIsDefaultTrue(Member member);
}

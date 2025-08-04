package com.busanit501.shoppingweb_project.dto;

import com.busanit501.shoppingweb_project.domain.Address;
import com.busanit501.shoppingweb_project.domain.Member;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberDTO {
    private String memberId;
    private String email;
    private String password;
    private String confirmPassword;
    private String userName;
    private String phone;
    private LocalDate birthDate;

    private String zipcode;
    private String addressLine;
    private String addressId;
    private boolean registerAddress;


}

package com.busanit501.shoppingweb_project.dto;

import com.busanit501.shoppingweb_project.domain.Address;
import com.busanit501.shoppingweb_project.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserinfoDTO {
    private String memberId;
    private String email;
    private String userName;
    private String phone;
    private LocalDate birthDate;

    private String addressLine;
    private String addressId;

    public static UserinfoDTO toUserinfoDTO(Member member, Address address){
        return UserinfoDTO.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .userName(member.getUserName())
                .phone(member.getPhone())
                .birthDate(member.getBirthDate())
                .addressLine(address.getAddressLine())
                .addressId(address.getAddressId())
                .build();
    }
}

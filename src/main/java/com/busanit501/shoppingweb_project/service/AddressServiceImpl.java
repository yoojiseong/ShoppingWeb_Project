package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.domain.Address;
import com.busanit501.shoppingweb_project.domain.Member;
import com.busanit501.shoppingweb_project.dto.AddressDTO;
import com.busanit501.shoppingweb_project.repository.AddressRepository;
import com.busanit501.shoppingweb_project.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<Address> getAddressesByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원 정보를 찾을 수 없습니다."));
        return addressRepository.findByMember(member);
    }

    @Override
    public Address addAddress(AddressDTO dto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원 정보를 찾을 수 없습니다."));

        Address address = Address.builder()
                .zipcode(dto.getZipcode())
                .addressLine(dto.getAddressLine())
                .addressId(dto.getAddressId())
                .createdAt(LocalDateTime.now())
                .isDefault(dto.isDefault())  // dto에 기본배송지 여부가 있으면 반영
                .member(member)
                .build();

        // 만약 기본배송지라면 기존 기본배송지 초기화
        if (address.isDefault()) {
            addressRepository.resetDefaultAddress(member.getMemberId());
        }

        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Long addressId, AddressDTO dto, Long authenticatedMemberId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new NoSuchElementException("배송지를 찾을 수 없습니다."));
        if (!address.getMember().getId().equals(authenticatedMemberId)) {
            throw new IllegalArgumentException("본인 주소가 아닙니다.");
        }

        address.setZipcode(dto.getZipcode());
        address.setAddressLine(dto.getAddressLine());
        address.setAddressId(dto.getAddressId());
        // 기본배송지 변경 시 처리
        if (dto.isDefault()) {
            addressRepository.resetDefaultAddress(address.getMember().getMemberId());
            address.setIsDefault(true);
        } else {
            address.setIsDefault(false);
        }
        return addressRepository.save(address);
    }

    @Override
    public void setDefaultAddress(Long memberId, Long addressId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원 정보를 찾을 수 없습니다."));
        addressRepository.resetDefaultAddress(member.getMemberId());
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new NoSuchElementException("배송지를 찾을 수 없습니다."));
        address.setIsDefault(true);
        addressRepository.save(address);
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 배송지를 찾을 수 없습니다."));

        return AddressDTO.builder()
                .id(address.getId())
                .zipcode(address.getZipcode())
                .addressId(address.getAddressId())
                .addressLine(address.getAddressLine())
                .isDefault(address.isDefault())
                .build();
    }
}
package com.busanit501.shoppingweb_project.domain.enums; // 패키지 경로 확인

public enum ProductCategory {
    // ✅ 의류 쇼핑몰 컨셉에 맞는 카테고리로 변경
    TOP("상의"),          // 티셔츠, 셔츠, 블라우스 등
    BOTTOM("하의"),         // 바지, 스커트, 반바지 등
    OUTER("아우터"),         // 자켓, 코트, 가디건 등
    DRESS("원피스"),        // 원피스, 점프수트 등
    SHOES("신발"),          // 운동화, 구두, 샌들 등
    BAG("가방"),           // 백팩, 토트백, 크로스백 등
    ACC("액세서리"),       // 모자, 목걸이, 귀걸이 등
    UNKNOWN("기타/알 수 없음"); // 예외 처리용 또는 기타 품목

    private final String koreanName;

    ProductCategory(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }

    // String 값을 받아서 Enum으로 변환하는 정적 팩토리 메서드
    public static ProductCategory fromKoreanName(String koreanName) {
        for (ProductCategory category : ProductCategory.values()) {
            if (category.getKoreanName().equals(koreanName)) {
                return category;
            }
        }
        return UNKNOWN; // 일치하는 이름이 없을 경우
    }
}
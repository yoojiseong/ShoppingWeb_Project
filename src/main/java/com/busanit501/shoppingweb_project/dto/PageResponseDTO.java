package com.busanit501.shoppingweb_project.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 페이징 처리 결과를 담는 DTO (Data Transfer Object)
 * 역할: 서버에서 클라이언트로 페이징된 데이터와 함께, UI 렌더링에 필요한 모든 정보를 담아 보내는 규격화된 상자.
 * 특징: 제네릭 타입 <E>를 사용하여, ProductDTO, ReviewDTO 등 어떤 종류의 DTO 목록이든 담을 수 있는 범용 클래스로 설계.
 *
 * (개발자의 생각) "이 DTO 하나만 있으면, 상품 목록을 담당하는 주현씨든, 리뷰 목록을 담당하는 다른 팀원이든
 * Controller에서 이 객체를 Model에 담아 View로 전달하기만 하면 돼. 그러면 View(HTML)에서는
 * 이 안에 있는 정보를 꺼내서 페이지네이션 UI를 그릴 수 있지. 서버와 화면의 역할을 명확히 분리하는 거야."
 */
@Data
public class PageResponseDTO<E> {

    // (상호작용) ProductService, ReviewService 등 각 서비스 계층에서 DB에서 가져온 Entity 목록을
    // DTO 목록으로 변환한 결과물이 여기에 담깁니다. (예: List<ProductResponseDto>)
    private List<E> dtoList;

    // (이유) "전체 데이터가 몇 개인지 알아야 전체 페이지 수를 계산할 수 있으니까 꼭 필요한 정보야."
    private long totalCount;

    // (이유) "UI에서 '1, 2, 3, ..., 10' 같은 페이지 번호 버튼들을 만들려면 이 숫자 목록이 필요해."
    private List<Integer> pageNumList;

    // (상호작용) 클라이언트가 보냈던 PageRequestDTO를 그대로 담아서 View로 보냅니다.
    // (이유) "View에서 현재 페이지가 몇 페이지인지, 사이즈가 몇인지 알아야 'active' 클래스를 적용하거나
    // 검색 조건을 그대로 유지한 채로 다음 페이지로 이동하는 링크를 만들 수 있기 때문이야."
    private PageRequestDTO pageRequestDTO;

    // (이유) "페이지가 1보다 크면 '이전' 버튼을, 마지막 페이지가 아니면 '다음' 버튼을 보여줘야 하잖아.
    // 그걸 판단하기 위한 boolean 값이야. View에서는 이 값으로 if문을 쓰면 되겠지."
    private boolean prev;
    private boolean next;

    // (이유) "페이지네이션 UI를 '1-10', '11-20' 단위로 보여주기 위해 필요한 시작 번호와 끝 번호야."
    private int start;
    private int end;

    /**
     * 생성자: 페이징 계산에 필요한 모든 핵심 로직을 여기서 수행.
     * (개발자의 생각) "서비스 로직에서 이런 복잡한 계산을 하게 두면 안돼.
     * 응답 객체는 스스로를 완성할 책임을 져야 해. 생성자에서 모든 계산을 끝내서,
     * 서비스 계층에서는 new PageResponseDTO(...) 호출 한 번으로 모든 게 끝나도록 만들자."
     *
     * @param dtoList 실제 데이터 목록
     * @param totalCount 전체 데이터 개수
     * @param pageRequestDTO 현재 페이지 요청 정보
     */
    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, long totalCount, PageRequestDTO pageRequestDTO) {
        this.dtoList = dtoList;
        this.totalCount = totalCount;
        this.pageRequestDTO = pageRequestDTO;

        // --- 페이지네이션 핵심 계산 로직 ---
        // 1. 끝 페이지 번호 (end) 계산
        // (설명) 현재 페이지가 7이면, Math.ceil(0.7) * 10 = 10. 현재 페이지가 13이면, Math.ceil(1.3) * 10 = 20.
        this.end = (int)(Math.ceil(pageRequestDTO.getPage() / 10.0)) * 10;

        // 2. 시작 페이지 번호 (start) 계산
        // (설명) 끝 번호가 10이면 시작은 1. 끝 번호가 20이면 시작은 11.
        this.start = this.end - 9;

        // 3. 진짜 마지막 페이지 번호 계산 및 보정
        // (설명) 전체 데이터가 78개고, 사이즈가 10이면, 마지막 페이지는 8.
        int last = (int)(Math.ceil(totalCount / (double)pageRequestDTO.getSize()));
        // (설명) 계산된 끝 번호(end)가 실제 마지막 페이지(last)보다 크면, 마지막 페이지로 값을 보정해준다.
        this.end = Math.min(end, last);

        // 4. 이전(prev)/다음(next) 링크 존재 여부 계산
        this.prev = this.start > 1;
        this.next = totalCount > (long)this.end * pageRequestDTO.getSize();

        // 5. 페이지 번호 목록(pageNumList) 생성
        // (설명) 위에서 계산된 start와 end를 기준으로 숫자 목록을 스트림으로 생성.
        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
package com.busanit501.shoppingweb_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.net.URLEncoder;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDTO<T> {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private int total;

    private int start;
    private int end;
    private boolean prev;
    private boolean next;

    private List<T> dtoList;

    private String category;
    private String keyword;
    private String link;



    public void setTotal(int total) {
        this.total = total;

        // 페이지 계산 로직
        int tempEnd = (int)(Math.ceil(this.page / 10.0)) * 10;
        int start = tempEnd - 9;
        int realEnd = (int)(Math.ceil((total) / (double) size));

        this.start = start;
        this.end = Math.min(tempEnd, realEnd);
        this.prev = this.start > 1;
        this.next = this.end < realEnd;
    }

    public String getLink() {
        if (link == null || link.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);

            if (keyword != null && !keyword.isEmpty()) {
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                } catch (Exception e) {
                    // 무시
                }
            }
            link = builder.toString();
        }
        return link;
    }


}

package com.busanit501.shoppingweb_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    private int page = 1;
    private int size = 10;

    private String category;
    private String keyword;

    private Long productId;

    public int getSkip() {
        return (page - 1) * size;
    }

    public Pageable getPageable() {
        return PageRequest.of(page - 1, size);
    }

    public String getLink() {
        StringBuilder builder = new StringBuilder();
        builder.append("page=").append(this.page);
        builder.append("&size=").append(this.size);

        if (category != null && !category.isEmpty()) {
            builder.append("&category=").append(category);
        }

        if (keyword != null && !keyword.isEmpty()) {
            builder.append("&keyword=").append(keyword);
        }
        if(productId != null){
            builder.append("&productId=").append(productId);
        }

        return builder.toString();
    }
}
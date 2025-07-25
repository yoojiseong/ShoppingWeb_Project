package com.busanit501.shoppingweb_project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class ReviewRequestDto {
    private String reviewContent;
    private int rating;
}
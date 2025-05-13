package com.app.authjwt.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {
    private String name;
    private List<BoardColumnDto> column;
}

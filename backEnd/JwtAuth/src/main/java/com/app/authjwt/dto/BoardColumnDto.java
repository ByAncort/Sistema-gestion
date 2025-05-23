package com.app.authjwt.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardColumnDto {
    private Long id;
    private String name;
    private Integer position;
}

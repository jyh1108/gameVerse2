package com.example.gameverse2.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {

    private String boardTitle;

    private String boardText;

    private int boardCode; //코드 롤,옵치 종류 등등

    private int tag; //태그 자유,팁 등등

}

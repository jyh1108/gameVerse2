package com.example.gameverse2.domain.board.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {
    @NotEmpty(message="제목은 필수항목입니다.")
    private String boardTitle;
    @NotEmpty(message="내용은 필수항목입니다.")
    private String boardText; //내용
    @NotEmpty(message="종류 필수항목입니다.")
    private String boardCode; // 코드 
    @NotEmpty(message="태그는 필수항목입니다.")
    private String tag; //태그 자유,팁 등등

}

package com.example.gameverse2.domain.reply.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCreateForm {
    @NotEmpty(message="내용 필수항목입니다.")
    private String replyContent; //댓글 내용

}

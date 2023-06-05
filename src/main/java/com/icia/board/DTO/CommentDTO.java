package com.icia.board.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentDTO {
    private Long id;
    private String comment_writer;
    private String comment_contents;
    private Long board_id;

}

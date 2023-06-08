package com.icia.board.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardFIleDTO {
    private Long id;
    private String original_file_name;
    private String stored_file_name;
    private Long boardId;
}

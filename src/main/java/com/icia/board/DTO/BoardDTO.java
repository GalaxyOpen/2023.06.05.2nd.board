package com.icia.board.DTO;

import com.icia.board.Entity.BoardEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder

@AllArgsConstructor
@NoArgsConstructor
//builder 패턴 쓰려면 @AllArgs와 @NoArgs가 필요하다.
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private LocalDateTime createdAt;
    private int boardHits;


    public static BoardDTO toDTO(BoardEntity boardEntity) {
//        BoardDTO boardDTO = new BoardDTO();
//        boardDTO.setId(boardEntity.getId());
//        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
//        boardDTO.setBoardPass(boardEntity.getBoardPass());
//        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
//        boardDTO.setBoardContents(boardEntity.getBoardContents());
//        boardDTO.setBoardHits(boardEntity.getBoardHits());
//        boardDTO.setCreatedAt(boardEntity.getCreatedAt());
//        return boardDTO;
//builder 패턴 1 1이 더 가독성이 좋나?
            return BoardDTO.builder()
                    .id(boardEntity.getId())
                    .boardWriter(boardEntity.getBoardWriter())
                    .boardTitle(boardEntity.getBoardTitle())
                    .boardPass(boardEntity.getBoardPass())
                    .boardContents(boardEntity.getBoardContents())
                    .boardHits(boardEntity.getBoardHits())
                    .createdAt(boardEntity.getCreatedAt())
                    .build();
            //builder 패턴2 1=2이다. 2가 더 가독성이 좋냐?
    }
}

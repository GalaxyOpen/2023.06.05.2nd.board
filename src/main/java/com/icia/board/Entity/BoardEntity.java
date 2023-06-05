package com.icia.board.Entity;


import com.icia.board.DTO.BoardDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "board_table")
@Getter @Setter
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String boardWriter;

    @Column(length = 50, nullable = false)
    private String boardTitle;

    @Column(length = 20, nullable = false)
    private String boardPass;

    @Column(length = 500)
    private String boardContents;

    @Column
    private int boardHits;
    // default=0을 적용하는 법도 있긴하지만 불편한 점이 있어 쓰지 않음.

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    // 따로 아래 엔터티에서 넣을 필요 없음. 알아서 입력됨.

    public static BoardEntity toSaveEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        return boardEntity;
    }

    public static BoardEntity toUpdateEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(boardDTO.getId());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardHits(boardDTO.getBoardHits());
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setCreatedAt(boardDTO.getCreatedAt());
        return boardEntity;
    }
}
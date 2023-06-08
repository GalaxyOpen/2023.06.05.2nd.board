package com.icia.board.Entity;

import com.icia.board.DTO.CommentDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="comment_table")
public class CommentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=20, nullable=false)
    private String commentWriter;

    @Column(length=200, nullable = false)
    private String commentContents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="boardId")
    private BoardEntity boardEntity;
    // board_id는 boardEntity에 있어서 쓰려면 boardEntity에서 꺼내야 한다.
    // 그리고 엔터티 입장에서 boardid는 boardEntity이다.

    public static CommentEntity toSaveEntity(BoardEntity boardEntity, CommentDTO commentDTO) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setCommentWriter(commentDTO.getCommentWriter());
        commentEntity.setCommentContents(commentDTO.getCommentContents());
        commentEntity.setBoardEntity(boardEntity);
        return commentEntity;
    }

}

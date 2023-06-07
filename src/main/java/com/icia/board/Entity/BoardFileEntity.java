package com.icia.board.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="board_file_table")
public class BoardFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    // 엔터티 간의 참조 관계가 이루어질려면 어떻게 해야 하는가!

    @ManyToOne(fetch = FetchType.LAZY)
    // 자식 테이블에 정의하는 방식
    // 자식에겐 ManyToOne이, 부모에겐 OneToMany 가 붙는다.
    // 부모 테이블(사용자)는 여러 자식 테이블(게시글)을 만들 수 있다.
    // fetch = FetchType.Lazy
    // 부모데이터를 가져올 때, 자식 데이터 필요할 떄 씀(fetch=FetchType.Lazy).
    // 필요하든 필요하지 않든 부모데이터 가져오면서 자식데이터를 가져올 떄 씀(fetch = FetchType.eager)
    @JoinColumn(name = "board_id") // 실제 DB에 생성되는 참조 컬럼의 이름
    private BoardEntity boardEntity; // 부모 엔터티 타입으로 정의


    public static BoardFileEntity toSaveBoardFileEntity(BoardEntity savedEntity, String originalFileName, String storedFileName) {
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setBoardEntity(savedEntity);
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        return boardFileEntity;
    }
}

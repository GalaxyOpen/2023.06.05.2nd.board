package com.icia.board;

import com.icia.board.Entity.BoardEntity;
import com.icia.board.Repository.BoardRepository;
import com.icia.board.Service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class BoardTest {
    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @Transactional
    @DisplayName("참조 관계 확인")
    public void test1(){
        BoardEntity boardEntity = boardRepository.findById(1L).get();
        // boardEntity로 첨부된 파일의 이름 조회하기(부모 엔터티에서 자식 엔터티를 조회하는 경우 Transactional 어노테이션 필요)
        // DB에서 조회한 데이터

        System.out.println("첨부파일 이름 = " + boardEntity.getBoardFileEntityList().get(0).getOriginalFileName());
        // boardEntity 객체의 보드파일 엔터티 리스트의 0번 객체(1번째 방)의 저장된 이름을 조회
        // 위의 구문만 잘 이해하면 부모 게시글 데이터만 조회하면 댓글 리스트를 그냥 바로 가져올 수도 있음.
    }
}

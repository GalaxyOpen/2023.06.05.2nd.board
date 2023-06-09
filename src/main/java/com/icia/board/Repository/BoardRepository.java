package com.icia.board.Repository;

import com.icia.board.Entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    // update board_table set board_hits=board_hits+1 where id = ? 가 필요한 상황.
    // Jpa에서 제공하는 거로 만들려면 매우 어려움(잘 적용이 안됨).
    // 그래서 따로 구문을 쓰려면 jpql이 필요함.
    // jpql(Java persistence query language): 필요한 쿼리문을 직접 사용할 때 사용.
    @Modifying
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id")
    //아래랑 비교했을 때 이걸 많이 쓰긴 함.
    //@Query(value = "update board_table b set b.board_hits=b.board_hits+1 where b.id=:id", nativeQuery = true)

    // update 다음에는 테이블 이름이 와야하지만 이 문법에서는 무조건 Entity이름이 와야함.
    // 그리고 약어를 써줘야 함.(ex : b)
    void updateHits(@Param("id")Long id);

    // 만약에, 제목으로 검색하는 경우라면(검색 메소드)
    // select *from board_table where board_title like '%q%'
    // 그거 ㄹ아래에 그대로 쓰면
    // Containing이 like의 역할을 함
    //제목으로 검색한 결과
    List<BoardEntity> findByBoardTitleContaining(String q);

    //작성자로 검색한 결과
    List<BoardEntity> findByBoardWriterContaining(String q);

    // 작성자로 검색한 결과 페이징
    Page<BoardEntity> findByBoardWriterContaining(String q, Pageable pageable);
    // 제목으로 검색한 결과 페이징
    Page<BoardEntity> findByBoardTitleContaining(String q, Pageable pageable);


    // 제목으로 검색한 결과를 id 기준 내림차순 정렬
    List<BoardEntity> findByBoardTitleContainingOrderByIdDesc(String q);

    // 작성자 또는 제목에 검색어가 포함된 결과 조회
    // select *from board_table where board_title like '%q%' or board_writer like '%q'
    List<BoardEntity> findByBoardTitleContainingOrOrBoardWriterContaining(String q1, String q2);

    // 제목이나 작성자가 포함된 정렬
    List<BoardEntity> findByBoardTitleContainingOrOrBoardWriterContainingOrderByIdDesc(String q1, String q2);
}

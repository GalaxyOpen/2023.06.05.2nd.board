package com.icia.board.Repository;

import com.icia.board.Entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}

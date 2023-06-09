package com.icia.board;

import com.icia.board.DTO.BoardDTO;
import com.icia.board.Entity.BoardEntity;
import com.icia.board.Repository.BoardRepository;
import com.icia.board.Service.BoardService;
import com.icia.board.Util.UtilClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

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

    @Test
    @Transactional
    @DisplayName("엔티티 클래스 ToString")
    public void entityToString(){
        BoardEntity boardEntity = boardRepository.findById(1L).get();
        System.out.println("boardEntity = " +boardEntity);
    }
    // 엔터티를 ToString으로 찍으면 안됨.

    @Test
    @Transactional
    @DisplayName("findAll할 떄 정렬해서 가져오기")
    public void findAllOrderBy(){
        List<BoardEntity> boardEntityList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "boardWriter"));
        boardEntityList.forEach(boardEntity -> {
            System.out.println(BoardDTO.toDTO(boardEntity));
        });
    }
    // paging test를 위해 DB를 다량으로 넣을 거임.
    private BoardDTO newBoard(int i){
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setBoardTitle("title"+i);
        boardDTO.setBoardWriter("writer"+i);
        boardDTO.setBoardContents("contents"+i);
        boardDTO.setBoardPass("pass"+i);
        return boardDTO;
    }
    @Test
    @Transactional
    @Rollback(value = false)
    @DisplayName("DB에 데이터 붓기")
    public void saveList(){
        IntStream.rangeClosed(1, 50).forEach(i->{
//            try {
//                boardService.save(newBoard(i));
                boardRepository.save(BoardEntity.toSaveEntity(newBoard(i)));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        });
    }
    @Test
    @Transactional
    @DisplayName("페이징 객체 메서드 확인")
    public void pagingMethod(){
        int page = 0; // 요청한 페이지 번호
        int pageLimit = 3; // 한 페이지당 보여줄 글 갯수
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        // 단순 List 객체가 아닌 Page 객체로 리턴을 준다.
        // 파라미터는 총 3개. page, pageLimit
        // 무엇을 기준으로 몇 페이지부터 몇 개를 볼 것인가? - 페이징의 기본 로직
        // Page 객체가 제공해주는 메서드 확인

        // boardEntities.hasPrevious() = true => 이 이야기는 우리가 보고 있는 페이지 이전 페이지가 있다는 뜻이다.
        //
        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫페이지인지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막페이지인지 여부

        //Page<BoardEntity> -> Page<BoardDTO> 로 변환해야함.
        // 방법
        Page<BoardDTO> boardList=boardEntities.map(boardEntity ->
                    BoardDTO.builder()
                    .id(boardEntity.getId())
                    .boardWriter(boardEntity.getBoardWriter())
                    .boardTitle(boardEntity.getBoardTitle())
                    .boardHits(boardEntity.getBoardHits())
                    .createdAt(UtilClass.dateFormat(boardEntity.getCreatedAt()))
                    .build()
        );
        System.out.println("boardEntities.getContent() = " + boardList.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardEntities.getTotalElements() = " + boardList.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardList.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardEntities.getTotalPages() = " + boardList.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardList.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardEntities.hasPrevious() = " + boardList.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardList.isFirst()); // 첫페이지인지 여부
        System.out.println("boardEntities.isLast() = " + boardList.isLast()); // 마지막페이지인지 여부
    }

    @Test
    @Transactional
    @DisplayName("검색 기능 테스트")
    public void searchTest(){
//        List<BoardEntity> boardEntityList = boardRepository.findByBoardTitleContaining("2");
        String q = "30";
        List<BoardEntity> boardEntityList = boardRepository.findByBoardTitleContainingOrOrBoardWriterContainingOrderByIdDesc(q, q);
        // 둘 다 q 값을 같은 값을 주어야 한다
        // 왜? 제목이나 작성자 둘다 30이 이썽야 하기에

        boardEntityList.forEach(boardEntity -> {
            System.out.println(BoardDTO.toDTO(boardEntity));
        });
    }
    @Test
    @Transactional
    @DisplayName("작성자로 검색 기능 테스트")
    public void searchWriterTest(){
        List<BoardEntity> boardEntityList = boardRepository.findByBoardWriterContaining("2");
        boardEntityList.forEach(boardEntity -> {
            System.out.println(BoardDTO.toDTO(boardEntity));
        });
    }
// Sort.by(Sort.Direction.DESC, "id")
@Test
@Transactional
@DisplayName("검색 기능 정렬 테스트")
public void searchSortTest(){
    List<BoardEntity> boardEntityList = boardRepository.findByBoardTitleContainingOrderByIdDesc("2" );
    boardEntityList.forEach(boardEntity -> {
        System.out.println(BoardDTO.toDTO(boardEntity));
    });
}
    @Test
    @Transactional
    @DisplayName("검색 결과 페이징")
    public void searchPaging(){
        String q="2";
        int page = 0;
        int pageLimit =3;
        Page<BoardEntity> boardEntities = boardRepository.findByBoardWriterContaining(q, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        Page<BoardDTO> boardList=boardEntities.map(boardEntity ->
                BoardDTO.builder()
                        .id(boardEntity.getId())
                        .boardWriter(boardEntity.getBoardWriter())
                        .boardTitle(boardEntity.getBoardTitle())
                        .boardHits(boardEntity.getBoardHits())
                        .createdAt(UtilClass.dateFormat(boardEntity.getCreatedAt()))
                        .build()
        );
        System.out.println("boardEntities.getContent() = " + boardList.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardEntities.getTotalElements() = " + boardList.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardList.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardEntities.getTotalPages() = " + boardList.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardList.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardEntities.hasPrevious() = " + boardList.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardList.isFirst()); // 첫페이지인지 여부
        System.out.println("boardEntities.isLast() = " + boardList.isLast()); // 마지막페이지인지 여부
    }

}

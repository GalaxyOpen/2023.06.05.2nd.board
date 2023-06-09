package com.icia.board.Service;

import com.icia.board.DTO.BoardDTO;
import com.icia.board.Entity.BoardEntity;
import com.icia.board.Entity.BoardFileEntity;
import com.icia.board.Repository.BoardFileRepository;
import com.icia.board.Repository.BoardRepository;
import com.icia.board.Util.UtilClass;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor

public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    public Long save(BoardDTO boardDTO) throws IOException {
// 기본 세이브(파일 첨부 배우기 전)
//        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
//        return boardRepository.save(boardEntity).getId();

//        if (boardDTO.getBoardFile().isEmpty())
            if (boardDTO.getBoardFile()==null || boardDTO.getBoardFile().get(0).isEmpty()){
            //파일 없음
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
            return boardRepository.save(boardEntity).getId();

        } else {
                // 파일 있음
                // 1. Board 테이블에 데이터를 먼저 저장
                BoardEntity boardEntity = BoardEntity.toSaveEntityWithFile(boardDTO);
                BoardEntity savedEntity = boardRepository.save(boardEntity);
                //Question : 위 두 엔터티의 차이는?
                // answer : 파일이 있고 없고의 차이

                //2-1. 파일 이름 꺼내고, 저장용 이름 만들고 파일 로컬에 저장()일
//            String originalFileName = boardDTO.getBoardFile().getOriginalFilename();
//            String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
//            String savePath = "D:\\springboot_img\\" + storedFileName;
//            boardDTO.getBoardFile().transferTo(new File(savePath));

                //2-2. 파일 이름 꺼내고, 저장용 이름 만들고 파일 로컬에 저장.
                for (MultipartFile boardFile : boardDTO.getBoardFile()) {
                    String originalFileName = boardFile.getOriginalFilename();
                    String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
                    String savePath = "D:\\springboot_img\\" + storedFileName;
                    boardFile.transferTo(new File(savePath));

                    //3. BoardFileEntity로 변환 후 board_file_table에 저장
                    // 자식 데이터를 저장할 때 반드시 부모의 id가 아닌 부모의 Entity 객체가 전달되어야 함.
                    // JPA를 썻을 때 주의할 점은 Entity 단위(값) 로 움직인다는 것.
                    BoardFileEntity boardFileEntity =
                            BoardFileEntity.toSaveBoardFileEntity(savedEntity, originalFileName, storedFileName);
                    boardFileRepository.save(boardFileEntity);

                }
                return savedEntity.getId();
            }

    }

    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
//        for (BoardEntity boardEntity:boardEntityList){
//            boardDTOList.add(BoardDTO.toDTO(boardEntity));
//        } 아래 문장(28~30)과 같음.
// 25번 줄이 29번 줄과 같음 // for(BoardEntity boardEntity:boardEntityList) = boardEntityList.forEach
        boardEntityList.forEach(boardEntity -> {
            boardDTOList.add(BoardDTO.toDTO(boardEntity));
        });
        return boardDTOList;
    }
    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    @Transactional
    public BoardDTO findById(Long id) {
       BoardEntity boardEntity=boardRepository.findById(id).orElseThrow(()-> new NoSuchElementException());
        return BoardDTO.toDTO(boardEntity);
    }

    public void update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
    }
    public BoardDTO detailAxios(Long id){
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(()-> new NoSuchElementException("글이 없습니다."));
       return BoardDTO.toDTO(boardEntity);
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }


    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() -1;
        int pageLimit = 5;
        Page<BoardEntity> boardEntities =
        boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        // 몇 페이지 볼것인지, 몇개를 볼건지, 어떤 정렬로 볼것인지를 정해서 모두 보여주는 형태의 findAll로 해서
        Page<BoardDTO> boardDTOS = boardEntities.map(boardEntity -> BoardDTO.builder()
                                                .id(boardEntity.getId())
                                                .boardTitle(boardEntity.getBoardTitle())
                                                .boardWriter(boardEntity.getBoardWriter())
                                                .createdAt(UtilClass.dateFormat(boardEntity.getCreatedAt()))
                                                .boardHits(boardEntity.getBoardHits())
                                                .build());
        return boardDTOS;
    }
}

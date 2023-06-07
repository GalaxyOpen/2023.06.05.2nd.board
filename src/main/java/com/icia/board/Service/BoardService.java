package com.icia.board.Service;

import com.icia.board.DTO.BoardDTO;
import com.icia.board.Entity.BoardEntity;
import com.icia.board.Repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor

public class BoardService {
    private final BoardRepository boardRepository;
    public Long save(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
        return boardRepository.save(boardEntity).getId();
    }

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
}

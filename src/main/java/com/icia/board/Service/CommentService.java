package com.icia.board.Service;

import com.icia.board.DTO.CommentDTO;
import com.icia.board.Entity.BoardEntity;
import com.icia.board.Entity.CommentEntity;
import com.icia.board.Repository.BoardRepository;
import com.icia.board.Repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    @Transactional
    public Long save(CommentDTO commentDTO) {
        BoardEntity boardEntity=boardRepository.findById(commentDTO.getBoardId()).orElseThrow(()->new NoSuchElementException());
        CommentEntity commentEntity = CommentEntity.toSaveEntity(boardEntity, commentDTO);
        return commentRepository.save(commentEntity).getId();
    }
    @Transactional
    public List<CommentDTO> findAll(Long boardId) {
        // 1. BoardEntity에서 댓글 목록 가져오기
        BoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(()-> new NoSuchElementException());
//        List<CommentEntity> commentEntityList = boardEntity.getCommentEntityList();
        //2. commentRepository에서 가져오기
        // 네이티브 쿼리를 쓸거면 select*from comment_table where board_id =? 를 써야함.
        // 우리는 BoardEntity를 저장했지, board_id를 저장 한게 아니다.
        // 이게 JPA의 Entity 형식인 것이다.
        List<CommentEntity> commentEntityList = commentRepository.findByBoardEntityOrderByIdDesc(boardEntity);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        commentEntityList.forEach(comment->{
            commentDTOList.add(CommentDTO.toDTO(comment));
        });
        return commentDTOList;
    }
}

package com.icia.board.DTO;

import com.icia.board.Entity.BoardEntity;
import com.icia.board.Entity.BoardFileEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder

@AllArgsConstructor
@NoArgsConstructor
//builder 패턴 쓰려면 @AllArgs와 @NoArgs가 필요하다.
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private LocalDateTime createdAt;
    private int boardHits;

    //   단일 private MultipartFile boardFile;
    private List<MultipartFile> boardFile;
    private int fileAttached;
    // 단일 private String originalFileName;
    private List<String> originalFileName;
//  단일  private List<String> storedFileName;
    private List<String> storedFileName;

    public static BoardDTO toDTO(BoardEntity boardEntity) {
        //builder 패턴1
// Question : 1이 더 가독성이 좋나?
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setCreatedAt(boardEntity.getCreatedAt());

        //파일 여부에 다른 코드 추가
        if(boardEntity.getFileAttached()==1){
            //파일 있음.
            boardDTO.setFileAttached(1);
// 단일코드     boardDTO.setOriginalFileName(boardEntity.getBoardFileEntityList().get(0).getOriginalFileName());
// 단일코드     boardDTO.setStoredFileName(boardEntity.getBoardFileEntityList().get(0).getStoredFileName());

            //파일 이름을 담을 리스트 객체 선언
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();

            //첨부 파일에 각각 접근
            for(BoardFileEntity boardFileEntity: boardEntity.getBoardFileEntityList()){
                originalFileNameList.add(boardFileEntity.getOriginalFileName());
                storedFileNameList.add(boardFileEntity.getStoredFileName());
            }
            boardDTO.setOriginalFileName(originalFileNameList);
            boardDTO.setStoredFileName(storedFileNameList);

        }else{
            // 파일 없음.
            boardDTO.setFileAttached(0);
        }

        return boardDTO;

//            return BoardDTO.builder()
//                    .id(boardEntity.getId())
//                    .boardWriter(boardEntity.getBoardWriter())
//                    .boardTitle(boardEntity.getBoardTitle())
//                    .boardPass(boardEntity.getBoardPass())
//                    .boardContents(boardEntity.getBoardContents())
//                    .boardHits(boardEntity.getBoardHits())
//                    .createdAt(boardEntity.getCreatedAt())
//                    .build();
            //builder 패턴2 1=2이다. 2가 더 가독성이 좋냐?
    }
}

package com.icia.board.Controller;

import com.icia.board.DTO.BoardDTO;
import com.icia.board.DTO.CommentDTO;
import com.icia.board.Service.BoardService;
import com.icia.board.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor

public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/board/save")
    public String saveForm(){
        return "/boardPages/boardSave";
    }

    @PostMapping("/board/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException{
        boardService.save(boardDTO);
        return "redirect:/board/";
    }
    @GetMapping("/board/")
    public String findAll(Model model){
        List<BoardDTO> boardDTOList =boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "/boardPages/boardList";
    }

    // 우리가 요청 받는 주소 값의 형태는 /board?page=1
    @GetMapping("/board")
    public String paging(@PageableDefault(page=1)Pageable pageable, Model model){
//        System.out.println("pageable = " + pageable);
        System.out.println("page="+pageable.getPageNumber());
        Page<BoardDTO> boardDTOS =boardService.paging(pageable);
        model.addAttribute("boardList", boardDTOS);
        // 시작페이지(startPage), 마지막페이지(endPage)값 계산
        // 하단에 보여줄 페이지 갯수 3개
        // PageDTO 없이 페이징 처리할 예정.
        // 원한다면 Service에서 만들거 다 만들어서 할 수도 있음.
        int blockLimit = 3;
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < boardDTOS.getTotalPages()) ? startPage + blockLimit - 1 : boardDTOS.getTotalPages();

//        if((startPage + blockLimit - 1) < boardDTOS.getTotalPages()){
//          endPage = startPage + blockLimit - 1;
//        }else{
//            endPage = boardDTOS.getTotalPages();
//        }
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "/boardPages/boardPaging";
    }

    @GetMapping("/board/{id}")
    public String findById(@PathVariable Long id, @RequestParam("page") int page, Model model){
        boardService.updateHits(id);
        model.addAttribute("page",page);
        try{
            BoardDTO boardDTO = boardService.findById(id);
            model.addAttribute("board", boardDTO);
            List<CommentDTO> commentDTOList = commentService.findAll(id);
            if (commentDTOList.size() > 0) {
                model.addAttribute("commentList", commentDTOList);
            }else{
                model.addAttribute("commentList", null);
            }
            return "/boardPages/boardDetail";
        } catch(NoSuchElementException e){
            return "/boardPages/boardNotFound";
        }

    }

    @GetMapping("/board/axios/{id}")
    public ResponseEntity detailAxios(@PathVariable("id") Long id) throws Exception {
        BoardDTO boardDTO = boardService.findById(id);
        return new ResponseEntity<>(boardDTO,HttpStatus.OK);
    }
    @GetMapping("/board/update/{id}")
    public String updateForm(@PathVariable Long id, Model model){
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "/boardPages/boardUpdate";
    }
//    @PostMapping("/board/update/{id}")
//    public String update(@ModelAttribute BoardDTO boardDTO){
//        BoardService.update(boardDTO);
//        return "redirect:/board/";
//    }
    @PutMapping("/board/{id}")
    public ResponseEntity update(@RequestBody BoardDTO boardDTO){
        boardService.update(boardDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/board/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        boardService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.icia.board.Controller;

import com.icia.board.DTO.BoardDTO;
import com.icia.board.Service.BoardService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/board/{id}")
    public String findById(@PathVariable Long id, Model model){
        boardService.updateHits(id);
        BoardDTO boardDTO = null;
        try{
            boardDTO = boardService.findById(id);
        } catch(NoSuchElementException e){
            return "/boardPages/boardNotFound";
        }
        model.addAttribute("board", boardDTO);
        return "/boardPages/boardDetail";
    }
//    @GetMapping("/board/{id}")
//    public String detail(@PathVariable Long id, Model model){
//        BoardDTO boardDTO = boardService.findById(id);
//        model.addAttribute("board", boardDTO);
//        return "/boardPages/boardDetail";
//    }
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

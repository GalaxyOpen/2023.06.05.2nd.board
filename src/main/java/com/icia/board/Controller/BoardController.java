package com.icia.board.Controller;

import com.icia.board.DTO.BoardDTO;
import com.icia.board.Service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String save(@ModelAttribute BoardDTO boardDTO){
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
            return "boardPages/boardNotFound";
        }
        model.addAttribute("board", boardDTO);
        return "/boardPages/boardDetail";
    }
//    @GetMapping("/board/{id}")
//    public String detail(@PathVariable Long id, Model model){
//        BoardDTO boardDTO = boardService.findById(id);
//        model.addAttribute("board", boardDTO);
//        return "/boardPages/boardDetail"
//    }
    @PutMapping("/board/{id}")
    public ResponseEntity update(@RequestBody BoardDTO boardDTO){
        boardService.update(boardDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
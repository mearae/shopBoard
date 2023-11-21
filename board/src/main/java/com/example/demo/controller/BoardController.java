package com.example.demo.controller;

import com.example.demo.DTO.BoardDto;
import com.example.demo.sevice.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
//@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/")
    public String home(){
        return "createBoard";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDto boardDto){
        System.out.println(boardDto.getTitle() + " : " + boardDto.getContents());
        boardService.saveBoard(boardDto);
        return "";
    }
}

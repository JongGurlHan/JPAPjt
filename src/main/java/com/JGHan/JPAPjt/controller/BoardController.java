package com.JGHan.JPAPjt.controller;

import com.JGHan.JPAPjt.model.Board;
import com.JGHan.JPAPjt.repository.BoardRepository;
import com.JGHan.JPAPjt.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardValidator boardValidator;

    @GetMapping("/list")
    public String list(Model model){
        List<Board> boards = boardRepository.findAll();
        model.addAttribute("boards", boards);
        return "board/list";
    }

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false)Long id){
        if(id == null){
            model.addAttribute("board", new Board());
        }else{
            Board board = boardRepository.findById(id).orElse(null); //조회된 값이 없으면 null처리
            model.addAttribute("board", board);

        }
        return "board/form";
    }


    @PostMapping("/form")
    public String form(@Valid @ModelAttribute Board board, BindingResult result) {
        boardValidator.validate(board, result);//(어떤 클래스를 체크할건지, BindingResult)

        if(result.hasErrors()){
            return "board/form";
        }
        boardRepository.save(board);
        return "redirect:/board/list";
    }


}

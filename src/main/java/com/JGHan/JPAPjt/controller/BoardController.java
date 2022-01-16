package com.JGHan.JPAPjt.controller;

import com.JGHan.JPAPjt.model.Board;
import com.JGHan.JPAPjt.repository.BoardRepository;
import com.JGHan.JPAPjt.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public String list(Model model, Pageable pageable){
        //Jpa페이지처리, 리턴타입이 Page다. JPA는 기본적으로 페이지가 0부터 시작
        Page<Board> boards = boardRepository.findAll(pageable);
        //boards.getTotalElements(); //총 게시글 수

        //시작 페이지 넘버 구하기, 최소값을 1으로 지정
        int startPage = Math.max(1, boards.getPageable().getPageNumber() -4); //현재페이지 페이지번호를 가져온다.
        //끝 페이지 넘버 구하기 현재 페이지 +4로 하고,
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() +4);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
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

package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import net.bytebuddy.asm.MemberSubstitution.Substitution.Chain.Step.ForField.Write;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.BoardRepository;

@Controller
public class BoardController {

    @Autowired
    private HttpSession session;

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping({ "/", "/board" })
    public String index() {
        return "index";
    }

    // 글 보기, 상세보기는 로그인 없어도 되지만 글쓰기는 로그인 해야 됨
    @GetMapping({ "/board/saveForm" })
    public String saveForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        return "board/saveForm";
    }

    @PostMapping("/board/save")
    public String save(WriteDTO writeDTO) {

        // 유효성 검사
        if (writeDTO.getTitle() == null || writeDTO.getTitle().isEmpty()) {

            return "redirect:/40x";
        }
        if (writeDTO.getContent() == null || writeDTO.getContent().isEmpty()) {
            return "redirect:/40x";
        }

        // 인증체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        boardRepository.save(writeDTO, sessionUser.getId());
        return "redirect:/";
    }

    // localhost:/board/1
    // localhost:/board/50
    @GetMapping({ "/board/{id}" })
    public String detail(@PathVariable Integer id) {
        return "board/detail";
    }

}

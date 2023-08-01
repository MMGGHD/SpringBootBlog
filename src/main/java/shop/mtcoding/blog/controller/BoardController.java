package shop.mtcoding.blog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.bytebuddy.asm.MemberSubstitution.Substitution.Chain.Step.ForField.Write;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.BoardRepository;

@Controller
public class BoardController {

    @Autowired
    private HttpSession session;

    @Autowired
    private BoardRepository boardRepository;

    // 유효성 검사 필요없음
    // 인증 검사 필요없음
    // @RequestParam << 값이 들어오지 않으면 기본값을 설정할수 있는 어노테이션
    // @RequestParam(defaultValue = "0") Integer page << page에 값 안들어오면 "0" 대입됨
    @GetMapping({ "/", "/board" })
    public String index(@RequestParam(defaultValue = "0") Integer page, HttpServletRequest request) {

        List<Board> boardList = boardRepository.findAll(page);
        System.out.println("테스트 : " + boardList.size());
        System.out.println("테스트 : " + boardList.get(0).getTitle());

        request.setAttribute("boardList", boardList);
        request.setAttribute("prevPage", page - 1);
        request.setAttribute("nextPage", page + 1);
        request.setAttribute("first", page == 0 ? true : false);

        // LastPage는 DB에서 카운트 연산해야함
        request.setAttribute("last", false);

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
    public String detail(@PathVariable Integer id, Model model) {
        Board board = boardRepository.findBoardById(id);
        model.addAttribute("board", board);
        return "board/detail";
    }

}

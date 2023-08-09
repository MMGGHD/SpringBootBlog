package shop.mtcoding.blog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.border.Border;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.bytebuddy.asm.MemberSubstitution.Substitution.Chain.Step.ForField.Write;
import shop.mtcoding.blog.dto.BoardDetailDTO;
import shop.mtcoding.blog.dto.UpdateDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.BoardRepository;
import shop.mtcoding.blog.repository.ReplyRepository;

@Controller
public class BoardController {

    @Autowired
    private HttpSession session;

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable Integer id, HttpServletRequest request) {
        // 1. 인증 검사
        // 2. 권한 체크
        // 3. 핵심 로직
        Board board = boardRepository.findById(id);
        request.setAttribute("board", board);

        return "board/updateForm";
    }

    // 비슷한게 있어도 수정을 위한 DTO를 만들어야 한다.
    @PostMapping("/board/{id}/update")
    public String update(@PathVariable Integer id, UpdateDTO updateDTO) {

        // 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {

            // Error : 401 (인증안됨)
            return "/loginForm";
        }

        // 권한 검사
        if (boardRepository.findById(id).getUser().getId() != sessionUser.getId()) {

            // Error : 403 (권한없음)
            return "/40x";
        }

        // 핵심 로직
        boardRepository.update(updateDTO, id);
        return "redirect:/board/" + id;
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable Integer id) {

        // <요구 사항>
        // 1. @PathVariable
        // 2. 인증검사 (postman으로 때릴수 있기 때문에)
        // 2-1 인증이 안되면 로그인 페이지로
        // 3. 유효성검사 필요 없음
        // 4. BoardRepository.deleteById(id); << 호출, 리턴을 받지 마세요

        // 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {

            // Error : 401 (인증안됨)
            return "/loginForm";
        }

        // 권한 검사
        if (boardRepository.findById(id).getUser().getId() != sessionUser.getId()) {

            // Error : 403 (권한없음)
            return "/40x";
        }

        // 핵심 로직
        boardRepository.deleteById(id);

        return "redirect:/";
    }

    // <스프링에서 쿼리스트링 자동 파싱의 예시>
    // restful API << 주소설계 프로토콜(주소를 사람이봐도 알수 있게 짜야 한다)
    // restful API 조건 변수를 받는 방식 2가지 1) @PathVariable, 2)쿼리스트링
    // 1) @PathVariable 조건은 무조건 PK또는 UK를 찾는다. (프로토콜)
    // 2) 쿼리스트링 << 유일값이 들어가면 안된다. (프로토콜)
    // 쿼리스트링 = 구체적 질의라는 뜻, Where절을 받기 위해서 사용된다.
    // 쿼리스트링 요청이 오면 스프링이 메서드의 매개변수로 자동으로 파싱해준다.
    // 예를들어 localhost:8080?a=1&b=50 이라는 URL요청이 들어온다면
    // 스프링이 아래 hello메서드 매개변수(a,b)안에 ?a=1&b=50을 파싱해서 1,50을 넣어준다.
    // 매개변수가 Wrapper클래스라면 쿼리스트링이 없어도 null값으로 입력된다.

    @ResponseBody
    @GetMapping("/hello")
    public String hello(Integer a, Integer b) {
        System.out.println("a : " + a);
        System.out.println("b : " + b);
        return "ok";
    }

    // 유효성 검사 필요없음
    // 인증 검사 필요없음
    // localhost:8080?page=1
    // @RequestParam << 값이 들어오지 않으면 기본값을 설정할수 있는 어노테이션
    // @RequestParam(defaultValue = "0") Integer page << page에 값 안들어오면 "0" 대입됨
    @GetMapping({ "/", "/board" })
    public String index(@RequestParam(defaultValue = "0") Integer page, HttpServletRequest request) {

        List<Board> boardList = boardRepository.findAll(page);

        // Last 계산하는 과정 (복잡하니 다른메서드로 빼는것이 낫다)

        // LastPage는 DB에서 카운트 연산해야함 (boardRepository.count())
        int totalCount = boardRepository.count();
        // System.out.println("totalCount : " + totalCount);
        int totalPage = totalCount / 3;
        if (totalCount % 3 == 0) {
            totalPage = totalPage - 1;
        }
        boolean last = totalPage == page;

        System.out.println("테스트 : " + boardList.size());
        System.out.println("테스트 : " + boardList.get(0).getTitle());

        // 아래처럼 페이지를 위한 데이터를 페이징 데이터라고 한다.
        request.setAttribute("boardList", boardList);
        request.setAttribute("prevPage", page - 1);
        request.setAttribute("nextPage", page + 1);
        request.setAttribute("first", page == 0 ? true : false);
        request.setAttribute("last", last);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("totalCount", totalCount);

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

    // 권한체크가 필요함
    @GetMapping({ "/board/{id}" })
    public String detail(@PathVariable Integer id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser"); // 세션접근
        List<BoardDetailDTO> dtos = null;
        if (sessionUser == null) {
            // 세션이 없다면
            dtos = boardRepository.findByIdJoinReply(id, null);
        } else {
            dtos = boardRepository.findByIdJoinReply(id, sessionUser.getId());
        }
        boolean pageOwner = false;
        if (sessionUser != null) {
            pageOwner = sessionUser.getId() == dtos.get(0).getBoardUserId();
        }
        request.setAttribute("dtos", dtos);
        request.setAttribute("pageOwner", pageOwner);
        return "board/detail";
    }

}
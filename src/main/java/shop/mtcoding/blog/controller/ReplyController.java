package shop.mtcoding.blog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.ReplyWriteDTO;
import shop.mtcoding.blog.dto.UpdateDTO;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.ReplyRepository;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReplyController {

    @Autowired
    private HttpSession session;

    @Autowired
    private ReplyRepository replyRepository;

    // Reply ORM테스트
    // ORM을 사용하면 다 끌고오기 때문에 쿼리의 질은 좋지 못하다. (Join쿼리가 좋은 코드이다)
    @ResponseBody
    @GetMapping("/test/reply/1")
    public List<Reply> test() {
        List<Reply> reply = replyRepository.findByBoardId(1);
        return reply;
    }

    @PostMapping("/reply/save")
    public String save(ReplyWriteDTO replyWriteDTO) {
        // comment 유효성 검사
        if (replyWriteDTO.getComment() == null || replyWriteDTO.getComment().isEmpty()) {

            return "redirect:/40x";
        }

        // 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 댓글 쓰기
        replyRepository.save(replyWriteDTO, sessionUser.getId());
        return "redirect:/board/" + replyWriteDTO.getBoardId();
    }

    @GetMapping("/reply/{id}/update")
    public String replyUpdate(@PathVariable Integer id, HttpServletRequest request) {
        // 1. 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            // Error : 401 (인증안됨)
            return "/loginForm";
        }
        // 권한 검사
        if (replyRepository.findByReplyId(id).getUser().getId() != sessionUser.getId()) {
            // Error : 403 (권한없음)
            return "/40x";
        }
        // 3. 핵심 로직
        Reply reply = replyRepository.findByReplyId(id);
        request.setAttribute("reply", reply);

        return "board/replyUpdateForm";
    }

    @PostMapping("/board/{id}/replyUpdate")
    public String update(@PathVariable Integer id, String content) {
        // 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            // Error : 401 (인증안됨)
            return "/loginForm";
        }

        Integer replyUserId = replyRepository.findByReplyId(id).getUser().getId();

        // 권한 검사
        if (replyUserId != sessionUser.getId()) {

            // Error : 403 (권한없음)
            return "/40x";
        }

        // 핵심 로직
        replyRepository.replyUpdateById(id, content);

        return "redirect:/board/" + replyRepository.findByReplyId(id).getBoard().getId();
    }

    @GetMapping("/replyDelete/{id}")
    public String replyDelete(@PathVariable Integer id) {
        // 1. 인증 검사
        // 2. 권한 체크
        // 3. 핵심 로직
        int page = replyRepository.findByReplyId(id).getBoard().getId();
        replyRepository.replyDelete(id);
        return "redirect:/board/" + page;
    }
}
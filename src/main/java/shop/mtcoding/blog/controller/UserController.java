package shop.mtcoding.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    // Get요청을 하는 방법
    // 1. a태그 (하이퍼 링크)
    // 2. form태그의 get메서드를 호출

    // get메서드를 호출하려면 요청이 ip주소와 포트번호를 타고 와야함
    // ip주소와 포트 부여: 10.5.9.200:8080 -> 불편하니까 도메인주소(DNS, mtcoding.com:8080)를 구매
    // 이때 포트번호가 80이면 생략가능
    // 자기 ip를 적으면 나갔다 다시 돌아온다 << 그래서 다시 나갔다 다시 돌아올 바에 루프백주소를 쓰면 된다
    @GetMapping({ "/joinForm" })
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping({ "/loginForm" })
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping({ "/user/updateForm" })
    public String updateForm() {
        return "user/updateForm";
    }

    @GetMapping({ "/logout" })
    public String logout() {
        return "redirect:/";
    }
}

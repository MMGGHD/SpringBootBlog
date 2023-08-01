package shop.mtcoding.blog.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.UserRepository;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // request는 가방, session은 락커
    @Autowired
    private HttpSession session;

    @PostMapping({ "/login" })
    public String login(LoginDTO loginDTO) {

        // 유효성 검사 (부가기능)
        if (loginDTO.getUsername() == null || loginDTO.getUsername().isEmpty()) {
            return "redirect:/40x";
        }
        if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
            return "redirect:/40x";
        }

        try {
            // 핵심기능
            User user = userRepository.findByUsernameAndPassword(loginDTO);
            /*
             * <세션을 유지하기>
             * User 정보로 로그인하면 서버측 User정보를 Key로 해서 session 집합에 저장 << setAttribute()
             * 이때 Value값은 JSessionID가 되고 이는 서버가 유저에게 락커키를 준것.
             * JSessionID는 Response될때 ResponseHeader의 Set-cookie에 담겨 클라이언트에게 보내짐
             * 클라이언트측 브라우저는 받은 JSessionID를 cookie에 담아 다른 요청을 할때마다. 쿠키를 가지고간다.
             * '프로토콜'이다. 개발자는 session에 유저정보를 담아주기만 하면 된다.
             */

            session.setAttribute("sessionUser", user);
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/exLogin";
        }

    }
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

    // 로그아웃은 세션만 무효화 해주면 된다.
    @GetMapping({ "/logout" })
    public String logout() {
        // 하나하나 날리는 메서드도 있지만 invalidate로 한번에 날린다. (서랍을 비우기)
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping({ "/test" })
    public String test() {
        return "board/test";
    }

    // <<실무> DTO class를 이용하는 방법>
    // DTO의 변수를 보면 받을 데이터 유형들을 바로 알수있다.
    // 또한 변수의 갯수나 타입이 바뀌어도 메서드를 바꾸지 않는다.
    @PostMapping({ "/join" })
    public String join(JoinDTO joinDTO) {
        System.out.println("join메서드 실행됨");

        // 유효성 검사
        // null값이 먼저 잡혀야 하기 때문에 null유효성 조건을 앞에둬야한다.
        // .isEmpty() << .equals("") 와 같다.
        if (joinDTO.getUsername() == null || joinDTO.getUsername().isEmpty()) {
            return "redirect:/40x";
        }
        if (joinDTO.getPassword() == null || joinDTO.getPassword().isEmpty()) {
            return "redirect:/40x";
        }
        if (joinDTO.getEmail() == null || joinDTO.getEmail().isEmpty()) {
            return "redirect:/40x";
        }

        try {
            userRepository.save(joinDTO);
        } catch (Exception e) {
            return "redirect:/50x";
        }
        return "redirect:/loginForm";
    }

    // <<정상>바디 데이터 파싱까지 DS에게 시키는방법>
    // 예전에 bufferedReader 등으로 받았던 문자열데이터(X-WWW)는 스프링에서 톰캣(서블릿)이 받는다.
    // Servlet은 톰캣이 들고있는 일종의 소켓 객체이다.
    // Servlet이 처음 데이터를 받으면 HttpServletRequest객체 에 헤더만 넣어 DS로 전달한다. (1차파싱)
    // 남겨놓은 바디데이터는 DS가 파라미터에 데이터를 대입하려할때 전달된다.
    // DS는 파싱과 메서드 찾아 때리기 두가지 기능을 가진다.
    // DS가 메서드의 매개변수에있는 HSRequest(Response)타입의 매개변수를 받으면 추가 파싱 안하고 넣어줌
    // String username과 같은 매개변수를 넣으면 알아서 파싱해서 데이터를 찾아 넣어줌 (2차파싱)
    // 서버 구조 안에서 데이터(DB에서 온것이든, 요청문이든)를 전달할때 request(mustache에서는 model)을 사용

    @PostMapping({ "/join1" })
    public String join1(String username, Integer password, String email) {

        System.out.println("username : " + username);
        System.out.println("password : " + password);
        System.out.println("email : " + email);
        return "redirect:/loginForm";
    }

    // <<약간 비정상>바디 데이터 파싱을 DS에게 시키지 않고 직접하는 방법>
    @PostMapping({ "/join2" })
    public String join2(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        System.out.println("username : " + username);
        System.out.println("password : " + password);
        System.out.println("email : " + email);

        return "redirect:/loginForm";
    }

    // <<매우 비정상> request.getReader(BufferedReader 타입)로 바디데이터를 받아서 직접 파싱>
    // 바디데이터가 버퍼용량보다 클 경우 반복문 써서 데이터 빼내야함
    @PostMapping({ "/join3" })
    public String join3(HttpServletRequest request) throws IOException {

        BufferedReader br = request.getReader();
        String body = br.readLine(); // readLine() 실행시 버퍼(request)안이 비워짐
        String username = request.getParameter("username");
        System.out.println("body : " + body);
        System.out.println("username : " + username);

        return "redirect:/loginForm";
    }

    // join2에서 데이터의 흐름보기
    @PostMapping({ "/join4" })
    public String join4(HttpServletRequest request) throws IOException {

        // request객체의 레퍼런스 주소가 출력
        System.out.println("request : " + request);

        // bufferedReader의 레퍼런스 주소가 출력
        System.out.println("getReader : " + request.getReader());

        // body데이터가 출력 ( + join() 과 다르게 password값으로 문자입력가능)
        System.out.println("readLine : " + request.getReader().readLine());

        // 위 코드에서 readLine으로 데이터가 모두 비워졌다.
        System.out.println("readLine : " + request.getReader().readLine()); // null값 출력
        System.out.println("username : " + request.getParameter("username")); // null값 출력
        System.out.println("password : " + request.getParameter("password")); // null값 출력
        System.out.println("email : " + request.getParameter("email")); // null값 출력
        return "redirect:/loginForm";
    }
}

package shop.mtcoding.blog.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.dto.UserUpdateDTO;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.UserRepository;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // request는 가방, session은 락커
    @Autowired
    private HttpSession session;

    // localhost:8080/check?username=ssar
    // ResponseEntity를 쓰면 <>타입의 '데이터'를 응답 (@ResponseBody 안붙여도됨)
    // 또한 HttpStatus(상태코드)를 같이 기입할수 있음
    // 이 상태코드를 통해서 로직을 짜게된다. 그래서 프로토콜로 정해져있다.
    // body메시지는 사람에게 표시하기 위해 필요한것
    @GetMapping("/check")
    public ResponseEntity<String> check(String username) {
        User user = userRepository.findByUsername(username);

        if (user != null) {
            return new ResponseEntity<String>("중복됨", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("중복되지 않음", HttpStatus.OK);
    }

    @PostMapping({ "/login" })
    public String login(LoginDTO loginDTO) {
        System.out.println("테스트 : login Controller 호출");

        // 유효성 검사 (부가기능)
        if (loginDTO.getUsername() == null || loginDTO.getUsername().isEmpty()) {
            return "redirect:/40x";
        }
        if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
            return "redirect:/40x";
        }

        System.out.println("테스트 : login Controller 유효성 검사 완료");

        System.out.println("테스트 : loginDTO findByUsername : " + loginDTO.getUsername());
        System.out.println("테스트 : loginDTO findByUsername : " + loginDTO.getPassword());
        try {
            // 핵심기능

            User user = userRepository.findByUsername(loginDTO.getUsername());

            System.out.println("테스트 : loginDTO findByUsername 메서드 호출완료");
            System.out.println("테스트 : loginDTO findUsername  : " + user.getUsername());
            System.out.println("테스트 : loginDTO findPassword : " + user.getPassword());
            /*
             * <세션을 유지하기>
             * 브라우저에서 HttpSession에 접근하는 순간 JSessionID(서랍키)가 부여됨
             * JSessionID 마다 HashMap이 새로 생성되고, Key(key)-Value(유저정보)를 저장
             * 즉, 브라우저마다 다른 HashMap이 생성되므로 브라우저 단위로 세션이 유지될 수 있다.
             * JSessionID는 Response될때 ResponseHeader의 Set-cookie에 담겨 클라이언트에게 보내짐
             * 클라이언트측 브라우저는 받은 JSessionID를 cookie에 담아 다른 요청을 할때마다. 쿠키를 가지고간다.
             * '프로토콜'이다. 개발자는 session에 유저정보를 담아주기만 하면 된다.
             */
            if (BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
                System.out.println("테스트 : loginDTO해시와 user.getPassword()값 비교 : "
                        + BCrypt.checkpw(loginDTO.getPassword(), user.getPassword()));
                session.setAttribute("sessionUser", user);

                System.out.println("테스트 : session부여 객체 : " + (User) session.getAttribute("sessionUser"));

                return "redirect:/";
            }
        } catch (

        Exception e) {
            return "redirect:/exLogin";
        }

        return "redirect:/exLogin";
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
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        return "user/updateForm";
    }

    @PostMapping("/update/{id}")
    public String updateById(@PathVariable Integer id, UserUpdateDTO userUpdateDTO) {
        System.out.println("테스트 : user updateById cotroller호출");
        String encPassword = BCrypt.hashpw(userUpdateDTO.getPassword(), BCrypt.gensalt());
        System.out.println("테스트 : userUpdateDTO 암호 해싱 : " + encPassword);
        userUpdateDTO.setPassword(encPassword);

        System.out.println("테스트 : userUpdateDTO.get 암호 : " + userUpdateDTO.getPassword());
        userRepository.updateById(id, userUpdateDTO);
        return "redirect:/";
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

        // 유효성 검사 (프론트에서 막아도 Postman등 다이렉트로 오는것은 막을수 없기때문에 한다.)
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
        System.out.println("테스트 : Join 무결성 검사");
        // 트랜젝션(write)이 실행되는 save코드와 달리 DBMS실행에서 효율적인 코드가 좋다.
        // DB에 해당 username이 있는지 read작업(중복체크)만 하는 코드 필요
        // 그래서 DB의 Transection과정에 대해 이해해야 한다
        // 중복되지 않으면 (가져왔는데 null이면) << save코드가 실행됨
        // (null이면 프레임워크에서 오류를 터트리기 때문에 try-catch를 걸어준다.)
        User user = userRepository.findByUsername(joinDTO.getUsername());
        System.out.println("테스트 : findByUsername");
        if (user != null) {
            return "redirect:/50x";
        }
        // 핵심기능

        String encPassword = BCrypt.hashpw(joinDTO.getPassword(), BCrypt.gensalt());
        System.out.println("테스트 :joinDTO 해시암호 생성 : " + encPassword);
        joinDTO.setPassword(encPassword);
        System.out.println("테스트 :joinDTO Set하고 get메서드 호출 : " + joinDTO.getPassword());
        userRepository.save(joinDTO);

        System.out.println("테스트 :joinDTO Save메서드 실행완료  : ");
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
    // yml 파일에 mustache에서도 HttpServletRequest를 사용할수 있게 설정하면 사용할수 있다.

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

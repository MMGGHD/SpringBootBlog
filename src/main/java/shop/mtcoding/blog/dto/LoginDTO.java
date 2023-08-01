package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

/*
 *  로그인 API (프론트에게 필요한 정보, 원래는 pdf등에 따로 작성함)
 *  1. URL : http://localhost:8080/login
 *  2. 메서드 : POST (로그인은 select 이지만 post로 한다.)
 *  3. RequestBody(요청 Body)
 *      ex) username=값(String)&password=값(String)
 *  4. MIME타입 : x-www-from-urlencoded
 *  5. 응답의 타입 : view를 응답함. index 페이지
 */

@Getter
@Setter
public class LoginDTO {

    private String username;
    private String password;

}

package shop.mtcoding.blog.dto;

import javax.persistence.Column;

import lombok.Getter;
import lombok.Setter;

/*
 *  회원가입 API (프론트에게 필요한 정보, 원래는 pdf등에 따로 작성함)
 *  1. URL : http://localhost:8080/join
 *  2. 메서드 : POST (Get은 데이터 바디 없다.)
 *  3. RequestBody(요청 Body)
 *      ex) username=값(String)&password=값(String)&email=값(String)
 *  4. MIME타입 : x-www-from-urlencoded
 *  5. 응답의 타입 : view를 응답함
 */

// 응답(select)를 위해서 뿐만 아니라 요청을 위해서도 DTO를 사용할수 있다.
// 요청(String, Integer등 여러 타입이 섞여있는경우 클래스DTO로 받을수 있다.)
// @Getter, @Setter만들어 주면 알아서 new해서 변수안에 때려 넣어 줌

@Setter
@Getter
public class JoinDTO {

    private String username;
    private String password;
    private String email;

}

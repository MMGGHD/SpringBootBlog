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

// Model에서 필요한 변수만 가져와서 DTO를 만든다. (변수가 추가되든 적어지든)
// 응답(select)를 위해서 뿐만 아니라 요청을 위해서도 DTO를 사용할수 있다.
// 요청에(String, Integer등) 여러 타입이 섞여있는경우 클래스DTO로 받을수 있다.)
// 매개변수로 객체를 준뒤 리플렉션으로 메서드를 때리면 알아서 new해서 변수안에 때려 넣어 줌
// get요청시에도 매개변수안에 객체를 넣고 쿼리스트링을 받으면 스프링이 파싱해서 객체의 변수안에 넣고 찾아준다.
// 스프
// 이때 @Getter, @Setter 필요

@Setter
@Getter
public class JoinDTO {

    private String username;
    private String password;
    private String email;

}

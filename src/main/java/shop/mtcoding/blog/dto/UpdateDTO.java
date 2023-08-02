package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

/*
 *  글수정 API (프론트에게 필요한 정보, 원래는 pdf등에 따로 작성함)
 *  1. URL : http://localhost:8080/board/{id}/update
 *  2. 메서드 : POST
 *  3. RequestBody(요청 Body)
 *      ex) title=값(String)&content=값(String)
 *  4. MIME타입 : x-www-from-urlencoded
 *  5. 응답의 타입 : view를 응답함. detail 페이지
 */

@Getter
@Setter
public class UpdateDTO {

    private String title;
    private String content;

}

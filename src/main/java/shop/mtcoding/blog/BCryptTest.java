package shop.mtcoding.blog;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptTest {
    public static void main(String[] args) {

        // gensalt() << 자동으로 salt를 뿌리는 메서드
        // salt가 달라지기 때문에 해시값이 매번 다르게 나온다.
        // joinDTO의 password를 해시로 바꾸어 로그인 할때 user.getPassword와 비교해서 세션 제공
        // DB에는 암호화 된 패스워드가 들어가야함
        String encPassword = BCrypt.hashpw("1234", BCrypt.gensalt());
        System.out.println("encPassword : " + encPassword);
        System.out.println(encPassword.length());
        boolean isValid = BCrypt.checkpw("1234", encPassword);
        System.out.println(isValid);
    }

}

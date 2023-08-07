package shop.mtcoding.blog.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.dto.UserUpdateDTO;
import shop.mtcoding.blog.model.User;

@Repository
public class UserRepository {

    @Autowired
    private EntityManager em;

    // 중복코드로 인한 오류를 미리 예방할수 있도록 하는 안전한 코드
    public User findByUsername(String username) {
        Query query = em.createNativeQuery(
                "select * from user_tb where username=:username", User.class);
        query.setParameter("username", username);
        return (User) query.getSingleResult();

    }

    public User findByUsernameAndPassword(LoginDTO loginDTO) {
        // System.out.println("테스트 : " + 5);
        Query query = em.createNativeQuery(
                "select * from user_tb where username=:username and password=:password", User.class);

        // System.out.println("테스트 : " + 6);
        query.setParameter("username", loginDTO.getUsername());
        query.setParameter("password", loginDTO.getPassword());

        // System.out.println("테스트 : " + 7);
        return (User) query.getSingleResult();

    }

    @Transactional
    public void save(JoinDTO joinDTO) {
        // System.out.println("테스트 : " + 1);
        Query query = em.createNativeQuery(
                "insert into user_tb(username, password, email) values(:username, :password, :email)");

        // System.out.println("테스트 : " + 2);
        query.setParameter("username", joinDTO.getUsername());
        query.setParameter("password", joinDTO.getPassword());
        query.setParameter("email", joinDTO.getEmail());

        // System.out.println("테스트 : " + 3);
        query.executeUpdate();

        // System.out.println("테스트 : " + 4);
    }

    @Transactional
    public void updateById(Integer id, UserUpdateDTO userUpdateDTO) {
        Query query = em.createNativeQuery(
                "update user_tb set password = :password where id = :id");
        query.setParameter("password", userUpdateDTO.getPassword());
        query.setParameter("id", id);
        query.executeUpdate();
    }
}

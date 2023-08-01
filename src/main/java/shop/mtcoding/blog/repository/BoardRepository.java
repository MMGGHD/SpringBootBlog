package shop.mtcoding.blog.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.User;

@Repository
public class BoardRepository {

    @Autowired
    private EntityManager em;

    @Transactional
    public void save(WriteDTO writeDTO, Integer userId) {
        System.out.println("save Repository 메서드 실행");
        Query query = em.createNativeQuery(
                "insert into board_tb(title, content, user_id, created_at) values(:title, :content, :userId, now())");
        query.setParameter("title", writeDTO.getTitle());
        query.setParameter("content", writeDTO.getContent());
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    // createQuery는 객체지향 쿼리(SPQL)라고 한다. SPQL을 쓰면 Migration안해도된다.
    // 즉, 객체지향 쿼리를 쓰면 DBMS마다 다른 문법을 신경쓰지 않고 자바에서 쓸수 있다.
    public List<Board> findAll(int page) {
        // 페이징을 해서 조회한다. 페이징쿼리는 DB에서 먼저 해보고 작성한다.
        // limit int a, int b << a는 시작페이지, b 는 페이지당 표시할 갯수
        // limit은 쿼리문의 가장 뒤에 들어가야 한다.
        // 페이지당 표시할 갯수는 변하면 안되기 때문에 final int로 지정한다.
        final int SIZE = 3;
        Query query = em.createNativeQuery(
                "select * from board_tb order by id desc limit :page, :size", Board.class);
        query.setParameter("page", page * SIZE);
        query.setParameter("size", SIZE);
        return query.getResultList();
    }

    public Board findBoardById(Integer id) {
        System.out.println("findBoardById 메서드 실행");
        Query query = em.createNativeQuery(
                "select * from board_tb where id =:id", Board.class);
        System.out.println("findBoardById 쿼리입력");
        query.setParameter("id", id);
        Board board = (Board) query.getSingleResult();
        System.out.println("findBoardById 객체 전달");
        return board;
    }
}

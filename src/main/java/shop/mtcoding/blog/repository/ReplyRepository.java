package shop.mtcoding.blog.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import shop.mtcoding.blog.dto.ReplyWriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.User;

@Repository
public class ReplyRepository {
    @Autowired
    private EntityManager em;

    @Transactional
    public void save(ReplyWriteDTO replyWriteDTO, Integer userId) {
        Query query = em.createNativeQuery(
                "insert into reply_tb(comment, user_id, board_id) values(:comment, :user_id, :board_id)");
        query.setParameter("comment", replyWriteDTO.getComment());
        query.setParameter("board_id", replyWriteDTO.getBoardId());
        query.setParameter("user_id", userId);
        query.executeUpdate();
    }

    public List<Reply> findByBoardId(Integer boardId) {
        try {
            Query query = em.createNativeQuery(
                    "select * from reply_tb where board_id= :boardId order by id desc", Reply.class);
            query.setParameter("boardId", boardId);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Reply findByReplyId(Integer id) {
        Query query = em.createNativeQuery(
                "select * from reply_tb where id=:id", Reply.class);
        query.setParameter("id", id);
        return (Reply) query.getSingleResult();
    }

    @Transactional
    public void replyUpdateById(Integer id, String content) {
        Query query = em.createNativeQuery(
                "update reply_tb set comment=:comment where id= :id");
        query.setParameter("id", id);
        query.setParameter("comment", content);
        query.executeUpdate();
    }

    @Transactional
    public void replyDelete(Integer id) {

        System.out.println("테스트 replyDelete 실행됨");
        Query query = em.createNativeQuery(
                "delete from reply_tb where id= :id");
        query.setParameter("id", id);

        System.out.println("테스트 replyDelete 쿼리작성 완료 됨");
        query.executeUpdate();
        System.out.println("테스트 replyDelete 완료 됨");
    }
}

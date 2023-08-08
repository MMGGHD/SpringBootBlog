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

    public List<Reply> findReplyByBoard(Integer boardId) {
        Query query = em.createNativeQuery(
                "select * from reply_tb where board_id= :boardId order by id desc", Reply.class);
        query.setParameter("boardId", boardId);
        return query.getResultList();
    }
}

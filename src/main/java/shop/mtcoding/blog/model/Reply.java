package shop.mtcoding.blog.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

// <관계설정>
// User(1) : Reply(N)
// Board(1) : Reply(N)

@Getter
@Setter
@Table(name = "reply_tb")
@Entity
public class Reply {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(nullable = false, length = 100)
    private String comment;

    @ManyToOne // FK 컬럼 이름이 user_id << 디폴트 값으로 만들어 진다.
    // @JoinColumn(name = "user_tb") << 참조 컬럼명을 직접 설정하고 싶을때
    private User User;

    @ManyToOne // FK
    private Board board;

}

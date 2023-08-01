package shop.mtcoding.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "board_tb")
@Entity // ddl-auto가 create이면 table만들어짐
public class Board {

    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto increment
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String content;

    // 생성일
    private Timestamp createdAt;

    @ManyToOne
    private User user;

}

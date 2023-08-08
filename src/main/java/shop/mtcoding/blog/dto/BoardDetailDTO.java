package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BoardDetailDTO {

    private Integer boardId;
    private Integer boardUserId;
    private Integer replyId;
    private String replyComment;
    private Integer replyUserId;
    private Integer replyBoardId;
    private String replyUserUsername;
    private boolean replyOwner;

    public BoardDetailDTO(Integer boardId, Integer boardUserId, Integer replyId, String replyComment,
            Integer replyUserId, Integer replyBoardId, String replyUserUsername, boolean replyOwner) {
        this.boardId = boardId;
        this.boardUserId = boardUserId;
        this.replyId = replyId;
        this.replyComment = replyComment;
        this.replyUserId = replyUserId;
        this.replyBoardId = replyBoardId;
        this.replyUserUsername = replyUserUsername;
        this.replyOwner = replyOwner;
    }
}

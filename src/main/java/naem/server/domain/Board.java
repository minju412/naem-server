package naem.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;
import naem.server.domain.post.Post;

@Entity
@Getter
@Setter
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @OneToMany(mappedBy = "board")
    private List<Post> posts = new ArrayList<>();

    //==생성 메서드==//
    // 새로운 게시판 생성
    public static Board createBoard(BoardType boardType, Post post) {

        Board board = new Board();
        board.setBoardType(boardType);
        board.getPosts().add(post);
        post.setBoard(board);

        return board;
    }

    // 이미 존재하는 게시판에 게시글 추가
    public void addPostToBoard(Post post) {
        this.posts.add(post);
        post.setBoard(this);
    }

    // 삭제된 게시글은 게시판에서도 삭제
    public void deletePostFromBoard(Post post) {
        this.posts.remove(post);
        post.setBoard(null);
    }
}

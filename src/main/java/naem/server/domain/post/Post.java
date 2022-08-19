package naem.server.domain.post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naem.server.domain.Board;
import naem.server.domain.Comment;
import naem.server.domain.member.Member;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 게시글이 한 명의 회원에 속한다.
    @JoinColumn(name = "member_id")
    private Member member; // 외래키가 있는 곳을 연관관계의 주인으로 정한다.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String title;
    private String content;
    private Integer likeCnt;
    private Integer viewCnt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostTag> postTags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createAt;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateAt;

    private Boolean isDeleted;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteAt;

    @Builder
    public Post(String title, String content, Member member, List<PostTag> postTag) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.postTags = postTag;
    }

    //==연관관계 메서드==//
    public void addPostTag(PostTag postTag) {
        postTags.add(postTag);
        postTag.setPost(this);
    }

    //==생성 메서드==//
    public static Post createPost(Member member, String title, String content, PostTag... postTags) {

        Post post = new Post();
        post.setMember(member);
        for (PostTag postTag : postTags) {
            post.addPostTag(postTag);
        }
        post.setTitle(title);
        post.setContent(content);
        // post.setCreateAt(LocalDateTime.now());
        return post;
    }



}

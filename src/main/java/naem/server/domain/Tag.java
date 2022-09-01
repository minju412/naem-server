package naem.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naem.server.domain.post.PostTag;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private TagType tagType;

    // @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    // private List<PostTag> postTags = new ArrayList<>();

    // @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    // private List<MemberTag> memberTags = new ArrayList<>();

    // 포스트 태그로부터 태그 받아오기
    public static Tag getTagFromPostTag(PostTag postTag) {
        return postTag.getTag();
    }
}

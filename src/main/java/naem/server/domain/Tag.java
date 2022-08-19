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

    //==생성 메서드==//
    public static Tag createTag(PostTag postTag) {
        return postTag.getTag();
    }

    // @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    // private List<PostTag> postTags = new ArrayList<>();
    //
    // @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    // private List<MemberTag> memberTags = new ArrayList<>();
}

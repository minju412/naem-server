package naem.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private TagType tagType;

    // @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    // private List<PostTag> postTags = new ArrayList<>();
    //
    // @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    // private List<MemberTag> memberTags = new ArrayList<>();
}

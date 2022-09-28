package naem.server.domain.member;

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

import lombok.Getter;
import lombok.Setter;
import naem.server.domain.Tag;

@Entity
@Getter
@Setter
public class MemberTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    //==생성 메서드==//
    public static MemberTag createMemberTag(Tag tag) {
        MemberTag memberTag = new MemberTag();
        memberTag.setTag(tag);

        return memberTag;
    }

    //==삭제 메서드==//
    public static void removeMemberTag(List<MemberTag> memberTags) {
        // member_id와 tag_id의 매핑 제거
        for (MemberTag memberTag : memberTags) {
            memberTag.setMember(null);
            memberTag.setTag(null);
        }
    }
}

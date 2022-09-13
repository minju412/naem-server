package naem.server.domain.member;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DisabledAuthImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disabled_auth_image_id")
    private Long id;

    private String imgUrl;
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disabled_member_info_id")
    private DisabledMemberInfo disabledMemberInfo;

    //==삭제 메서드==//
    public static void deleteDisabledAuthImages(List<DisabledAuthImage> images) {
        if (!images.isEmpty()) {
            for (DisabledAuthImage image : images) {
                image.setImgUrl(null);
                image.setFileName(null);
                image.setDisabledMemberInfo(null);
            }
        }
    }

}

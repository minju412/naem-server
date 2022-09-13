package naem.server.domain.member;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DisabledMemberInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disabled_member_info_id")
    private Long id;

    private String username;

    @OneToMany(mappedBy = "disabledMemberInfo", cascade = CascadeType.ALL)
    private List<DisabledAuthImage> img = new ArrayList<>();

    //==생성 메서드==//
    public static DisabledMemberInfo createDisabledMemberInfo(String username) {

        DisabledMemberInfo disabledMemberInfo = new DisabledMemberInfo();
        disabledMemberInfo.setUsername(username);

        return disabledMemberInfo;
    }
}

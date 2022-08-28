package naem.server.domain.post.dto;

import lombok.Data;

@Data
public class PostReadCondition {

    private String keyword; // 검색 키워드
    private Boolean isDeleted;

    public PostReadCondition() {
        this.isDeleted = false;
    }

    public PostReadCondition(String keyword) {
        this.keyword = keyword;
        this.isDeleted = false;
    }
}

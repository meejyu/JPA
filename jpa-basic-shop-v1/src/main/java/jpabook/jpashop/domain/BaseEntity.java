package jpabook.jpashop.domain;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 생성자, 생성시간, 수정자, 수정시간을 모든 엔티티에 공통으로 가져가야하는 상황에서
 * 아래와 같이 BaseEntity를 정의해서 활용할 수 있다.
 * @MappedSuperclass는 매핑정보만 상속받을수 있게 만드는 어노테이션
 * 공통 매핑 정보가 필요할때, 부모 클래스에서 선언하고 속성만 상속 받아서 사용하고 싶을때
 */
@MappedSuperclass
public abstract class BaseEntity {

    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

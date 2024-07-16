package sendman.backend.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // JPA Entity 클래스들이 BaseTimeEntity 를 상속할 경우 필드들(createDate, modifiedDate)도 컬럼으로 인식하
@EntityListeners(AuditingEntityListener.class) // BaseTimeEntity 클래스 Auditing 기능을 포함
public abstract class BaseTime {
    @CreatedDate // 회원가입시 저장
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate // 정보 변경시 자동 저장
    private LocalDateTime modifiedDate;
}

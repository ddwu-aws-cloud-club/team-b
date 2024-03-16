package org.course.registration.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.course.registration.exception.NotEnoughException;

@Entity
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue
    @Column(name = "course_id")
    private int id; // 과목 id

    private String name; // 과목 이름

    private String pro; // 교수님 성함

    private int limited; // 수강 정원

    private int count; // 현재 정원

    //== 비지니스 로직==//
    // 과목 엔티티는 count를 속성으로 가지고 있기 때문에 변경 시 setter getter를 통하는 것이 아니라 내부에 관련 비지니스 로직을 넣는 것이 좋다고 해서 넣었습니다

    /**
     * 신청 인원 증가
     */
    public void addCount(int count){
        int limitedCount = this.count + count;
        if(limitedCount > limited){
            throw new NotEnoughException("full");
        }
    }

    /**
     * 신청 인원 감소
     */
    public void removeCount(int count){
        int restCount = this.limited - count;
        if( restCount < 0 ){
            throw new NotEnoughException("under");
        }
    }
}
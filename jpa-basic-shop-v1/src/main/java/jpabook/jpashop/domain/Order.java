package jpabook.jpashop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// @Table 에 name 을 적은 이유는
// 특정 DB 의 경우 'Order' 가 예약어로 걸려 있어
// 테이블명으로 사용할 수 없으므로 ORDER 로 설정해줌.
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    // @Column name 없이 DDL 실행시
    //
    // 1. JPA + 하이버네이트 사용 > orderDate 그대로 생성
    // 2. 스프링부트에서 사용 > order_date
    //
    // 스프링부트의 경우 관례상 가장 많이 사용하는 컬럼명 네이밍 방식인
    // 소문자 + 언더바 조합으로 생성한다.(snake_case)
    // 언더바 생성 조건은 camelCase 에서 대문자를 만났을때이다.
    //
    // * 설정에서 변경 가능(대문자 + 언더바 조합으로 변경한다던지..)
    private LocalDateTime orderDate;

    // EnumType 은 꼭 String 으로..
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}

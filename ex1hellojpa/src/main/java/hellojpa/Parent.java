package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent {

    @Id @GeneratedValue
    private Long id;

    private String name;

    // ALL PERSIST REMOVE만 사용함 대부분
    // orphanRemoval 고아객체를 자동으로 삭제해주는거, 컬렉션에서 지워주면 자동으로 삭제, CascadeType.REMOVE 처럼 동작한다고 보면됨!!
    // 부모가 한명일떄만,,! 게시판 댓글, 카테고리,
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> children = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    public void addChild(Child child) {
        children.add(child);
        if(!this.children.contains(child)) {
            children.add(child);
        }
        child.setParent(this);
    }
}

package hellojpa;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        PersistenceUnitUtil util = emf.getPersistenceUnitUtil();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            /**
             * cascade가 걸려있으면 굳이 em.persist 두번 안해도됨
             * cascade를 사용하여 child의 생명주기를 Parent에서 관리
             **/
            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);

            Child child1 = new Child();
            child1.setName("child1");
            Child child2 = new Child();
            child2.setName("child2");

            List<Member> resultList = em.createNativeQuery("select MEMBER_ID, city, street, zipcode, USERNAME from MEMBER", Member.class)
                                    .getResultList();

            Parent parent = new Parent();
            parent.setName("parent");
            parent.addChild(child1);
            parent.addChild(child2);
            System.out.println("1번");
            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1.getUsername());
            }
            System.out.println("2번");
            // 시퀀스를 부른다
            em.persist(parent);
            System.out.println("3번");
            // 플러시할때 디비로 쿼리문이 날라감
            em.flush();
            System.out.println("너는 모하니");
            em.clear();
            System.out.println("4번");
            Parent findParent = em.find(Parent.class, parent.getId());
            System.out.println("5번");
            findParent.getChildren().remove(0);
            System.out.println("6번");
//            List<Child> result = parent.getChildren();
//
//            Child findChild = result.get(0);
//            System.out.println("findChild = " + findChild.getName());

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }

    private static void printUserAndTeam(EntityManager em, Member member) {
        Member m = em.find(Member.class, member.getId());
        Team t = m.getTeam();

        System.out.println("회원 이름 = " + m.getUsername());
        System.out.println("팀 이름 = " + t.getName());
    }

    private static void printUser(EntityManager em, Member member) {
        Member m = em.find(Member.class, member.getId());

        System.out.println("회원 이름 = " + m.getUsername());
    }


}
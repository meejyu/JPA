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
            Member member1 = new Member();
            member1.setUsername("memberA");
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("memberB");
            em.persist(member2);

            em.flush();
            em.clear();

            Member m1 = em.find(Member.class, member1.getId());             //Member
            Member m2 = em.getReference(Member.class, member2.getId());     //Proxy

            /**
             프록시를 사용할 경우 프록시 클래스(hellojpa.Member$HibernateProxy$7W3KQX3G)라고 뜨기 떄문에 == 으로 비교하면 false가 뜬다.
             그러므로 instanceof Member를 사용하여 참조하기 위해 사용하였던 클래스를 찾아서 비교하여야 true가 뜬다.
             */
            System.out.println("m1 == m2 : " + (m1.getClass() == m2.getClass()));
            System.out.println("m1 : "+ m1.getClass().getName());
            System.out.println("m2 : "+ m2.getClass().getName());
            System.out.println("m1 instanceof Member : " + (m1 instanceof Member));
            System.out.println("m2 instanceof Member : " + (m2 instanceof Member));

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
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

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("memberA");
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            Member refMember = em.getReference(Member.class, member.getId());


            // 초기화 전
            System.out.println("isLoaded = " + util.isLoaded(refMember));

            //refMember.getUsername();
            //org.hibernate.Hibernate.initialize(refMember);

            // 초기화 후
            System.out.println("isLoaded = " + util.isLoaded(refMember));

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
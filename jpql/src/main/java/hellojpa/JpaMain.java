package hellojpa;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        PersistenceUnitUtil util = emf.getPersistenceUnitUtil();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setTeam(teamA);
            em.persist(member1);

            em.flush();
            em.clear();

//            List<Member> result = em.createQuery("select m from Member m", Member.class)
//                    .getResultList();

            List<Member> result = em.createNativeQuery("select * from Member", Member.class)
                .getResultList();


            for (Member m : result) {
                System.out.println("m = " + m.getTeam().getClass());
                m.setUsername("memberA");
            }

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }


}
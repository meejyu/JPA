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

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamA");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setTeam(teamB);
            em.persist(member2);
            
            em.flush();
            em.clear();


            /**
             * 즉시로딩과 지연로딩!
             * 지연로딩은 필요할때 객체를 불러와 로딩한다!!
             * 팀을 지연로딩으로 했을떄 팀을불러올떄 로딩한다.
             * 즉시로딩은 조인되있는 컬럼 몽땅다 가져옴!!
             Member member = em.find(Member.class, member1.getId());
             member.getTeam().getName();
             * */

            System.out.println("단건 조회");
            Member findMember = em.find(Member.class, member1.getId());

            // m을 프로제션이라고함 select 다음에 오는거
            /**
            *
            */
            System.out.println("jpql 조회");
            List<Member> result = em.createQuery("select m from Member m").getResultList();

            /**
             * 지연 로딩과 즉시로딩을 하면 하면 N+1 문제가 있음
             * N+1 문제에 대해 좀 더 검색해볼것
             * 패치 조인을 사용해서 하면 해결됨,,! => 조인 쿼리를 날린다고 생각하면 됨!
             */
            for (Member member : result) {
                System.out.println("member.getUsername() = " + member.getUsername());
            }
             
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
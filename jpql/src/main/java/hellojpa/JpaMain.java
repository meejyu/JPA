package hellojpa;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
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
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setAge(20);
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(30);
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setAge(40);
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            String query11 = "select m.username from Team t join t.members m";

            List<String> result11 = em.createQuery(query11, String.class).getResultList();
            System.out.println("result11 = " + result11);

            System.out.println("================================패치 조인==========================================");
            // 패치 조인인 = inner 조인 완전히 일치하는 값만 가져옴,,! 지연 로딩 X
            // 팀 데이터도 같이 출력
            String query = "select m from Member m join fetch  m.team";
            List<Member> result = em.createQuery(query, Member.class).getResultList();

            for (Member member : result) {
                System.out.println("member = " + member.getUsername() + " , team = "+member.getTeam().getName());
            }

            System.out.println("==============================컬렉션 조인============================================");
            // 일대다 관계, 컬렉션 패치 조인
            /**
             * 일대다 관계 패치 조인을 데이터 뻥튀기가 될수 있다.
             * 예를 들어 teamA와 teamB가 있는데
             * teamA의 회원이 2명이고 teamB의 회원이 1명이면
             * temaA의 회원이 2명이므로 row가 두줄이 되서 값이 두개가 나온다.
             * 아래의 쿼리문의 실행 결과
             *     ID  	NAME  	ID  	AGE  	MEMBERTYPE  	USERNAME  	TEAM_ID
             *     1	 팀A	 3	    20	    null	        회원1	        1
             *     1	 팀A	 4	    30	    null	        회원2	        1
             *     2	 팀B	 5	    40	    null	        회원3	        2
             */
            String query2 = "select t from Team t join fetch t.members";

            List<Team> result2 = em.createQuery(query2, Team.class).getResultList();
            for (Team team : result2) {
                System.out.println("team = " + team.getMembers().getClass());
                System.out.println("team = " + team.getName() + ", members = " + team.getMembers().size());

                for (Member member : team.getMembers()) {
                    System.out.println("member = " + member);
                }
            }

            System.out.println("====================일반 조인과 패치 조인의 차이======================================================");

            List<Team> result3 = em.createQuery(query2, Team.class).getResultList();

            /**
             fetch 조인은 다 가져와야함,,! 그래서 alias도 쓰면 안되고 다 쓰면 안댐,,!
             distinct 완전히 일치 할때 삭제함. JPA는 삭제해줌,! sql은 삭제 안해줌!!
             * 여기도 뻥튀기 발생함,,,!
             */
            String query3 = "select t from Team t join t.members m";

            int i = 0;
            for (Team team : result3) {
                if(i==0) {
                    //  일대다로 가져온 컬렉션은 프록시 아님
                    // 결과 : team.getMembers().getClass() = class org.hibernate.collection.internal.PersistentBag
                    System.out.println("team.getMembers().getClass() = " + team.getMembers().getClass());
                }

                // N + 1 문제 발생!
                System.out.println("team.getName() = " + team.getName() + " | members = " + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("member = " + member);
                }
                i++;
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


}
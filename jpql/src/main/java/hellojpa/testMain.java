package hellojpa;

import javax.persistence.*;
import java.util.List;

public class testMain {

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

            String query1 = "select m.username from Team t join t.members m";

            List<String> result1 = em.createQuery(query1, String.class).getResultList();
            System.out.println("result1 = " + result1);

            System.out.println("===================패치 조인=====================");
            //패치 조인인 = inner 조인 완전히 일치하는 값만 가져옴,,! 지연 로딩 X
            // 팀 데이터도 같이 출력
            String query2 = "select m from Member m join fetch m.team";
            List<Member> result2 = em.createQuery(query2, Member.class).getResultList();
            for (Member member : result2) {
                System.out.println("member.getUsername() = " + member.getUsername() +
                        " , team = "+member.getTeam().getName());
            }
            System.out.println("==============================컬렉션 조인============================================");

            String query3 = "select t from Team t join fetch t.members";
            List<Team> result3 = em.createQuery(query3, Team.class).getResultList();
            for (Team team : result3) {
                System.out.println("team.getMembers().getClass() = " + team.getMembers().getClass());
                System.out.println("team.getName() = " + team.getName() + " , members = "+team.getMembers().size());

                for (Member member : team.getMembers()) {
                    System.out.println("member = " + member);
                }
            }

            System.out.println("====================일반 조인과 패치 조인의 차이======================================================");

            String query4 = "select t from Team t join t.members m";
            List<Team> result4 = em.createQuery(query4, Team.class).getResultList();

            int i = 0;
            for (Team team : result3) {
                if(i==0) {
                    System.out.println("team.getMembers().getClass() = " + team.getMembers().getClass());
                }

                System.out.println("team.getName() = " + team.getName() + " | members = "
                    + team.getMembers().size());
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
package hellojpa;

import javax.persistence.*;
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

            for (int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("member"+i);
                member.setTeam(team);
                member.setAge(i);

                em.persist(member);
            }

            em.flush();
            em.clear();     //영속성 컨텍스트 초기화

            List result = em.createQuery("select m from Member m" +
                    " where exists (select t from m.team t where t.name = 'teamA')", Member.class)
                    .getResultList();

            for (Object o : result) {
                System.out.println("member = " + o);
            }

//            List<Member> result = em.createQuery("select m from Member m", Member.class)
//                    .getResultList();

            //조회하는 대상이 명확할떄는 타입쿼리를 써줘라
            // 두번째 인자에 명시를 해주면 타입쿼리 아니면 그냥 쿼리



            //조인쿼리가 나가는데 조인쿼리에 대한 내용이 없음, jpql에 조심해야함, 묵시적 조인
            // 그래서 묵시적 조인은 안쓰는게 좋음,,!!! 명시적 조인으로 사용,,!
//            UserDto singleResult = em.createQuery("select new hellojpa.UserDto(m.username, m.age) from Member m", UserDto.class).getSingleResult();

            //스칼라 타입 조회 하는 방법 2번
//            Object[] singleResult = em.createQuery("select m.username, m.age from Member m", Object[].class).getSingleResult();

            //스칼라 타입 조회 하는 방법 1번
//            Object[] temp = (Object[]) result;

//            System.out.println("result = " + singleResult[0]);
//            System.out.println("result = " + singleResult);




//            // iter로 하면 for 단축키 사용가능
//            for (Member member : result) {
//                System.out.println("member = " + member);
//            }

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }


}
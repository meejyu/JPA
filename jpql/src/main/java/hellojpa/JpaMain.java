package hellojpa;

import javax.persistence.*;
import java.util.Collection;
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

            Product product = new Product();
            product.setName("상품이름");
            product.setPrice(10000);
            product.setStockAmount(100);
            em.persist(product);


//            for (int i = 0; i < 10; i++) {
//                Member member = new Member();
//                member.setUsername("member"+i);
//                member.setMemberType(MemberType.USER);
//                member.setTeam(team);
//                member.setAge(i);
//
//                em.persist(member);
//            }

            Member member2 = new Member();
            member2.setUsername("member"+00011);
            member2.setMemberType(MemberType.USER);
            member2.setTeam(team);
            member2.setAge(00011);

            Member member1 = new Member();
            member1.setUsername("member"+000);
            member1.setMemberType(MemberType.USER);
            member1.setTeam(team);
            member1.setAge(000);

            em.persist(member1);
            em.persist(member2);

            Order order = new Order();
            order.setAddress(new Address("city", "dsf", "dsfdsf"));
            order.setMember(member1);
            order.setProduct(product);
            order.setOrderAmount(1);

            Order order1 = new Order();
            order1.setAddress(new Address("city2", "dsf2", "dsfdsf2"));
            order1.setMember(member2);
            order1.setProduct(product);
            order1.setOrderAmount(2);

            em.persist(order1);
            em.persist(order);

            em.flush();
            em.clear();     //영속성 컨텍스트 초기화

            // 묵시적 조인은 사용하면 안됨, 묵시적 내부조인 발생, 탐색가능하나 사용하면 안됨.
            // jpa가 탐색을 못하게함.
            // 해결하기 위해서 명시적 조인을 해
            List<Member> result = em.createQuery("select t.members from Team", Member.class)
                    .getResultList();

            for (Object s : result) {
                System.out.println("s = " + s);
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
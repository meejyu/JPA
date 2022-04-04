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

            Address addr1 = new Address("city", "street", "100000");
//            Address addr2 = new Address(addr1.getCity(), addr1.getStreet(), addr1.getZipcode());
//
//            /*
//            * equals에 대해 알아보기 jpa 분석하는거
//            * equals, hashcode
//            * */
//            System.out.println("addr1 == addr2 " + addr1.equals(addr2));
//
//            Member member1 = new Member();
//            member1.setUsername("member1");
//            member1.setHomeAddress(new Address("city", "street", "100000"));
//            member1.setWorkAddress(addr2);
//            em.persist(member1);

            /**
             * 값 타입 컬렉션 저장
             */
            Member member1 = new Member();
            member1.setUsername("member2");
            member1.getFavoriteFoods().add("치킨");
            member1.getFavoriteFoods().add("족발");
            member1.getFavoriteFoods().add("피자");
            member1.getAddressHistory().add(new Address("oldCity1", "street1", "100000"));
            member1.getAddressHistory().add(new Address("oldCity2", "street2", "200000"));
            em.persist(member1);

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member1.getId());
            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for(String favoriteFood : favoriteFoods) {
                System.out.println("favoriteFood = " + favoriteFood);
            }


            findMember.getFavoriteFoods().remove("족발");
            findMember.getFavoriteFoods().add("초밥");


            Member findMember2 = em.find(Member.class, member1.getId());
            findMember2.getAddressHistory().remove(addr1);

                        List<Address> addressHistory = findMember2.getAddressHistory();
            for(Address address : addressHistory) {
                System.out.println("address = " + address.getCity());
            }

            //체크박스일 경우에만 사용한다. 컬렉션은~

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
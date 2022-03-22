package jpabook.jpashop;

import jpabook.jpashop.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 EntityManagerFactory 애플리케이션이 로딩되는 시점에 딱 1개만 만들어야 한다.
 EntityManager 실제 트랜잭션이 수행될때마다 생성한다.
 쓰레드간에 공유를 하면 안되고 트랜잭션이 수행된 이후에는 반드시 닫고 디비 커넥션을 반환해야한다.
 EntityTransaction를 사용하여 커밋을 하는 순간 디비에 쿼리가 보내진다.
 EntityManager는 데이터 변경시 트랜잭션을 시작해야한다. tx.begin();
 */
public class JpaMain {

    public static void main(String[] args) {
        // 커맨드 옵션 v = 변수명 자동완성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 네이티브 쿼리는 jpa쿼리를 타지 않는다. 인서트, 딜리트를 안해줘서 따로 플러시 해줘야한다.

        try {

            Book book = new Book();
            book.setPrice(10000);
            book.setStockQuantity(100);
            book.setName("어린왕자");
            book.setAuthor("생텍쥐페리");
            book.setIsbn("9791186288139");

            em.persist(book);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}

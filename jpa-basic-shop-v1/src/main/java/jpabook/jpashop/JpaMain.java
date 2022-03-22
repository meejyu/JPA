package jpabook.jpashop;

import jpabook.jpashop.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 07. 고급 매핑 > 실전 예제 4 - 상속관계 매핑
 *
 * - 요구사항 추가
 * 상품의 종류는 음반, 도서, 영화가 있고 이후 더 확장될 수 있다.
 * 모든 데이터는 등록일과 수정일이 필수다.
 */
public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

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

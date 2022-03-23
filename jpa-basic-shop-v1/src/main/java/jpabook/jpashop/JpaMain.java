package jpabook.jpashop;

import jpabook.jpashop.domain.Book;
import jpabook.jpashop.domain.Category;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

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
            Category category1 = new Category();
            category1.setName("인형");
            em.persist(category1);

            Category category2 = new Category();
            category2.setName("동물");
            category2.setParent(category1);
            em.persist(category2);

            Category category3 = new Category();
            category3.setName("육식동물");
            category3.setParent(category2);
            em.persist(category3);

            Category category4 = new Category();
            category4.setName("초식동물");
            category4.setParent(category2);
            em.persist(category4);

            em.flush();
            em.clear();

            Category depth1 = em.find(Category.class, category1.getId());
            List<Category> child = depth1.getChild();
            for (Category category : child) {
                System.out.println("depth1.child.getName() = " + category.getName());
            }
            Category depth2 = em.find(Category.class, category2.getId());
            Category parent = depth2.getParent();
            List<Category> child1 = depth2.getChild();
            System.out.println("depth2.parent = " + parent.getName());
            for (Category aa : child1) {
                System.out.println("depth2.child.getName() = " + aa.getName());
            }
            Category depth3 = em.find(Category.class, category3.getId());
            System.out.println("depth3.parent = " + depth3.getParent().getName());
            System.out.println("depth3.getParent().getParent().getName() = " + depth3.getParent().getParent().getName());
            
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}

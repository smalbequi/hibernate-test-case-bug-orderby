package org.hibernate.bugs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("templatePU");
    }

    @After
    public void destroy() {
        entityManagerFactory.close();
    }

    // Entities are auto-discovered, so just add them anywhere on class-path
    // Add your tests, using standard JUnit.
    @Test
    public void test() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createQuery(
                        "select " +
                                "c.id," +
                                "c.name," +
                                "t.code," +
                                "g.id," +
                                "sum(e.balance)" +
                                "from Card e " +
                                "inner join e.generation g " +
                                "inner join g.type t " +
                                "inner join t.client c " +
                                "group by c.id, t.code, g.id " +
                                "order by c.name, t.code, g.id", Object[].class)
                .getResultList();

        // Everything ok

        /*
		select
			client3_.id as col_0_0_,
			client3_.name as col_1_0_,
			cardtype2_.code as col_2_0_,
			generation1_.id as col_3_0_,
			sum(card0_.balance) as col_4_0_
		from
			Card card0_
		inner join
			Generation generation1_
				on card0_.generation_id=generation1_.id
		inner join
			CardType cardtype2_
				on generation1_.type_code=cardtype2_.code
		inner join
			Client client3_
				on cardtype2_.client_id=client3_.id
		group by
			client3_.id ,
			cardtype2_.code ,
			generation1_.id
		order by
			client3_.name,
			cardtype2_.code,
			generation1_.id
         */

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}

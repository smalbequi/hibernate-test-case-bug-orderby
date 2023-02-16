package org.hibernate.bugs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

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
    public void wrong_group_by_and_order_by_generated() {
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

        Assertions.fail("Wrong group by and wrong order by generated (see log)");

        /*
        select
            t1_0.client_id,
            c2_0.name,
            g1_0.type_code,
            c1_0.generation_id,
            sum(c1_0.balance)
        from
            Card c1_0
        join
            Generation g1_0
                on g1_0.id=c1_0.generation_id
        join
            CardType t1_0
                on t1_0.code=g1_0.type_code
        join
            Client c2_0
                on c2_0.id=t1_0.client_id
        group by
            t1_0.client_id,
            g1_0.type_code,
            c1_0.generation_id
        order by
            c2_0.name,
            g1_0.type_code,
            c1_0.generation_id
        */

        /*
        The ‘group by’ should be: group by c2_0.id, t1_0.code, g1_0.id
        The ‘order by’ should be: order by c2_0.name, t1_0.code, g1_0.id

        This sql worked with H2 but doesn't work with Postgresql (11 and latest version)
        [42803] ERROR: column "c2_0.name," must appear in the GROUP BY clause or be used in an aggregate function Position : 31
         */

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}

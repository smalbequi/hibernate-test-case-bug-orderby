package org.hibernate.bugs;


import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class SuperGeneration {

    @Id
    private Long id;

    @ManyToOne
    private CardType type;
}

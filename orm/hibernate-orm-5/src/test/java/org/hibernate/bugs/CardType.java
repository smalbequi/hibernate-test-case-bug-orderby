package org.hibernate.bugs;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CardType {

    @Id
    private String code;

    @ManyToOne(optional = false)
    private Client client;
}

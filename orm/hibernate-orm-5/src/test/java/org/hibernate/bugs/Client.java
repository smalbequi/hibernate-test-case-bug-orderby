package org.hibernate.bugs;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Client {

    @Id
    private Long id;

    private String name;
}
/*
 * Copyright (c) 2023
 */

package mo.specdoc.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.sql.Date;
import java.util.Objects;

@Data
@Entity
@Table(name = "SECRECY_PERSON", schema = "PUBLIC", catalog = "SPECDOC")
public class SecrecyPerson {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    @Basic
    @Column(name = "DATE_ADD_SECRECY", nullable = false)
    private Date dateAddSecrecy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_SECRECY_TYPE", nullable = true)
    private SecrecyType secrecyType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PERSONA", nullable = true)
    private Persona persona;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecrecyPerson that = (SecrecyPerson) o;
        return id == that.id && Objects.equals(dateAddSecrecy, that.dateAddSecrecy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateAddSecrecy);
    }

}

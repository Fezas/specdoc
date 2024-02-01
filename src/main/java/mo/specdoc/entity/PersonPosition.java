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
@Table(name = "PERSON_POSITION", schema = "PUBLIC", catalog = "SPECDOC")
public class PersonPosition {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    @Basic
    @Column(name = "DATE_ADD_POSITION", nullable = true)
    private Date dateAddPosition;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_STATE", nullable = true)
    private State state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PERSONA", nullable = true)
    private Persona personaFromPosition;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonPosition that = (PersonPosition) o;
        return id == that.id && Objects.equals(dateAddPosition, that.dateAddPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateAddPosition);
    }
}

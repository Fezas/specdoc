/*
 * Copyright (c) 2023
 */

package mo.specdoc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;
@Data
@Entity
public class State {
    @GeneratedValue
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    @Basic
    @Column(name = "ID_PERSONA", nullable = false)
    private long idPersona;
    @Basic
    @Column(name = "ID_POSITION", nullable = false)
    private long idPosition;
    @Basic
    @Column(name = "DATE_ADD_POSITION", nullable = true)
    private Timestamp dateAddPosition;
    @Basic
    @Column(name = "ID_SUBDIVISION", nullable = false)
    private long idSubdivision;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return id == state.id && idPersona == state.idPersona && idPosition == state.idPosition && idSubdivision == state.idSubdivision && Objects.equals(dateAddPosition, state.dateAddPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idPersona, idPosition, dateAddPosition, idSubdivision);
    }
}

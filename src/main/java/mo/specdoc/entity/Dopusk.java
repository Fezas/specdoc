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
public class Dopusk {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    @Basic
    @Column(name = "DATE_DOPUSK", nullable = true)
    private Date dateDopusk;
    @Basic
    @Column(name = "NUMB_ORDER_DOPUSK", nullable = true, length = -1)
    private String numbOrderDopusk;
    @Basic
    @Column(name = "ACTIVE", nullable = false)
    private boolean active;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name="ID_PERSONA")
    private Persona persona;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_STATE_POST", nullable = true)
    private Post post;

    @Transient
    private String titleWithStructure;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dopusk dopusk = (Dopusk) o;
        return id == dopusk.id &&  active == dopusk.active && Objects.equals(dateDopusk, dopusk.dateDopusk) && Objects.equals(numbOrderDopusk, dopusk.numbOrderDopusk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateDopusk, numbOrderDopusk, active);
    }
}

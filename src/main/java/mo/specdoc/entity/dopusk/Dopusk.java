/*
 * Copyright (c) 2023-2024
 */

package mo.specdoc.entity.dopusk;

import jakarta.persistence.*;
import lombok.Data;
import mo.specdoc.entity.Persona;
import mo.specdoc.entity.State;

import java.sql.Date;
import java.util.Objects;
@Data
@Entity
@Table(name = "DOPUSK", schema = "PUBLIC", catalog = "SPECDOC")
public class Dopusk {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name="ID_PERSONA")
    private Persona persona;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_STATE", nullable = true)
    private State state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_DOC_DOPUSK", nullable = true)
    private DocDopusk docDopusk;

    @Transient
    private String titleWithStructure;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dopusk dopusk = (Dopusk) o;
        return id == dopusk.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

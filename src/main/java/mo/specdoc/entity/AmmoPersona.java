/*
 * Copyright (c) 2023
 */

package mo.specdoc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Data
@Entity
@Table(name = "AMMO_PERSONA", schema = "PUBLIC", catalog = "SPECDOC")
public class AmmoPersona {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    //@Basic
    //@Column(name = "ID_AMMO_TYPE", nullable = false)
    //private long idAmmoType;
    //@Basic
    //@Column(name = "ID_PERSONA", nullable = false)
    //private long idPersona;
    @Basic
    @Column(name = "NUMB_AMMO", nullable = true, length = 12)
    private String numbAmmo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_AMMO_TYPE", nullable = true)
    private AmmoType ammoType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PERSONA", nullable = true)
    private Persona persona;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmmoPersona that = (AmmoPersona) o;
        return id == that.id && Objects.equals(numbAmmo, that.numbAmmo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numbAmmo);
    }
}

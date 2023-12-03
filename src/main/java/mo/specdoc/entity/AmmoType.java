/*
 * Copyright (c) 2023
 */

package mo.specdoc.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "AMMO_TYPE", schema = "PUBLIC", catalog = "SPECDOC")
public class AmmoType {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    @Basic
    @Column(name = "TITLE", nullable = false, length = 50)
    private String title;


    @OneToMany(mappedBy="ammoType")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<AmmoPersona> ammoPersons;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmmoType ammoType = (AmmoType) o;
        return id == ammoType.id && Objects.equals(title, ammoType.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return  title;
    }
}

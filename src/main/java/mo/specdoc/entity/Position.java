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
@Table(name = "POSITION", schema = "PUBLIC", catalog = "SPECDOC")
public class Position {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    private long id;
    @Basic
    @Column(name = "TITLE")
    private String title;
    @Basic
    @Column(name = "TITLE_RP")
    private String titleRp;
    @Basic
    @Column(name = "TITLE_DP")
    private String titleDp;
    @Basic
    @Column(name = "TITLE_SHORT")
    private String titleShort;
    @Basic
    @Column(name = "ID_PARENT_POSITION", nullable = true)
    private Long idParentPosition;
    @Basic
    @Column(name = "IS_CARD")
    private boolean isCard;
    @Basic
    @Column(name = "VUS")
    private String vus;
    @Basic
    @Column(name = "IS_SUBDIV")
    private boolean isSubdiv;
    @Basic
    @Column(name = "ADDRESS")
    private String address;

    @OneToMany(mappedBy="personaFromPosition")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<PersonPosition> personPositions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return id == position.id && isCard == position.isCard && vus == position.vus && isSubdiv == position.isSubdiv && Objects.equals(title, position.title) && Objects.equals(titleRp, position.titleRp) && Objects.equals(titleDp, position.titleDp) && Objects.equals(titleShort, position.titleShort) && Objects.equals(idParentPosition, position.idParentPosition) && Objects.equals(address, position.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, titleRp, titleDp, id, titleShort, idParentPosition, isCard, vus, isSubdiv, address);
    }

    @Override
    public String toString() {
        return title;
    }
}

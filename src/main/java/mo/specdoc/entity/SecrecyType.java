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
@Table(name = "SECRECY_TYPE", schema = "PUBLIC", catalog = "SPECDOC")
public class SecrecyType {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private int id;
    @Basic
    @Column(name = "TITLE", nullable = false, length = 50)
    private String title;
    @Basic
    @Column(name = "TITLE_SHORT", nullable = false, length = 10)
    private String titleShort;

    @OneToMany(mappedBy="secrecy")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Post> posts;

    @OneToMany(mappedBy="secrecyType")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<SecrecyPerson> secrecyPeople;

    @OneToMany(mappedBy="secrecyType")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<State> secrecyStates;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecrecyType that = (SecrecyType) o;
        return id == that.id && Objects.equals(title, that.title) && Objects.equals(titleShort, that.titleShort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, titleShort);
    }

    @Override
    public String toString() {
        return title;
    }
}

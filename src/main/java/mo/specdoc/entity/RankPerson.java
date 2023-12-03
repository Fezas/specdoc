/*
 * Copyright (c) 2023
 */

package mo.specdoc.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
@Data
@Entity
@Table(name = "RANK_PERSON", schema = "PUBLIC", catalog = "SPECDOC")
public class RankPerson {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    @Basic
    @Column(name = "DATE_ADD_RANK", nullable = true)
    private Date dateAddRank;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_RANK", nullable = true)
    private Rank rank;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PERSONA", nullable = true)
    private Persona persona;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RankPerson that = (RankPerson) o;
        return id == that.id && Objects.equals(dateAddRank, that.dateAddRank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateAddRank);
    }
}

/*
 * Copyright (c) 2023
 */

package mo.specdoc.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.h2.engine.User;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;
import java.util.Objects;

@Data
@Entity
public class Rank {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private int id;
    @Basic
    @Column(name = "TITLE", nullable = false, length = 120)
    private String title;
    @Basic
    @Column(name = "TITLE_RP", nullable = false, length = 120)
    private String titleRp;
    @Basic
    @Column(name = "TITLE_DP", nullable = false, length = 120)
    private String titleDp;
    @Basic
    @Column(name = "TITLE_SHORT", nullable = true, length = 120)
    private String titleShort;

    @OneToMany(mappedBy="rank")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<RankPerson> rankPeople;

    @OneToMany(mappedBy="rank")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<State> stateDefaultRank;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rank rank = (Rank) o;
        return id == rank.id && Objects.equals(title, rank.title) && Objects.equals(titleRp, rank.titleRp) && Objects.equals(titleDp, rank.titleDp) && Objects.equals(titleShort, rank.titleShort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, titleRp, titleDp, titleShort);
    }

    @Override
    public String toString() {
        return title;
    }
}

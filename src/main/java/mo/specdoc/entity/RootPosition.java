/*
 * Copyright (c) 2023
 */

package mo.specdoc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Data
@Entity
@Table(name = "ROOT_POSITION", schema = "PUBLIC", catalog = "SPECDOC")
public class RootPosition {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RootPosition that = (RootPosition) o;
        return id == that.id && Objects.equals(title, that.title) && Objects.equals(titleRp, that.titleRp) && Objects.equals(titleDp, that.titleDp) && Objects.equals(titleShort, that.titleShort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, titleRp, titleDp, titleShort);
    }
}

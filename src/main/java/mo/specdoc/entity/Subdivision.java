/*
 * Copyright (c) 2023
 */

package mo.specdoc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
@Data
@Entity
public class Subdivision {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    @Basic
    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;
    @Basic
    @Column(name = "TITLE_RP", nullable = false, length = 200)
    private String titleRp;
    @Basic
    @Column(name = "TITLE_PP", nullable = false, length = 200)
    private String titlePp;
    @Basic
    @Column(name = "TITLE_SHORT", nullable = true, length = 150)
    private String titleShort;
    @Basic
    @Column(name = "CONDITIONAL_NUMB", nullable = true, length = 20)
    private String conditionalNumb;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subdivision that = (Subdivision) o;
        return id == that.id && Objects.equals(title, that.title) && Objects.equals(titleRp, that.titleRp) && Objects.equals(titlePp, that.titlePp) && Objects.equals(titleShort, that.titleShort) && Objects.equals(conditionalNumb, that.conditionalNumb);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, titleRp, titlePp, titleShort, conditionalNumb);
    }

    @Override
    public String toString() {
        return title;
    }
}

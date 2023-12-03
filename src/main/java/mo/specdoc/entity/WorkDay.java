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
@Table(name = "WORK_DAY", schema = "PUBLIC", catalog = "SPECDOC")
public class WorkDay {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    @Basic
    @Column(name = "ID_DOPUSK", nullable = false)
    private long idDopusk;
    @Basic
    @Column(name = "ID_WORK_TYPE", nullable = true)
    private Integer idWorkType;
    @Basic
    @Column(name = "DATE_WORK", nullable = false)
    private Date dateWork;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkDay workDay = (WorkDay) o;
        return id == workDay.id && idDopusk == workDay.idDopusk && Objects.equals(idWorkType, workDay.idWorkType) && Objects.equals(dateWork, workDay.dateWork);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idDopusk, idWorkType, dateWork);
    }

}

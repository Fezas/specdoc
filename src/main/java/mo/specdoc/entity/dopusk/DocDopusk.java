/*
 * Copyright (c) 2024
 */

package mo.specdoc.entity.dopusk;

import jakarta.persistence.*;
import lombok.Data;
import mo.specdoc.entity.PersonPosition;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Table(name = "DOC_DOPUSK", schema = "PUBLIC", catalog = "SPECDOC")
public class DocDopusk {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID_DOC", nullable = false)
    private long idDoc;
    @Basic
    @Column(name = "NUMBER", nullable = false, length = -1)
    private String number;
    @Basic
    @Column(name = "DATE", nullable = true)
    private Date date;
    @Basic
    @Column(name = "SECRECY_TYPE", nullable = false)
    private int secrecyType;
    @Basic
    @Column(name = "REVISION", nullable = false)
    private int revision;
    @Basic
    @Column(name = "ACTIVE", nullable = false)
    private boolean active;

    @OneToMany(mappedBy="docDopusk")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Dopusk> dopuskList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocDopusk docDopusk = (DocDopusk) o;
        return idDoc == docDopusk.idDoc && secrecyType == docDopusk.secrecyType && revision == docDopusk.revision && active == docDopusk.active && Objects.equals(number, docDopusk.number) && Objects.equals(date, docDopusk.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDoc, number, date, secrecyType, revision, active);
    }
}

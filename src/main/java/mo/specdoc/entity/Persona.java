/*
 * Copyright (c) 2023
 */

package mo.specdoc.entity;

import jakarta.persistence.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.sql.Date;
import java.util.List;
import java.util.Objects;
@Data
@Entity
@Table(name = "PERSONA", schema = "PUBLIC", catalog = "SPECDOC")
public class Persona {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    @Basic
    @Column(name = "FAMILY", nullable = false, length = 30)
    private String family;
    @Basic
    @Column(name = "FAMILY_RP", nullable = false, length = 30)
    private String familyRp;
    @Basic
    @Column(name = "FAMILY_DP", nullable = false, length = 30)
    private String familyDp;
    @Basic
    @Column(name = "NAME_PERSON", nullable = false, length = 30)
    private String namePerson;
    @Basic
    @Column(name = "NAME_RP", nullable = false, length = 30)
    private String nameRp;
    @Basic
    @Column(name = "NAME_DP", nullable = false, length = 30)
    private String nameDp;
    @Basic
    @Column(name = "LASTNAME", nullable = false, length = 30)
    private String lastname;
    @Basic
    @Column(name = "LASTNAME_RP", nullable = false, length = 30)
    private String lastnameRp;
    @Basic
    @Column(name = "LASTNAME_DP", nullable = false, length = 30)
    private String lastnameDp;
    @Basic
    @Column(name = "NUMB", nullable = false, length = 30)
    private String numb;
    @Basic
    @Column(name = "GENDER", nullable = false)
    private boolean gender;
    @Basic
    @Column(name = "DATE_BIRTH", nullable = false)
    private Date dateBirth;

    @ManyToMany
    @JoinTable (name="RANK_PERSON",
            joinColumns=@JoinColumn (name="ID_PERSONA"),
            inverseJoinColumns=@JoinColumn(name="ID_RANK"))
    private List<Rank> ranks;

    @OneToMany(mappedBy="persona")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<AmmoPersona> ammoPersons;

    @OneToMany(mappedBy="persona")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<RankPerson> rankPeople;

    @OneToMany(mappedBy="persona")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<SecrecyPerson> secrecyPeople;


    @Transient
    private String fio;
    @Transient
    private StringProperty familyStringProperty= new SimpleStringProperty();
    @Transient
    private StringProperty nameStringProperty = new SimpleStringProperty();
    @Transient
    private StringProperty lastnameStringProperty = new SimpleStringProperty();
    @Transient
    private final ObjectProperty<HBox> control = new SimpleObjectProperty<>();
    @Transient
    private final ObjectProperty<Date> birthdayObjectProperty = new SimpleObjectProperty<>();
    @Transient
    private CheckBox check = new CheckBox();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return id == persona.id && gender == persona.gender && Objects.equals(family, persona.family) && Objects.equals(familyRp, persona.familyRp) && Objects.equals(familyDp, persona.familyDp) && Objects.equals(namePerson, persona.namePerson) && Objects.equals(nameRp, persona.nameRp) && Objects.equals(nameDp, persona.nameDp) && Objects.equals(lastname, persona.lastname) && Objects.equals(lastnameRp, persona.lastnameRp) && Objects.equals(lastnameDp, persona.lastnameDp) && Objects.equals(numb, persona.numb) && Objects.equals(dateBirth, persona.dateBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, family, familyRp, familyDp, namePerson, nameRp, nameDp, lastname, lastnameRp, lastnameDp, numb, gender, dateBirth);
    }

    @Override
    public String toString() {
        return family;
    }

    public String getFio() {
        return family + " " + namePerson.charAt(0) + "." + lastname.charAt(0) + ".";
    }
}

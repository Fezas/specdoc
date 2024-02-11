/*
 * Copyright (c) 2024
 */

package mo.specdoc.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;
import java.util.Objects;

@Entity
@Data
@Table(name = "STATE", schema = "PUBLIC", catalog = "SPECDOC")
public class State {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID_STATE", nullable = false)
    private long idState;
    @Basic
    @Column(name = "TYPE_STATE", nullable = false)
    private int typeState;
    @Basic
    @Column(name = "PARENT_ID_STATE", nullable = true)
    private Long parentIdState;
    @Basic
    @Column(name = "TITLE_STATE", nullable = false, length = -1)
    private String titleState;
    @Basic
    @Column(name = "TITLE_STATE_SHORT", nullable = true, length = -1)
    private String titleStateShort;
    @Basic
    @Column(name = "TITLE_STATE_RP", nullable = true, length = -1)
    private String titleStateRp;
    @Basic
    @Column(name = "TITLE_STATE_DP", nullable = true, length = -1)
    private String titleStateDp;
    @Basic
    @Column(name = "TITLE_STATE_PP", nullable = true, length = -1)
    private String titleStatePp;
    @Basic
    @Column(name = "POST_ARMED", nullable = false)
    private boolean postArmed;
    @Basic
    @Column(name = "POST_NUMB", nullable = true, length = -1)
    private String postNumb;
    @Basic
    @Column(name = "POST_IS_AMPLIFICATION", nullable = false)
    private boolean postIsAmplification;
    @Basic
    @Column(name = "POSITION_VUS", nullable = true, length = -1)
    private String positionVus;
    @Basic
    @Column(name = "SUBDIVISION_ADDRESS", nullable = true, length = -1)
    private String subdivisionAddress;
    @Basic
    @Column(name = "SUBDIVISION_COND_NUMBER", nullable = true, length = -1)
    private String subdivisionCondNumber;
    @Basic
    @Column(name = "SORT_VALUE", nullable = false)
    private int sortValue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "POSITION_RANK_DEFAULT", nullable = true)
    private Rank rank;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "POST_SECRECY_DEFAULT", nullable = true)
    private SecrecyType secrecyType;

    @OneToMany(mappedBy="personaFromPosition")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<PersonPosition> personPositions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return idState == state.idState && typeState == state.typeState && postArmed == state.postArmed && postIsAmplification == state.postIsAmplification && sortValue == state.sortValue && Objects.equals(parentIdState, state.parentIdState) && Objects.equals(titleState, state.titleState) && Objects.equals(titleStateShort, state.titleStateShort) && Objects.equals(titleStateRp, state.titleStateRp) && Objects.equals(titleStateDp, state.titleStateDp) && Objects.equals(titleStatePp, state.titleStatePp) && Objects.equals(postNumb, state.postNumb) && Objects.equals(positionVus, state.positionVus) && Objects.equals(subdivisionAddress, state.subdivisionAddress) && Objects.equals(subdivisionCondNumber, state.subdivisionCondNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idState, typeState, parentIdState, titleState, titleStateShort, titleStateRp, titleStateDp, titleStatePp, postArmed, postNumb, postIsAmplification, positionVus, subdivisionAddress, subdivisionCondNumber, sortValue);
    }

    @Override
    public String toString() {
        return titleState;
    }
}

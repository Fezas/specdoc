/*
 * Copyright (c) 2024
 */

package mo.specdoc.dto;

import javafx.beans.property.*;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import lombok.Data;
import mo.specdoc.entity.*;
import mo.specdoc.entity.dopusk.Dopusk;
import mo.specdoc.model.*;

import java.sql.Date;
import java.util.List;
@Data
public class PersonaDTO {
    private Persona persona;
    private StringProperty fio;
    private LongProperty idLongProperty;
    private StringProperty secrecyStringProperty;
    private StringProperty rankStringProperty;
    private StringProperty ammoStringProperty;
    private StringProperty stateStringProperty;
    private StringProperty dopuskStringProperty;
    private StringProperty familyStringProperty;
    private StringProperty nameStringProperty;
    private StringProperty lastnameStringProperty;
    private ObjectProperty<HBox> control = new SimpleObjectProperty<>();
    private ObjectProperty<Date> birthdayObjectProperty = new SimpleObjectProperty<>();
    private CheckBox check = new CheckBox();

    public PersonaDTO() {
    }

    public PersonaDTO(Persona persona) {
        this.persona = persona;//заметь на ID
        this.idLongProperty = new SimpleLongProperty(persona.getId());
        this.familyStringProperty = new SimpleStringProperty(persona.getFamily());
        this.nameStringProperty = new SimpleStringProperty(persona.getNamePerson());
        this.lastnameStringProperty = new SimpleStringProperty(persona.getLastname());
        this.birthdayObjectProperty = new SimpleObjectProperty(persona.getDateBirth());
        this.secrecyStringProperty = new SimpleStringProperty(initSecrecy(persona));
        this.rankStringProperty = new SimpleStringProperty(initRank(persona));
        this.ammoStringProperty = new SimpleStringProperty(initAmmo(persona));
        this.stateStringProperty = new SimpleStringProperty(initState(persona));
        this.dopuskStringProperty = new SimpleStringProperty(initPosts(persona));
        this.check = new CheckBox();
    }

    private String initPosts(Persona persona) {
        List<Dopusk> posts = DopuskModel.getByIdPersona(persona.getId());
        String str = "";
        if (!posts.isEmpty()) {
            if (posts.size() > 1) {
                for (Dopusk dopusk : posts) {
                    str = str + dopusk.getState().getTitleState() + "\n";
                }
            } else str = posts.get(0).getState().getTitleState();

        } else str = "нет допуска";
        return str;
    }

    private String initSecrecy(Persona persona) {
        String str = "";
        SecrecyPerson secrecyPerson = SecrecyPersonModel.getLastSecrecyByIdPerson(persona.getId());
        if (secrecyPerson != null) {
            str = secrecyPerson.getSecrecyType().getTitle();
        } else str = "нет ДССГТ";
        return str;
    }

    private String initState(Persona persona) {
        String str = "";
        PersonPosition personPosition = PersonPositionModel.getLastPositionByIdPerson(persona.getId());
        if (personPosition != null) {
            str = personPosition.getState().getTitleState();
        } else str = "в распоряжении";
        return str;
    }

    private String initAmmo(Persona persona) {
        String str = "";
        AmmoPersona ammoPersona = AmmoPersonaModel.getByIdPersona(persona.getId());
        if (ammoPersona != null) {
            str = ammoPersona.getAmmoType() + " №" + ammoPersona.getNumbAmmo();
        } else str = "не закреплено";
        return str;
    }

    private String initRank(Persona persona) {
        String str = "";
        RankPerson rankPerson = RankPersonModel.getLastRankByIdPerson(persona.getId());
        if (rankPerson != null) {
            str = rankPerson.getRank().getTitle();
        } else str = "гражданский персонал";
        return str;
    }
}

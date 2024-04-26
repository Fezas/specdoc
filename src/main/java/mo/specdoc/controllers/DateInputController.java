/*
 * Copyright (c) 2022-2023. Stepantsov P.V.
 */

package mo.specdoc.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import mo.specdoc.entity.PersonPosition;
import mo.specdoc.entity.Persona;
import mo.specdoc.entity.State;
import mo.specdoc.model.PersonPositionModel;
import mo.specdoc.util.FXMLControllerManager;
import org.kordamp.ikonli.javafx.FontIcon;


public class DateInputController {
    private Persona persona;
    private State state;
    @FXML    private Button btnCancel, btnSave;
    @FXML    private DatePicker dp;

    @FXML
    void cancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void save() {
        btnSave.setGraphic(new FontIcon("anto-save"));
        btnCancel.setGraphic(new FontIcon("anto-close"));
        PersonPosition personPosition = new PersonPosition();
        State state = FXMLControllerManager.getInstance().getPositionController().getCurrentState();
        Persona persona = FXMLControllerManager.getInstance().getPersonsViewController().getCurrentPersona().getPersona();
        personPosition.setState(state);
        personPosition.setPersonaFromPosition(persona);
        personPosition.setDateAddPosition(java.sql.Date.valueOf(dp.getValue()));
        PersonPositionModel.saveOrUpdate(personPosition);
        FXMLControllerManager.getInstance().getPositionController().refresh();
        FXMLControllerManager.getInstance().getPersonsViewController().cancel();
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

}

/*
 * Copyright (c) 2023
 */

package mo.specdoc.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import mo.specdoc.entity.PersonPosition;
import mo.specdoc.entity.Persona;
import mo.specdoc.entity.Position;
import mo.specdoc.model.PersonPositionModel;
import org.kordamp.ikonli.javafx.FontIcon;


public class DateInputController {
    private Persona persona;
    private Position position;
    @FXML    private Button btnCancel, btnSave;
    @FXML    private DatePicker dp;

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void save() {
        btnSave.setGraphic(new FontIcon("anto-save"));
        btnCancel.setGraphic(new FontIcon("anto-close"));
        PersonPosition personPosition = new PersonPosition();
        personPosition.setPosition(position);
        personPosition.setPersonaFromPosition(persona);
        personPosition.setDateAddPosition(java.sql.Date.valueOf(dp.getValue()));
        PersonPositionModel.saveOrUpdate(personPosition);
        PositionController.getInstance().refresh();
        PersonsViewController.getInstance().cancel();
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void setPersonAndPosition(Persona persona, Position position) {
        this.persona = persona;
        this.position = position;
    }
}

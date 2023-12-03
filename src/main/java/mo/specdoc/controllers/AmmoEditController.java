package mo.specdoc.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import mo.specdoc.entity.*;
import mo.specdoc.model.AmmoPersonaModel;
import mo.specdoc.model.AmmoTypeModel;
import mo.specdoc.model.RankModel;
import mo.specdoc.model.RankPersonModel;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AmmoEditController {
    private PersonEditController personEditController;
    private AmmoPersona ammoPersona = new AmmoPersona();
    private Persona persona;
    @FXML    private Button btnCancel, btnSave, btnHelp;

    @FXML    private ComboBox<AmmoType> cmbBoxWeaponType;
    @FXML    private TextField tfieldNumbWeapon;

    public void setParent (PersonEditController personEditController){
        this.personEditController = personEditController;
    }

    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void help(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Внимание");
        alert.setHeaderText("Невозможно сохранить");
        alert.setContentText("error");
        alert.showAndWait();
    }


    private void alert(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Внимание");
        alert.setHeaderText("Невозможно сохранить");
        alert.setContentText(error);
        alert.showAndWait();
    }

    @FXML
    private void save(ActionEvent event) {
        if (tfieldNumbWeapon.getText() == "") {
            alert ("Не введен номер");
        } else if (cmbBoxWeaponType.getSelectionModel().getSelectedItem() == null) {
            alert ("Не выбран тип оружия");
        } else {
            ammoPersona.setPersona(persona);
            ammoPersona.setAmmoType(cmbBoxWeaponType.getSelectionModel().getSelectedItem());
            ammoPersona.setNumbAmmo(tfieldNumbWeapon.getText());
            AmmoPersonaModel.saveOrUpdate(ammoPersona);
            personEditController.getLblAmmo().setDisable(false);
            personEditController.getLbl5().setDisable(false);
            personEditController.getLblAmmo().setText(cmbBoxWeaponType.getSelectionModel().getSelectedItem().getTitle() +
                    " №" + tfieldNumbWeapon.getText()
            );
            cmbBoxWeaponType.getSelectionModel().clearSelection();
            tfieldNumbWeapon.setText("");
            ammoPersona = new  AmmoPersona();//сбрасываем редактирование
        }
    }

    public void initialize(Persona persona) {
        this.persona = persona;
        btnSave.setGraphic(new FontIcon("anto-save"));
        btnCancel.setGraphic(new FontIcon("anto-close"));
        btnHelp.setGraphic(new FontIcon("anto-info-circle"));
        for (AmmoType ammoType : AmmoTypeModel.getAllRecords()) {
            cmbBoxWeaponType.getItems().add(ammoType);
        }
    }
}

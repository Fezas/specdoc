package mo.specdoc.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mo.specdoc.entity.AmmoPersona;
import mo.specdoc.entity.AmmoType;
import mo.specdoc.entity.Persona;
import mo.specdoc.model.AmmoPersonaModel;
import mo.specdoc.model.AmmoTypeModel;
import mo.specdoc.util.FXMLControllerManager;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class AmmoEditController implements Initializable {
    private AmmoPersona ammoPersona = new AmmoPersona();
    private Persona currentPersona;
    @FXML    private Button btnCancel, btnSave, btnHelp;

    @FXML    private ComboBox<AmmoType> cmbBoxWeaponType;
    @FXML    private TextField tfieldNumbWeapon;

    @FXML
    private void close() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void help() {
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

    /**
     *Процедура проверки установки в зависимости от наличия оружия у персоны
     * видимости Label lblAmmo PersonEditController
     */
    private void setLabelDisableEditForm() {
        Label label = FXMLControllerManager.getInstance().getPersonEditController().getLblAmmo();
        if (ammoPersona.getNumbAmmo() != null) {
            label.setDisable(false);
            label.setText(cmbBoxWeaponType.getSelectionModel().getSelectedItem().getTitle() +
                    " №" + tfieldNumbWeapon.getText());
        } else {
            label.setDisable(true);
        }
    }

    @FXML
    private void save() {
        if ("".equals(tfieldNumbWeapon.getText())) {
            alert ("Не введен номер");
        } else if (cmbBoxWeaponType.getSelectionModel().getSelectedItem() == null) {
            alert ("Не выбран тип оружия");
        } else {
            ammoPersona.setPersona(currentPersona);
            ammoPersona.setAmmoType(cmbBoxWeaponType.getSelectionModel().getSelectedItem());
            ammoPersona.setNumbAmmo(tfieldNumbWeapon.getText());
            AmmoPersonaModel.saveOrUpdate(ammoPersona);
            setLabelDisableEditForm();
            cmbBoxWeaponType.getSelectionModel().clearSelection();
            tfieldNumbWeapon.setText("");
            ammoPersona = new  AmmoPersona();//сбрасываем редактирование
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentPersona = FXMLControllerManager.getInstance().getPersonEditController().getCurrentPersona();
        btnSave.setGraphic(new FontIcon("anto-save"));
        btnCancel.setGraphic(new FontIcon("anto-close"));
        btnHelp.setGraphic(new FontIcon("anto-info-circle"));
        for (AmmoType ammoType : AmmoTypeModel.getAllRecords()) {
            cmbBoxWeaponType.getItems().add(ammoType);
        }
    }
}

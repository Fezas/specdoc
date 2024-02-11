package mo.specdoc.controllers;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Data;
import mo.specdoc.entity.*;
import mo.specdoc.entity.dopusk.Dopusk;
import mo.specdoc.model.*;
import mo.specdoc.util.Case;
import mo.specdoc.util.FXMLControllerManager;
import org.controlsfx.control.ToggleSwitch;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Data
public class PersonEditController implements Initializable {
    private Persona currentPersona;
    private PersonsViewController personsViewController;
    @FXML    private Button btnCancel, btnSave, btnAddPost, btnEditSecrecyPerson, btnEditRankPerson, btnEditPositionPerson,
            btnEditPostPerson, btnEditAmmoPerson;
    @FXML    private Label lblSecrecy, lblRank, lblPosition, lblPost, lblAmmo;
    @FXML    private Button btnAutoInput;
    @FXML    private TextField tfieldFamily, tfieldFamilyDP, tfieldFamilyRP;
    @FXML    private TextField tfieldLastname, tfieldLastnameDP, tfieldLastnameRP;
    @FXML    private TextField tfieldName, tfieldNameDP, tfieldNameRP;
    @FXML    private ToggleSwitch tglSwitchGender;
    @FXML    private TextField tfieldNumberPersona;
    @FXML    private DatePicker datePickerBirth;

    public void setParent (PersonsViewController controller){
        this.personsViewController = controller;
    }

    /**
     * Процедура заполнения полей нужными падежами
     */
    @FXML
    void autoInputPadej(ActionEvent event) {
        Case padej = new Case(
                tfieldFamily.getText(),
                tfieldName.getText(),
                tfieldLastname.getText(),
                tglSwitchGender.isSelected()
        );
        String[] rp = padej.getFamInRP();
        tfieldFamilyRP.setText(rp[0]);
        tfieldNameRP.setText(rp[1]);
        tfieldLastnameRP.setText(rp[2]);
        String[] dp = padej.getFamInDP();
        tfieldFamilyDP.setText(dp[0]);
        tfieldNameDP.setText(dp[1]);
        tfieldLastnameDP.setText(dp[2]);
    }

    @FXML
    void cancelAction() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private Persona personCreate(Persona person) {
        person.setFamily(tfieldFamily.getText());
        person.setFamilyRp(tfieldFamilyRP.getText());
        person.setFamilyDp(tfieldFamilyDP.getText());
        person.setNamePerson(tfieldName.getText());
        person.setNameRp(tfieldNameRP.getText());
        person.setNameDp(tfieldNameDP.getText());
        person.setLastname(tfieldLastname.getText());
        person.setLastnameRp(tfieldLastnameRP.getText());
        person.setLastnameDp(tfieldLastnameDP.getText());
        person.setGender(tglSwitchGender.isSelected());
        person.setNumb(tfieldNumberPersona.getText());
        person.setDateBirth(java.sql.Date.valueOf(datePickerBirth.getValue()));
        return person;
    }

    @FXML
    void saveAction() {
        //Persona-------------------------------------------------------------------------------------------------------
        Persona person;
        if (currentPersona == null) {
            person = new Persona();
        } else {
            person = personCreate(currentPersona);
        }
        PersonaModel.saveOrUpdate(person);
        FXMLControllerManager.getInstance().getPersonsViewController().initData();//обновляем данные в таблице
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();


    }

    public void loadPersona() {
        currentPersona = FXMLControllerManager.getInstance().getPersonsViewController().getCurrentPersona().getPersona();
        if (currentPersona.getId() != 0L) { //режим редактирования персоны
            SecrecyPerson secrecyPerson = SecrecyPersonModel.getLastSecrecyByIdPerson(currentPersona.getId());
            if (secrecyPerson != null) {
                lblSecrecy.setText(secrecyPerson.getSecrecyType().getTitle());
                lblSecrecy.setDisable(false);
            }
            RankPerson rankPerson = RankPersonModel.getLastRankByIdPerson(currentPersona.getId());
            if (rankPerson != null) {
                lblRank.setText(rankPerson.getRank().getTitle());
                lblRank.setDisable(false);
            }
            PersonPosition personPosition = PersonPositionModel.getLastPositionByIdPerson(currentPersona.getId());
            if (personPosition != null) {
                lblPosition.setText(personPosition.getState().getTitleState() + " с " + personPosition.getDateAddPosition());
                lblPosition.setDisable(false);
            }
            AmmoPersona ammoPersona = AmmoPersonaModel.getByIdPersona(currentPersona.getId());
            if (ammoPersona != null) {
                lblAmmo.setText(ammoPersona.getAmmoType().getTitle() + " №" + ammoPersona.getNumbAmmo());
                lblAmmo.setDisable(false);
            }
            List<Dopusk> dopusk = DopuskModel.getByIdPersona(currentPersona.getId());
            if (!dopusk.isEmpty()) {
                lblPost.setText("допущен к " + dopusk.size() + " постам");
                lblPost.setDisable(false);
            }

            //Persona---------------------------------------------------------------------------------------------------
            tfieldFamily.setText(currentPersona.getFamily());
            tfieldFamilyDP.setText(currentPersona.getFamilyDp());
            tfieldFamilyRP.setText(currentPersona.getFamilyRp());
            tfieldLastname.setText(currentPersona.getLastname());
            tfieldLastnameDP.setText(currentPersona.getLastnameDp());
            tfieldLastnameRP.setText(currentPersona.getLastnameRp());
            tfieldName.setText(currentPersona.getNamePerson());
            tfieldNameDP.setText(currentPersona.getNameDp());
            tfieldNameRP.setText(currentPersona.getNameRp());
            tglSwitchGender.setSelected(currentPersona.isGender());
            tfieldNumberPersona.setText(currentPersona.getNumb());
            datePickerBirth.setValue(currentPersona.getDateBirth().toLocalDate());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMLControllerManager.getInstance().setPersonEditController(this);
        loadPersona();
        btnEditSecrecyPerson.setGraphic(new FontIcon("anto-lock:14"));
        btnEditRankPerson.setGraphic(new FontIcon("antf-star:14"));
        btnEditPositionPerson.setGraphic(new FontIcon("anto-partition:14"));
        btnEditPostPerson.setGraphic(new FontIcon("anto-schedule:14"));
        btnEditAmmoPerson.setGraphic(new FontIcon("anto-thunderbolt:14"));
        //иницилизация значений элементов управления формы
        btnAutoInput.setGraphic(new FontIcon("anto-reload:12"));
        //валидация кнопки сохранения
        btnSave.disableProperty().bind(
                Bindings.isEmpty(tfieldFamily.textProperty())
                        .or(Bindings.isEmpty(tfieldFamily.textProperty()))
                        .or(Bindings.isEmpty(tfieldFamilyRP.textProperty()))
                        .or(Bindings.isEmpty(tfieldFamilyDP.textProperty()))
                        .or(Bindings.isEmpty(tfieldName.textProperty()))
                        .or(Bindings.isEmpty(tfieldNameRP.textProperty()))
                        .or(Bindings.isEmpty(tfieldNameDP.textProperty()))
                        .or(Bindings.isEmpty(tfieldLastname.textProperty()))
                        .or(Bindings.isEmpty(tfieldLastnameRP.textProperty()))
                        .or(Bindings.isEmpty(tfieldLastnameDP.textProperty()))
                        .or(Bindings.isEmpty(tfieldNumberPersona.textProperty()))
                        .or(datePickerBirth.valueProperty().isNull())
                        //.or(Bindings.isEmpty(positions))
                        //.or(Bindings.isEmpty(posts))
                        //.or(datePickerAchievSecrecyWork.valueProperty().isNull())
        );

    }

    @FXML
    private void editDopuskPerson() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/person-post.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Уровень допуска с СГТ");
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (NullPointerException e) {
            e.printStackTrace();
            //logger.error("Error", e);
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
            //logger.error("Error", e);
        }
    }

    @FXML
    private void editSecrecyPerson() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/person-secrecy.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Уровень допуска с СГТ");
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (NullPointerException e) {
            e.printStackTrace();
            //logger.error("Error", e);
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
            //logger.error("Error", e);
        }
    }

    @FXML
    private void editRankPerson() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/person-rank.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Звание");
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (NullPointerException e) {
            e.printStackTrace();
            //logger.error("Error", e);
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
            //logger.error("Error", e);
        }
    }

    @FXML
    private void editAmmoPerson() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/person-ammo.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Вооружение");
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (NullPointerException e) {
            e.printStackTrace();
            //logger.error("Error", e);
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
            //logger.error("Error", e);
        }
    }
}

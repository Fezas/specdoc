package mo.specdoc.controllers;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Data;
import mo.specdoc.entity.*;
import mo.specdoc.model.*;
import mo.specdoc.util.Case;
import org.controlsfx.control.ToggleSwitch;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

@Data
public class PersonEditController {
    private Persona currentPersona;
    private PositionController positionController;
    private static PersonEditController singleton;
    @FXML    private Button btnCancel, btnSave, btnAddPost, btnEditSecrecyPerson, btnEditRankPerson, btnEditPositionPerson,
            btnEditPostPerson, btnEditAmmoPerson;
    @FXML    private Label lblSecrecy, lblRank, lblPosition, lblPost, lblAmmo, lbl1, lbl2, lbl3, lbl4, lbl5;
    //Persona
    @FXML    private Button btnAutoInput;
    @FXML    private TextField tfieldFamily, tfieldFamilyDP, tfieldFamilyRP;
    @FXML    private TextField tfieldLastname, tfieldLastnameDP, tfieldLastnameRP;
    @FXML    private TextField tfieldName, tfieldNameDP, tfieldNameRP;
    @FXML    private ToggleSwitch tglSwitchGender;
    @FXML    private TextField tfieldNumberPersona;
    @FXML    private DatePicker datePickerBirth;

    public static PersonEditController getInstance() {
        if (singleton == null)
            singleton = new PersonEditController();
        return singleton;
    }

    public void setParent (PositionController positionController){
        this.positionController = positionController;
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
    void cancelAction(ActionEvent event) {
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
    void saveAction(ActionEvent event) {
        //Persona-------------------------------------------------------------------------------------------------------
        Persona person;
        if (currentPersona == null) {
            person = new Persona();
        } else {
            person = personCreate(currentPersona);
        }
        PersonaModel.saveOrUpdate(person);
        PersonsViewController.getInstance().allPersons();
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    public void initialize(Persona persona) {
        currentPersona = persona;
        System.out.println("@@@@@@@@@@@@@@@" + currentPersona.getFamily());
        btnEditSecrecyPerson.setGraphic(new FontIcon("anto-lock:14"));
        btnEditRankPerson.setGraphic(new FontIcon("antf-star:14"));
        btnEditPositionPerson.setGraphic(new FontIcon("anto-partition:14"));
        btnEditPostPerson.setGraphic(new FontIcon("anto-schedule:14"));
        btnEditAmmoPerson.setGraphic(new FontIcon("anto-thunderbolt:14"));
        if (persona.getId() != 0L) {
            SecrecyPerson secrecyPerson = SecrecyPersonModel.getLastSecrecyByIdPerson(persona.getId());
            if (secrecyPerson != null) {
                lblSecrecy.setText(secrecyPerson.getSecrecyType().getTitle());
                lblSecrecy.setDisable(false);
                lbl1.setDisable(false);
            }
            RankPerson rankPerson = RankPersonModel.getLastRankByIdPerson(persona.getId());
            if (rankPerson != null) {
                lblRank.setText(rankPerson.getRank().getTitle());
                lblRank.setDisable(false);
                lbl2.setDisable(false);
            }
            PersonPosition personPosition = PersonPositionModel.getLastPositionByIdPerson(persona.getId());
            if (personPosition != null) {
                lblPosition.setText(personPosition.getPosition().getTitle() + " с " + personPosition.getDateAddPosition());
                lblPosition.setDisable(false);
                lbl3.setDisable(false);
            }
            AmmoPersona ammoPersona = AmmoPersonaModel.getByIdPersona(persona.getId());
            if (ammoPersona != null) {
                lblAmmo.setText(ammoPersona.getAmmoType().getTitle() + " №" + ammoPersona.getNumbAmmo());
                lblAmmo.setDisable(false);
                lbl5.setDisable(false);
            }
            List<Dopusk> dopusk = DopuskModel.getByIdPersona(persona.getId());
            if (!dopusk.isEmpty()) {
                lblPost.setText("допущен к " + dopusk.size() + " постам");
                lblPost.setDisable(false);
                lbl4.setDisable(false);
            }
            //lbl3.setDisable(false);
        }

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
        if (persona.getId() != 0L) { //режим редактирования персоны
            //Persona---------------------------------------------------------------------------------------------------
            tfieldFamily.setText(persona.getFamily());
            tfieldFamilyDP.setText(persona.getFamilyDp());
            tfieldFamilyRP.setText(persona.getFamilyRp());
            tfieldLastname.setText(persona.getLastname());
            tfieldLastnameDP.setText(persona.getLastnameDp());
            tfieldLastnameRP.setText(persona.getLastnameRp());
            tfieldName.setText(persona.getNamePerson());
            tfieldNameDP.setText(persona.getNameDp());
            tfieldNameRP.setText(persona.getNameRp());
            tglSwitchGender.setSelected(persona.isGender());
            tfieldNumberPersona.setText(persona.getNumb());
            datePickerBirth.setValue(persona.getDateBirth().toLocalDate());
        }
    }

    @FXML
    private void editDopuskPerson() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/person-post.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Уровень допуска с СГТ");
            Scene scene = new Scene(loader.load());
            DopuskController c = loader.getController();
            c.setParent(this);
            c.initalize();
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
            SecrecyEditController c = loader.getController();
            c.initialize(currentPersona);
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
            RankEditController c = loader.getController();
            c.initialize(currentPersona);
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
            AmmoEditController children = loader.getController();
            children.setParent(this);
            children.initialize(currentPersona);
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

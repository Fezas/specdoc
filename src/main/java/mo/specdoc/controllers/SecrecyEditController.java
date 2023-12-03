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
import mo.specdoc.entity.Persona;
import mo.specdoc.entity.SecrecyPerson;
import mo.specdoc.entity.SecrecyType;
import mo.specdoc.model.SecrecyPersonModel;
import mo.specdoc.model.SecrecyTypeModel;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SecrecyEditController {
    private static ObservableList<SecrecyPerson> secrecies = FXCollections.observableArrayList();
    private SecrecyPerson secrecyPerson = new SecrecyPerson();
    private Persona currentPersona;
    @FXML    private Button btnCancel, btnSave, btnHelp;

    @FXML    private ComboBox<SecrecyType> cmbBoxSecrecyPersona;
    @FXML    private TextField tfieldNumbDocsSecrecy;
    @FXML    private DatePicker datePickerAchievSecrecyWork;
    @FXML    private TableView<SecrecyPerson> tblSecrecy;
    @FXML    private TableColumn<SecrecyPerson, String> tblClmnSecrecyLevel;
    @FXML    private TableColumn<SecrecyPerson, String> tblClmnDateAddSecrecy;

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
        if (datePickerAchievSecrecyWork.getValue() == null) {
            alert ("Не введена дата");
        } else if (cmbBoxSecrecyPersona.getSelectionModel().getSelectedItem() == null) {
            alert ("Не выбран уровень допуска");
        } else {
            secrecyPerson.setPersona(currentPersona);
            secrecyPerson.setSecrecyType(cmbBoxSecrecyPersona.getSelectionModel().getSelectedItem());
            secrecyPerson.setDateAddSecrecy(java.sql.Date.valueOf(datePickerAchievSecrecyWork.getValue()));
            SecrecyPersonModel.saveOrUpdate(secrecyPerson);
            datePickerAchievSecrecyWork.setValue(null);
            PersonEditController.getInstance().getLblSecrecy().setText(
                    cmbBoxSecrecyPersona.getSelectionModel().getSelectedItem().getTitle()
            );
            PersonEditController.getInstance().getLblSecrecy().setDisable(false);
            secrecyPerson = new SecrecyPerson();//сбрасываем редактирование
            cmbBoxSecrecyPersona.getSelectionModel().clearSelection();
        }
        data();
    }


    public void initialize(Persona persona) {
        currentPersona = persona;
        btnSave.setGraphic(new FontIcon("anto-save"));
        btnCancel.setGraphic(new FontIcon("anto-close"));
        btnHelp.setGraphic(new FontIcon("anto-info-circle"));
        data();
        for (SecrecyType secrecyType : SecrecyTypeModel.getAllRecords()) {
            cmbBoxSecrecyPersona.getItems().add(secrecyType);
        }

        tblClmnSecrecyLevel.setCellValueFactory(new PropertyValueFactory<>("secrecyType"));
        tblClmnDateAddSecrecy.setCellValueFactory(new PropertyValueFactory<>("dateAddSecrecy"));
        tblSecrecy.setItems(secrecies);

        tblSecrecy.setRowFactory(
                tableView -> {
                    //событие по двойному клику строки
                    final TableRow<SecrecyPerson> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                            SecrecyPerson rowData = row.getItem();
                        }
                    });
                    //контексное меню
                    final ContextMenu rowMenu = new ContextMenu();
                    MenuItem editItem = new MenuItem("Редактировать");
                    MenuItem removeItem = new MenuItem("Удалить пункт");

                    editItem.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            cmbBoxSecrecyPersona.getSelectionModel().select(row.getItem().getSecrecyType());
                            datePickerAchievSecrecyWork.setValue(row.getItem().getDateAddSecrecy().toLocalDate());
                            secrecyPerson.setId(row.getItem().getId());
                        }
                    });
                    removeItem.setOnAction(event -> {
                        Alert alertDelete = new Alert(Alert.AlertType.CONFIRMATION);
                        alertDelete.setTitle("Внимание");
                        alertDelete.setHeaderText("Удаление записи");
                        alertDelete.setContentText("Удалить запись: " + row.getItem().getSecrecyType() + "?");
                        Optional<ButtonType> option = alertDelete.showAndWait();
                        if (option.get() == null) {

                        } else if (option.get() == ButtonType.OK) {
                            SecrecyPersonModel.delete(row.getItem());
                            tblSecrecy.getItems().remove(row.getItem());
                            data();
                        } else if (option.get() == ButtonType.CANCEL) {

                        }
                    });
                    rowMenu.getItems().addAll(editItem, removeItem);
                    row.contextMenuProperty().bind(
                            Bindings.when(row.emptyProperty())
                                    .then((ContextMenu) null)
                                    .otherwise(rowMenu));
                    return row;
                }
        );
    }


    private void data() {//инициализация истории званий
        secrecies.clear();
        for (SecrecyPerson secrecyPerson : SecrecyPersonModel.getAllSecreciesByIdPerson(currentPersona.getId())) {
            secrecies.add(secrecyPerson);
        }
    }
}

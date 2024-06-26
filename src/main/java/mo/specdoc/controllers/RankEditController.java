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
import mo.specdoc.entity.Rank;
import mo.specdoc.entity.RankPerson;
import mo.specdoc.model.RankModel;
import mo.specdoc.model.RankPersonModel;
import mo.specdoc.util.FXMLControllerManager;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class RankEditController implements Initializable {
    private static ObservableList<RankPerson> ranks = FXCollections.observableArrayList();
    private RankPerson rankPerson = new RankPerson();
    private Persona currentPersona;
    @FXML    private Button btnCancel, btnSave, btnHelp;

    @FXML    private ComboBox<Rank> cmbBoxRankPersona;
    @FXML    private TextField tfieldNumbDocsRank;
    @FXML    private DatePicker datePickerAchievRank;
    @FXML    private TableView<RankPerson> tblRank;
    @FXML    private TableColumn<RankPerson, String> tblClmnRank;
    @FXML    private TableColumn<RankPerson, String> tblClmnDateAddRank;

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
     *Процедура проверки установки в зависимости от наличия званий у персоны
     * видимости Label lblRank PersonEditController
     */
    private void setLabelDisableEditForm() {
        Label label = FXMLControllerManager.getInstance().getPersonEditController().getLblRank();
        if (!ranks.isEmpty()) {
            label.setDisable(false);
            label.setText(ranks.get(ranks.size() - 1).getRank().getTitle());
        } else {
            label.setDisable(true);
        }
    }

    @FXML
    private void save() {
        if (datePickerAchievRank.getValue() == null) {
            alert ("Не введена дата");
        } else if (cmbBoxRankPersona.getSelectionModel().getSelectedItem() == null) {
            alert ("Не выбрано звание");
        } else {
            rankPerson.setPersona(currentPersona);
            rankPerson.setRank(cmbBoxRankPersona.getSelectionModel().getSelectedItem());
            rankPerson.setDateAddRank(java.sql.Date.valueOf(datePickerAchievRank.getValue()));
            RankPersonModel.saveOrUpdate(rankPerson);
            datePickerAchievRank.setValue(null);
            FXMLControllerManager.getInstance().getPersonEditController().getLblRank().setText(
                    cmbBoxRankPersona.getSelectionModel().getSelectedItem().getTitle()
            );
            cmbBoxRankPersona.getSelectionModel().clearSelection();
            FXMLControllerManager.getInstance().getPersonEditController().getLblRank().setDisable(false);
            rankPerson = new RankPerson();//сбрасываем редактирование
        }
        data();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMLControllerManager.getInstance().setRankEditController(this);
        currentPersona = FXMLControllerManager.getInstance().getPersonEditController().getCurrentPersona();
        btnSave.setGraphic(new FontIcon("anto-save"));
        btnCancel.setGraphic(new FontIcon("anto-close"));
        btnHelp.setGraphic(new FontIcon("anto-info-circle"));

        data();

        for (Rank rank : RankModel.getAllRecords()) {
            cmbBoxRankPersona.getItems().add(rank);
        }
        tblClmnRank.setCellValueFactory(new PropertyValueFactory<>("rank"));
        tblClmnDateAddRank.setCellValueFactory(new PropertyValueFactory<>("dateAddRank"));
        tblRank.setItems(ranks);
        tblRank.setRowFactory(
                tableView -> {
                    //событие по двойному клику строки
                    final TableRow<RankPerson> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                            RankPerson rowData = row.getItem();
                        }
                    });
                    //контексное меню
                    final ContextMenu rowMenu = new ContextMenu();
                    MenuItem editItem = new MenuItem("Редактировать");
                    MenuItem removeItem = new MenuItem("Удалить пункт");

                    editItem.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            cmbBoxRankPersona.getSelectionModel().select(row.getItem().getRank());
                            datePickerAchievRank.setValue(row.getItem().getDateAddRank().toLocalDate());
                            rankPerson.setId(row.getItem().getId());
                        }
                    });
                    removeItem.setOnAction(event -> {
                        Alert alertDelete = new Alert(Alert.AlertType.CONFIRMATION);
                        alertDelete.setTitle("Внимание");
                        alertDelete.setHeaderText("Удаление записи");
                        alertDelete.setContentText("Удалить запись: " + row.getItem().getRank() + "?");
                        Optional<ButtonType> option = alertDelete.showAndWait();
                        if (option.get() == ButtonType.OK) {
                            RankPersonModel.delete(row.getItem());
                            tblRank.getItems().remove(row.getItem());
                            data();
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

    private void data() {
        ranks.clear();
        ranks.addAll(RankPersonModel.getAllRanksByIdPerson(currentPersona.getId()));
        setLabelDisableEditForm();
    }
}

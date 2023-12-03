package mo.specdoc.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import mo.specdoc.entity.Subdivision;
import mo.specdoc.model.SubdivisionModel;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SubdivisionController implements Initializable {
    private static ObservableList<Subdivision> data = FXCollections.observableArrayList();
    private static Subdivision currentEntry;
    private static SubdivisionController instance;
    @FXML    private Button btnAddSubdivision;
    @FXML    private TableView<Subdivision> tblSubdivision = new TableView<>();
    @FXML    private TableColumn<Subdivision, String> tblClmnTitle;
    @FXML    private TableColumn<Subdivision, String> tblClmnTitleRP;
    @FXML    private TableColumn<Subdivision, String> tblClmnCondNumb;

    public static SubdivisionController getInstance() {
        if (instance == null) {
            instance = new SubdivisionController();
        }
        return instance;
    }

    private void sceneCreate(Subdivision entity) {
        try {
            SubdivisionEditController subdivisionEditController = new SubdivisionEditController(entity);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/subdivision-edit.fxml"));
            loader.setController(subdivisionEditController);
            Stage stage = new Stage();
            stage.setTitle("Ввод подразделения");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void addSubdivision(ActionEvent event) {
        currentEntry = new Subdivision();
        sceneCreate(currentEntry);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //иконки на форме
            btnAddSubdivision.setGraphic(new FontIcon("anto-plus"));
            //контексное меню
            tblSubdivision.setRowFactory(
                    new Callback<TableView<Subdivision>, TableRow<Subdivision>>() {
                        @Override
                        public TableRow<Subdivision> call(TableView<Subdivision> tableView) {
                            //событие по двойному клику строки
                            final TableRow<Subdivision> row = new TableRow<>();
                            row.setOnMouseClicked(event -> {
                                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {

                                }
                            });
                            //контексное меню
                            final ContextMenu rowMenu = new ContextMenu();
                            MenuItem editItem = new MenuItem("Редактировать");
                            MenuItem removeItem = new MenuItem("Удалить");
                            editItem.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    currentEntry = row.getItem();
                                    sceneCreate(currentEntry);
                                }
                            });
                            removeItem.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    Alert alertDelete = new Alert(Alert.AlertType.CONFIRMATION);
                                    alertDelete.setTitle("Внимание");
                                    alertDelete.setHeaderText("Удаление записи");
                                    alertDelete.setContentText("Удалить запись: " + row.getItem().getTitle() + "?");
                                    Optional<ButtonType> option = alertDelete.showAndWait();
                                    if (option.get() == null) {

                                    } else if (option.get() == ButtonType.OK) {
                                        SubdivisionModel.delete(row.getItem());
                                        tblSubdivision.getItems().remove(row.getItem());
                                        initalizeData();
                                    } else if (option.get() == ButtonType.CANCEL) {

                                    }
                                }
                            });
                            rowMenu.getItems().addAll(editItem, removeItem);
                            // only display context menu for non-empty rows:
                            row.contextMenuProperty().bind(
                                    Bindings.when(row.emptyProperty())
                                            .then((ContextMenu) null)
                                            .otherwise(rowMenu));
                            return row;
                        }
                    }
            );
            initalizeData();
            // устанавливаем тип и значение которое должно хранится в колонке
            tblClmnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            tblClmnTitleRP.setCellValueFactory(new PropertyValueFactory<>("titleRp"));
            tblClmnCondNumb.setCellValueFactory(new PropertyValueFactory<>("conditionalNumb"));
            // заполняем таблицу данными
            tblSubdivision.setItems(data);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void initalizeData(){
        data.clear();
        for (Subdivision subdivision : SubdivisionModel.getAllRecords()) {
            data.add(subdivision);
        }
    }
}

/*
 * Copyright (c) 2023
 */

package mo.specdoc.controllers;

import impl.org.controlsfx.tableview2.TableRow2;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Data;
import mo.specdoc.entity.Persona;
import mo.specdoc.entity.Position;
import mo.specdoc.model.PersonPositionModel;
import mo.specdoc.model.PersonaModel;
import mo.specdoc.util.FXMLControllerManager;
import org.controlsfx.control.table.TableFilter;
import org.controlsfx.control.tableview2.FilteredTableColumn;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;
import org.controlsfx.control.tableview2.filter.popupfilter.PopupFilter;
import org.controlsfx.control.tableview2.filter.popupfilter.PopupStringFilter;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Data
public class PersonsViewController implements Initializable {
    private final FilteredTableColumn<Persona, String> family = new FilteredTableColumn<>("Фамилия");
    private final FilteredTableColumn<Persona, String> name = new FilteredTableColumn<>("Имя");
    private final FilteredTableColumn<Persona, String> lastname = new FilteredTableColumn<>("Отчество");
    private final FilteredTableColumn<Persona, Date> birthday = new FilteredTableColumn<>("День рождения");
    private SouthFilter<Persona, String> editorFamily;
    private SouthFilter<Persona, String> editorName;
    private SouthFilter<Persona, String> editorLastName;
    private final ObservableList<Persona> persons = FXCollections.observableArrayList();
    private BooleanProperty southVisible = new SimpleBooleanProperty();
    private PositionController positionController;
    private Position position;
    private Date dateAddPosition;
    private String optionEdit;
    public Boolean flag;
    public Persona currentPersona;
    private TableFilter<Persona> tableFilter;

    @FXML    private FilteredTableView tablePersonal;
    @FXML    private Button btnAddPersona;



    void edit() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/persona-edit.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Добавление нового служащего");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOption(String option) {
        optionEdit = option;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        persons.addAll(PersonaModel.getAll());
        FXMLControllerManager.getInstance().setPersonsViewController(this);
        btnAddPersona.setGraphic(new FontIcon("anto-user-add:14"));
        btnAddPersona.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentPersona = new Persona();
                edit();
            }
        });

        family.setCellValueFactory(p -> p.getValue().getFamilyStringProperty());
        family.setPrefWidth(110);
        name.setCellValueFactory(p -> p.getValue().getNameStringProperty());
        name.setPrefWidth(110);
        lastname.setCellValueFactory(p -> p.getValue().getLastnameStringProperty());
        lastname.setPrefWidth(110);
        birthday.setCellValueFactory(p -> p.getValue().getBirthdayObjectProperty());
        birthday.setPrefWidth(80);

        tablePersonal.getColumns().setAll(family, name, lastname, birthday);

        tablePersonal.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tablePersonal.rowHeaderVisibleProperty().set(true);
        tablePersonal.tableMenuButtonVisibleProperty().set(true);
        tablePersonal.setItems(persons);
        editorFamily = new SouthFilter<>(family, String.class);
        filterAction();

        Callback<TableView<Persona>, TableRow<Persona>> rowFactory = row -> {
            return new TableRow2(tablePersonal) {
                @Override
                protected void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    currentPersona = row.getSelectionModel().getSelectedItem();
                    if (item != null) {
                        //событие по двойному клику строки
                            row.setOnMouseClicked(event -> {
                                if (event.getClickCount() == 2) {
                                    edit();
                                }
                            });
                        //контексное меню
                        final ContextMenu rowMenu = new ContextMenu();

                        MenuItem editItem = new MenuItem(" Редактировать");
                        editItem.setGraphic(new FontIcon("anto-edit"));
                        editItem.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                edit();
                            }
                        });
                        rowMenu.getItems().add(editItem);

                        MenuItem removeItem = new MenuItem(" Удалить");
                        removeItem.setGraphic(new FontIcon("anto-delete"));
                        removeItem.setOnAction(event -> {
                            Alert alertDelete = new Alert(Alert.AlertType.CONFIRMATION);
                            alertDelete.setTitle("Внимание");
                            alertDelete.setHeaderText("Удаление записи");
                            alertDelete.setContentText("Удалить запись: " + row.getSelectionModel().getSelectedItem().getFio() + "?");
                            Optional<ButtonType> option = alertDelete.showAndWait();
                            if (option.get() == null) {
                            } else if (option.get() == ButtonType.OK) {
                                PersonaModel.delete(row.getSelectionModel().getSelectedItem());
                                tablePersonal.getItems().remove(row.getSelectionModel().getSelectedItem());
                            }
                        });
                        rowMenu.getItems().add(removeItem);

                        MenuItem dismissItem = new MenuItem(" Уволить");
                        dismissItem.setGraphic(new FontIcon("anto-disconnect"));
                        rowMenu.getItems().add(dismissItem);

                        if (optionEdit == "freeFromSelectPosition") {
                            MenuItem addPosition = new MenuItem(" Назначить");
                            addPosition.setGraphic(new FontIcon("anto-user-switch"));
                            addPosition.setOnAction(event -> {
                                try {
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/input-date.fxml"));
                                    Scene scene = new Scene(fxmlLoader.load());
                                    Stage stage = new Stage();
                                    stage.initModality(Modality.APPLICATION_MODAL);
                                    stage.setTitle("Введите дату назначения на должность");
                                    stage.setResizable(false);
                                    stage.setScene(scene);
                                    stage.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                            rowMenu.getItems().add(addPosition);
                        }
                        row.contextMenuProperty().setValue(rowMenu);
                    }
                }
            };
        };

        tablePersonal.rowFactoryProperty().addListener(new ChangeListener<Callback<TableView<Persona>, TableRow<Persona>>>() {
            @Override
            public void changed(ObservableValue<? extends Callback<TableView<Persona>, TableRow<Persona>>> observable, Callback<TableView<Persona>, TableRow<Persona>> oldValue, Callback<TableView<Persona>, TableRow<Persona>> newValue) {
                tablePersonal.rowFactoryProperty().unbind();
                if (tablePersonal.rowFactoryProperty().get() != rowFactory) {
                    tablePersonal.setRowFactory(rowFactory);
                }
            }
        });
        tableFilter = TableFilter.forTableView(tablePersonal).apply();
    }

    public void setupFilter(boolean southFilter) {
        if (southFilter) {
            southNodeFilterAction();
            family.setSouthNode(editorFamily);
        } else {
            filterAction();
            family.setSouthNode(null);
        }
        southVisible.set(southFilter);
    }

    private void southNodeFilterAction() {
        family.setOnFilterAction(e -> {
            if (family.getPredicate() != null) {
                editorFamily.getFilterEditor().cancelFilter();
            }
        });
    }

    private void filterAction() {
        PopupFilter<Persona, String> popupFirstNameFilter = new PopupStringFilter<>(family);
        family.setOnFilterAction(e -> popupFirstNameFilter.showPopup());
    }

    void cancel(){
        Stage stage = (Stage) tablePersonal.getScene().getWindow();
        stage.close();
    }
}

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
import org.controlsfx.control.tableview2.FilteredTableColumn;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;
import org.controlsfx.control.tableview2.filter.popupfilter.PopupFilter;
import org.controlsfx.control.tableview2.filter.popupfilter.PopupStringFilter;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.sql.Date;
import java.util.Optional;

@Data
public class PersonsViewController {
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
    private static PersonsViewController singleton;

    @FXML    private FilteredTableView tablePersonal;
    @FXML    private Button btnAddPersona;

    public PersonsViewController() {
        singleton = this;
    }

    public static PersonsViewController getInstance() {
        if (singleton == null)
            singleton = new PersonsViewController();
        return singleton;
    }

    @FXML
    void edit(Persona persona) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/persona-edit.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Добавление нового служащего");
            stage.setResizable(false);
            stage.setScene(scene);
            PersonEditController children = fxmlLoader.getController();
            children.initialize(persona);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setParent (PositionController controller){
        this.positionController = controller;
    }

    public void initialize(Boolean flag) {

        btnAddPersona.setGraphic(new FontIcon("anto-user-add:14"));
        btnAddPersona.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                edit(new Persona());
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

        if (flag) vakantPersons();
        else allPersons();

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
                    if (item != null) {
                        //событие по двойному клику строки
                            row.setOnMouseClicked(event -> {
                                if (event.getClickCount() == 2) {
                                    if(flag) {
                                        try {
                                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/input-date.fxml"));
                                            Scene scene = new Scene(fxmlLoader.load());
                                            Stage stage = new Stage();
                                            stage.initModality(Modality.APPLICATION_MODAL);
                                            stage.setTitle("Введите дату назначения на должность");
                                            stage.setResizable(false);
                                            stage.setScene(scene);
                                            DateInputController children = fxmlLoader.getController();
                                            children.setPersonAndPosition(row.getSelectionModel().getSelectedItem(), position);
                                            stage.show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else edit(row.getSelectionModel().getSelectedItem());
                                }
                            });
                        //контексное меню
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem editItem = new MenuItem(" Редактировать");
                        MenuItem dismissItem = new MenuItem(" Уволить");
                        MenuItem removeItem = new MenuItem(" Удалить");
                        editItem.setGraphic(new FontIcon("anto-edit"));
                        dismissItem.setGraphic(new FontIcon("anto-disconnect"));
                        removeItem.setGraphic(new FontIcon("anto-delete"));
                        editItem.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                edit(row.getSelectionModel().getSelectedItem());
                            }
                        });

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
                        rowMenu.getItems().addAll(editItem, removeItem, dismissItem);
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

    public void allPersons() {
        persons.clear();
        persons.addAll(PersonaModel.getAll());
    }

    private void vakantPersons() {
        persons.clear();
        for (Persona persona : PersonaModel.getAll()) {
            if (PersonPositionModel.getAllFromIdPersona(persona.getId()).isEmpty()) {
                persons.add(persona);
            }
        }
    }

    void cancel(){
        Stage stage = (Stage) tablePersonal.getScene().getWindow();
        stage.close();
    }
}
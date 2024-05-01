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
import mo.specdoc.dto.PersonaDTO;
import mo.specdoc.entity.*;
import mo.specdoc.model.PersonPositionModel;
import mo.specdoc.model.PersonaModel;
import mo.specdoc.model.SecrecyPersonModel;
import mo.specdoc.util.FXMLControllerManager;
import org.controlsfx.control.table.TableFilter;
import org.controlsfx.control.tableview2.FilteredTableColumn;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;
import org.controlsfx.control.tableview2.filter.popupfilter.PopupFilter;
import org.controlsfx.control.tableview2.filter.popupfilter.PopupStringFilter;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.swing.text.Position;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.Optional;
import java.util.ResourceBundle;

@Data
public class PersonsViewController implements Initializable {
    private final FilteredTableColumn<PersonaDTO, String> family = new FilteredTableColumn<>("Фамилия");
    private final FilteredTableColumn<PersonaDTO, String> name = new FilteredTableColumn<>("Имя");
    private final FilteredTableColumn<PersonaDTO, String> lastname = new FilteredTableColumn<>("Отчество");
    private final FilteredTableColumn<PersonaDTO, Date> birthday = new FilteredTableColumn<>("День рождения");
    private final FilteredTableColumn<PersonaDTO, String> secrecy = new FilteredTableColumn<>("Доступ к ГТ");
    private final FilteredTableColumn<PersonaDTO, String> rank = new FilteredTableColumn<>("Звание");
    private final FilteredTableColumn<PersonaDTO, String> ammo = new FilteredTableColumn<>("Оружие");
    private final FilteredTableColumn<PersonaDTO, String> state = new FilteredTableColumn<>("Должность");
    private final FilteredTableColumn<PersonaDTO, String> dopusk = new FilteredTableColumn<>("Допуски");
    private SouthFilter<PersonaDTO, String> editorFamily;
    private SouthFilter<PersonaDTO, String> editorName;
    private SouthFilter<PersonaDTO, String> editorLastName;
    private final ObservableList<Persona> persons = FXCollections.observableArrayList();
    private final ObservableList<PersonaDTO> personsDTO = FXCollections.observableArrayList();
    private BooleanProperty southVisible = new SimpleBooleanProperty();
    private StateController positionController;
    private Position position;
    private Date dateAddPosition;
    private String optionEdit;
    public Boolean flag;
    public PersonaDTO currentPersona;
    private TableFilter<PersonaDTO> tableFilter;

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
        initData();
    }

    public void initData() {
        persons.clear();
        personsDTO.clear();
        switch (optionEdit) {
            case ("freePersonsFromSelectPosition") :
                for (Persona persona : PersonaModel.getAll()) {
                    if (PersonPositionModel.getAllFromIdPersona(persona.getId()).isEmpty()) persons.add(persona);
                }
                break;
            case ("allPersons") :
                persons.addAll(PersonaModel.getAll());
                break;
            case ("allPersonsWithEnableSecrecyType") :
                State state = FXMLControllerManager.getInstance().getStateController().getCurrentState();
                SecrecyType secrecyType = state.getSecrecyType();
                for (Persona persona : PersonaModel.getAll()) {
                    SecrecyPerson lastSecrecyPerson = SecrecyPersonModel.getLastSecrecyByIdPerson(persona.getId());
                    if (lastSecrecyPerson != null) {
                        if (lastSecrecyPerson.getSecrecyType().equals(secrecyType)) persons.add(persona);
                    }
                }
                break;
        }
        for (Persona persona : persons) {
            PersonaDTO personaDTO = new PersonaDTO(persona);
            personsDTO.add(personaDTO);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMLControllerManager.getInstance().setPersonsViewController(this);
        btnAddPersona.setGraphic(new FontIcon("anto-user-add:14"));
        btnAddPersona.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentPersona = new PersonaDTO();
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
        secrecy.setCellValueFactory(p -> p.getValue().getSecrecyStringProperty());
        secrecy.setPrefWidth(80);
        rank.setCellValueFactory(p -> p.getValue().getRankStringProperty());
        rank.setPrefWidth(60);
        ammo.setCellValueFactory(p -> p.getValue().getAmmoStringProperty());
        ammo.setPrefWidth(50);
        state.setCellValueFactory(p -> p.getValue().getStateStringProperty());
        state.setPrefWidth(80);
        dopusk.setCellValueFactory(p -> p.getValue().getDopuskStringProperty());
        dopusk.setPrefWidth(80);
        tablePersonal.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablePersonal.getColumns().setAll(family, name, lastname, birthday, secrecy, rank, ammo, state, dopusk);
        tablePersonal.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tablePersonal.rowHeaderVisibleProperty().set(true);
        tablePersonal.tableMenuButtonVisibleProperty().set(true);
        tablePersonal.setItems(personsDTO);
        editorFamily = new SouthFilter<>(family, String.class);
        filterAction();

        Callback<TableView<PersonaDTO>, TableRow<PersonaDTO>> rowFactory = row -> {
            return new TableRow2(tablePersonal) {
                @Override
                protected void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        currentPersona = row.getSelectionModel().getSelectedItem();
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
                                PersonaModel.delete(row.getSelectionModel().getSelectedItem().getPersona());
                                tablePersonal.getItems().remove(row.getSelectionModel().getSelectedItem());
                            }
                        });
                        rowMenu.getItems().add(removeItem);

                        MenuItem dismissItem = new MenuItem(" Уволить");
                        dismissItem.setGraphic(new FontIcon("anto-disconnect"));
                        rowMenu.getItems().add(dismissItem);

                        if (optionEdit == "freePersonsFromSelectPosition") {
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
        PopupFilter<PersonaDTO, String> popupFirstNameFilter = new PopupStringFilter<>(family);
        family.setOnFilterAction(e -> popupFirstNameFilter.showPopup());
    }



    void cancel(){
        Stage stage = (Stage) tablePersonal.getScene().getWindow();
        stage.close();
    }
}

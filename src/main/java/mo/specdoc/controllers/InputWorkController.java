/*
 * Copyright (c) 2022
 */

package mo.specdoc.controllers;

import impl.org.controlsfx.tableview2.TableRow2;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import mo.specdoc.dto.PersonaDTO;
import mo.specdoc.dto.StateDTO;
import mo.specdoc.entity.*;
import mo.specdoc.entity.dopusk.Dopusk;
import mo.specdoc.model.*;
import mo.specdoc.util.Month;
import org.controlsfx.control.table.TableFilter;
import org.controlsfx.control.tableview2.FilteredTableColumn;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;
import org.controlsfx.control.tableview2.filter.popupfilter.PopupFilter;
import org.controlsfx.control.tableview2.filter.popupfilter.PopupStringFilter;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class InputWorkController implements Initializable {
    private ObservableList<State> posts = FXCollections.observableArrayList();
    private List<Persona> persons;
    @FXML    private TabPane tabPaneSubdivions;
    @FXML    private Button btnCreateDocDayWork;
    @FXML private AnchorPane anchor;

    public InputWorkController() {
    }
    //исключить пустые табы


    private void createTabs(State root, TabPane tabPane) {
        List<State> data = StateModel.getChildrenPosition(root.getIdState());
        if (!data.isEmpty()) {
            for (State state : data) {
                int id = state.getTypeState();
                if (id == 1 | id == 2 | id == 3 | id == 5 | id == 6 | id == 7) {
                    Tab tabState = new Tab(state.getTitleStateShort());
                    tabPane.getTabs().add(tabState);
                    TabPane childTabPane = new TabPane();
                    if (id == 6) {
                        List<PersonaDTO> list = createListPersona(state);
                        if (!list.isEmpty()) {
                            tabState.setContent(createTable(list));
                        }
                    } else tabState.setContent(childTabPane);
                    createTabs(state, childTabPane);
                }
            }
        }
    }

    private TableView<PersonaDTO> createTable(List<PersonaDTO> list) {
        ObservableList<PersonaDTO> personaDTOS = FXCollections.observableArrayList();
        personaDTOS.addAll(list);
        TableView<PersonaDTO> table = new TableView<>();
        TableColumn <PersonaDTO, String> family = new TableColumn<>();
        TableColumn <PersonaDTO, String> name = new TableColumn<>();
        TableColumn <PersonaDTO, String> lastname = new TableColumn<>();
        family.setCellValueFactory(p -> p.getValue().getFamilyStringProperty());
        family.setPrefWidth(110);
        name.setCellValueFactory(p -> p.getValue().getNameStringProperty());
        name.setPrefWidth(110);
        lastname.setCellValueFactory(p -> p.getValue().getLastnameStringProperty());
        lastname.setPrefWidth(110);
        table.getColumns().setAll(family, name, lastname);
        table.setItems(personaDTOS);
        return table;
    }

    private List<PersonaDTO> createListPersona(State state) {
        List<Dopusk> dopusks = DopuskModel.getAllByStatePostId(state.getIdState());
        List<PersonaDTO> persons = new ArrayList<>();
        for (Dopusk dopusk : dopusks) {
            persons.add(new PersonaDTO(dopusk.getPersona()));
        }
        return persons;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnCreateDocDayWork.setGraphic(new FontIcon("ri-docs-com:18"));
        createTabs(StateModel.getRootState(), tabPaneSubdivions);
    }




    /**
     * Процедура загрузки постов {@link State} и сортировка пузырьком по значению sortValue
     */
    private void sortedPosts(List<State> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for(int j = 0; j < list.size() - i - 1; j++) {
                if(list.get(j + 1).getSortValue() < list.get(j).getSortValue()) {
                    State swap = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, swap);
                }
            }
        }
    }

}

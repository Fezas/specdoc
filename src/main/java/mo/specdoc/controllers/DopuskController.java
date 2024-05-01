/*
 * Copyright (c) 2023
 */

package mo.specdoc.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import mo.specdoc.dto.PersonaDTO;
import mo.specdoc.entity.PersonPosition;
import mo.specdoc.entity.State;
import mo.specdoc.entity.dopusk.Dopusk;
import mo.specdoc.model.DopuskModel;
import mo.specdoc.model.PersonPositionModel;
import mo.specdoc.model.StateModel;
import org.controlsfx.control.tableview2.FilteredTableView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DopuskController implements Initializable {
    @FXML    private FilteredTableView<PersonaDTO> tablePersonal;
    @FXML    private TreeTableView<State> tblStructure;
    @FXML    private TreeTableColumn<State, String> titleSubdiv;
    @FXML    private TabPane tabPaneStructureState;
    private List<State> data = new ArrayList<>();

    public DopuskController() {
    }
   
    private ObservableList<PersonaDTO> loadPersonal() {
        ObservableList<PersonaDTO> personaDTOS = FXCollections.observableArrayList();
        List<PersonPosition> personal = PersonPositionModel.getAllActualPersons();
        for (PersonPosition pp : personal) {
            personaDTOS.add(new PersonaDTO(pp.getPersonaFromPosition()));
        }
        return personaDTOS;
    }
    
    private void createTabs(State root, TabPane tabPane) {
        List<State> data = StateModel.getChildrenPosition(root);
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
        TableColumn<PersonaDTO, String> family = new TableColumn<>();
        TableColumn <PersonaDTO, String> name = new TableColumn<>();
        TableColumn <PersonaDTO, String> lastname = new TableColumn<>();
        family.setCellValueFactory(p -> p.getValue().getFamilyStringProperty());
        family.setPrefWidth(110);
        name.setCellValueFactory(p -> p.getValue().getNameStringProperty());
        name.setPrefWidth(110);
        lastname.setCellValueFactory(p -> p.getValue().getLastnameStringProperty());
        lastname.setPrefWidth(110);
        tablePersonal.getColumns().setAll(family, name, lastname);
        tablePersonal.setItems(personaDTOS);
        return tablePersonal;
    }

    public void structure(State root) {
        //List<State> data = StateModel.getChildrenPosition(root); //дочерние узлы
        for(State state : data) {
            
        }
        if (!data.isEmpty()) {
            for (State state : data) {
                int id = state.getTypeState();
                if (id == 1 | id == 2 | id == 3) {
                    
                }
            }
        }
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
        createTabs(StateModel.getFromTypeState(1).get(0), tabPaneStructureState);
        data.addAll(StateModel.getChildrenPositionAllLevel(StateModel.getFromTypeState(1).get(0)));
        //structure();
        createTable(loadPersonal());
    }
}

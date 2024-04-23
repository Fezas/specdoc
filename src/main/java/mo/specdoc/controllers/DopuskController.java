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
import mo.specdoc.dto.StateDTO;
import mo.specdoc.entity.PersonPosition;
import mo.specdoc.entity.Persona;
import mo.specdoc.entity.State;
import mo.specdoc.entity.dopusk.Dopusk;
import mo.specdoc.model.DopuskModel;
import mo.specdoc.model.PersonPositionModel;
import mo.specdoc.model.StateModel;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.tableview2.FilteredTableView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DopuskController implements Initializable {
    @FXML
    private CheckComboBox<State> checkComboBoxStructure;
    @FXML
    private FilteredTableView<PersonaDTO> tblPersonal;


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
        tblPersonal.getColumns().setAll(family, name, lastname);
        tblPersonal.setItems(personaDTOS);
        return tblPersonal;
    }

    public void structure(State root) {
        StringBuilder prefix = new StringBuilder();
        List<State> data = StateModel.getChildrenPosition(root.getIdState()); //дочерние узлы
        if (!data.isEmpty()) {
            prefix.append(" ");
            for (State state : data) {
                state.setTitleState(prefix + state.getTitleState());
                //StateDTO stateDTO = new StateDTO();
                //stateDTO.setTitle(state.getTitleState());
                //stateDTO.setState(state);
                //stateDTO.setSort(String.valueOf(state.getSortValue()));
                int id = state.getTypeState();
                if (id == 1 | id == 2 | id == 3) {
                    checkComboBoxStructure.getItems().add(state);
                }
                structure(state);
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
        structure(StateModel.getRootState());
        createTable(loadPersonal());
    }
}

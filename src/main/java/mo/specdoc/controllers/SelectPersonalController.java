/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mo.specdoc.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import mo.specdoc.dto.PersonaDTO;
import mo.specdoc.dto.StateDTO;
import mo.specdoc.entity.PersonPosition;
import mo.specdoc.entity.Persona;
import mo.specdoc.entity.State;
import mo.specdoc.model.PersonPositionModel;
import mo.specdoc.model.StateModel;
import mo.specdoc.util.FXMLControllerManager;
import org.controlsfx.control.CheckTreeView;
import org.controlsfx.control.tableview2.FilteredTableColumn;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

/**
 *
 * @author Stepantsov P.V.
 */
public class SelectPersonalController implements Initializable{
    @FXML    private FilteredTableView<PersonaDTO> tablePersonal;
    @FXML    private CheckTreeView<StateDTO> checkTreeView;
    @FXML    private Label lblPost;
    private final FilteredTableColumn<PersonaDTO, String> family = new FilteredTableColumn<>("Фамилия");
    private final FilteredTableColumn<PersonaDTO, String> name = new FilteredTableColumn<>("Имя");
    private final FilteredTableColumn<PersonaDTO, String> lastname = new FilteredTableColumn<>("Отчество");
    private final FilteredTableColumn<PersonaDTO, CheckBox> check = new FilteredTableColumn<>("");
    private SouthFilter<PersonaDTO, String> editorFamily;
    private SouthFilter<PersonaDTO, String> editorName;
    private SouthFilter<PersonaDTO, String> editorLastName;
    private final ObservableList<PersonaDTO> personsDTO = FXCollections.observableArrayList();
    private final List<State> personalPositions = new ArrayList<>(StateModel.getFromTypeState(4));
    private final Map<Long, PersonaDTO> personalPositionsMap = new HashMap<Long, PersonaDTO>();
    private final List<Persona> personal = new ArrayList<>();

    public void createStructure() {
        State stateRoot = StateModel.getFromTypeState(1).get(0);
        CheckBoxTreeItem<StateDTO> itemRoot = createNode(stateRoot); // корень всей структуры
        itemRoot.setExpanded(true);
        structure(itemRoot);
        checkTreeView.setRoot(itemRoot);
    }
    
    private CheckBoxTreeItem<StateDTO> createNode(State state) {
        StateDTO node = new StateDTO();
        node.setTitle(state.getTitleState());
        node.setState(state);
        node.setSort(String.valueOf(state.getSortValue()));
        node.setRemark(new CheckBox());
        CheckBoxTreeItem<StateDTO> item = new CheckBoxTreeItem<StateDTO>(node);
        return item;
    }
    
    public void structure(CheckBoxTreeItem<StateDTO> itemRoot) {
        List<State> data = StateModel.getChildrenPosition(itemRoot.getValue().getState()); //itemRoot.getValue().getState().getChildState(); //дочерние узлы
        if (!data.isEmpty()) {
            for (State state : data) {
                int id = state.getTypeState();
                if (id == 1 | id == 2 | id == 3) {
                    CheckBoxTreeItem<StateDTO> item = createNode(state);
                    itemRoot.getChildren().add(item);
                    structure(item);
                }
            }
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (State state : personalPositions) {
            PersonPosition pp = PersonPositionModel.getActualPersonsFromPositionId(state.getIdState());
            if (pp != null) {
                personalPositionsMap.put(state.getIdState(), new PersonaDTO(pp.getPersonaFromPosition()));
            }                      
        }
        lblPost.setText(FXMLControllerManager.getInstance().getStateController().getCurrentState().getTitleState());
        createStructure();
        check.setCellValueFactory(p -> p.getValue().getCheck());
        family.setCellValueFactory(p -> p.getValue().getFamilyStringProperty());
        family.setPrefWidth(110);
        name.setCellValueFactory(p -> p.getValue().getNameStringProperty());
        name.setPrefWidth(110);
        lastname.setCellValueFactory(p -> p.getValue().getLastnameStringProperty());
        lastname.setPrefWidth(110);
        tablePersonal.getColumns().setAll(check, family, name, lastname);
        tablePersonal.setItems(personsDTO);
        checkTreeView.getCheckModel().getCheckedItems().addListener(new ListChangeListener<TreeItem<StateDTO>>() {
            public void onChanged (ListChangeListener.Change<? extends TreeItem<StateDTO>> c) {
                System.out.println(checkTreeView.getCheckModel().getCheckedItems());
                personsDTO.clear();
                ObservableList<TreeItem<StateDTO>> items = checkTreeView.getCheckModel().getCheckedItems();
                for (TreeItem<StateDTO> item : items) {
                    for (State state : personalPositions) {
                        if(item.getValue().getState().equals(state.getStateParent())) {
                            PersonPosition pp = PersonPositionModel.getActualPersonsFromPositionId(state.getIdState());
                            if (pp != null) {          
                                personsDTO.add(new PersonaDTO(pp.getPersonaFromPosition()));
                            }
                        }
                    }                 
                }
            }
        });
    }
    
}

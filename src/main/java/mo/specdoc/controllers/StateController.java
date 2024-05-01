/*
 * Copyright (c) 2022-2023. Stepantsov P.V.
 */

package mo.specdoc.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Data;
import mo.specdoc.dto.StateDTO;
import mo.specdoc.entity.PersonPosition;
import mo.specdoc.entity.Persona;
import mo.specdoc.entity.State;
import mo.specdoc.model.PersonPositionModel;
import mo.specdoc.model.StateModel;
import mo.specdoc.util.FXMLControllerManager;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.*;
@Data
public class StateController implements Initializable {
    @FXML    private TreeTableColumn<StateDTO, String> clmnButtons, clmnInfo, clmnSort;
    @FXML    private TreeTableColumn<StateDTO, String> clmnTitleStructure;
    @FXML    private TreeTableView<StateDTO> tblStructure;
    private StateDTO node;
    private Persona currentPersona = new Persona();
    public State currentState;
    public State currentParentState;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMLControllerManager.getInstance().setStateController(this);
        //привязка столбцов таблицы к свойствам объекта
        clmnTitleStructure.setCellValueFactory(new TreeItemPropertyValueFactory<StateDTO, String>("title"));
        clmnButtons.setCellValueFactory(new TreeItemPropertyValueFactory<StateDTO, String>("boxBtn"));
        clmnSort.setCellValueFactory(new TreeItemPropertyValueFactory<StateDTO, String>("sort"));
        clmnTitleStructure.setCellFactory(ttc -> new TreeTableCell<StateDTO, String>() {
            final FontIcon iconVakant = new FontIcon("ri-vcard-o:20:gray");
            final FontIcon iconPersona = new FontIcon("ri-vcard-o:20:green");
            final FontIcon iconPost = new FontIcon("antf-safety-certificate:20:green");
            final FontIcon iconPostAmplification = new FontIcon("antf-safety-certificate:20:red");
            final FontIcon iconRaschet = new FontIcon("antf-flag:20:green");
            final FontIcon iconRaschetAmplification = new FontIcon("antf-flag:20:red");
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(empty){
                    setText(null);
                    setGraphic(null);
                } else {
                    StateDTO node = getTableRow().getItem();
                    if (node != null) {
                        setText(null);
                        setGraphic(null);
                        FontIcon icon = null;
                        switch (node.getState().getTypeState()) {
                            case 0, 1, 3 -> {
                            }
                            case 4 -> icon = node.getPersona() != null ? iconPersona : iconVakant;
                            case 5 -> icon = !node.getState().isPostIsAmplification() ? iconRaschet : iconRaschetAmplification;
                            case 6 -> icon = !node.getState().isPostIsAmplification() ? iconPost : iconPostAmplification;
                        }
                        setGraphic(icon);
                        setText(item);
                    }
                }
            }
        });
        createStructure();
    }

    public void refresh() {
        tblStructure.getRoot().getChildren().clear();
        structure(tblStructure.getRoot());
    }

    /**
     * Процедура загрузки узлов штата {@link State и сортировка пузырьком по значению sortValue
     * @param List<State> data
     */

    public void createStructure() {
        State stateRoot = StateModel.getFromTypeState(1).get(0);
        TreeItem<StateDTO> itemRoot = createNode(stateRoot); // корень всей структуры
        itemRoot.setExpanded(true);
        structure(itemRoot);
        tblStructure.setRoot(itemRoot);
    }
    
    /**
     * Функция создания узла штата {@link State} в структуре типа {@link TreeTableView}
     * @return возвращает узел {@link StateDTO}
     */
    
    private TreeItem<StateDTO> createNode(State state) {
        StateDTO node = new StateDTO();
        node.setTitle(state.getTitleState());
        node.setState(state);
        node.setSort(String.valueOf(state.getSortValue()));
        TreeItem<StateDTO> item = new TreeItem<>(node);
        switch (state.getTypeState()) {
            case 1 -> {
                //войсковая часть
                createButtonAddRootSubdivision(node);
                createButtonAddSubdivision(node);
                createButtonAddPosition(node);
                createButtonAddRaschet(node);
            }
            case 2 -> {
                //отдельное подразделение
                createButtonAddRootSubdivision(node);
                createButtonAddSubdivision(node);
                createButtonAddPosition(node);
                createButtonAddRaschet(node);
                createButtonAddPost(node);
                createButtonEdit(node);
                createButtonDelete(node);
            }
            case 3 -> {
                //линейное подразделение
                createButtonAddSubdivision(node);
                createButtonAddPosition(node);
                createButtonAddRaschet(node);
                createButtonAddPost(node);
                createButtonEdit(node);
                createButtonDelete(node);
            }
            case 4 -> {
                //должность
                PersonPosition pp = PersonPositionModel.getActualPersonsFromPositionId(state.getIdState()); //персоны в узлах
                if (pp == null) {
                    node.setTitle(state.getTitleState() + " - вакант");
                    createButtonAddPersonaFromPosition(node);
                } else {
                    Persona persona = pp.getPersonaFromPosition();
                    node.setPersona(persona);
                    node.setTitle(state.getTitleState() + " " + persona.getFamily() + " " + persona.getNamePerson().charAt(0) + ". " + persona.getLastname().charAt(0) + ".");
                    createButtonDeletePersona(node, pp);
                }
                createButtonEdit(node);
                createButtonDelete(node);
            }
            case 5 -> {
                //боевой расчет
                createButtonAddRaschet(node);
                createButtonAddPost(node);
                createButtonEdit(node);
                createButtonDelete(node);
            }
            case 6, 7 -> //боевой пост
            {
                createButtonAddPersonaFromPost(node);
                createButtonEdit(node);
                createButtonDelete(node);
            }
        }
        //боевой пост
        //item.setExpanded(true);
        return item;
    }

    /**
     * Функция рекурсивного формирования {@link TreeTableView} с помощью запроса  {@link State#getChildState()}
     * @param itemRoot
     */
    
    public void structure(TreeItem<StateDTO> itemRoot) {
        List<State> data = StateModel.getChildrenPosition(itemRoot.getValue().getState()); //itemRoot.getValue().getState().getChildState(); //дочерние узлы
        if (!data.isEmpty()) {
            for (State state : data) {
                TreeItem<StateDTO> item = createNode(state);
                itemRoot.getChildren().add(item);
                structure(item);
            }
        }
    }

    private void createButtonAddRootSubdivision(StateDTO stateDTO) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-apartment"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Добавить отдельное подразделение в \n\"" + stateDTO.getState().getTitleState() + "\"\n");
        button.setTooltip(tooltip);
        button.setOnAction((ActionEvent e) -> {
            currentParentState = stateDTO.getState();
            currentState = new State();
            editRootSubdivision("Отдельное подразделение");
        });
        stateDTO.getBoxBtn().getChildren().add(button);
    }

    private void createButtonAddSubdivision(StateDTO stateDTO) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-node-expand"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Добавить линейное подразделение в \n\"" + stateDTO.getState().getTitleState() + "\"\n");
        button.setTooltip(tooltip);
        button.setOnAction((ActionEvent e) -> {
            currentParentState = stateDTO.getState();
            currentState = new State();
            editSubdivision("Линейное подразделение");
        });
        stateDTO.getBoxBtn().getChildren().add(button);
    }

    private void createButtonAddPosition(StateDTO stateDTO) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-contacts"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Добавить должность в \n\"" + stateDTO.getState().getTitleState() + "\"\n");
        button.setTooltip(tooltip);
        button.setOnAction((ActionEvent e) -> {
            currentParentState = stateDTO.getState();
            currentState = new State();
            editPosition("Должность");
        });
        stateDTO.getBoxBtn().getChildren().add(button);
    }

    private void createButtonAddRaschet(StateDTO stateDTO) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-flag"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Добавить расчет в \n\"" + stateDTO.getState().getTitleState() + "\"\n");
        button.setTooltip(tooltip);
        button.setOnAction((ActionEvent e) -> {
            currentParentState = stateDTO.getState();
            currentState = new State();
            editRaschet("Боевой расчет");
        });
        stateDTO.getBoxBtn().getChildren().add(button);
    }

    private void createButtonAddPost(StateDTO stateDTO) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-notification"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Добавить пост в \n\"" + stateDTO.getState().getTitleState() + "\"\n");
        button.setTooltip(tooltip);
        button.setOnAction((ActionEvent e) -> {
            currentParentState = stateDTO.getState();
            currentState = new State();
            editPost("Боевой пост");
        });
        stateDTO.getBoxBtn().getChildren().add(button);
    }

    private void createButtonEdit(StateDTO stateDTO) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-edit"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Редактировать \n\"" + stateDTO.getState().getTitleState() + "\"\n");
        button.setTooltip(tooltip);
        switch (stateDTO.getState().getTypeState()) {
            case 2 -> //отдельное подразделение
                button.setOnAction((ActionEvent e) -> {
                    currentParentState = stateDTO.getState();
                    currentState = stateDTO.getState();
                    editRootSubdivision("Редактирование записи");
        });
            case 3 -> //линейное подразделение
                button.setOnAction((ActionEvent e) -> {
                    currentParentState = stateDTO.getState();
                    currentState = stateDTO.getState();
                    editSubdivision("Редактирование записи");
        });
            case 4 -> //должность
                button.setOnAction((ActionEvent e) -> {
                    currentParentState = stateDTO.getState();
                    currentState = stateDTO.getState();
                    editPosition("Должность");
        });
            case 5 -> //боевой расчет
                button.setOnAction((ActionEvent e) -> {
                    currentParentState = stateDTO.getState();
                    currentState = stateDTO.getState();
                    editRaschet("Боевой расчет");
        });
            case 6 -> //боевой пост
                button.setOnAction((ActionEvent e) -> {
                    currentParentState = stateDTO.getState();
                    currentState = stateDTO.getState();
                    editPost("Боевой пост");
        });
        }
        stateDTO.getBoxBtn().getChildren().add(button);
    }

    private void createButtonDelete(StateDTO stateDTO) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-delete"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Удалить \n\"" + stateDTO.getState().getTitleState() + "\"\n");
        button.setTooltip(tooltip);
        button.setOnAction((ActionEvent e) -> {
            showAlertDeletePositionWithHeaderText(stateDTO.getState().getTitleState(), stateDTO);
        });
        stateDTO.getBoxBtn().getChildren().add(button);
    }

    private void createButtonAddPersonaFromPosition(StateDTO stateDTO) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-user-add"));
        Tooltip tooltip = new Tooltip();
        //tooltip.setText("Добавить персонал в \n\"" + state.getPosition().getTitle() + "\"\n");
        button.setTooltip(tooltip);
        button.setOnAction((ActionEvent e) -> {
            currentPersona = new Persona();
            currentState = stateDTO.getState();
            addPersona("Назначение на должность", "freePersonsFromSelectPosition");
        });
        stateDTO.getBoxBtn().getChildren().add(button);
    }
    
    private void createButtonAddPersonaFromPost(StateDTO stateDTO) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-subnode"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Добавить допуск персонала в \n\"" + stateDTO.getState().getTitleState() + "\"\n");
        button.setTooltip(tooltip);
        button.setOnAction((ActionEvent e) -> {
            currentState = stateDTO.getState();
            filterPersonal("Назначение на пост");
        });
        stateDTO.getBoxBtn().getChildren().add(button);
    }

    private void createButtonDeletePersona(StateDTO state, PersonPosition personPosition) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-user-delete"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Открепить служащего");
        button.setTooltip(tooltip);
        button.setOnAction((ActionEvent e) -> {
            showAlertDeletePersonaWithHeaderText(state.getState().getTitleState(), personPosition);
        });
        state.getBoxBtn().getChildren().add(button);
    }

    public void editRootSubdivision(String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/state-root.fxml"));
            Stage stage = new Stage();
            stage.setTitle(title);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editSubdivision(String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/state-subdiv.fxml"));
            Stage stage = new Stage();
            stage.setTitle(title);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editPosition(String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/state-position.fxml"));
            Stage stage = new Stage();
            stage.setTitle(title);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editRaschet(String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/state-raschet.fxml"));
            Stage stage = new Stage();
            stage.setTitle(title);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editPost(String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/state-post.fxml"));
            Stage stage = new Stage();
            stage.setTitle(title);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPersona(String title, String option) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/personal.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setResizable(false);
            stage.setScene(scene);
            PersonsViewController children = fxmlLoader.getController();
            children.setOption(option);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filterPersonal(String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/personal-select.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private void showAlertDeletePositionWithHeaderText(String title, StateDTO node) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление записи");
        alert.setHeaderText("Внимание!");
        alert.setContentText("Вы действительно хотите удалить запись \"" + title + "\" и все дочерние записи?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            StateModel.delete(node.getState());
            refresh();
        }
    }

    private void showAlertDeletePersonaWithHeaderText(String title, PersonPosition personPosition) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление записи");
        alert.setHeaderText("Внимание!");
        alert.setContentText("Вы действительно хотите удалить запись \"" + title + "\" и все связанные записи?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            PersonPositionModel.delete(personPosition);
            refresh();
        }
    }

    public State getCurrentState() {
        return currentState;
    }
}

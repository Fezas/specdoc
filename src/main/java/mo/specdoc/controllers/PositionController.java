/*
 * Copyright (c) 2022-2023. Stepantsov P.V.
 */

package mo.specdoc.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import mo.specdoc.entity.Post;
import mo.specdoc.entity.State;
import mo.specdoc.model.PersonPositionModel;
import mo.specdoc.model.PersonaModel;
import mo.specdoc.model.PostModel;
import mo.specdoc.model.StateModel;
import mo.specdoc.util.FXMLControllerManager;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.*;
@Data
public class PositionController implements Initializable {
    @FXML    private TreeTableColumn<StateDTO, String> clmnButtons, clmnInfo;
    @FXML    private TreeTableColumn<StateDTO, String> clmnTitleStructure;
    @FXML    private TreeTableView<StateDTO> tblStructure;
    private StateDTO node;
    private Persona currentPersona = new Persona();
    private State currentState;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMLControllerManager.getInstance().setPositionController(this);
        //привязка столбцов таблицы к свойствам объекта
        clmnTitleStructure.setCellValueFactory(new TreeItemPropertyValueFactory<StateDTO, String>("title"));
        clmnButtons.setCellValueFactory(new TreeItemPropertyValueFactory<StateDTO, String>("boxBtn"));
        clmnInfo.setCellValueFactory(new TreeItemPropertyValueFactory<StateDTO, String>("info"));
        createStructure();


        //тут была какая то дикая ошибка - при коллапсе слетают иконки молов, но после оптимизации кода куда то делась....
        clmnTitleStructure.setCellFactory(ttc -> new TreeTableCell<StateDTO, String>() {
            //final Node nodeImageUser = new ImageView(
            //        new Image(getClass().getResourceAsStream("/images/user.png"))
            //);
            //final Node nodeImageUserNone = new ImageView(
            //        new Image(getClass().getResourceAsStream("/images/user-none.png"))
            //);
            final FontIcon iconVakant = new FontIcon("ri-vcard-o:20:gray");
            final FontIcon iconPersona = new FontIcon("ri-vcard-o:20:green");
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(empty){
                    setText(null);
                    setGraphic(null);
                    return;
                }
                StateDTO node = getTableRow().getItem();
                if (node != null) {
                    //сделать серыми ваканты
                    if (node.getState().getTypeState() == 4) {
                        //setGraphic(node.getPersona().getId() != 0L ? nodeImageUser : nodeImageUserNone);
                        setGraphic(node.getPersona() != null ? iconPersona : iconVakant);
                    } else setGraphic(null);

                }
                setText(empty ? null : item);
            }
        });
    }

    public void refresh() {
        tblStructure.getRoot().getChildren().clear();
        structure(tblStructure.getRoot());
    }

    /**
     * Процедура загрузки узлов штата {@link State и сортировка пузырьком по значению sortValue
     * @param List<State> data
     */
    private List<State> sortedListState(List<State> data) {
        List<State> result = new ArrayList<>();
        for (int i = 0; i < data.size() - 1; i++) {
            for(int j = 0; j < data.size() - i - 1; j++) {
                if(data.get(j + 1).getSortValue() < data.get(j).getSortValue()) {
                    State swap = data.get(j);
                    data.set(j, data.get(j + 1));
                    data.set(j + 1, swap);
                }
            }
        }
        return data;
    }


    public void createStructure() {
        State stateRoot = StateModel.getFromTypeState(1).get(0);
        StateDTO root = createNode(stateRoot);
        TreeItem<StateDTO> itemRoot = new TreeItem<StateDTO>(root); // корень всей структуры
        itemRoot.setExpanded(true);
        structure(itemRoot);
        tblStructure.setRoot(itemRoot);
    }
    
    /**
     * Функция создания узла штата {@link State} в структуре типа {@link TreeTableView}
     * @return возвращает узел {@link StateDTO}
     */
    
    private StateDTO createNode(State state) {
        StateDTO node = new StateDTO();
        node.setTitle(state.getTitleState());
        node.setState(state);
        switch (state.getTypeState()) {
            case 1: //войсковая часть
                createButtonAddRootSubdivision(node);
                createButtonAddSubdivision(node);
                createButtonAddPosition(node);
                createButtonAddRaschet(node);
                break;
            case 2://отдельное подразделение
                createButtonAddRootSubdivision(node);
                createButtonAddSubdivision(node);
                createButtonAddPosition(node);
                createButtonAddRaschet(node);
                createButtonAddPost(node);
                createButtonEdit(node);
                createButtonDelete(node);
                break;
            case 3://линейное подразделение
                createButtonAddSubdivision(node);
                createButtonAddPosition(node);
                createButtonAddRaschet(node);
                createButtonAddPost(node);
                createButtonEdit(node);
                createButtonDelete(node);
                break;
            case 4://должность
                PersonPosition pp = PersonPositionModel.getActualPersonsFromPositionId(state.getIdState()); //персоны в узлах
                if (pp == null) {
                    node.setTitle(state.getTitleState() + " - вакант");
                    createButtonAddPersona(node);
                } else {
                    Persona persona = pp.getPersonaFromPosition();
                    node.setPersona(persona);
                    node.setTitle(state.getTitleState() + " " + persona.getFamily() + " " + persona.getNamePerson().charAt(0) + ". " + persona.getLastname().charAt(0) + ".");
                    createButtonDeletePersona(node, pp);
                }
                createButtonEdit(node);
                createButtonDelete(node);
                break;
            case 5://боевой расчет
                createButtonAddRaschet(node);
                createButtonAddPost(node);
                createButtonEdit(node);
                createButtonDelete(node);
                break;
            case 6://боевой пост
            case 7:
                createButtonEdit(node);
                createButtonDelete(node);
                break;
        }
        return node;
    }

    /**
     * Функция рекурсивного формирования {@link TreeTableView} с помощью запроса  {@link StateModel#getChildrenPosition(long)}
     */
    
    public void structure(TreeItem<StateDTO> itemRoot) {
        List<State> data = StateModel.getChildrenPosition(itemRoot.getValue().getState().getIdState()); //дочерние узлы
        if (!data.isEmpty()) {
            for (State state : sortedListState(data)) {
                StateDTO stateDTO = createNode(state);
                TreeItem<StateDTO> item = new TreeItem<StateDTO>(stateDTO);
                item.setExpanded(true);
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
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                editRootSubdivision("Отдельное подразделение", new State(), stateDTO.getState().getIdState());
            }
        });
        stateDTO.getBoxBtn().getChildren().add(button);
    }

    private void createButtonAddSubdivision(StateDTO stateDTO) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-node-expand"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Добавить линейное подразделение в \n\"" + stateDTO.getState().getTitleState() + "\"\n");
        button.setTooltip(tooltip);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                editSubdivision("Линейное подразделение", new State(), stateDTO.getState().getIdState());
            }
        });
        stateDTO.getBoxBtn().getChildren().add(button);
    }

    private void createButtonAddPosition(StateDTO stateDTO) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-contacts"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Добавить должность в \n\"" + stateDTO.getState().getTitleState() + "\"\n");
        button.setTooltip(tooltip);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                editPosition("Должность", new State(), stateDTO.getState().getIdState());
            }
        });
        stateDTO.getBoxBtn().getChildren().add(button);
    }

    private void createButtonAddRaschet(StateDTO stateDTO) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-flag"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Добавить расчет в \n\"" + stateDTO.getState().getTitleState() + "\"\n");
        button.setTooltip(tooltip);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                editRaschet("Боевой расчет", new State(), stateDTO.getState().getIdState());
            }
        });
        stateDTO.getBoxBtn().getChildren().add(button);
    }

    private void createButtonAddPost(StateDTO stateDTO) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-notification"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Добавить пост в \n\"" + stateDTO.getState().getTitleState() + "\"\n");
        button.setTooltip(tooltip);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                editPost("Боевой пост", new State(), stateDTO.getState().getIdState());
            }
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
            case 2://отдельное подразделение
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        editRootSubdivision("Редактирование новой записи", stateDTO.getState(), stateDTO.getState().getIdState());
                    }
                });
                break;
            case 3://линейное подразделение
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        editSubdivision("Редактирование новой записи", stateDTO.getState(), stateDTO.getState().getIdState());
                    }
                });
                break;
            case 4://должность
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        editPosition("Должность", stateDTO.getState(), stateDTO.getState().getIdState());
                    }
                });
                break;
            case 5://боевой расчет
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        editRaschet("Боевой расчет", stateDTO.getState(), stateDTO.getState().getIdState());
                    }
                });
                break;
            case 6://боевой пост
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        editPost("Боевой пост", stateDTO.getState(), stateDTO.getState().getIdState());
                    }
                });
                break;
        }
        stateDTO.getBoxBtn().getChildren().add(button);
    }

    private void createButtonDelete(StateDTO stateDTO) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-delete"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Редактировать \n\"" + stateDTO.getState().getTitleState() + "\"\n");
        button.setTooltip(tooltip);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                showAlertDeletePositionWithHeaderText(stateDTO.getState().getTitleState(), stateDTO);
            }
        });
        stateDTO.getBoxBtn().getChildren().add(button);
    }


    private void createButtonAddDopusk(StateDTO state) {

    }

    private void createButtonAddPersona(StateDTO stateDTO) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-user-add"));
        Tooltip tooltip = new Tooltip();
        //tooltip.setText("Добавить персонал в \n\"" + state.getPosition().getTitle() + "\"\n");
        button.setTooltip(tooltip);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                currentPersona = new Persona();
                currentState = stateDTO.getState();
                addPersona("Добавление нового служащего");
            }
        });
        stateDTO.getBoxBtn().getChildren().add(button);
    }

    private void createButtonDeletePersona(StateDTO state, PersonPosition personPosition) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-user-delete"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Открепить служащего");
        button.setTooltip(tooltip);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                showAlertDeletePersonaWithHeaderText(state.getState().getTitleState(), personPosition);
            }
        });
        state.getBoxBtn().getChildren().add(button);
    }

    public void editRootSubdivision(String title, State state, long id) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/state-root.fxml"));
            StateRootEditController controller = new StateRootEditController(state, id);
            fxmlLoader.setController(controller);
            Stage stage = new Stage();
            stage.setTitle(title);
            StateRootEditController c = fxmlLoader.getController();
            c.setParent(this);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editSubdivision(String title, State state, long id) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/state-subdiv.fxml"));
            StateSubdivEditController controller = new StateSubdivEditController(state, id);
            fxmlLoader.setController(controller);
            Stage stage = new Stage();
            stage.setTitle(title);
            StateSubdivEditController c = fxmlLoader.getController();
            c.setParent(this);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editPosition(String title, State state, long id) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/state-position.fxml"));
            StatePositionEditController controller = new StatePositionEditController(state, id);
            fxmlLoader.setController(controller);
            Stage stage = new Stage();
            stage.setTitle(title);
            StatePositionEditController c = fxmlLoader.getController();
            c.setParent(this);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editRaschet(String title, State state, long id) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/state-raschet.fxml"));
            StateRaschetEditController controller = new StateRaschetEditController(state, id);
            fxmlLoader.setController(controller);
            Stage stage = new Stage();
            stage.setTitle(title);
            StateRaschetEditController c = fxmlLoader.getController();
            c.setParent(this);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editPost(String title, State state, long id) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/state-post.fxml"));
            StatePostEditController controller = new StatePostEditController(state, id);
            fxmlLoader.setController(controller);
            Stage stage = new Stage();
            stage.setTitle(title);
            StatePostEditController c = fxmlLoader.getController();
            c.setParent(this);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPersona(String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/personal.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setResizable(false);
            stage.setScene(scene);
            PersonsViewController children = fxmlLoader.getController();
            children.setOption("freeFromSelectPosition");
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

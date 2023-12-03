/*
 * Copyright (c) 2022-2023. Stepantsov P.V.
 */

package mo.specdoc.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Data;
import mo.specdoc.dto.State;
import mo.specdoc.entity.PersonPosition;
import mo.specdoc.entity.Persona;
import mo.specdoc.entity.Position;
import mo.specdoc.model.PersonPositionModel;
import mo.specdoc.model.PersonaModel;
import mo.specdoc.model.PositionModel;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.*;
@Data
public class PositionController implements Initializable {
    @FXML    private TreeTableColumn<State, String> clmnButtons, clmnInfo;
    @FXML    private TreeTableColumn<State, String> clmnTitleStructure;
    @FXML    private TreeTableView<State> tblStructure;
    private static PositionController singleton;
    private State node;
    private Persona currentPersona = new Persona();
    private Position currentPosition = new Position();

    public PositionController() {
        singleton = this;
    }

    public static PositionController getInstance() {
        if (singleton == null)
            singleton = new PositionController();
        return singleton;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //привязка столбцов таблицы к свойствам объекта
        clmnTitleStructure.setCellValueFactory(new TreeItemPropertyValueFactory<State, String>("title"));
        clmnButtons.setCellValueFactory(new TreeItemPropertyValueFactory<State, String>("boxBtn"));
        clmnInfo.setCellValueFactory(new TreeItemPropertyValueFactory<State, String>("info"));
        createStructure();


        //тут была какая то дикая ошибка - при коллапсе слетают иконки молов, но после оптимизации кода куда то делась....
        clmnTitleStructure.setCellFactory(ttc -> new TreeTableCell<State, String>() {
            final Node nodeImageUser = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/user.png"))
            );
            final Node nodeImageUserNone = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/user-none.png"))
            );
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
                State node = getTableRow().getItem();
                if (node != null) {
                    //сделать серыми ваканты
                    if (node.getPosition().isCard()) {
                        //setGraphic(node.getPersona().getId() != 0L ? nodeImageUser : nodeImageUserNone);
                        setGraphic(node.getPersona().getId() != 0L ? iconPersona : iconVakant);
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

    public void createStructure() {
        Position positionRoot = PositionModel.getRootPosition();
        State root = createNode(positionRoot);
        TreeItem<State> itemRoot = new TreeItem<State>(root); // корень всей структуры
        itemRoot.setExpanded(true);
        structure(itemRoot);
        tblStructure.setRoot(itemRoot);
    }
    
    /**
     * Функция создания узла штата {@link Position} в структуре типа {@link TreeTableView}
     * @return возвращает узел {@link State}
     */
    
    private State createNode(Position position) {
        Persona persona;
        if (position.getIdParentPosition() != null) {
            if (position.isCard()) {
                PersonPosition pp = PersonPositionModel.getActualPersonsFromPositionId(position.getId()); //персоны в узлах
                if (pp == null) {
                    node = new State(position, new Persona());
                    node.setTitle(position.getTitle() + " - вакант");
                    createButtonAddPersona(node);
                    createButtonEditPosition(node);
                } else {
                    persona = pp.getPersonaFromPosition();
                    node = new State(position, persona);
                    node.setTitle(position.getTitle() + " " + persona.getFamily() + " " + persona.getNamePerson().charAt(0) + ". " + persona.getLastname().charAt(0) + ".");
                    createButtonEditPersona(node);
                    createButtonDeletePersona(node, pp);
                    createButtonEditPosition(node);
                }
            } else {
                node = new State(position, new Persona());
                node.setTitle(position.getTitle());
                createButtonAddPosition(node);
                createButtonEditPosition(node);
                createButtonDelPosition(node);
            }
        } else {
            node = new State(position, new Persona());
            node.setTitle(position.getTitle());
            createButtonAddPosition(node);
            createButtonEditPosition(node);
            createButtonDelPosition(node);
        }
        return node;
    }

    /**
     * Функция рекурсивного формирования {@link TreeTableView} с помощью запроса  {@link PositionModel#getChildrenPosition(long)}
     */
    
    public void structure(TreeItem<State> itemRoot) {
        List<Position> data = PositionModel.getChildrenPosition(itemRoot.getValue().getPosition().getId()); //дочерние узлы
        if (!data.isEmpty()) {
            for (Position position : data) {
                State state = createNode(position);
                TreeItem<State> item = new TreeItem<State>(state);
                item.setExpanded(true);
                itemRoot.getChildren().add(item);
                structure(item);
            }
        }
    }

    private void createButtonAddPosition(State state) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-plus-circle"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Добавить узел или должность в \n\"" + state.getPosition().getTitle() + "\"\n");
        button.setTooltip(tooltip);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                editNode("Добавление новой записи", new Position(), state.getPosition().getId());
            }
        });
        state.getBoxBtn().getChildren().add(button);
    }

    private void createButtonEditPosition(State state) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-edit"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Редактировать узел или должность в \n\"" + state.getPosition().getTitle() + "\"\n");
        button.setTooltip(tooltip);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                editNode("Добавление новой записи", state.getPosition(), state.getPosition().getIdParentPosition());
            }
        });
        state.getBoxBtn().getChildren().add(button);
    }

    private void createButtonDelPosition(State state) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-delete"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Удаление узла в \n\"" + state.getPosition().getTitle() + "\"\n");
        button.setTooltip(tooltip);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                showAlertDeletePositionWithHeaderText("Удаление узла", state.getPosition());
            }
        });
        state.getBoxBtn().getChildren().add(button);
    }

    private void createButtonAddPersona(State state) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-user-add"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Добавить персонал в \n\"" + state.getPosition().getTitle() + "\"\n");
        button.setTooltip(tooltip);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                currentPersona = new Persona();
                currentPosition = state.getPosition();
                addPersona("Добавление нового служащего", state.getPosition());
            }
        });
        state.getBoxBtn().getChildren().add(button);
    }

    private void createButtonEditPersona(State state) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-user"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Редактировать запись о служащем");
        button.setTooltip(tooltip);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                currentPosition = state.getPosition();
                editPersona("Редактировать запись о служащем", state.getPersona());
            }
        });
        state.getBoxBtn().getChildren().add(button);
    }

    private void createButtonDeletePersona(State state, PersonPosition personPosition) {
        Button button = new Button();
        button.setGraphic(new FontIcon("anto-user-delete"));
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Открепить служащего");
        button.setTooltip(tooltip);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                showAlertDeletePersonaWithHeaderText(state.getPosition().getTitle(), personPosition);
            }
        });
        state.getBoxBtn().getChildren().add(button);
    }

    public void editNode(String title, Position position, long id) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/position-edit.fxml"));
            PositionEditController positionEditController = new PositionEditController(position, id);
            fxmlLoader.setController(positionEditController);
            Stage stage = new Stage();
            stage.setTitle(title);
            PositionEditController c = fxmlLoader.getController();
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

    public void addPersona(String title, Position position) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/personal.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setResizable(false);
            stage.setScene(scene);
            PersonsViewController children = fxmlLoader.getController();
            children.setParent(this);
            children.setPosition(position);
            children.initialize(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editPersona(String title, Persona persona) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/persona-edit.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setResizable(false);
            stage.setScene(scene);
            PersonEditController children = fxmlLoader.getController();
            children.initialize(persona);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlertDeletePositionWithHeaderText(String title, Position position) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление записи");
        alert.setHeaderText("Внимание!");
        alert.setContentText("Вы действительно хотите удалить запись \"" + title + "\" и все дочерние записи?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            PositionModel.delete(position);
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
}

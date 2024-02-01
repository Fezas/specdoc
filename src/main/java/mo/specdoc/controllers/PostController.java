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
import mo.specdoc.dto.Raschet;
import mo.specdoc.entity.Position;
import mo.specdoc.entity.Post;
import mo.specdoc.model.PositionModel;
import mo.specdoc.model.PostModel;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PostController implements Initializable {
    @FXML    private TreeTableColumn<Raschet, String> clmnInfo, clmnBtn;
    @FXML    private TreeTableColumn<Raschet, String> clmnTitleStructure;
    @FXML    private TreeTableView<Raschet> tblStructure;
    private MainController controller;
    private Post post;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //привязка столбцов таблицы к свойствам объекта
        clmnTitleStructure.setCellValueFactory(new TreeItemPropertyValueFactory<Raschet, String>("title"));
        clmnInfo.setCellValueFactory(new TreeItemPropertyValueFactory<Raschet, String>("info"));
        clmnBtn.setCellValueFactory(new TreeItemPropertyValueFactory<Raschet, String>("boxBtn"));
        createStructure();

        tblStructure.setRowFactory(
                new Callback<TreeTableView<Raschet>, TreeTableRow<Raschet>>() {
                    @Override
                    public TreeTableRow<Raschet> call(TreeTableView<Raschet> tableView) {
                        final TreeTableRow<Raschet> row = new TreeTableRow<Raschet>();
                        row.setOnMouseEntered(event -> {
                            if (row.getTreeItem() != null) {
                                Raschet item = row.getTreeItem().getValue();
                                item.getBoxBtn().setVisible(true);
                            }
                        });
                        //прячем кнопки при уходе со строки курсора
                        row.setOnMouseExited(event -> {
                            if (row.getTreeItem() != null) {
                                Raschet item = row.getTreeItem().getValue();
                                item.getBoxBtn().setVisible(false);
                            }
                        });
                        return row;
                    }
                }
        );
        //тут была какая то дикая ошибка - при коллапсе слетают иконки молов, но после оптимизации кода куда то делась....
        clmnTitleStructure.setCellFactory(ttc -> new TreeTableCell<Raschet, String>() {
            final Node nodeImageUser = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/user.png"))
            );
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(empty){
                    setText(null);
                    setGraphic(null);
                    return;
                }
                Raschet node = getTableRow().getItem();
                if (node != null) {
                    //сделать серыми ваканты
                    setGraphic(node.getPost().getIsCard() ? nodeImageUser : null);
                }
                setText(empty ? null : item);
            }
        });
    }

    public void refresh() {
        tblStructure.getRoot().getChildren().clear();
        structure(tblStructure.getRoot());
    }

    private Post createSubdivision(Position position){
        Post subdiv = new Post();
        subdiv.setTitle(position.getTitle());
        subdiv.setIsCard(false);
        subdiv.setAmplification(false);
        subdiv.setArmed(false);
        return subdiv;
    }


    public void createStructure() {
        Raschet root = createNode(createSubdivision(PositionModel.getRootPosition()));
        TreeItem<Raschet> itemRoot = new TreeItem<Raschet>(root); // корень всей структуры
        itemRoot.setExpanded(true);
        structure(itemRoot);
        tblStructure.setRoot(itemRoot);
        for (Position position : PositionModel.getOnlySubDivision()) {
            Raschet subdiv = createNode(createSubdivision(position));
            TreeItem<Raschet> item = new TreeItem<Raschet>(subdiv);
            itemRoot.getChildren().add(item);
            List<Post> postsInCurrentSubDivision = PostModel.getByIdSubDivision(position.getId());
            createStructureCurrendSubDiv(item);
        }
    }
    
    /**
     * Функция создания узла штата {@link Post} в структуре типа {@link TreeTableView}
     * @return возвращает узел {@link Raschet}
     */
    
    private Raschet createNode(Post post) {
        Raschet bp = new Raschet(post);
        if (post.getIsCard()) {
            bp.setTitle(post.getTitle() + " БП №" + post.getConditionalNumb());
        } else {
            addActionAdd(bp);
            bp.setTitle(post.getTitle());
        }
        if (post.getIdParentPost() != null) {
            addActionEdit(bp);
            addActionDel(bp);

        }
        if (post.isArmed()) {
            Label label = new Label();
            FontIcon icon = new FontIcon("antf-safety-certificate");
            icon.setIconColor(Color.RED);
            icon.setIconSize(14);
            label.setGraphic(icon);
            bp.getInfo().getChildren().add(label);
        }
        if (post.getAmplification()) {
            Label label = new Label();
            FontIcon icon = new FontIcon("anto-field-binary");
            icon.setIconColor(Color.RED);
            icon.setIconSize(14);
            label.setGraphic(icon);
            bp.getInfo().getChildren().add(label);
        }
        return bp;
    }

    /**
     * Функция рекурсивного формирования {@link TreeTableView} с помощью запроса  {@link PositionModel#getChildrenPosition(long)}
     */

    public void createStructureCurrendSubDiv(TreeItem<Raschet> root) {
        List<Post> posts = PostModel.getChildrenPost(root.getValue().getPost().getId()); //дочерние узлы
        if (!posts.isEmpty()) {
            for (Post post : posts) {
                Raschet raschet = createNode(post);
                TreeItem<Raschet> item = new TreeItem<Raschet>(raschet);
                item.setExpanded(true);
                root.getChildren().add(item);
                structure(item);
            }
        }
    }

    /**
     * Функция рекурсивного формирования {@link TreeTableView} с помощью запроса  {@link PositionModel#getChildrenPosition(long)}
     */
    
    public void structure(TreeItem<Raschet> itemRoot) {
        List<Post> data = PostModel.getChildrenPost(itemRoot.getValue().getPost().getId()); //дочерние узлы
        if (!data.isEmpty()) {
            for (Post post : data) {
                Raschet raschet = createNode(post);
                TreeItem<Raschet> item = new TreeItem<Raschet>(raschet);
                item.setExpanded(true);
                itemRoot.getChildren().add(item);
                structure(item);
            }
        }
    }

    private void addActionEdit(Raschet raschet) {
        Button btnEdit = new Button();
        btnEdit.setGraphic(new FontIcon("anto-edit"));
        Tooltip tooltipEdit = new Tooltip();
        tooltipEdit.setText("Корректировка узла \n\"" + raschet.getTitle() + "\"\n");
        btnEdit.setTooltip(tooltipEdit);
        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                editNode(raschet.getTitle(), raschet.getPost(), raschet.getPost().getIdParentPost());
            }
        });
        raschet.getBoxBtn().getChildren().add(btnEdit);
    }
    private void addActionAdd(Raschet raschet) {
        Button btnAddNode = new Button();
        btnAddNode.setGraphic(new FontIcon("anto-plus-circle"));
        Tooltip tooltipAdd = new Tooltip();
        tooltipAdd.setText("Добавить подчиненный узел к \n\"" + raschet.getTitle() + "\"\n");
        btnAddNode.setTooltip(tooltipAdd);
        btnAddNode.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                editNode("Добавление нового узла", new Post(), raschet.getPost().getId());
            }
        });
        raschet.getBoxBtn().getChildren().add(btnAddNode);
    }
    private void addActionDel(Raschet raschet) {
        Button btnDelete = new Button();
        btnDelete.setGraphic(new FontIcon("anto-delete"));
        Tooltip tooltipDelete = new Tooltip();
        tooltipDelete.setText("Удаление узла \n\"" + raschet.getTitle() + "\"\n");
        btnDelete.setTooltip(tooltipDelete);
        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                showAlertWithHeaderText(raschet.getTitle(), raschet.getPost());
            }
        });
        raschet.getBoxBtn().getChildren().add(btnDelete);
    }

    public void editNode(String title, Post post, long id) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/post-edit.fxml"));
            PostEditController postEditController = new PostEditController(post, id);
            fxmlLoader.setController(postEditController);
            Stage stage = new Stage();
            stage.setTitle(title);
            PostEditController c = fxmlLoader.getController();
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

    private void showAlertWithHeaderText(String title, Post post) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление записи");
        alert.setHeaderText("Внимание!");
        alert.setContentText("Вы действительно хотите удалить запись \"" + title + "\" и все дочерние записи?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            PostModel.delete(post);
            refresh();
        }
    }
}

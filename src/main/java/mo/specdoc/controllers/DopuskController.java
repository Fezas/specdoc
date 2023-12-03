/*
 * Copyright (c) 2023
 */

package mo.specdoc.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.control.cell.PropertyValueFactory;
import mo.specdoc.entity.Dopusk;
import mo.specdoc.entity.Persona;
import mo.specdoc.entity.Post;
import mo.specdoc.entity.RankPerson;
import mo.specdoc.model.DopuskModel;
import mo.specdoc.model.PostModel;
import mo.specdoc.model.RankPersonModel;
import org.controlsfx.control.CheckListView;

import java.util.*;

public class DopuskController {
    @FXML    private Button btnAddPost;
    @FXML    private TreeView<Post> treeViewStructurePosts;
    @FXML    private DatePicker datePickerAchievPostWork;
    @FXML    private TextField tfieldNumbDocsPost;
    @FXML    private TableView<Dopusk> tblPosts;
    @FXML    private TableColumn<Dopusk, String> tblClmnPostsTitle;
    @FXML    private TableColumn<Dopusk, Date> tblClmnPostsDateAddPosts;
    private static ObservableList<Dopusk> dopusks = FXCollections.observableArrayList();
    private static Map<Long, Post> mapPosts = new HashMap<Long, Post>();
    private PersonEditController personEditController;

    public void setParent (PersonEditController personEditController){
        this.personEditController = personEditController;
    }

    private void disableLbl() {
        if (dopusks.isEmpty()) {
            personEditController.getLblPost().setDisable(true);
            personEditController.getLbl4().setDisable(true);
        } else {
            personEditController.getLblPost().setDisable(false);
            personEditController.getLbl4().setDisable(false);
            personEditController.getLblPost().setText("Допущен к " + String.valueOf(dopusks.size()) + " постам");
        }
    }

    @FXML
    void addPost(ActionEvent event) {
        if (!mapPosts.isEmpty()) {
            for (Map.Entry<Long, Post> entry : mapPosts.entrySet()) {
                Dopusk dopusk = new Dopusk();
                dopusk.setPost(entry.getValue());
                dopusk.setNumbOrderDopusk(tfieldNumbDocsPost.getText());
                dopusk.setPersona(personEditController.getCurrentPersona());
                dopusk.setDateDopusk(java.sql.Date.valueOf(datePickerAchievPostWork.getValue()));
                DopuskModel.saveOrUpdate(dopusk);
                dopusks.add(dopusk);
                initalizePosts();
            }
        }
    }

    private String titlePostWithStructure(Post post, String title) {
        if (post.getIdParentPost() != null) {
            Post parent = PostModel.getById(post.getIdParentPost());
            title = title + " > " + parent.getTitle();
            System.out.println("222222222222         " + title);
            //post.setTitleWithStructure(post.getTitleWithStructure() + " > " + parent.getTitle());
            titlePostWithStructure(parent, title);
        }
        return title;
    }

    /**
     * Функция-фильтр выбора боевых постов из коллекции {@link PostModel#getChildrenCard(long)}
     * и добавления (удаления) к временному хранилищу  {@link HashMap}
     */
    private void createListenerCheckBoxTreeItem(CheckBoxTreeItem<Post> item) {
        item.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (item.getValue().getIsCard()) {
                if (newVal) {
                    mapPosts.put(item.getValue().getId(), item.getValue());
                } else {
                    mapPosts.remove(item.getValue().getId());
                }
                System.out.println(item.getValue() + " selection state: " + newVal + " " + mapPosts.size());
            }
        });
    }

    /**
     * Функция рекурсивного формирования {@link CheckBoxTreeItem} с помощью запроса  {@link PostModel#getChildrenPostWithoutCard(long)}
     * и добавления к родительскому узлу структуры  {@link TreeView<Post>}
     */
    public void createStructurePost(TreeItem<Post> root) {
        List<Post> data = PostModel.getChildrenPost(root.getValue().getId()); //дочерние узлы
        if (!data.isEmpty()) {
            for (Post post : data) {
                CheckBoxTreeItem<Post> item = new CheckBoxTreeItem<Post>(post);
                item.setExpanded(true);
                createListenerCheckBoxTreeItem(item);
                root.getChildren().add(item);
                createStructurePost(item);
            }
        }
    }

    public void initalize() {
        datePickerAchievPostWork.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue != null) {
                btnAddPost.setDisable(false);
            } else btnAddPost.setDisable(true);
        });
        tfieldNumbDocsPost.textProperty().addListener((ov, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                btnAddPost.setDisable(false);
            } else btnAddPost.setDisable(true);
        });
        Post root = PostModel.getRootPosition();
        CheckBoxTreeItem<Post> rootItem = new CheckBoxTreeItem<Post>(root);
        createListenerCheckBoxTreeItem(rootItem);
        rootItem.setExpanded(true);
        createStructurePost(rootItem);
        treeViewStructurePosts.setRoot(rootItem);
        treeViewStructurePosts.setCellFactory(CheckBoxTreeCell.<Post>forTreeView());
        tblClmnPostsTitle.setCellValueFactory(new PropertyValueFactory<>("titleWithStructure"));
        tblClmnPostsDateAddPosts.setCellValueFactory(new PropertyValueFactory<>("dateDopusk"));
        tblPosts.setRowFactory(
                tableView -> {
                    //событие по двойному клику строки
                    final TableRow<Dopusk> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                            Dopusk rowData = row.getItem();
                        }
                    });
                    //контексное меню
                    final ContextMenu rowMenu = new ContextMenu();
                    MenuItem removeItem = new MenuItem("Удалить пункт");
                    removeItem.setOnAction(event -> {
                        Alert alertDelete = new Alert(Alert.AlertType.CONFIRMATION);
                        alertDelete.setTitle("Внимание");
                        alertDelete.setHeaderText("Удаление записи");
                        alertDelete.setContentText("Удалить запись: " + row.getItem().getPost().getTitle() + "?");
                        Optional<ButtonType> option = alertDelete.showAndWait();
                        if (option.get() == ButtonType.OK) {
                            DopuskModel.delete(row.getItem());
                            initalizePosts();
                        }
                    });
                    rowMenu.getItems().addAll(removeItem);
                    row.contextMenuProperty().bind(
                            Bindings.when(row.emptyProperty())
                                    .then((ContextMenu) null)
                                    .otherwise(rowMenu));
                    return row;
                }
        );
        initalizePosts();
        tblPosts.setItems(dopusks);
    }

    private void initalizePosts() {//инициализация допуска к постам
        dopusks.clear();
        for (Dopusk dopusk : DopuskModel.getByIdPersona(personEditController.getCurrentPersona().getId())) {
            dopusk.setTitleWithStructure(titlePostWithStructure(dopusk.getPost(),dopusk.getPost().getTitle()));
            dopusks.add(dopusk);
        }
        disableLbl();
    }
}

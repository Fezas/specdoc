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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mo.specdoc.util.FXMLControllerManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML    private Menu menuGraf;
    private void createScene(String nameResourceXML, String title, Boolean resizable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + nameResourceXML));
            Stage stage = new Stage();
            stage.setTitle(title);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toString());
            stage.initModality(Modality.APPLICATION_MODAL);
            //stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/telegra.png"))));
            stage.setResizable(resizable);
            stage.showAndWait();
        } catch (NullPointerException e) {
            e.printStackTrace();
            //logger.error("Error", e);
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
            //logger.error("Error", e);
        }
    }

    public void personal(String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/personal.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setResizable(true);
            stage.setScene(scene);
            PersonsViewController children = fxmlLoader.getController();
            children.setOption("allPersons");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void viewSubdivisions() {
        createScene("structure-position-table.fxml", "Штат", true);
    }
    @FXML
    void viewPosts() {
        createScene("-structure-post-table.fxml", "Посты", false);
    }
    @FXML
    void viewStatePost() {
        createScene("-posts.fxml", "Боевой расчет", false);
    }
    @FXML
    void viewPosition() {
        createScene("positions.fxml", "Должности", false);
    }
    @FXML
    void viewDopusk() {
        createScene("input-dopusk.fxml", "Допуски", true);
    }
    @FXML
    void viewPersonal() {
        personal("Персонал");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMLControllerManager.getInstance().setMainController(this);
        startSimleMenu(menuGraf, "input-work.fxml", "Графики");
    }

    /**
     * Процедура запуска меню без дочерних элементов
     * так как по умолчанию такой запуск невозможен - создаем элемент MenuItem и вешаем на него событие
     * @param menu  меню
     * @param xml  xml представления
     * @param title  загловок представления
     */
    private void startSimleMenu(Menu menu, String xml, String title) {
        MenuItem menuItem = new MenuItem("firthItem");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createScene(xml, title, true);
            }
        });
        menu.getItems().add(menuItem);
        menu.showingProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (newValue) {
                        // запуск первого элемента
                        menu.getItems().get(0).fire();
                    }
                }
        );
    }


}

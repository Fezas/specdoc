/*
 * Copyright (c) 2022
 */

package mo.specdoc.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import mo.specdoc.entity.*;
import mo.specdoc.model.*;
import mo.specdoc.util.Month;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.ResourceBundle;

public class InputWorkController implements Initializable {
    private ObservableList<State> posts = FXCollections.observableArrayList();

    @FXML    private TabPane tabPanePosts;
    @FXML    private Button btnCreateDocDayWork;

    public InputWorkController() {
    }
//исключить пустые табы
    private void createTabs() {
        for (Position position : PositionModel.getOnlySubDivision()) {
            Tab tab = new Tab(position.getTitleShort());
            tabPanePosts.getTabs().add(tab);

            for (State post : posts) {
            }
        }
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnCreateDocDayWork.setGraphic(new FontIcon("ri-docs-com:18"));
        createTabs();
    }

    private void showAlertWithHeaderText() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка внесения данных");
        alert.setContentText("Невозможно дежурство более 1 суток подряд!");
        alert.showAndWait();
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

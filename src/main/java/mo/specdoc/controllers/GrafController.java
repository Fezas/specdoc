/*
 * Copyright (c) 2022
 */

package mo.specdoc.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import lombok.SneakyThrows;
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

public class GrafController implements Initializable {
    private ObservableList<Post> posts = FXCollections.observableArrayList();
    private Month months = new Month();
    private Integer[] years = new Integer[3];
    private int daysInMonth;
    private ObservableList<String[]> data = FXCollections.observableArrayList();
    private int year;
    private int month;

    @FXML    private TableView<String[]> tblGraf;
    @FXML    private ComboBox<Month> cmbBoxMonth;
    @FXML    private ComboBox<Integer> cmbBoxYear;
    @FXML    private ComboBox<Post> cmbBoxPost;
    @FXML    private TabPane tabPanePosts;
    @FXML    private Button btnCreateDocDayWork;
    public GrafController() {
    }

    private Date sqlDate(String day) {
        return (java.sql.Date.valueOf(year + "-" + month + "-" + day));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnCreateDocDayWork.setGraphic(new FontIcon("ri-docs-com:18"));
        loadPosts();
        for (Post post : posts) {
            Tab tab = new Tab(post.getTitleShort());
            tabPanePosts.getTabs().add(tab);
        }
        cmbBoxPost.getItems().addAll(posts);
        cmbBoxPost.getSelectionModel().selectFirst();
        cmbBoxPost.valueProperty().addListener((observable, oldValue, newValue) -> {
            createClmnInfo();
            createClmnDays();
            buildArrayWork();
        });

        LocalDate nowDate = LocalDate.now();
        //вычисляем количество дней в текущей дате
        YearMonth yearMonthObject = YearMonth.of(nowDate.getYear(), nowDate.getMonthValue());
        daysInMonth = yearMonthObject.lengthOfMonth();
        years[0] = nowDate.getYear() - 1; // предыдущий год
        for (int i = 0; i < 2; i++) { // текущий и следующий год
            years[i + 1] = nowDate.getYear() + i;
        }
        cmbBoxMonth.getItems().addAll(months.createListMonths());
        cmbBoxYear.getItems().addAll(years);
        cmbBoxMonth.getSelectionModel().select(nowDate.getMonthValue() - 1);
        cmbBoxYear.getSelectionModel().select(1); // выбираем текущий год
        // назначаем слушателей на ComboBox-ы
        cmbBoxMonth.valueProperty().addListener((observable, oldValue, newValue) -> {
            days(cmbBoxMonth.getValue().getMonthNumber() + 1, cmbBoxYear.getValue());
            month = cmbBoxMonth.getSelectionModel().getSelectedItem().getMonthNumber() + 1;
            createClmnInfo();
            createClmnDays();
            buildArrayWork();
        });
        cmbBoxYear.valueProperty().addListener((observable, oldValue, newValue) -> {
            days(cmbBoxMonth.getValue().getMonthNumber() + 1, cmbBoxYear.getValue());
            year = cmbBoxYear.getSelectionModel().getSelectedItem();
            createClmnInfo();
            createClmnDays();
            buildArrayWork();
        });
        cmbBoxMonth.getSelectionModel().selectFirst();
        cmbBoxYear.getSelectionModel().selectFirst();
        // возможность выделять конкретную ячейку (без этого выделяются строки)
        tblGraf.getSelectionModel().setCellSelectionEnabled(true);
        tblGraf.getFocusModel().focusedCellProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal.getTableColumn() != null) {
                tblGraf.getSelectionModel().selectRange(0, newVal.getTableColumn(), tblGraf.getItems().size(), newVal.getTableColumn());
                String idDopusk = data.get(newVal.getRow())[2];
                String currDay = newVal.getTableColumn().getText();
                if(newVal.getColumn() != 0)  {
                    newVal.getTableView().edit(newVal.getRow(), newVal.getTableColumn());
                    //System.out.println("Selected TableColumn: "+ newVal.getTableColumn().getText());
                    //System.out.println("Selected column index: "+ newVal.getColumn());
                    //System.out.println("Selected row index: "+ newVal.getRow());
                    String[] temp = data.get(newVal.getRow());
                    int titleColumnDay = Integer.parseInt(newVal.getTableColumn().getText());
                    if (temp[titleColumnDay + 2] == "д") {
                        temp[titleColumnDay + 2] = "";
                        System.out.println("Clear: "+ titleColumnDay);
                        data.set(newVal.getRow(), temp);
                        System.out.println(sqlDate(currDay));
                        WorkDayModel.delFromData(sqlDate(currDay), Long.parseLong(idDopusk));
                    } else {
                        //!НУЖНО ВСТАВИТЬ ПРОВЕРКУ ПРЕДЫДУЩЕГО - СЛЕДУЮЩЕГО МЕСЯЦА
                        if  (temp[titleColumnDay + 1] == "д" || temp[titleColumnDay + 3] == "д") {
                                showAlertWithHeaderText();
                        } else {
                            temp[titleColumnDay + 2] = "д";
                            data.set(newVal.getRow(), temp);
                            WorkDay workDay = new WorkDay();
                            workDay.setDateWork(sqlDate(currDay));
                            workDay.setIdDopusk(Long.parseLong(idDopusk));
                            workDay.setIdWorkType(65);
                            WorkDayModel.saveOrUpdate(workDay);
                        }
                    }
                }
            }
        });
        tblGraf.setItems(data);
    }

    private void showAlertWithHeaderText() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка внесения данных");
        alert.setContentText("Невозможно дежурство более 1 суток подряд!");
        alert.showAndWait();
    }

    /**
     * Функция вычисления количества дней в месяце на основании значений ComboBox года и месяца
     * @param month ComboBox month
     * @param year ComboBox year
     */
    private void days(int month, int year) {
        // количество дней в выбранном месяце
        YearMonth yearMonthObject = YearMonth.of(year, month);
        daysInMonth = yearMonthObject.lengthOfMonth();
        System.out.println("daysInMonth = " + daysInMonth);
    }

    /**
     * Процедура загрузки постов {@link Post} и сортировка пузырьком по значению sortValue
     */
    private void loadPosts() {
        List<Post> list = PostModel.getOnlyPost();
        for (int i = 0; i < list.size() - 1; i++) {
            for(int j = 0; j < list.size() - i - 1; j++) {
                if(list.get(j + 1).getSortValue() < list.get(j).getSortValue()) {
                    Post swap = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, swap);
                }
            }
        }
        posts.addAll(list);
    }

    /**
     * процедура создания в таблице колонок с информацией
     */
    private void createClmnInfo() {
        TableColumn<String[], String> tableColumnRank = new TableColumn<>("Звание");
        tableColumnRank.setPrefWidth(80);
        tableColumnRank.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));
        TableColumn<String[], String> tableColumnFio = new TableColumn<>("ФИО");
        tableColumnFio.setPrefWidth(120);
        tableColumnFio.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));
        TableColumn<String[], String> tableColumnIdDopusk = new TableColumn<>("ID");
        tableColumnIdDopusk.setPrefWidth(50);
        tableColumnIdDopusk.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[2]));
        tblGraf.getColumns().addAll(tableColumnRank, tableColumnFio, tableColumnIdDopusk);
    }

    private static String toHex(Color c) {
        return "#" + Integer.toHexString(c.hashCode());
    }

    /**
     * процедура создания в таблице графика дней с элементами управления
     */
    private void createClmnDays() {
        //вывод при смене года/месяца
        int clmnCount = tblGraf.getColumns().size();
        tblGraf.getColumns().remove(3, clmnCount); //очищаем таблицу кроме колонок с информацией
        for (int i = 1; i <= daysInMonth; i++) {
            TableColumn<String[], String> tableColumn = new TableColumn<>("" + i);
            int finalI = i;
            tableColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[finalI + 2]));
            tableColumn.prefWidthProperty().bind(tblGraf.widthProperty().divide(36));
            tableColumn.setStyle( "-fx-alignment: CENTER;");
            Date date = sqlDate(String.valueOf(finalI));
            switch (date.toLocalDate().getDayOfWeek().getValue()) {
                case (6):
                    tableColumn.setStyle( "-fx-alignment: CENTER;" +
                            "-fx-background-color:" +  toHex(Color.hsb(140.0, 0.5, 0.5, 0.1)));
                    break;
                case (7):
                    tableColumn.setStyle( "-fx-alignment: CENTER;" +
                            "-fx-background-color:" +  toHex(Color.hsb(240.0, 1.0, 1.0, 0.2)));
                    break;
            }
            tblGraf.getColumns().add(tableColumn);
        }
    }

    private ObservableList<String[]> buildArrayWork() {
        String lastRank;
        data.clear();
        for (Dopusk dopusk : DopuskModel.getAllByStatePostId(cmbBoxPost.getSelectionModel().getSelectedItem().getId())){
            String[] kit = new String[daysInMonth + 3];
            RankPerson rankPerson = RankPersonModel.getLastRankByIdPerson(dopusk.getPersona().getId());
            if (rankPerson == null) lastRank = "не введен";
            else lastRank = rankPerson.getRank().toString();
            kit[0] = lastRank;
            kit[1] = dopusk.getPersona().getFio();
            kit[2] = String.valueOf(dopusk.getId());
            for (int i = 0; i < daysInMonth; i++) {
                kit[i + 3] = "";
            }
            for (WorkDay work : WorkDayModel.getByIdDopusk(dopusk.getId())) {
                LocalDate date = work.getDateWork().toLocalDate();
                if (date.getMonthValue() == cmbBoxMonth.getSelectionModel().getSelectedIndex() + 1 &&
                date.getYear() == cmbBoxYear.getSelectionModel().getSelectedItem()) {
                    kit[date.getDayOfMonth() + 2] = "д";
                }
            }
            data.add(kit);
        }
        return data;
    }



}

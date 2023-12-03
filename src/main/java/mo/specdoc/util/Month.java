/*
 * Copyright (c) 2022
 */

package mo.specdoc.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Month {
    private String monthName;
    private int monthNumber;
    private ObservableList<Month> months = FXCollections.observableArrayList();

    public Month(String monthName, int monthNumber) {
        this.monthName = monthName;
        this.monthNumber = monthNumber;
    }

    public Month() {
    }

    /**
     * Функция формирования списка месяцев для ComboBox
     * @return коллекция ObservableList объектов Month
     */
    public ObservableList<Month> createListMonths() {
        String[] names = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
                "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

        for (int i = 0; i < 12; i++) {
            Month month = new Month(names[i], i);
            months.add(month);
        }
        return months;
    }

    @Override
    public String toString() {
        return monthName;
    }
}

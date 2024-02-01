/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.specdoc.util;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * Класс проверки ввода в TextField.
 * @autor Pavel Stepantsov
 * @version 1.0
 */
public class ValidatorTextField {
    public ValidatorTextField() {}
    /**
     * Функция получения значения поля {@link ValidatorTextField#validate}
     * @param textField - поле TextField
     * @param limit - лимит ввода
     * @param rus - разрешен ввод кириллицы
     * @param eng - разрешен ввод английских символов
     * @param numeric - разрешен числовой ввод
     * @param symbol - разрешен ввод спецсимоволов и пробела
     */
    public void validate (TextField textField, int limit, boolean rus, boolean eng, boolean numeric, boolean symbol) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    if (rus && !eng && !numeric && !symbol) {
                        textField.setText(newValue.replaceAll("[A-aZ-z0-9 \\%/\\\\&\\?\\,\\'\\;:!\\-\\+!@#\\$\\^*\\=]", ""));
                    }
                    if (rus && eng && !numeric && !symbol) {
                        textField.setText(newValue.replaceAll("[0-9 \\%/\\\\&\\?\\,\\'\\;:!\\-\\+!@#\\$\\^*\\=]", ""));
                    }
                    if (rus && eng && numeric && !symbol) {
                        textField.setText(newValue.replaceAll("[ \\%/\\\\&\\?\\,\\'\\;:!\\-\\+!@#\\$\\^*\\=]", ""));
                    }
                    if (rus && !eng && numeric && !symbol) {
                        textField.setText(newValue.replaceAll("[A-aZ-z \\%/\\\\&\\?\\,\\'\\;:!\\-\\+!@#\\$\\^*\\=]", ""));
                    }
                    if (rus && !eng && numeric && symbol) {
                        textField.setText(newValue.replaceAll("[A-aZ-z]", ""));
                    }
                    if (rus && !eng && !numeric && symbol) {
                        textField.setText(newValue.replaceAll("[A-aZ-z0-9]", ""));
                    }
                    if (!rus && !eng && !numeric && symbol) {
                        textField.setText(newValue.replaceAll("[A-aZ-zА-аЯ-я0-9]", ""));
                    }
                    if (!rus && !eng && numeric && symbol) {
                        textField.setText(newValue.replaceAll("[A-aZ-zА-аЯ-я]", ""));
                    }
                    if (!rus && !eng && numeric && !symbol) {
                        textField.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                    if (!rus && eng && !numeric && !symbol) {
                        textField.setText(newValue.replaceAll("[А-аЯ-я0-9 \\%/\\\\&\\?\\,\\'\\;:!\\-\\+!@#\\$\\^*\\=]", ""));
                    }
                    if (!rus && eng && numeric && !symbol) {
                        textField.setText(newValue.replaceAll("[А-аЯ-я \\%/\\\\&\\?\\,\\'\\;:!\\-\\+!@#\\$\\^*\\=]", ""));
                    }
                    if (textField.getText().length() > limit) {
                        String s = textField.getText().substring(0, limit);
                        textField.setText(s);
                    }
                }
            }
        });
    }
}

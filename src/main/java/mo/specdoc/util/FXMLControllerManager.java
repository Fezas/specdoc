/*
 * Copyright (c) 2023
 */

package mo.specdoc.util;

import lombok.Data;
import mo.specdoc.controllers.*;
/**
 * Класс менеджер контроллеров FXML форм в реализации паттерна singleton.
 * @autor Fezas
 * @version 0.1
 *  */
@Data
public class FXMLControllerManager {
    private final static FXMLControllerManager instance = new FXMLControllerManager();
    public static FXMLControllerManager getInstance() {
        return instance;
    }
    // контроллеры------------------------------------------------------------------------------------------------------
    private PersonEditController personEditController;
    private SecrecyEditController secrecyEditController;
    private MainController mainController;
    private PersonsViewController personsViewController;
    private StateController positionController;
    private RankEditController rankEditController;
    private DopuskController dopuskController;

}

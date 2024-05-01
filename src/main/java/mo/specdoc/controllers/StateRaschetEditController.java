package mo.specdoc.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mo.specdoc.entity.State;
import mo.specdoc.model.StateModel;

import java.net.URL;
import java.util.ResourceBundle;
import mo.specdoc.util.FXMLControllerManager;

public class StateRaschetEditController implements Initializable {
    private State currentState, stateParent;
    @FXML    private Button btnSave, btnCancel;
    @FXML    private TextField tfieldTitle, tfieldTitleRP, tfieldTitleDP, tfieldTitleShort, tfieldSortValue;


    @FXML
    public void saveAction() {
        try {
            currentState.setTitleState(tfieldTitle.getText());
            currentState.setTitleStateRp(tfieldTitleRP.getText());
            currentState.setTitleStateDp(tfieldTitleDP.getText());
            currentState.setTitleStateShort(tfieldTitleShort.getText());
            currentState.setSortValue(Integer.parseInt(tfieldSortValue.getText()));
            currentState.setStateParent(FXMLControllerManager.getInstance().getStateController().getCurrentParentState());
            currentState.setTypeState(5);
            StateModel.saveOrUpdate(currentState);
            FXMLControllerManager.getInstance().getStateController().refresh();
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void close() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            currentState = FXMLControllerManager.getInstance().getStateController().getCurrentState();
            if (currentState.getIdState() != 0L) {
                tfieldTitle.setText(currentState.getTitleState());
                tfieldTitleRP.setText(currentState.getTitleStateRp());
                tfieldTitleDP.setText(currentState.getTitleStateDp());
                tfieldTitleShort.setText(currentState.getTitleStateShort());
                tfieldSortValue.setText(String.valueOf(currentState.getSortValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

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

public class StateSubdivEditController implements Initializable {
    private StateController positionController;
    private State currentState;
    private long id;
    @FXML    private Button btnSave, btnCancel;
    @FXML    private TextField tfieldTitle, tfieldTitleRP, tfieldTitleDP, tfieldTitleShort, tfieldSortValue;

    public StateSubdivEditController(State state, long id) {
        this.currentState = state;
        this.id = id;
    }

    public void setParent (StateController controller){
        this.positionController = controller;
    }

    @FXML
    public void saveAction() {
        try {
            currentState.setTitleState(tfieldTitle.getText());
            currentState.setTitleStateRp(tfieldTitleRP.getText());
            currentState.setTitleStateDp(tfieldTitleDP.getText());
            currentState.setTitleStateShort(tfieldTitleShort.getText());
            currentState.setSortValue(Integer.parseInt(tfieldSortValue.getText()));
            currentState.setParentIdState(id);
            currentState.setTypeState(3);
            StateModel.saveOrUpdate(currentState);
            positionController.refresh();
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

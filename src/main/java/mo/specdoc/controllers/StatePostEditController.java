package mo.specdoc.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mo.specdoc.entity.SecrecyType;
import mo.specdoc.entity.State;
import mo.specdoc.model.SecrecyTypeModel;
import mo.specdoc.model.StateModel;
import mo.specdoc.util.ValidatorTextField;
import org.controlsfx.control.ToggleSwitch;

import java.net.URL;
import java.util.ResourceBundle;
import mo.specdoc.util.FXMLControllerManager;

public class StatePostEditController implements Initializable {
    private State currentState, stateParent;
    @FXML    private Button btnSave, btnCancel;
    @FXML    private TextField tfieldTitle, tfieldTitleRP, tfieldTitlePP, tfieldTitleShort,
            tfieldPostNumb, tfieldSortValue;
    @FXML    private ToggleSwitch tglSwitchArmed, tglSwitchAmplif;
    @FXML    private ComboBox<SecrecyType> cmbBoxSecrecyType;


    @FXML
    public void saveAction(ActionEvent event) {
        currentState.setTitleState(tfieldTitle.getText());
        currentState.setTitleStateRp(tfieldTitleRP.getText());
        currentState.setTitleStatePp(tfieldTitlePP.getText());
        currentState.setTitleStateShort(tfieldTitleShort.getText());
        currentState.setPostNumb(tfieldPostNumb.getText());
        currentState.setSecrecyType(cmbBoxSecrecyType.getSelectionModel().getSelectedItem());
        currentState.setPostIsAmplification(tglSwitchAmplif.isSelected());
        currentState.setPostArmed(tglSwitchArmed.isSelected());
        currentState.setStateParent(FXMLControllerManager.getInstance().getStateController().getCurrentParentState());
        currentState.setSortValue(Integer.parseInt(tfieldSortValue.getText()));
        currentState.setTypeState(6);
        StateModel.saveOrUpdate(currentState);
        FXMLControllerManager.getInstance().getStateController().refresh();
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void cancelAction(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ValidatorTextField validador = new ValidatorTextField();
        validador.validate(tfieldTitle, 200, true, false, true, true);
        validador.validate(tfieldTitleRP, 200, true, false, true, true);
        validador.validate(tfieldTitlePP, 200, true, false, true, true);
        validador.validate(tfieldTitleShort, 10, true, false, true, true);
        validador.validate(tfieldPostNumb, 15, true, false, true, true);
        validador.validate(tfieldSortValue, 6, false, false, true, false);
        try {
            currentState = FXMLControllerManager.getInstance().getStateController().getCurrentState();
            cmbBoxSecrecyType.getItems().addAll(SecrecyTypeModel.getAllRecords());
            if (currentState.getIdState() != 0L) { //режим редактирования
                tfieldTitle.setText(currentState.getTitleState());
                tfieldTitleRP.setText(currentState.getTitleStateRp());
                tfieldTitlePP.setText(currentState.getTitleStatePp());
                tfieldTitleShort.setText(currentState.getTitleStateShort());
                tfieldPostNumb.setText(currentState.getPostNumb());
                tfieldSortValue.setText(String.valueOf(currentState.getSortValue()));
                cmbBoxSecrecyType.getSelectionModel().select(currentState.getSecrecyType());
                tglSwitchAmplif.setSelected(currentState.isPostIsAmplification());
                tglSwitchArmed.setSelected(currentState.isPostArmed());
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

    }
}

package mo.specdoc.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mo.specdoc.entity.Position;
import mo.specdoc.model.PositionModel;

import java.net.URL;
import java.util.ResourceBundle;

public class PositionEditController implements Initializable {
    private PositionController positionController;
    private Position currentPosition;
    private long id;
    @FXML    private Button btnSave, btnCancel;
    @FXML    private TextField tfieldTitle, tfieldTitleRP, tfieldTitleDP, tfieldTitleShort, tfVus, tfAddress;
    @FXML    private CheckBox chkBoxIsSubdvs;

    @FXML    private CheckBox chkBoxIsCard;

    public PositionEditController(Position position, long id) {
        this.currentPosition = position;
        this.id = id;
    }

    public void setParent (PositionController controller){
        this.positionController = controller;
    }

    @FXML
    public void saveAction() {
        try {
            currentPosition.setTitle(tfieldTitle.getText());
            currentPosition.setTitleRp(tfieldTitleRP.getText());
            currentPosition.setTitleDp(tfieldTitleDP.getText());
            currentPosition.setTitleShort(tfieldTitleShort.getText());
            currentPosition.setIdParentPosition(id);
            currentPosition.setCard(chkBoxIsCard.isSelected());
            currentPosition.setVus(tfVus.getText());
            currentPosition.setSubdiv(chkBoxIsSubdvs.isSelected());
            currentPosition.setAddress(tfAddress.getText());
            PositionModel.saveOrUpdate(currentPosition);
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
            chkBoxIsCard.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    chkBoxIsSubdvs.setSelected(!newValue);
                    tfAddress.setDisable(true);
                    tfVus.setDisable(false);
                }
            });
            chkBoxIsSubdvs.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    chkBoxIsCard.setSelected(!newValue);
                    tfAddress.setDisable(false);
                    tfVus.setDisable(true);
                }
            });
            if (currentPosition.getId() != 0L) {
                tfieldTitle.setText(currentPosition.getTitle());
                tfieldTitleRP.setText(currentPosition.getTitleRp());
                tfieldTitleDP.setText(currentPosition.getTitleDp());
                tfieldTitleShort.setText(currentPosition.getTitleShort());
                if (currentPosition.isCard()) {
                    tfVus.setText(currentPosition.getVus());
                    chkBoxIsCard.setSelected(true);
                }
                if (currentPosition.isSubdiv()) {
                    chkBoxIsSubdvs.setSelected(true);
                    tfAddress.setText(currentPosition.getAddress());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

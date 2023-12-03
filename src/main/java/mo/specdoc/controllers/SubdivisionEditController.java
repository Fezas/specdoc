package mo.specdoc.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mo.specdoc.entity.Subdivision;
import mo.specdoc.model.SubdivisionModel;

import java.net.URL;
import java.util.ResourceBundle;

public class SubdivisionEditController implements Initializable {
    private final Subdivision currentEntry;
    private SubdivisionController subdivisionController = SubdivisionController.getInstance();
    @FXML    private Button btnSave, btnCancel;
    @FXML    private TextField tfieldTitle, tfieldTitleRP, tfieldTitlePP, tfieldTitleShort, tfieldTitleConditional;

    public SubdivisionEditController(Subdivision currentEntry) {
        this.currentEntry = currentEntry;
    }


    @FXML
    public void saveAction(ActionEvent event) {
        currentEntry.setTitle(tfieldTitle.getText());
        currentEntry.setTitleRp(tfieldTitleRP.getText());
        currentEntry.setTitlePp(tfieldTitlePP.getText());
        currentEntry.setTitleShort(tfieldTitleShort.getText());
        currentEntry.setConditionalNumb(tfieldTitleConditional.getText());
        SubdivisionModel.saveOrUpdate(currentEntry);
        subdivisionController.initalizeData();
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
        try {
            if (currentEntry.getId() != 0L) {
                tfieldTitle.setText(currentEntry.getTitle());
                tfieldTitleRP.setText(currentEntry.getTitleRp());
                tfieldTitlePP.setText(currentEntry.getTitlePp());
                tfieldTitleShort.setText(currentEntry.getTitleShort());
                tfieldTitleConditional.setText(currentEntry.getConditionalNumb());
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

    }
}

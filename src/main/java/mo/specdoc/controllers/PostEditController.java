package mo.specdoc.controllers;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mo.specdoc.entity.Post;
import mo.specdoc.entity.SecrecyType;
import mo.specdoc.model.PostModel;
import mo.specdoc.model.SecrecyTypeModel;
import org.controlsfx.control.ToggleSwitch;
import javafx.beans.value.ObservableValue;

import java.net.URL;
import java.util.ResourceBundle;

public class PostEditController implements Initializable {
    private PostController postController;
    private Post post;
    private long id;
    @FXML    private Button btnSave, btnCancel;
    @FXML    private TextField tfieldTitle, tfieldTitleRP, tfieldTitlePP, tfieldTitleShort,
            tfieldTitleConditional, tfieldSortValue;
    @FXML    private ToggleSwitch tglSwitchArmed;
    @FXML    private ComboBox<SecrecyType> cmbBoxSecrecyType;
    @FXML    private CheckBox chkBoxIsPost, chkBoxAmp;

    public PostEditController(Post post, long id) {
        this.post = post;
        this.id = id;
    }

    public void setParent (PostController controller){
        this.postController = controller;
    }

    @FXML
    public void saveAction(ActionEvent event) {
        post.setTitle(tfieldTitle.getText());
        post.setTitleRp(tfieldTitleRP.getText());
        post.setTitlePp(tfieldTitlePP.getText());
        post.setTitleShort(tfieldTitleShort.getText());
        if (chkBoxIsPost.isSelected()) {
            post.setConditionalNumb(tfieldTitleConditional.getText());
            post.setIsCard(true);
            post.setArmed(tglSwitchArmed.isSelected());
            if (tfieldSortValue.getText().trim().isEmpty()) {
                post.setSortValue(0);
            } else post.setSortValue(Integer.parseInt(tfieldSortValue.getText()));
        } else {
            post.setIsCard(false);
        }
        post.setSecrecy(cmbBoxSecrecyType.getSelectionModel().getSelectedItem());
        if (chkBoxAmp.isSelected()) {
            post.setAmplification(true);
        } else post.setAmplification(false);
        post.setIdParentPost(id);
        PostModel.saveOrUpdate(post);
        postController.refresh();
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
            cmbBoxSecrecyType.getItems().addAll(SecrecyTypeModel.getAllRecords());
            //включаем поля для заполнения если это карточка боевого поста
            chkBoxIsPost.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue ov,Boolean old_val, Boolean new_val) {
                    tfieldTitleConditional.setDisable(old_val);
                    tfieldSortValue.setDisable(old_val);
                    cmbBoxSecrecyType.setDisable(old_val);
                    tglSwitchArmed.setDisable(old_val);
                }
            });
            if (post.getId() != 0L) { //режим редактирования
                tfieldTitle.setText(post.getTitle());
                tfieldTitleRP.setText(post.getTitleRp());
                tfieldTitlePP.setText(post.getTitlePp());
                tfieldTitleShort.setText(post.getTitleShort());
                if (post.getIsCard()) {
                    chkBoxIsPost.setSelected(true);
                    tfieldTitleConditional.setText(post.getConditionalNumb());
                    tfieldSortValue.setText(post.getSortValue().toString());
                    cmbBoxSecrecyType.getSelectionModel().select(post.getSecrecy());
                    tglSwitchArmed.setSelected(post.isArmed());
                } else {
                    cmbBoxSecrecyType.getSelectionModel().selectFirst();
                }
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

    }
}

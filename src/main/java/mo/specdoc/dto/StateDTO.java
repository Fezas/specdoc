/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.specdoc.dto;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import lombok.Data;
import mo.specdoc.entity.Persona;
import mo.specdoc.entity.Position;
import mo.specdoc.entity.State;

@Data
public class StateDTO {
    private String title;
    public Button btnAddNode;
    public Button btnEdit;
    public Button btnDelete;
    public CheckBox remark;
    private Persona persona;
    private State state;
    public HBox boxBtn;
    public HBox info;

    public StateDTO() {
        this.remark = new CheckBox();
        this.boxBtn = new HBox();
        this.info = new HBox();
        boxBtn.setSpacing(2);
        info.setSpacing(2);
    }


    @Override
    public String toString() {
        return title;
    }
}

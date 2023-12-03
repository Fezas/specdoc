/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.specdoc.dto;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import lombok.Data;
import mo.specdoc.entity.Post;

@Data
public class Raschet {
    public String title;
    public CheckBox remark;
    private Post post;
    private HBox boxBtn;
    public HBox info;

    public Raschet(Post post) {
        this.post = post;
        this.remark = new CheckBox();
        this.boxBtn = new HBox();
        this.info = new HBox();
        boxBtn.setSpacing(2);
        info.setSpacing(2);
    }

    public Raschet() {
    }


    @Override
    public String toString() {
        return title;
    }
}

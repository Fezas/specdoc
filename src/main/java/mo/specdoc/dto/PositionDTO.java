/*
 * Copyright (c) 2023
 */

package mo.specdoc.dto;

import javafx.scene.control.CheckBox;
import lombok.Data;
import mo.specdoc.entity.Position;
import mo.specdoc.entity.Post;

import java.sql.Timestamp;

@Data
public class PositionDTO {
    private Position position;
    private String title;
    private String titleWithStructure;
    private CheckBox checked;
    private String docFixPost;
    private Timestamp dateFixPosition;


    public PositionDTO() {
        this.checked = new CheckBox();
    }
}

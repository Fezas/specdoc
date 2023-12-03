/*
 * Copyright (c) 2023
 */

package mo.specdoc.entity;

import jakarta.persistence.*;
import javafx.scene.control.CheckBox;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;
import java.util.Objects;
@Data
@Entity
@Table(name = "POST", schema = "PUBLIC", catalog = "SPECDOC")
public class Post {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    private long id;
    @Basic
    @Column(name = "TITLE")
    private String title;
    @Basic
    @Column(name = "TITLE_RP")
    private String titleRp;
    @Basic
    @Column(name = "TITLE_PP")
    private String titlePp;
    @Basic
    @Column(name = "TITLE_SHORT")
    private String titleShort;
    @Basic
    @Column(name = "CONDITIONAL_NUMB")
    private String conditionalNumb;
    @Basic
    @Column(name = "SORT_VALUE")
    private Integer sortValue;
    @Basic
    @Column(name = "ARMED")
    private boolean armed;
    @Basic
    @Column(name = "ID_PARENT_POST")
    private Long idParentPost;
    @Basic
    @Column(name = "AMPLIFICATION")
    private Boolean amplification;
    @Basic
    @Column(name = "IS_CARD")
    private Boolean isCard;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_SECRECY_TYPE", nullable = true)
    private SecrecyType secrecy;

    @OneToMany(mappedBy="post")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Dopusk> dopusks;

    @Transient
    private String titleWithStructure;

    @Transient
    private CheckBox checked;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id && armed == post.armed && Objects.equals(title, post.title) && Objects.equals(titleRp, post.titleRp) && Objects.equals(titlePp, post.titlePp) && Objects.equals(titleShort, post.titleShort) && Objects.equals(conditionalNumb, post.conditionalNumb) && Objects.equals(sortValue, post.sortValue) && Objects.equals(idParentPost, post.idParentPost) && Objects.equals(amplification, post.amplification)  && Objects.equals(isCard, post.isCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, titleRp, titlePp, titleShort, conditionalNumb, sortValue, armed, idParentPost, amplification, isCard);
    }

    @Override
    public String toString() {
        return  title ;
    }
}

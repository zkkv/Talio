package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;

    private int red;
    private int green;
    private int blue;

    @ManyToMany(mappedBy = "tags")
    private List<Card> cards;

    @SuppressWarnings("unused")
    public Tag() {
        // for object mapper
    }

    /**
     * Tag constructor
     *
     * @param title text displayed for the tag
     * @param red   red color component
     * @param green green color component
     * @param blue  blue color component
     * @param cards list of cards having this tag
     * @author      Kirill Zhankov
     */
    public Tag(String title, int red, int green, int blue, List<Card> cards) {
        this.title = title;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.cards = cards;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }
}

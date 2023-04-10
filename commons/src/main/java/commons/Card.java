package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;

    @Column(length = 5000)
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SubTask> subTasks;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Tag> tags;

    public Card(){
        subTasks = new ArrayList<>();
        this.description = "";
    }
    public Card(String title,List<Tag> tags,List<SubTask> subTasks){
        this.title = title;
        this.subTasks = subTasks;
        this.tags = tags;
        this.description = "";
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, new String[0]);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, new String[0]);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean hasDescription() {
        return !description.equals(" ") && !description.equals("");
    }

    public List<SubTask> getTasks() {
        return subTasks;
    }

    public void setTasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
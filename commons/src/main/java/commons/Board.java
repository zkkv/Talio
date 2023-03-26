package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Board {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long key;

    @OneToMany(cascade=CascadeType.ALL)
    private List<CardList> cardLists;

    public Board(){

    }

    public Board(List<CardList> cardLists){
        this.cardLists = cardLists;
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

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public List<CardList> getCardLists() {
        return cardLists;
    }

    public void setCardLists(List<CardList> cardLists) {
        this.cardLists = cardLists;
    }
}

package server.database;

import commons.Card;

import commons.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Tag> findTagsById(long cardId);
}

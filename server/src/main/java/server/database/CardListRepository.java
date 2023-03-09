package server.database;

import commons.CardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardListRepository extends JpaRepository<CardList, Long> {}

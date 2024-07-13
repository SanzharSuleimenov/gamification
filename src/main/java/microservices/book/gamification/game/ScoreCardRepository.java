package microservices.book.gamification.game;

import java.util.List;
import java.util.Optional;
import microservices.book.gamification.game.domain.LeaderBoardRow;
import microservices.book.gamification.game.domain.ScoreCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ScoreCardRepository extends CrudRepository<ScoreCard, Long> {

  @Query("SELECT SUM(s.score) FROM ScoreCard s WHERE s.userId = :userId GROUP BY s.userId")
  Optional<Integer> getTotalScoreForUser(@Param("userId") Long userId);

  @Query("SELECT NEW microservices.book.gamification.game.domain.LeaderBoardRow(s.userId, SUM(s.score)) "
      + "FROM ScoreCard s "
      + "GROUP BY s.userId ORDER BY SUM(s.score) DESC")
  List<LeaderBoardRow> findFirst10();

  List<ScoreCard> findByUserIdOrderByScoreTimestampDesc(Long userId);

  @Query("SELECT s.userId, SUM(s.score) FROM ScoreCard s GROUP BY s.userId")
  List<ScoreCard> getAllWithTotalScorePerUser();
}

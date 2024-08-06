package microservices.book.gamification.game;

import java.util.List;
import microservices.book.gamification.challenge.ChallengeSolvedEvent;
import microservices.book.gamification.game.domain.BadgeType;

public interface GameService {

  /**
   * Process a new attempt from a given user.
   *
   * @param challenge the challenge data with user details, factors, etc.
   * @return a {@link GameResult} object containing the new score and badge card obtained
   */
  GameResult newAttemptForUser(ChallengeSolvedEvent challenge);

  record GameResult(int score, List<BadgeType> badges) {

  }
}

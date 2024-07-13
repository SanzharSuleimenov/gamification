package microservices.book.gamification.game.badgeprocessors;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import microservices.book.gamification.game.domain.BadgeType;
import microservices.book.gamification.game.domain.ScoreCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FirstWonBadgeProcessorTest {

  private BadgeProcessor badgeProcessor;

  @BeforeEach
  public void setUp() {
    badgeProcessor = new FirstWonBadgeProcessor();
  }

  @Test
  public void shouldGiveBadgeIfFirstWonScore() {
    Optional<BadgeType> badgeType = badgeProcessor
        .processForOptionalBadge(10, List.of(new ScoreCard(1L, 1L)), null);
    assertThat(badgeType).contains(BadgeType.FIRST_WON);
  }

  @Test
  public void shouldNotGiveBadgeIfSecondWonScore() {
    Optional<BadgeType> badgeType = badgeProcessor
        .processForOptionalBadge(20,
            List.of(new ScoreCard(1L, 1L), new ScoreCard(1L, 2L)),
            null);
    assertThat(badgeType).isEmpty();
  }
}

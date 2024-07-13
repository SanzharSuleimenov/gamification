package microservices.book.gamification.game.badgeprocessors;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import microservices.book.gamification.game.domain.BadgeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BronzeBadgeProcessorTest {

  private BronzeBadgeProcessor badgeProcessor;

  @BeforeEach
  public void setUp() {
    badgeProcessor = new BronzeBadgeProcessor();
  }

  @Test
  public void shouldGiveBadgeIfScoreAboveThreshold() {
    Optional<BadgeType> badgeType = badgeProcessor.processForOptionalBadge(60, List.of(), null);
    assertThat(badgeType).contains(BadgeType.BRONZE);
  }

  @Test
  public void shouldNotGiveBadgeIfScoreBelowThreshold() {
    Optional<BadgeType> badgeType = badgeProcessor.processForOptionalBadge(50, List.of(), null);
    assertThat(badgeType).isEmpty();
  }
}

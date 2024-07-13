package microservices.book.gamification.game.badgeprocessors;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import microservices.book.gamification.game.domain.BadgeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GoldBadgeProcessorTest {

  private BadgeProcessor badgeProcessor;

  @BeforeEach
  public void setUp() {
    badgeProcessor = new GoldBadgeProcessor();
  }

  @Test
  public void shouldGiveBadgeWhenScoreAboveThreshold() {
    Optional<BadgeType> badgeType = badgeProcessor.processForOptionalBadge(410, List.of(), null);
    assertThat(badgeType).contains(BadgeType.GOLD);
  }

  @Test
  public void shouldNotGiveBadgeWhenScoreBelowThreshold() {
    Optional<BadgeType> badgeType = badgeProcessor.processForOptionalBadge(400, List.of(), null);
    assertThat(badgeType).isEmpty();
  }
}

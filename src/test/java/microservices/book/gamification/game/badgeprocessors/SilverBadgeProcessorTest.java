package microservices.book.gamification.game.badgeprocessors;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import microservices.book.gamification.game.domain.BadgeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SilverBadgeProcessorTest {

  private BadgeProcessor badgeProcessor;

  @BeforeEach
  public void setUp() {
    badgeProcessor = new SilverBadgeProcessor();
  }

  @Test
  public void shouldGiveBadgeWhenAboveThreshold() {
    Optional<BadgeType> badgeType = badgeProcessor.processForOptionalBadge(160, List.of(), null);
    assertThat(badgeType).contains(BadgeType.SILVER);
  }

  @Test
  public void shouldNotGiveBadgeWhenBelowThreshold() {
    Optional<BadgeType> badgeType = badgeProcessor.processForOptionalBadge(150, List.of(), null);
    assertThat(badgeType).isEmpty();
  }

}

package microservices.book.gamification.game.badgeprocessors;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import microservices.book.gamification.challenge.ChallengeSolvedEvent;
import microservices.book.gamification.game.domain.BadgeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LuckyNumberBadgeProcessorTest {

  private BadgeProcessor badgeProcessor;

  @BeforeEach
  public void setUp() {
    badgeProcessor = new LuckyNumberBadgeProcessor();
  }

  @Test
  public void shouldGiveBadgeWhenLuckyNumbersMet() {
    Optional<BadgeType> badgeType = badgeProcessor.processForOptionalBadge(10, List.of(),
        new ChallengeSolvedEvent(1L, true, 42, 1, 1L, null));
    assertThat(badgeType).contains(BadgeType.LUCKY_NUMBER);
  }

  @Test
  public void shouldNotGiveBadgeWhenLuckyNumbersNotMet() {
    Optional<BadgeType> badgeType = badgeProcessor.processForOptionalBadge(10, List.of(),
        new ChallengeSolvedEvent(1L, true, 43, 12, 1L, null));
    assertThat(badgeType).isEmpty();
  }
}

package microservices.book.gamification.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import microservices.book.gamification.challenge.ChallengeSolvedEvent;
import microservices.book.gamification.game.GameService.GameResult;
import microservices.book.gamification.game.badgeprocessors.BadgeProcessor;
import microservices.book.gamification.game.domain.BadgeCard;
import microservices.book.gamification.game.domain.BadgeType;
import microservices.book.gamification.game.domain.ScoreCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

  GameService gameService;

  @Mock
  BadgeCardRepository badgeCardRepository;
  @Mock
  ScoreCardRepository scoreCardRepository;
  @Mock
  BadgeProcessor badgeProcessor;

  @BeforeEach
  void setUp() {
    gameService = new GameServiceImpl(scoreCardRepository, badgeCardRepository,
        List.of(badgeProcessor));
  }

  @Test
  void testCorrectAttemptForUser() {
    // given
    Long userId = 100L, attemptId = 1L;
    ChallengeSolvedEvent attempt = new ChallengeSolvedEvent(attemptId, true, 1, 2, userId,
        "jackie");
    ScoreCard exptectedScoreCard = new ScoreCard(userId, attemptId);
    given(scoreCardRepository.getTotalScoreForUser(userId))
        .willReturn(Optional.of(10));
    given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
        .willReturn(List.of(exptectedScoreCard));
    given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
        .willReturn(List.of(new BadgeCard(userId, BadgeType.FIRST_WON)));
    given(badgeProcessor.badgeType()).willReturn(BadgeType.LUCKY_NUMBER);
    given(badgeProcessor
        .processForOptionalBadge(10, List.of(exptectedScoreCard), attempt))
        .willReturn(Optional.of(BadgeType.LUCKY_NUMBER));

    // when
    GameResult result = gameService.newAttemptForUser(attempt);

    // then
    assertEquals(new GameResult(10, List.of(BadgeType.LUCKY_NUMBER)), result);
    verify(scoreCardRepository).save(exptectedScoreCard);
    verify(badgeCardRepository).saveAll(List.of(new BadgeCard(userId, BadgeType.LUCKY_NUMBER)));
  }

  @Test
  void testIncorrectAttemptForUser() {
    // given
    ChallengeSolvedEvent challengeSolvedEvent = new ChallengeSolvedEvent(2L, false, 1, 2, 100L, "jackie");

    // when
    GameResult result = gameService.newAttemptForUser(challengeSolvedEvent);

    // then
    assertEquals(new GameResult(0, List.of()), result);
  }
}
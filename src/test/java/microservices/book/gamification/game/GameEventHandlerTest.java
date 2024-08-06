package microservices.book.gamification.game;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import microservices.book.gamification.challenge.ChallengeSolvedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

@ExtendWith(MockitoExtension.class)
public class GameEventHandlerTest {

  @Mock
  private GameService gameService;

  private GameEventHandler gameEventHandler;

  @BeforeEach
  public void setUp() {
    gameEventHandler = new GameEventHandler(gameService);
  }

  @Test
  void verifyAttemptForUserMethodCalled() {
    // given
    ChallengeSolvedEvent challengeSolvedEvent = new ChallengeSolvedEvent(1L, true, 10, 30, 1L,
        "jackie");
    // when
    gameEventHandler.handleMultiplicationSolved(challengeSolvedEvent);
    // then
    verify(gameService).newAttemptForUser(challengeSolvedEvent);
  }

  @Test
  void verifyAmqpExceptionIsRethrownIfBusinessLogicCaughtAnException() {
    // given
    ChallengeSolvedEvent challengeSolvedEvent = new ChallengeSolvedEvent(1L, true, 10, 30, 1L,
        "jackie");
    given(gameService.newAttemptForUser(eq(challengeSolvedEvent)))
        .willThrow(RuntimeException.class);
    // when
    assertThrows(AmqpRejectAndDontRequeueException.class,
        () -> gameEventHandler.handleMultiplicationSolved(challengeSolvedEvent));
  }
}

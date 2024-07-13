package microservices.book.gamification.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import java.util.List;
import microservices.book.gamification.game.domain.BadgeCard;
import microservices.book.gamification.game.domain.BadgeType;
import microservices.book.gamification.game.domain.LeaderBoardRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeaderBoardServiceImplTest {

  LeaderBoardService leaderBoardService;

  @Mock
  ScoreCardRepository scoreCardRepository;
  @Mock
  BadgeCardRepository badgeCardRepository;

  @BeforeEach
  void setUp() {
    leaderBoardService = new LeaderBoardServiceImpl(scoreCardRepository, badgeCardRepository);
  }

  @Test
  void testGetLeaderBoard() {
    // given
    LeaderBoardRow leader1 = new LeaderBoardRow(1L, 30L);
    LeaderBoardRow leader2 = new LeaderBoardRow(2L, 20L);
    given(scoreCardRepository.findFirst10())
        .willReturn(List.of(leader1, leader2));
    given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(2L))
        .willReturn(List.of(new BadgeCard(2L, BadgeType.FIRST_WON)));
    given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(1L))
        .willReturn(List.of(new BadgeCard(1L, BadgeType.LUCKY_NUMBER),
            new BadgeCard(1L, BadgeType.FIRST_WON)));

    // when
    List<LeaderBoardRow> leaderBoard = leaderBoardService.getCurrentLeaderBoard();

    // then
    assertEquals(2, leaderBoard.size());
    assertEquals(leader1.getUserId(), leaderBoard.get(0).getUserId());
    assertEquals(leader1.getTotalScore(), leaderBoard.get(0).getTotalScore());
    assertThat(leaderBoard.get(0).getBadges())
        .hasSameElementsAs(
            List.of(BadgeType.LUCKY_NUMBER.getDescription(), BadgeType.FIRST_WON.getDescription()));
    assertEquals(leader2.getUserId(), leaderBoard.get(1).getUserId());
    assertEquals(leader2.getTotalScore(), leaderBoard.get(1).getTotalScore());
    assertThat(leaderBoard.get(1).getBadges()).hasSameElementsAs(
        List.of(BadgeType.FIRST_WON.getDescription()));
  }
}
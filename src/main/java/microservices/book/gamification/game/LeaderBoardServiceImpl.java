package microservices.book.gamification.game;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import microservices.book.gamification.game.domain.LeaderBoardRow;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeaderBoardServiceImpl implements LeaderBoardService {

  private final ScoreCardRepository scoreCardRepository;
  private final BadgeCardRepository badgeCardRepository;

  @Override
  public List<LeaderBoardRow> getCurrentLeaderBoard() {
    List<LeaderBoardRow> leaderBoardRows = scoreCardRepository.findFirst10();

    return leaderBoardRows.stream().map(row -> {
      List<String> badges =
          badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(row.getUserId())
              .stream()
              .map(b -> b.getBadgeType().getDescription())
              .collect(Collectors.toList());
      return row.withBadges(badges);
    }).toList();
  }
}

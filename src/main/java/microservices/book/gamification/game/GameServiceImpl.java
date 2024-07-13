package microservices.book.gamification.game;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.challenge.ChallengeSolvedDTO;
import microservices.book.gamification.game.badgeprocessors.BadgeProcessor;
import microservices.book.gamification.game.domain.BadgeCard;
import microservices.book.gamification.game.domain.BadgeType;
import microservices.book.gamification.game.domain.ScoreCard;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class GameServiceImpl implements GameService {

  private final ScoreCardRepository scoreCardRepository;
  private final BadgeCardRepository badgeCardRepository;
  private final List<BadgeProcessor> badgeProcessors;

  @Override
  public GameResult newAttemptForUser(ChallengeSolvedDTO challenge) {
    if (challenge.correct()) {
      ScoreCard scoreCard = new ScoreCard(challenge.userId(), challenge.attemptId());
      scoreCardRepository.save(scoreCard);
      log.info("User {} scored {} points for attempt id {}", challenge.userAlias(),
          challenge.attemptId(), challenge.userId());
      List<BadgeCard> badgeCards = processForBadges(challenge);

      return new GameResult(scoreCard.getScore(), badgeCards
          .stream().map(BadgeCard::getBadgeType).toList());
    } else {
      log.info("Attempt id {} is not correct. User {} doesn't get score.", challenge.attemptId(),
          challenge.userAlias());

      return new GameResult(0, List.of());
    }
  }

  private List<BadgeCard> processForBadges(final ChallengeSolvedDTO challenge) {
    Optional<Integer> optTotalScore = scoreCardRepository.getTotalScoreForUser(challenge.userId());
    if (optTotalScore.isEmpty()) {
      return List.of();
    }
    int totalScore = optTotalScore.get();
    // Gets the total score and existing badges for that user
    List<ScoreCard> scoreCardList = scoreCardRepository
        .findByUserIdOrderByScoreTimestampDesc(challenge.userId());
    Set<BadgeType> alreadyGotBadges = badgeCardRepository
        .findByUserIdOrderByBadgeTimestampDesc(challenge.userId())
        .stream()
        .map(BadgeCard::getBadgeType)
        .collect(Collectors.toSet());

    // Calls the badge processors for badges that the user doesn't have yet
    List<BadgeCard> newBadgeCards = badgeProcessors.stream()
        .filter(bp -> !alreadyGotBadges.contains(bp.badgeType()))
        .map(bp -> bp.processForOptionalBadge(totalScore, scoreCardList, challenge))
        .flatMap(Optional::stream) // returns an empty stream if empty
        // maps the optionals if present to new BadgeCards
        .map(badgeType ->
            new BadgeCard(challenge.userId(), badgeType)
        )
        .toList();

    badgeCardRepository.saveAll(newBadgeCards);

    return newBadgeCards;
  }
}

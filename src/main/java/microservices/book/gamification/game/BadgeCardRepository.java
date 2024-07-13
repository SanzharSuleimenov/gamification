package microservices.book.gamification.game;

import java.util.List;
import microservices.book.gamification.game.domain.BadgeCard;
import org.springframework.data.repository.CrudRepository;

public interface BadgeCardRepository extends CrudRepository<BadgeCard, Long> {

  List<BadgeCard> findByUserIdOrderByBadgeTimestampDesc(Long userId);
}

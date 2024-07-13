package microservices.book.gamification.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;
import microservices.book.gamification.game.domain.BadgeType;
import microservices.book.gamification.game.domain.LeaderBoardRow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(LeaderBoardController.class)
public class LeaderBoardControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LeaderBoardService leaderBoardService;

  @Autowired
  JacksonTester<List<LeaderBoardRow>> leaderBoardRowListJacksonTester;

  @Test
  void getLeaderBoard() throws Exception {
    // given
    LeaderBoardRow row1 = new LeaderBoardRow(1L, 60L)
        .withBadges(
            List.of(BadgeType.BRONZE.getDescription(), BadgeType.FIRST_WON.getDescription()));
    LeaderBoardRow row2 = new LeaderBoardRow(2L, 50L)
        .withBadges(
            List.of(BadgeType.FIRST_WON.getDescription()));
    given(leaderBoardService.getCurrentLeaderBoard()).willReturn(List.of(row1, row2));

    // when
    MockHttpServletResponse response = mockMvc.perform(get("/leaders"))
        .andReturn().getResponse();

    // then
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(response.getContentAsString(),
        leaderBoardRowListJacksonTester.write(List.of(row1, row2)).getJson());
  }
}

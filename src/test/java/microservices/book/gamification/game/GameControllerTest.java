package microservices.book.gamification.game;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;
import microservices.book.gamification.challenge.ChallengeSolvedDTO;
import microservices.book.gamification.game.GameService.GameResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(GameController.class)
public class GameControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  GameService gameService;

  @Autowired
  JacksonTester<ChallengeSolvedDTO> challengeSolvedDTOJacksonTester;
  @Autowired
  JacksonTester<GameResult> gameResultJacksonTester;

  @Test
  void postValidAttempt() throws Exception {
    ChallengeSolvedDTO challengeSolvedDTO = new ChallengeSolvedDTO(1L, true, 5, 10, 1L, "jackie");
    GameResult result = new GameResult(10, List.of());
    given(gameService.newAttemptForUser(eq(challengeSolvedDTO)))
        .willReturn(result);

    MockHttpServletResponse response = mockMvc.perform(
            post("/attempts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(challengeSolvedDTOJacksonTester.write(challengeSolvedDTO).getJson()))
        .andReturn()
        .getResponse();

    then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    verify(gameService).newAttemptForUser(challengeSolvedDTO);
  }
}

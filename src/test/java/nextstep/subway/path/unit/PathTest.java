package nextstep.subway.path.unit;

import nextstep.line.entity.Line;
import nextstep.member.domain.Member;
import nextstep.path.dto.Path;
import nextstep.path.exception.PathException;
import nextstep.section.entity.Section;
import nextstep.section.entity.Sections;
import nextstep.station.dto.StationResponse;
import nextstep.station.entity.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static nextstep.common.constant.ErrorCode.PATH_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class PathTest {

    Station 강남역;
    Station 역삼역;
    Section 강남역_역삼역_구간;
    Sections 구간들;
    Line 신분당선;
    Path path;

    Long 총_거리 = 10L;
    Long 총_시간 = 5L;
    Long 총_비용 = 1250L;

    Member 로그인_사용자_비할인대상;

    @BeforeEach
    public void setup() {
        강남역 = Station.of(1L, "강남역");
        역삼역 = Station.of(2L, "역삼역");

        강남역_역삼역_구간 = Section.of(강남역, 역삼역, 10L, 5L);
        구간들 = new Sections(Collections.singletonList(강남역_역삼역_구간));
        신분당선 = Line.of(1L, "신분당선", "red", 15L, 구간들);

        로그인_사용자_비할인대상 = Member.of(1L, "test@test.com", "password", 20);

        path = Path.of(로그인_사용자_비할인대상, List.of(신분당선), List.of(강남역, 역삼역), 구간들);
    }

    @DisplayName("getVertexList와 getWeight의 정상 동작을 확인한다.")
    @Test
    public void getVertexList_getWeight() {
        // then
        assertAll(
                () -> assertEquals(List.of(강남역, 역삼역), path.getStations()),
                () -> assertEquals(총_거리, path.getTotalDistance()),
                () -> assertEquals(총_시간, path.getTotalDuration()),
                () -> assertEquals(총_비용, path.getTotalPrice())
        );
    }

    @DisplayName("[createPathResponse] pathResponse를 생성한다.")
    @Test
    void createPathResponse_success() {
        // when
        var pathResponse = path.createPathResponse();

        // then
        assertAll(
                () -> assertNotNull(pathResponse),
                () -> assertEquals(총_거리, pathResponse.getTotalDistance()),
                () -> assertEquals(총_시간, pathResponse.getTotalDuration()),
                () -> assertEquals(총_비용, pathResponse.getTotalPrice()),
                () -> assertEquals(List.of(
                        StationResponse.of(강남역.getId(), 강남역.getName()),
                        StationResponse.of(역삼역.getId(), 역삼역.getName())
                ), pathResponse.getStationResponses())
        );
    }

    @DisplayName("[createPathResponse] path의 stationList가 비어 있으면 예외가 발생한다.")
    @Test
    void createPathResponse_fail1() {
        // given
        var path = Path.of(로그인_사용자_비할인대상, List.of(신분당선), List.of(), 구간들);

        // when & then
        assertThrows(PathException.class, () -> path.createPathResponse())
                .getMessage().equals(PATH_NOT_FOUND.getDescription());
    }

    @DisplayName("[calculateOverFare] distance가 0일 때, 1250원을 반환한다.")
    @Test
    void calculateOverFare_0() {
        var 거리 = 10L;
        var 예상_요금 = 1250L;
        var 요금 = Path.calculateOverFare(거리);
        assertAll(
                () -> assertThat(요금).isEqualTo(예상_요금)
        );
    }

    @DisplayName("[calculateOverFare] distance가 11일 때, 1350원을 반환한다.")
    @Test
    void calculateOverFare_11() {
        var 거리 = 11L;
        var 예상_요금 = 1350L;
        var 요금 = Path.calculateOverFare(거리);
        assertAll(
                () -> assertThat(요금).isEqualTo(예상_요금)
        );
    }

    @DisplayName("[calculateOverFare] distance가 21일 때, 1550원을 반환한다.")
    @Test
    void calculateOverFare_21() {
        var 거리 = 21L;
        var 예상_요금 = 1550L;
        var 요금 = Path.calculateOverFare(거리);
        assertAll(
                () -> assertThat(요금).isEqualTo(예상_요금)
        );
    }

    @DisplayName("[calculateOverFare] distance가 51일 때, 1850원을 반환한다.")
    @Test
    void calculateOverFare_51() {
        var 거리 = 51L;
        var 예상_요금 = 1850L;
        var 요금 = Path.calculateOverFare(거리);
        assertAll(
                () -> assertThat(요금).isEqualTo(예상_요금)
        );
    }
}


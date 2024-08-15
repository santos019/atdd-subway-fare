package nextstep.subway.path.unit;

import nextstep.path.dto.PathResponse;
import nextstep.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathResponseTest {

    @DisplayName("getStationResponseList와 getDistance를 실행시킨다.")
    @Test
    void getStationResponseList_getDistance() {
        // given
        var stationResponses = List.of(
                StationResponse.of(1L, "Station1"),
                StationResponse.of(2L, "Station2")
        );
        var 총_거리 = 20L;
        var 총_시간 = 10L;
        var 총_비용 = 1250L;

        // when
        var pathResponse = PathResponse.of(stationResponses, 총_거리, 총_시간, 총_비용);

        // then
        assertAll(
                () -> assertEquals(stationResponses, pathResponse.getStationResponses()),
                () -> assertEquals(총_거리, pathResponse.getTotalDistance()),
                () -> assertEquals(총_시간, pathResponse.getTotalDuration()),
                () -> assertEquals(총_비용, pathResponse.getTotalPrice())
        );
    }

}


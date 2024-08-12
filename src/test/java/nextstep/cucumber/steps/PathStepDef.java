package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import nextstep.cucumber.AcceptanceContext;
import nextstep.line.dto.LineResponse;
import nextstep.path.dto.PathResponse;
import nextstep.section.dto.SectionRequest;
import nextstep.section.dto.SectionResponse;
import nextstep.station.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.utils.step.LineStep.지하철_노선_생성;
import static nextstep.utils.step.PathStep.경로_조회_길이;
import static nextstep.utils.step.PathStep.경로_조회_시간;
import static nextstep.utils.step.SectionStep.지하철_구간_등록;
import static nextstep.utils.step.StationStep.지하철_역_등록;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PathStepDef implements En {
    @Autowired
    private AcceptanceContext context;

    public PathStepDef() {
        Given("지하철역들을 생성 요청하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            for (Map<String, String> params : maps) {
                StationResponse stationResponse = 지하철_역_등록(params.get("name"));

                context.store.put(stationResponse.getName(), stationResponse);
            }
        });

        And("지하철 노선들을 생성 요청하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            for (Map<String, String> params : maps) {
                StationResponse upStation = (StationResponse) context.store.get(params.get("upStation"));
                StationResponse downStation = (StationResponse) context.store.get(params.get("downStation"));

                LineResponse lineResponse = 지하철_노선_생성(params.get("name"), params.get("color"), upStation.getId(), downStation.getId(), Long.valueOf(params.get("distance")), Long.valueOf(params.get("duration")));

                context.store.put(params.get("name"), lineResponse);
            }
        });

        And("지하철 구간을 등록 요청하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            for (Map<String, String> params : maps) {
                LineResponse lineResponse = (LineResponse) context.store.get(params.get("lineName"));
                StationResponse upStation = (StationResponse) context.store.get(params.get("upStation"));
                StationResponse downStation = (StationResponse) context.store.get(params.get("downStation"));
                Long distance = Long.valueOf(params.get("distance"));
                Long duration = Long.valueOf(params.get("duration"));

                SectionResponse sectionResponse = 지하철_구간_등록(lineResponse.getId(), SectionRequest.of(upStation.getId(), downStation.getId(), distance, duration));
            }
        });

        When("{string}과 {string}의 경로를 조회하면", (String source, String target) -> {
            Long sourceId = ((StationResponse) context.store.get(source)).getId();
            Long targetId = ((StationResponse) context.store.get(target)).getId();

            PathResponse pathResponse = 경로_조회_길이(sourceId, targetId);
            context.store.put("path", pathResponse);
        });

        Then("{string} 경로가 조회된다", (String pathString) -> {
            List<String> split = List.of(pathString.split(","));
            PathResponse pathResponse = (PathResponse) context.store.get("path");
            List<String> actualPath = pathResponse.getStationResponses().stream().map(value -> value.getName()).collect(Collectors.toList());
            assertThat(actualPath).containsExactly(split.toArray(new String[0]));
        });

        When("{string}과 {string}의 경로를 최소 시간 기준으로 경로 조회하면", (String source, String target) -> {
            StationResponse sourceStation = (StationResponse) context.store.get(source);
            StationResponse targetStation = (StationResponse) context.store.get(target);

            PathResponse pathResponse = 경로_조회_시간(sourceStation.getId(), targetStation.getId());
            context.store.put("path_duration", pathResponse);
        });

        Then("최소 시간 {string} 시간 기준으로 {string} 경로가 조회된다", (String expectedDuration, String pathString) -> {
            List<String> split = List.of(pathString.split(","));
            PathResponse pathResponse = (PathResponse) context.store.get("path_duration");
            List<String> actualPath = pathResponse.getStationResponses().stream().map(value -> value.getName()).collect(Collectors.toList());

            assertAll(
                    () -> assertThat(actualPath).containsExactly(split.toArray(new String[0])),
                    () -> assertThat(pathResponse.getWeight()).isEqualTo(Double.valueOf(expectedDuration))
            );
        });

    }
}


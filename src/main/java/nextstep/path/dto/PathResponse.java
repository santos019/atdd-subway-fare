package nextstep.path.dto;

import nextstep.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {

    private List<StationResponse> stationResponses = new ArrayList<>();
    private Double weight;

    private PathResponse() {
    }

    public PathResponse(List<StationResponse> stationResponses, Double weight) {
        this.stationResponses = stationResponses;
        this.weight = weight;
    }

    public static PathResponse of(final List<StationResponse> stationResponses, final Double weight) {
        return new PathResponse(stationResponses, weight);
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public Double getWeight() {
        return weight;
    }

}


package nextstep.path.dto;

import nextstep.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {

    private List<StationResponse> stationResponses = new ArrayList<>();
    private Long totalDistance;
    private Long totalDuration;
    private Long totalPrice;

    private PathResponse() {
    }

    public PathResponse(final List<StationResponse> stationResponses, final Long totalDistance, final Long totalDuration, final Long totalPrice) {
        this.stationResponses = stationResponses;
        this.totalDistance = totalDistance;
        this.totalDuration = totalDuration;
        this.totalPrice = totalPrice;
    }

    public static PathResponse of(final List<StationResponse> stationResponses, final Long totalDistance, final Long totalDuration, final Long totalPrice) {
        return new PathResponse(stationResponses, totalDistance, totalDuration, totalPrice);
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public Long getTotalDistance() {
        return totalDistance;
    }

    public Long getTotalDuration() {
        return totalDuration;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }
}


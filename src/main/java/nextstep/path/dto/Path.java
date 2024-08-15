package nextstep.path.dto;

import nextstep.path.exception.PathException;
import nextstep.station.dto.StationResponse;
import nextstep.station.entity.Station;

import java.util.ArrayList;
import java.util.List;

import static nextstep.common.constant.ErrorCode.PATH_NOT_FOUND;

public class Path {
    private final List<Station> stations;
    private final Long totalDistance;
    private final Long totalDuration;
    private final Long totalPrice;


    public Path(List<Station> stations, Long totalDistance, Long totalDuration, Long totalPrice) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.totalDuration = totalDuration;
        this.totalPrice = totalPrice;
    }

    public static Path of(final List<Station> stations, final Long totalDistance, final Long totalDuration) {

        return new Path(stations, totalDistance, totalDuration, calculateOverFare(totalDistance));
    }

    public static Long calculateOverFare(final Long distance) {
        if (distance <= 10) {
            return 1250L;
        }
        int overFare = 5;

        if (distance > 50) {
            overFare = 8;
        }

        return (long) ((Math.ceil((distance - 11) / overFare) + 1) * 100) + 1250L;
    }

    public PathResponse createPathResponse() {
        if (stations == null || stations.isEmpty()) {
            throw new PathException(String.valueOf(PATH_NOT_FOUND));
        }

        List<StationResponse> stationResponses = new ArrayList<>();
        for (Station station : stations) {
            stationResponses.add(StationResponse.of(station.getId(), station.getName()));
        }

        return PathResponse.of(stationResponses, totalDistance, totalDuration, totalPrice);

    }

    public List<Station> getStations() {
        return stations;
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


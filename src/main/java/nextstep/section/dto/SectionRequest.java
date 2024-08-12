package nextstep.section.dto;

import javax.validation.constraints.NotNull;

public class SectionRequest {
    @NotNull
    private Long upStationId;
    @NotNull
    private Long downStationId;
    @NotNull
    private Long distance;
    @NotNull
    private Long duration;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, Long distance, Long duration) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }

    public static SectionRequest of(final Long upStationId, final Long downStationId, final Long distance, final Long duration) {
        return new SectionRequest(upStationId, downStationId, distance, duration);
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getDuration() {
        return duration;
    }

}


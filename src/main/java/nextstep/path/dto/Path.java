package nextstep.path.dto;

import nextstep.line.entity.Line;
import nextstep.member.domain.Member;
import nextstep.path.exception.PathException;
import nextstep.section.entity.Section;
import nextstep.section.entity.Sections;
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

    private static final int MIN_AGE_FOR_DISCOUNT = 6;
    private static final int MAX_AGE_FOR_DISCOUNT = 18;
    private static final int DISCOUNT_PERCENT_CHILD = 50;
    private static final int DISCOUNT_PERCENT_ADULT = 20;
    private static final int MINIMUM_PRICE_FOR_DISCOUNT = 350;

    public Path(List<Station> stations, Long totalDistance, Long totalDuration, Long totalPrice) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.totalDuration = totalDuration;
        this.totalPrice = totalPrice;
    }

    public static Path of(final Member member, final List<Line> lines, final List<Station> stations, final Sections sections) {
        Long totalDistance = sections.getTotalDistance();
        Long totalDuration = sections.getTotalDuration();
        Long totalPrice = calculateOverFare(totalDistance);
        totalPrice = calculateLineAdditionalFare(lines, sections, totalPrice);
        totalPrice = calculateMemberAge(member, totalPrice);

        return new Path(stations, totalDistance, totalDuration, totalPrice);
    }

    public static Long calculateLineAdditionalFare(final List<Line> lines, final Sections sections, final Long totalPrice) {
        long additionalFare = sections.getSections().stream()
                .mapToLong(section -> getMaxAdditionalFareForSection(lines, section))
                .max()
                .orElse(0);

        return totalPrice + additionalFare;
    }

    private static long getMaxAdditionalFareForSection(List<Line> lines, Section section) {
        return lines.stream()
                .filter(line -> line.getAdditionalFare() > 0 && line.hasSection(section))
                .mapToLong(Line::getAdditionalFare)
                .max()
                .orElse(0);
    }

    public static Long calculateMemberAge(final Member member, final Long totalPrice) {
        if (member == null) {
            return totalPrice;
        }

        int age = member.getAge();

        if (totalPrice < MINIMUM_PRICE_FOR_DISCOUNT) {
            return totalPrice;
        }

        double discountPercent = getDiscountPercent(age);
        long discountAmount = (long) (((totalPrice - 350) * discountPercent));

        return totalPrice - discountAmount;
    }

    private static double getDiscountPercent(final int age) {
        if (age < MIN_AGE_FOR_DISCOUNT || age > MAX_AGE_FOR_DISCOUNT) {
            return 0;
        }

        if (age >= MIN_AGE_FOR_DISCOUNT && age < 13) {
            return DISCOUNT_PERCENT_CHILD * 0.01;
        }

        return DISCOUNT_PERCENT_ADULT * 0.01;
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


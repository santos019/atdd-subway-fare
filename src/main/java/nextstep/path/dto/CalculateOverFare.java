package nextstep.path.dto;

public class CalculateOverFare {
    private Long totalPrice = 0L;

    private static final Long MIN_DISTANCE_OVERFARE = 10L;
    private static final Long SECOND_DISTANCE_OVERFARE = 50L;
    private static final Long DEFAULT_PRICE = 1250L;
    private static final int OVERFARE_FIRST = 5;
    private static final int OVERFARE_SECOND = 8;

    public CalculateOverFare() {}

    public CalculateOverFare(final Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public static CalculateOverFare of (final Long totalDistance) {
        return new CalculateOverFare(calculateOverFare(totalDistance));
    }

    public static Long calculateOverFare(final Long distance) {
        if (distance <= MIN_DISTANCE_OVERFARE) {
            return DEFAULT_PRICE;
        }
        int overFare = OVERFARE_FIRST;

        if (distance > SECOND_DISTANCE_OVERFARE) {
            overFare = OVERFARE_SECOND;
        }

        return (long) ((Math.ceil((distance - 11) / overFare) + 1) * 100) + DEFAULT_PRICE;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }
}


package nextstep.path.dto;

import nextstep.member.domain.Member;

public class CalculateMemberAgeFare {

    private Long totalPrice = 0L;
    private static final int MINIMUM_PRICE_FOR_DISCOUNT = 350;
    private static final int MIN_AGE_FOR_DISCOUNT = 6;
    private static final int MAX_AGE_FOR_DISCOUNT = 18;
    private static final int DISCOUNT_PERCENT_CHILD = 50;
    private static final int DISCOUNT_PERCENT_ADULT = 20;
    public CalculateMemberAgeFare () {}

    public CalculateMemberAgeFare (Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public static Path of (Path path) {
        Long totalPrice = calculateMemberAge(path.getMember(), path.getTotalPrice());
        path.setTotalPrice(totalPrice);
        return path;
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

    public Long getTotalPrice() {
        return totalPrice;
    }
}

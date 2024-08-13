package nextstep.common.constant;

public enum PathType {
    DISTANCE("distance"),
    DURATION("duration");

    private final String value;

    PathType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PathType fromString(String value) {
        for (PathType type : PathType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid type: " + value);
    }
}

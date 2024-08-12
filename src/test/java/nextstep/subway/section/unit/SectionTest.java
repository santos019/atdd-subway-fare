package nextstep.subway.section.unit;

import nextstep.section.entity.Section;
import nextstep.section.entity.Sections;
import nextstep.section.exception.SectionException;
import nextstep.station.entity.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.common.constant.ErrorCode.*;
import static nextstep.common.constant.Type.DISTANCE;
import static nextstep.common.constant.Type.DURATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class SectionTest {

    private Station 역삼역;
    private Station 강남역;
    private Station 선릉역;
    private Station 선정릉역;
    private Section 역삼역_강남역;
    private Section 강남역_선릉역;
    private Section 선릉역_선정릉역;

    private Sections 생성된_구간들;

    private Long 역삼역_강남역_길이 = 2L;
    private Long 강남역_선릉역_길이 = 2L;
    private Long 선릉역_선정릉역_길이 = 1L;

    private Long 역삼역_강남역_시간 = 5L;
    private Long 강남역_선릉역_시간 = 3L;
    private Long 선릉역_선정릉역_시간 = 1L;
    /**
     * 역삼역 --- ds: 2 --- 강남역 --- ds: 2 --- 선릉역 --- ds: 1 --- 선정릉역 *
     *          dr: 5               dr: 3               dr: 1            *
     */
    @BeforeEach
    public void setup() {
        역삼역 = Station.of(1L, "역삼역");
        강남역 = Station.of(2L, "강남역");
        선릉역 = Station.of(3L, "선릉역");
        선정릉역 = Station.of(4L, "선정릉역");
        역삼역_강남역 = Section.of(1L, 역삼역, 강남역, 역삼역_강남역_길이, 역삼역_강남역_시간);
        강남역_선릉역 = Section.of(2L, 강남역, 선릉역, 강남역_선릉역_길이, 강남역_선릉역_시간);
        선릉역_선정릉역 = Section.of(3L, 선릉역, 선정릉역, 선릉역_선정릉역_길이, 선릉역_선정릉역_시간);

        생성된_구간들 = new Sections();
    }

    @Test
    @DisplayName("Section의 Distance는 1 이상의 값을 가져야한다.")
    public void section_distance() {
        // when & then
        assertThrows(SectionException.class, () -> Section.of(역삼역, 강남역, 0L, 역삼역_강남역_시간))
                .getMessage().equals(SECTION_DISTANCE_TOO_SHORT.getDescription());
    }

    @Test
    @DisplayName("Section의 Duration은 1 이상의 값을 가져야한다.")
    public void section_duration() {
        // when & then
        assertThrows(SectionException.class, () -> Section.of(역삼역, 강남역, 역삼역_강남역_길이, 0L))
                .getMessage().equals(SECTION_DURATION_TOO_SHORT.getDescription());
    }

    @Test
    @DisplayName("새로운 구간을 첫번째 구간에 추가한다.")
    public void add_section() {
        // when
        생성된_구간들.addSection(역삼역_강남역);

        // then
        Section 생성된_Section_조회 = 생성된_구간들.getSectionByUpStationId(역삼역.getId());
        assertThat(역삼역_강남역).isEqualTo(생성된_Section_조회);
    }

    @Test
    @DisplayName("새로운 구간을 중간 구간에 추가한다.")
    public void add_section_middle() {
        // given
        생성된_구간들.addSection(역삼역_강남역);
        생성된_구간들.addSection(강남역_선릉역);

        // when
        var 중간_구간에_추가할_Section = Section.of(강남역, 선정릉역, 1L, 1L);
        생성된_구간들.addSection(중간_구간에_추가할_Section);

        // then
        var 생성된_Section_조회 = 생성된_구간들.getSectionByUpStationId(강남역.getId());
        assertThat(중간_구간에_추가할_Section).isEqualTo(생성된_Section_조회);
    }

    @Test
    @DisplayName("새로운 구간을 마지막 구간에 추가한다.")
    public void add_section_last() {
        // given
        생성된_구간들.addSection(역삼역_강남역);

        // when
        var 마지막_구간에_추가할_Section = Section.of(강남역, 선릉역, 강남역_선릉역_길이, 강남역_선릉역_시간);
        생성된_구간들.addSection(마지막_구간에_추가할_Section);

        // then
        var 생성된_Section_조회 = 생성된_구간들.getSectionByUpStationId(강남역.getId());
        assertThat(마지막_구간에_추가할_Section).isEqualTo(생성된_Section_조회);
    }

    @Test
    @DisplayName("새로운 구간의 상행역과 하행역 둘 다 기존 노선에 존재하면 구간 생성은 실패한다.")
    public void add_section_fail1() {
        // given
        생성된_구간들.addSection(역삼역_강남역);

        // when & then
        assertThrows(SectionException.class, () -> 생성된_구간들.addSection(역삼역_강남역))
                .getMessage().equals(SECTION_ALREADY_EXIST.getDescription());
    }

    @Test
    @DisplayName("기존에 등록된 구간이 존재할 때, 새로운 구간의 상행역과 하행역 둘 다 기존 노선에 존재하지 않으면 구간 생성은 실패한다.")
    public void add_section_fail2() {
        // given
        생성된_구간들.addSection(역삼역_강남역);
        var 구간에_추가할_Section = Section.of(선정릉역, 선릉역, 2L, 5L);

        // when & then
        assertThrows(SectionException.class, () -> 생성된_구간들.addSection(구간에_추가할_Section))
                .getMessage().equals(SECTION_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("기존 구간의 Distance보다 길거나 같은 Distance를 가진 구간 생성은 실패한다.")
    public void add_section_fail3() {
        // given
        생성된_구간들.addSection(역삼역_강남역);
        var 구간에_추가할_Section = Section.of(역삼역, 선릉역, 2L, 강남역_선릉역_길이);

        // when & then
        assertThrows(SectionException.class, () -> 생성된_구간들.addSection(구간에_추가할_Section))
                .getMessage().equals(SECTION_DISTANCE_LESS_THAN_EXISTING.getDescription());
    }

    @Test
    @DisplayName("기존 구간의 Duration보다 길거나 같은 Duration를 가진 구간 생성은 실패한다.")
    public void add_section_fail4() {
        // given
        생성된_구간들.addSection(역삼역_강남역);
        var 구간에_추가할_Section = Section.of(역삼역, 선릉역, 2L, 2L);

        // when & then
        assertThrows(SectionException.class, () -> 생성된_구간들.addSection(구간에_추가할_Section))
                .getMessage().equals(SECTION_DISTANCE_LESS_THAN_EXISTING.getDescription());
    }

    @Test
    @DisplayName("상행 종점역을 가진 첫 번째 구간을 삭제한다.")
    public void delete_section_first_section() {
        // given
        생성된_구간들.addSection(역삼역_강남역);
        생성된_구간들.addSection(강남역_선릉역);

        // when
        생성된_구간들.removeSection(역삼역_강남역);

        // then
        var 삭제_후_강남역_선릉역_구간 = 생성된_구간들.getSectionByUpStationId(강남역.getId());
        assertAll(
                () -> assertThat(생성된_구간들.getSections().size()).isEqualTo(1),
                () -> assertThat(삭제_후_강남역_선릉역_구간.getNextSection()).isNull(),
                () -> assertThat(삭제_후_강남역_선릉역_구간.getUpStation()).isEqualTo(강남역),
                () -> assertThat(삭제_후_강남역_선릉역_구간.getDownStation()).isEqualTo(선릉역),
                () -> assertThat(삭제_후_강남역_선릉역_구간.getDistance()).isEqualTo(강남역_선릉역_길이),
                () -> assertThat(삭제_후_강남역_선릉역_구간.getDuration()).isEqualTo(강남역_선릉역_시간)
        );
    }

    @Test
    @DisplayName("중간 구간을 삭제한다.")
    public void delete_section_middle_section() {
        // given
        생성된_구간들.addSection(역삼역_강남역);
        생성된_구간들.addSection(강남역_선릉역);
        생성된_구간들.addSection(선릉역_선정릉역);

        // when
        생성된_구간들.removeSection(강남역_선릉역);

        // then
        var 삭제_후_역삼역_선릉역_구간 = 생성된_구간들.getSectionByUpStationId(역삼역.getId());
        var 삭제_후_선릉역_선정릉역_구간 = 생성된_구간들.getSectionByUpStationId(선릉역.getId());

        assertAll(
                () -> assertThat(생성된_구간들.getSections().size()).isEqualTo(2),
                () -> assertThat(삭제_후_역삼역_선릉역_구간.getPreviousSection()).isNull(),
                () -> assertThat(삭제_후_역삼역_선릉역_구간.getNextSection()).isEqualTo(삭제_후_선릉역_선정릉역_구간),
                () -> assertThat(삭제_후_역삼역_선릉역_구간.getUpStation()).isEqualTo(역삼역),
                () -> assertThat(삭제_후_역삼역_선릉역_구간.getDownStation()).isEqualTo(선릉역),
                () -> assertThat(삭제_후_역삼역_선릉역_구간.getDistance()).isEqualTo(역삼역_강남역_길이 + 강남역_선릉역_길이),
                () -> assertThat(삭제_후_역삼역_선릉역_구간.getDuration()).isEqualTo(역삼역_강남역_시간 + 강남역_선릉역_시간),
                () -> assertThat(삭제_후_선릉역_선정릉역_구간.getPreviousSection()).isEqualTo(역삼역_강남역),
                () -> assertThat(삭제_후_선릉역_선정릉역_구간.getNextSection()).isNull(),
                () -> assertThat(삭제_후_선릉역_선정릉역_구간.getUpStation()).isEqualTo(선릉역),
                () -> assertThat(삭제_후_선릉역_선정릉역_구간.getDownStation()).isEqualTo(선정릉역),
                () -> assertThat(삭제_후_선릉역_선정릉역_구간.getDistance()).isEqualTo(선릉역_선정릉역_길이),
                () -> assertThat(삭제_후_선릉역_선정릉역_구간.getDuration()).isEqualTo(선릉역_선정릉역_시간)
        );
    }

    @Test
    @DisplayName("하행 종점역을 가진 마지막 구간을 삭제한다.")
    public void delete_section_last_section() {
        // given
        생성된_구간들.addSection(역삼역_강남역);
        생성된_구간들.addSection(강남역_선릉역);
        생성된_구간들.addSection(선릉역_선정릉역);

        // when
        생성된_구간들.removeSection(선릉역_선정릉역);

        // then
        var 삭제_후_강남역_선릉역_구간 = 생성된_구간들.getSectionByUpStationId(강남역.getId());
        assertAll(
                () -> assertThat(생성된_구간들.getSections().size()).isEqualTo(2),
                () -> assertThat(삭제_후_강남역_선릉역_구간.getNextSection()).isNull(),
                () -> assertThat(삭제_후_강남역_선릉역_구간.getPreviousSection()).isEqualTo(역삼역_강남역),
                () -> assertThat(삭제_후_강남역_선릉역_구간.getUpStation()).isEqualTo(강남역),
                () -> assertThat(삭제_후_강남역_선릉역_구간.getDownStation()).isEqualTo(선릉역),
                () -> assertThat(삭제_후_강남역_선릉역_구간.getDistance()).isEqualTo(강남역_선릉역_길이),
                () -> assertThat(삭제_후_강남역_선릉역_구간.getDuration()).isEqualTo(강남역_선릉역_시간)
        );
    }

    @Test
    @DisplayName("구간이 2개 미만일 때, 구간 삭제 요청은 실패한다.")
    public void delete_section_fail1() {
        // given
        생성된_구간들.addSection(역삼역_강남역);

        // when & then
        assertThrows(SectionException.class, () -> 생성된_구간들.removeSection(역삼역_강남역))
                .getMessage().equals(SECTION_DISTANCE_TOO_SHORT.getDescription());
    }

    @Test
    @DisplayName("노선에 존재하지 않는 역을 가진 구간의 삭제 요청은 실패한다.")
    public void delete_section_fail2() {
        생성된_구간들.addSection(역삼역_강남역);
        생성된_구간들.addSection(강남역_선릉역);

        // when & then
        assertThrows(SectionException.class, () -> 생성된_구간들.removeSection(선릉역_선정릉역))
                .getMessage().equals(SECTION_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("distance 타입의 값을 조회한다.")
    public void getWeight_distance() {
        // when & then
        assertThat(역삼역_강남역.getWeight(DISTANCE.getValue())).isEqualTo(역삼역_강남역_길이);
    }

    @Test
    @DisplayName("duration 타입의 값을 조회한다.")
    public void getWeight_duration() {
        // when & then
        assertThat(역삼역_강남역.getWeight(DURATION.getValue())).isEqualTo(역삼역_강남역_시간);
    }

    @Test
    @DisplayName("존재하지 않는 타입의 값을 조회한다.")
    public void getWeight_noneExist() {
        // given
        String noneExistType = "None";

        // when & then
        assertThrows(SectionException.class, () -> 역삼역_강남역.getWeight(noneExistType))
                .getMessage().equals(SECTION_TYPE_NOT_FOUND.getDescription() + "Invalid type: " + noneExistType);
    }
}


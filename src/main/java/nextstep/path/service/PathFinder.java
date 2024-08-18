package nextstep.path.service;

import nextstep.line.entity.Line;
import nextstep.line.service.LineService;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.exception.MemberException;
import nextstep.path.dto.PathResponse;
import nextstep.station.service.StationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static nextstep.common.constant.ErrorCode.MEMBER_NOT_FOUND;

@Service
public class PathFinder {

    private StationService stationService;
    private LineService lineService;
    private PathService pathService;
    private MemberService memberService;

    public PathFinder(StationService stationService, LineService lineService, PathService pathService, MemberService memberService) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.pathService = pathService;
        this.memberService = memberService;
    }

    @Transactional(readOnly = true)
    public PathResponse retrieveStationPath(final Optional<LoginMember> loginMember, final String type, final Long source, final Long target) {
        validateStationExist(source, target);
        List<Line> lineList = lineService.getAllLines();
        Member member = getMember(loginMember);
        return pathService.findPath(member, type, source, target, lineList);
    }

    private void validateStationExist(final Long source, final Long target) {
        stationService.getStationByIdOrThrow(source);
        stationService.getStationByIdOrThrow(target);
    }

    private Member getMember(Optional<LoginMember> loginMember) {
        if (loginMember.isEmpty()) return null;
        Member member = memberService.findMemberOptionalByEmail(loginMember.get().getEmail()).orElseThrow(
                () -> new MemberException(String.valueOf(MEMBER_NOT_FOUND))
        );

        return member;
    }

}


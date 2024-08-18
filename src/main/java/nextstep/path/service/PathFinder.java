package nextstep.path.service;

import nextstep.line.entity.Line;
import nextstep.line.service.LineService;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.path.dto.PathResponse;
import nextstep.station.service.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        Member member = findMemberByOptionalLoginMember(loginMember);
        return pathService.findPath(member, type, source, target, lineList);
    }

    private void validateStationExist(final Long source, final Long target) {
        stationService.getStationByIdOrThrow(source);
        stationService.getStationByIdOrThrow(target);
    }

    private Member findMemberByOptionalLoginMember(Optional<LoginMember> loginMember) {
        if (loginMember.isEmpty()) return null;
        return memberService.findMemberByEmail(loginMember.get().getEmail());
    }

}


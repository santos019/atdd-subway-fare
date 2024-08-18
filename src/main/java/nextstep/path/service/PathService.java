package nextstep.path.service;

import nextstep.line.entity.Line;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.path.dto.PathResponse;

import java.util.List;
import java.util.Optional;

public interface PathService {
    PathResponse findPath(Member member, String type, Long source, Long target, List<Line> lineList);
}


package nextstep.path.service;

import nextstep.line.entity.Line;
import nextstep.member.domain.Member;
import nextstep.path.domain.GraphModel;
import nextstep.path.dto.Path;
import nextstep.path.dto.PathResponse;

import java.util.List;

public class DijkstraShortestPathService implements PathService {
    @Override
    public PathResponse findPath(final Member member, final String type, final Long source, final Long target, final List<Line> lineList) {
        GraphModel graphModel = new GraphModel(source, target);
        Path path = graphModel.findPath(member, lineList, type);
        return path.createPathResponse();
    }
}


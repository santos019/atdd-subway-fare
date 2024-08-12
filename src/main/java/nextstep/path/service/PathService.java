package nextstep.path.service;

import nextstep.line.entity.Line;
import nextstep.path.dto.PathResponse;

import java.util.List;

public interface PathService {
    PathResponse findPath(String type, Long source, Long target, List<Line> lineList);
}


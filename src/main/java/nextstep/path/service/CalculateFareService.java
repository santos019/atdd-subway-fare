package nextstep.path.service;

import nextstep.path.dto.CalculateMemberAgeFare;
import nextstep.path.dto.CalculateOverFare;
import nextstep.path.dto.Path;
import nextstep.path.dto.PathResponse;
import org.springframework.stereotype.Service;

@Service
public class CalculateFareService {

    public PathResponse calculateFare(Path path) {
        path = CalculateOverFare.of(path);
        path = CalculateMemberAgeFare.of(path);


    }
}

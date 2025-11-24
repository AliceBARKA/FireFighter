package model;

import util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MotorizedFirefighter extends Firefighter {

    public MotorizedFirefighter(Position startPosition) {
        super(startPosition);
    }

    @Override
    public List<Position> act(Fire fire,
                              Map<Position, List<Position>> neighbors,
                              Set<Position> mountainPositions) {
        List<Position> modified = new ArrayList<>();

        // 1er déplacement + extinction
        modified.addAll(moveAndExtinguishOnce(fire, neighbors, mountainPositions));

        // 2e déplacement + extinction
        modified.addAll(moveAndExtinguishOnce(fire, neighbors, mountainPositions));

        return modified;
    }
}

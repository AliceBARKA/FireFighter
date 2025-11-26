package model;

import util.MovementStrategy;
import util.Position;
import util.TargetStrategy;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Firefighter extends AbstractFirefighter {
    public Firefighter(Position startPosition, MovementStrategy movementStrategy) {
        super(startPosition, movementStrategy);
    }

    @Override
    protected int stepsPerTurn() {
        return 1;
    }

    @Override
    public ModelElement getModelElement() {
        return ModelElement.FIREFIGHTER;
    }
}

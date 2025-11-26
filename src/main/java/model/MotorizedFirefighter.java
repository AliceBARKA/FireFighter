package model;

import util.MovementStrategy;
import util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MotorizedFirefighter extends AbstractFirefighter {

    public MotorizedFirefighter(Position startPosition, MovementStrategy movementStrategy) {
        super(startPosition, movementStrategy);
    }

    @Override
    protected int stepsPerTurn() {
        return 2;
    }

    @Override
    public ModelElement getModelElement() {
        return ModelElement.MOTORIZED_FIREFIGHTER;
    }
}

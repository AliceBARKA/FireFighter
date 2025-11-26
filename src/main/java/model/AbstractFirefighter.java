package model;

import model.Actor;
import model.Fire;
import model.TerrainPolicy;
import util.MovementStrategy;
import util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractFirefighter implements Actor {

    protected Position position;
    protected final MovementStrategy movementStrategy;

    protected AbstractFirefighter(Position startPosition, MovementStrategy movementStrategy) {
        this.position = startPosition;
        this.movementStrategy = movementStrategy;
    }

    @Override
    public Position getPosition() { return position; }

    @Override
    public void setPosition(Position position) { this.position = position; }

    protected abstract int stepsPerTurn();

    @Override
    public List<Position> act(Fire fire, Map<Position, List<Position>> neighbors, TerrainPolicy terrainPolicy) {
        List<Position> modified = new ArrayList<>();

        for (int step = 0; step < stepsPerTurn(); step++) {
            Position newPosition = movementStrategy.nextPosition(position, fire.getFirePositions(), neighbors, terrainPolicy);
            if (!newPosition.equals(position)) {
                modified.add(position);
                position = newPosition;
                modified.add(position);
            }


            fire.extinguish(position);
            for (Position adj : neighbors.get(position)) {
                if (fire.isOnFire(adj)) {
                    fire.extinguish(adj);
                    modified.add(adj);
                }
            }
        }
        return modified;
    }
}

package util;

import model.TerrainPolicy;
import util.Position;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MovementStrategy {
    Position nextPosition(Position current,
                          Collection<Position> firePositions,
                          Map<Position, List<Position>> neighbors,
                          TerrainPolicy terrainPolicy);
}

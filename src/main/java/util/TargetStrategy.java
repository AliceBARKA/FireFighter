package util;

import java.util.*;

public class TargetStrategy {
  public Position neighborClosestToFire(Position position,
    Collection<Position> targets,
    Map<Position, List<Position>> neighbors,
    Set<Position> mountainPositions) {
    Set<Position> seen = new HashSet<>();
    Map<Position, Position> firstMove = new HashMap<>();
    Queue<Position> toVisit = new LinkedList<>();

    for (Position initialMove : neighbors.get(position)) {
      if (mountainPositions.contains(initialMove)) continue;
      toVisit.add(initialMove);
      seen.add(initialMove);
      firstMove.put(initialMove, initialMove);
    }

    while (!toVisit.isEmpty()) {
      Position current = toVisit.poll();
      if (targets.contains(current))
        return firstMove.get(current);
      for (Position adjacent : neighbors.get(current)) {
        if (mountainPositions.contains(adjacent)) continue;
        if (seen.contains(adjacent)) continue;
        toVisit.add(adjacent);
        seen.add(adjacent);
        firstMove.put(adjacent, firstMove.get(current));
      }
    }
    return position;
  }
}

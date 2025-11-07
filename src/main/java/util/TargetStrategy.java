//package model;
package  util;

import util.Position;

import java.util.*;

public class TargetStrategy {


    /**
     * @param position current position.
     * @param targets positions that are targeted.
     * @return the position next to the current position that is on the path to the closest target.
     */
   public  Position neighborClosestToFire(Position position, Collection<Position> targets,
                                   Map<Position,List<Position>>neighbors) {
        Set<Position> seen = new HashSet<Position>();
        HashMap<Position, Position> firstMove = new HashMap<Position, Position>();
        Queue<Position> toVisit = new LinkedList<Position>(neighbors.get(position));
        for (Position initialMove : toVisit)
            firstMove.put(initialMove, initialMove);
        while (!toVisit.isEmpty()) {
            Position current = toVisit.poll();
            if (targets.contains(current))
                return firstMove.get(current);
            for (Position adjacent : neighbors.get(current)) {
                if (seen.contains(adjacent)) continue;
                toVisit.add(adjacent);
                seen.add(adjacent);
                firstMove.put(adjacent, firstMove.get(current));
            }
        }
        return position;
    }
}
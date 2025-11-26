package model;

import util.Position;

public interface TerrainPolicy {
    TerrainType terrainAt(Position position);
    boolean canFirefighterMoveTo(Position position,boolean motorizes);
    boolean canCloudMoveTo(Position position);
    int fireSpreadPeriodFor(Position position);
}

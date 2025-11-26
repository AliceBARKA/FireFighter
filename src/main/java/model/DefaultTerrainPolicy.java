package model;

import model.TerrainPolicy;
import model.TerrainType;
import util.Position;

public class DefaultTerrainPolicy implements TerrainPolicy {

    private final TerrainType[][] terrainGrid;

    public DefaultTerrainPolicy(TerrainType[][] terrainGrid) {
        this.terrainGrid = terrainGrid;
    }

    @Override
    public TerrainType terrainAt(Position position) {
        return terrainGrid[position.row()][position.column()];
    }

    @Override
    public boolean canFirefighterMoveTo(Position position, boolean motorized) {
        TerrainType type = terrainAt(position);
        return switch (type) {
            case MOUNTAIN -> false;
            case ROAD, PLAIN, ROCK -> true;
        };
    }

    @Override
    public boolean canCloudMoveTo(Position position) {
        TerrainType type = terrainAt(position);
        return switch (type) {
            case MOUNTAIN, ROAD -> false;
            case PLAIN, ROCK -> true;
        };
    }

    @Override
    public int fireSpreadPeriodFor(Position position) {
        TerrainType type = terrainAt(position);
        return switch (type) {
            case MOUNTAIN, ROAD -> Integer.MAX_VALUE; // jamais
            case PLAIN -> 2;   // normal : tous les 2 tours
            case ROCK -> 4;   // rocailles : tous les 4 tours
        };
    }
}

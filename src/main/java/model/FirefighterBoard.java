package model;

import util.MovementStrategy;
import util.Position;
import util.TargetStrategy;

import java.util.*;

public class FirefighterBoard implements Board<List<ModelElement>> {

  private final int columnCount;
  private final int rowCount;
  private final int initialFireCount;
  private final int initialFirefighterCount;
  private final int initialCloudCount = 3;
  private final double motorizedRatio = 0.2;
  private final int initialMountainCount = 4;
  private final int initialRockyCount = 4;

  private final Position[][] positions;
  private final Map<Position, List<Position>> neighbors = new HashMap<>();
  private final Random random = new Random();

  private int step = 0;

  private TerrainType[][] terrainGrid;
  private TerrainPolicy terrainPolicy;

  private Fire fire;
  private final List<AbstractFirefighter> firefighters = new ArrayList<>();
  private final List<Cloud> clouds = new ArrayList<>();

  // ------------------------------------------------------------
  public FirefighterBoard(int columnCount, int rowCount, int initialFireCount, int initialFirefighterCount) {
    this.columnCount = columnCount;
    this.rowCount = rowCount;
    this.initialFireCount = initialFireCount;
    this.initialFirefighterCount = initialFirefighterCount;
    this.positions = new Position[rowCount][columnCount];

    initPositions();
    initNeighbors();
    initTerrain();
    initializeElements();
  }


  private void initPositions() {
    for (int row = 0; row < rowCount; row++) {
      for (int column = 0; column < columnCount; column++) {
        positions[row][column] = new Position(row, column);
      }
    }
  }


  private void initNeighbors() {
    for (int row = 0; row < rowCount; row++) {
      for (int column = 0; column < columnCount; column++) {
        List<Position> list = new ArrayList<>();
        if (row > 0) list.add(positions[row - 1][column]);
        if (column > 0) list.add(positions[row][column - 1]);
        if (row < rowCount - 1) list.add(positions[row + 1][column]);
        if (column < columnCount - 1) list.add(positions[row][column + 1]);
        neighbors.put(positions[row][column], list);
      }
    }
  }


  private void initTerrain() {
    terrainGrid = new TerrainType[rowCount][columnCount];


    for (int row = 0; row < rowCount; row++) {
      for (int col = 0; col < columnCount; col++) {
        terrainGrid[row][col] = TerrainType.PLAIN;
      }
    }

    int roadRow = rowCount / 2;
    for (int col = 0; col < columnCount; col++) {
      terrainGrid[roadRow][col] = TerrainType.ROAD;
    }
    int roadCol = columnCount / 2;
    for (int row = 0; row < rowCount; row++) {
      terrainGrid[row][roadCol] = TerrainType.ROAD;
    }


    for (int i = 0; i < initialMountainCount; i++) {
      Position p = randomPosition();
      terrainGrid[p.row()][p.column()] = TerrainType.MOUNTAIN;
    }


    for (int i = 0; i < initialRockyCount; i++) {
      Position p = randomPosition();
      terrainGrid[p.row()][p.column()] = TerrainType.ROCK;
    }

    terrainPolicy = new DefaultTerrainPolicy(terrainGrid);
  }

  private Position randomPosition() {
    return positions[random.nextInt(rowCount)][random.nextInt(columnCount)];
  }


  public void initializeElements() {
    step = 0;
    firefighters.clear();
    clouds.clear();


    Set<Position> firePositions = new HashSet<>();
    while (firePositions.size() < initialFireCount) {
      Position p = randomPosition();
      if (terrainPolicy.terrainAt(p) != TerrainType.MOUNTAIN) {
        firePositions.add(p);
      }
    }
    this.fire = new Fire(firePositions);


    MovementStrategy movementStrategy = new TargetStrategy();

    // pompier normal / motorisé
    int motorizedCount = Math.max(1, (int) Math.round(initialFirefighterCount * motorizedRatio));
    int normalCount = initialFirefighterCount - motorizedCount;

    for (int i = 0; i < normalCount; i++) {
      Position p = randomFreePosition();
      firefighters.add(new Firefighter(p, movementStrategy));
    }

    for (int i = 0; i < motorizedCount; i++) {
      Position p = randomFreePosition();
      firefighters.add(new MotorizedFirefighter(p, movementStrategy));
    }

    for (int i = 0; i < initialCloudCount; i++) {
      Position p = randomFreePosition();
      clouds.add(new Cloud(p));
    }
  }

  private Position randomFreePosition() {
    while (true) {
      Position p = randomPosition();
      TerrainType t = terrainPolicy.terrainAt(p);
      if (t == TerrainType.MOUNTAIN) continue;

      boolean occupied =
              firefighters.stream().anyMatch(f -> f.getPosition().equals(p)) ||
                      clouds.stream().anyMatch(c -> c.getPosition().equals(p)) ||
                      fire.isOnFire(p);

      if (!occupied) return p;
    }
  }
  @Override
  public List<ModelElement> getState(Position position) {
    List<ModelElement> result = new ArrayList<>();


    TerrainType type = terrainPolicy.terrainAt(position);
    switch (type) {
      case MOUNTAIN -> result.add(ModelElement.MOUNTAIN);
      case ROAD     -> result.add(ModelElement.ROAD);
      case ROCK    -> result.add(ModelElement.ROCK);
      case PLAIN    -> {}
    }

    if (fire.isOnFire(position))
      result.add(ModelElement.FIRE);

    for (Cloud c : clouds) {
      if (c.getPosition().equals(position)) {
        result.add(ModelElement.CLOUD);
      }
    }

    for (AbstractFirefighter ff : firefighters) {
      if (ff.getPosition().equals(position)) {
        result.add(ff.getModelElement());
      }
    }

    return result;
  }


  @Override
  public int rowCount() { return rowCount; }

  @Override
  public int columnCount() { return columnCount; }

  @Override
  public int stepNumber() { return step; }

  @Override
  public void reset() { initializeElements(); }

  @Override
  public void setState(List<ModelElement> state, Position position) {
  }

  @Override
  public List<Position> updateToNextGeneration() {
    List<Position> modified = new ArrayList<>();


    for (AbstractFirefighter ff : firefighters) {
      modified.addAll(ff.act(fire, neighbors, terrainPolicy));
    }


    for (Cloud c : clouds) {
      modified.addAll(c.act(fire, neighbors, terrainPolicy));
    }


    modified.addAll(fire.spread(step, neighbors, terrainPolicy));

    step++;
    return modified;
  }
}

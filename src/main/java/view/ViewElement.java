package view;

import javafx.scene.paint.Color;

public enum ViewElement {
  FIREFIGHTER(Color.BLUE), FIRE(Color.RED), EMPTY(Color.WHITE) , CLOUD(Color.BLACK) , MOTORIZED_FIREFIGHTER(Color.YELLOW),
  MOUNTAIN(Color.GREEN) ,  ROAD(Color.DARKGRAY), ROCK(Color.SADDLEBROWN);
  final Color color;
  ViewElement(Color color) {
    this.color = color;
  }
}

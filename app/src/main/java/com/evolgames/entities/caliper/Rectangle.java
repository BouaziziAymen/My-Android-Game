package com.evolgames.entities.caliper; // NAME: Justin Ward

// ALGORITHM PAPER: Rotating Calipers
// COURSE TITLE: Data Structures and Algorithms
// COURSE NUMBER:CPSC 4355
// TERM: Summer 2019

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

public class Rectangle extends Polygon {
  public float width;
  public float height;

  public Rectangle(float x, float y, float width, float height) {
    this.width = width;
    this.height = height;
    this.points = new ArrayList<>(4);
    this.points.add(new Vector2(x, y));
    this.points.add(new Vector2(x + width, y));
    this.points.add(new Vector2(x + width, y + height));
    this.points.add(new Vector2(x, y + height));
    calcCenter();
  }

  public Rectangle(float width, float height) {
    this.width = width;
    this.height = height;
    this.points = new ArrayList<>(4);
    this.points.add(new Vector2(0, 0));
    this.points.add(new Vector2(width, 0));
    this.points.add(new Vector2(width, height));
    this.points.add(new Vector2(0, height));
    calcCenter();
  }

  public float area() {
    return width * height;
  }
}

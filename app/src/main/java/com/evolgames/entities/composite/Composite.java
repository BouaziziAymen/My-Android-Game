package com.evolgames.entities.composite;

import com.evolgames.entities.blockvisitors.Visitor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Composite<T extends Composite<T>> extends Component<T> {

  protected List<T> children;

  public Composite() {
    children = new ArrayList<>();
  }

  protected abstract T getThis();

  protected void addBlock(T b) {
    children.add(b);
  }

  @SuppressWarnings("unused")
  public void removeBlock(T b) {
    children.remove(b);
  }

  public Iterator<T> createIterator() {
    return new CompositeIterator<>(getThis());
  }

  public Iterator<T> getIterator() {
    return children.iterator();
  }

  public List<T> getChildren() {
    return children;
  }

  @Override
  public void acceptVisitor(Visitor<T> visitor) {
    visitor.visitTheElement(getThis());
  }
}

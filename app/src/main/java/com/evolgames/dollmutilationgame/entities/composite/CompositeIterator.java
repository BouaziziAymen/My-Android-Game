package com.evolgames.dollmutilationgame.entities.composite;

import java.util.Iterator;
import java.util.Stack;

public class CompositeIterator<T extends Composite<T>> implements Iterator<T> {
    private final Stack<T> stack = new Stack<>();

    CompositeIterator(T composite) {
        stack.push(composite);
    }

    public T next() {
        if (hasNext()) {
            T current = stack.peek();
            stack.pop();
            Iterator<T> iter = current.getIterator();
            while (iter.hasNext()) {
                T component = iter.next();
                stack.push(component);
            }
            return current;
        } else {

            return null;
        }
    }

    public boolean hasNext() {
        return !stack.empty();
    }

  /*
   * No longer needed as of Java 8
   *
   * (non-Javadoc)
   * @see java.util.Iterator#remove()
   *
  public void remove() {
  	throw new UnsupportedOperationException();
  }
  */
}

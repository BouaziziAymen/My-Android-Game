package com.evolgames.userinterface.view.visitor;


import com.evolgames.userinterface.view.basics.Container;
import com.evolgames.userinterface.view.basics.Element;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

public class ContentTraverser {
  private VisitBehavior behavior;

  public void traverse(Element startingElement, boolean visitStartingElement) {
    traverse(startingElement, visitStartingElement, false);
  }

  public void traverse(Element startingElement, boolean visitStartingElement, boolean inverted) {
    ArrayDeque<Element> deque = new ArrayDeque<>();
    deque.push(startingElement);
    while (!deque.isEmpty()) {
      Element current = deque.pop();
      if (visitStartingElement || current != startingElement) behavior.visitElement(current);
      if (!behavior.carryOnCondition()) return;
      if (behavior.forkCondition(current))
        if (current instanceof Container) {
          Container container = (Container) current;
          ArrayList<Element> list = new ArrayList<>(container.getContents());
          Collections.sort(list);
          if (inverted) Collections.reverse(list);
          for (int i = 0; i < list.size(); i++) deque.push(list.get(i));
        }
    }
  }

  public VisitBehavior getBehavior() {
    return behavior;
  }

  public void setBehavior(VisitBehavior behavior) {
    this.behavior = behavior;
  }
}

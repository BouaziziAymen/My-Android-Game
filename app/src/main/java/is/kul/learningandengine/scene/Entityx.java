package is.kul.learningandengine.scene;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.util.adt.list.SmartList;

public class Entityx extends Entity{

	

	private void assertEntityHasNoParent(IEntity pEntity) throws IllegalStateException {
		if (pEntity.hasParent()) {
			String entityClassName = pEntity.getClass().getSimpleName();
			String currentParentClassName = pEntity.getParent().getClass().getSimpleName();
			String newParentClassName = getClass().getSimpleName();
			throw new IllegalStateException("pEntity '" + entityClassName + "' already has a parent: '" + currentParentClassName + "'. New parent: '" + newParentClassName + "'!");
		}
	}
	private static final int CHILDREN_CAPACITY_DEFAULT = 4;
	private void allocateChildren() {
        mChildren = new SmartList<IEntity>(Entityx.CHILDREN_CAPACITY_DEFAULT);
	}

	
	
	public void attachChildFirst(IEntity pEntity) throws IllegalStateException {
        assertEntityHasNoParent(pEntity);

		if (mChildren == null) {
            allocateChildren();
		}
        mChildren.addLast(pEntity);
		pEntity.setParent(this);
		pEntity.onAttached();
	}
}

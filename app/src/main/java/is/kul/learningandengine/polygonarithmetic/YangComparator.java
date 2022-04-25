package is.kul.learningandengine.polygonarithmetic;

import java.util.Comparator;

public class YangComparator implements Comparator<Compass> {

	@Override
	public int compare(Compass lhs, Compass rhs) {
		if(lhs.yangDistance<=rhs.yangDistance)return 1; else return -1;
	}

}

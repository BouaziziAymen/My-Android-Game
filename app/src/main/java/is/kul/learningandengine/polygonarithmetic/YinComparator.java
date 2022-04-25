package is.kul.learningandengine.polygonarithmetic;

import java.util.Comparator;


	public class YinComparator implements Comparator<Compass> {
		@Override
		public int compare(Compass lhs, Compass rhs) {
			if(lhs.yinDistance<=rhs.yinDistance)return 1; else return -1;
		}

	}



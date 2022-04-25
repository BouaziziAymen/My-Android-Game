package is.kul.learningandengine.helpers;
/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/



import java.util.Comparator;

/** Implementation of Tony Hoare's quickselect algorithm. Running time is generally O(n), but worst case is O(n^2) Pivot choice is
 * median of three method, providing better performance than a random pivot for partially sorted data.
 * http://en.wikipedia.org/wiki/Quickselect
 * @author Jon Renner */
public class QuickSelect<T> {
	private T[] array;
	private Comparator<? super T> comp;

	public int select (T[] items, Comparator<T> comp, int n, int size) {
        array = items;
		this.comp = comp;
		return this.recursiveSelect(0, size - 1, n);
	}

	private int partition (int left, int right, int pivot) {
		T pivotValue = this.array[pivot];
        this.swap(right, pivot);
		int storage = left;
		for (int i = left; i < right; i++) {
			if (this.comp.compare(this.array[i], pivotValue) < 0) {
                this.swap(storage, i);
				storage++;
			}
		}
        this.swap(right, storage);
		return storage;
	}

	private int recursiveSelect (int left, int right, int k) {
		if (left == right) return left;
		int pivotIndex = this.medianOfThreePivot(left, right);
		int pivotNewIndex = this.partition(left, right, pivotIndex);
		int pivotDist = pivotNewIndex - left + 1;
		int result;
		if (pivotDist == k) {
			result = pivotNewIndex;
		} else if (k < pivotDist) {
			result = this.recursiveSelect(left, pivotNewIndex - 1, k);
		} else {
			result = this.recursiveSelect(pivotNewIndex + 1, right, k - pivotDist);
		}
		return result;
	}

	/** Median of Three has the potential to outperform a random pivot, especially for partially sorted arrays */
	private int medianOfThreePivot (int leftIdx, int rightIdx) {
		T left = this.array[leftIdx];
		int midIdx = (leftIdx + rightIdx) / 2;
		T mid = this.array[midIdx];
		T right = this.array[rightIdx];

		// spaghetti median of three algorithm
		// does at most 3 comparisons
		if (this.comp.compare(left, mid) > 0) {
			if (this.comp.compare(mid, right) > 0) {
				return midIdx;
			} else if (this.comp.compare(left, right) > 0) {
				return rightIdx;
			} else {
				return leftIdx;
			}
		} else {
			if (this.comp.compare(left, right) > 0) {
				return leftIdx;
			} else if (this.comp.compare(mid, right) > 0) {
				return rightIdx;
			} else {
				return midIdx;
			}
		}
	}

	private void swap (int left, int right) {
		T tmp = this.array[left];
        this.array[left] = this.array[right];
        this.array[right] = tmp;
	}
}
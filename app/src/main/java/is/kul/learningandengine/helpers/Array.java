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
import java.util.Iterator;
import java.util.NoSuchElementException;

import is.kul.learningandengine.helpers.Predicate.PredicateIterable;


/** BlockA resizable, ordered or unordered array of objects. If unordered, this class avoids a memory copy when removing elements (the
 * last element is moved to the removed element's position).
 * @author Nathan Sweet */
public class Array<T> implements Iterable<T> {
	/** Provides direct access to the underlying array. If the Array's generic type is not Object, this field may only be accessed
	 * if the {@link Array#Array(boolean, int, Class)} constructor was used. */
	public T[] items;

	public int size;
	public boolean ordered;

	private Array.ArrayIterable iterable;
	private PredicateIterable<T> predicateIterable;

	/** Creates an ordered array with a capacity of 16. */
	public Array () {
		this(true, 16);
	}

	/** Creates an ordered array with the specified capacity. */
	public Array (int capacity) {
		this(true, capacity);
	}

	/** @param ordered If false, methods that remove elements may change the order of other elements in the array, which avoids a
	 *           memory copy.
	 * @param capacity Any elements added beyond this will cause the backing array to be grown. */
	public Array (boolean ordered, int capacity) {
		this.ordered = ordered;
        this.items = (T[])new Object[capacity];
	}

	/** Creates a new array with {@link #items} of the specified type.
	 * @param ordered If false, methods that remove elements may change the order of other elements in the array, which avoids a
	 *           memory copy.
	 * @param capacity Any elements added beyond this will cause the backing array to be grown. */
	public Array (boolean ordered, int capacity, Class arrayType) {
		this.ordered = ordered;
        this.items = (T[])ArrayReflection.newInstance(arrayType, capacity);
	}

	/** Creates an ordered array with {@link #items} of the specified type and a capacity of 16. */
	public Array (Class arrayType) {
		this(true, 16, arrayType);
	}

	/** Creates a new array containing the elements in the specified array. The new array will have the same type of backing array
	 * and will be ordered if the specified array is ordered. The capacity is set to the number of elements, so any subsequent
	 * elements added will cause the backing array to be grown. */
	public Array (Array<? extends T> array) {
		this(array.ordered, array.size, array.items.getClass().getComponentType());
        this.size = array.size;
		System.arraycopy(array.items, 0, this.items, 0, this.size);
	}

	/** Creates a new ordered array containing the elements in the specified array. The new array will have the same type of
	 * backing array. The capacity is set to the number of elements, so any subsequent elements added will cause the backing array
	 * to be grown. */
	public Array (T[] array) {
		this(true, array, 0, array.length);
	}

	/** Creates a new array containing the elements in the specified array. The new array will have the same type of backing array.
	 * The capacity is set to the number of elements, so any subsequent elements added will cause the backing array to be grown.
	 * @param ordered If false, methods that remove elements may change the order of other elements in the array, which avoids a
	 *           memory copy. */
	public Array (boolean ordered, T[] array, int start, int count) {
		this(ordered, count, (Class)array.getClass().getComponentType());
        this.size = count;
		System.arraycopy(array, start, this.items, 0, this.size);
	}

	public void add (T value) {
		T[] items = this.items;
		if (this.size == items.length) items = this.resize(Math.max(8, (int)(this.size * 1.75f)));
		items[this.size++] = value;
	}

	public void addAll (Array<? extends T> array) {
        this.addAll(array, 0, array.size);
	}

	public void addAll (Array<? extends T> array, int start, int count) {
		if (start + count > array.size)
			throw new IllegalArgumentException("start + count must be <= size: " + start + " + " + count + " <= " + array.size);
        this.addAll(array.items, start, count);
	}

	public void addAll (T... array) {
        this.addAll(array, 0, array.length);
	}

	public void addAll (T[] array, int start, int count) {
		T[] items = this.items;
		int sizeNeeded = this.size + count;
		if (sizeNeeded > items.length) items = this.resize(Math.max(8, (int)(sizeNeeded * 1.75f)));
		System.arraycopy(array, start, items, this.size, count);
        this.size += count;
	}

	public T get (int index) throws IndexOutOfBoundsException {
		if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size);
		return this.items[index];
	}

	public void set (int index, T value) throws IndexOutOfBoundsException{
		if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size);
        this.items[index] = value;
	}

	public void insert (int index, T value) throws IndexOutOfBoundsException {
		if (index > this.size) throw new IndexOutOfBoundsException("index can't be > size: " + index + " > " + this.size);
		T[] items = this.items;
		if (this.size == items.length) items = this.resize(Math.max(8, (int)(this.size * 1.75f)));
		if (this.ordered)
			System.arraycopy(items, index, items, index + 1, this.size - index);
		else
			items[this.size] = items[index];
        this.size++;
		items[index] = value;
	}

	public void swap (int first, int second) throws IndexOutOfBoundsException {
		if (first >= this.size) throw new IndexOutOfBoundsException("first can't be >= size: " + first + " >= " + this.size);
		if (second >= this.size) throw new IndexOutOfBoundsException("second can't be >= size: " + second + " >= " + this.size);
		T[] items = this.items;
		T firstValue = items[first];
		items[first] = items[second];
		items[second] = firstValue;
	}

	/** Returns if this array contains value.
	 * @param value May be null.
	 * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
	 * @return true if array contains value, false if it doesn't */
	public boolean contains (T value, boolean identity) {
		T[] items = this.items;
		int i = this.size - 1;
		if (identity || value == null) {
			while (i >= 0)
				if (items[i--] == value) return true;
		} else {
			while (i >= 0)
				if (value.equals(items[i--])) return true;
		}
		return false;
	}

	/** Returns the index of first occurrence of value in the array, or -1 if no such value exists.
	 * @param value May be null.
	 * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
	 * @return An index of first occurrence of value in array or -1 if no such value exists */
	public int indexOf (T value, boolean identity) {
		T[] items = this.items;
		if (identity || value == null) {
			for (int i = 0, n = this.size; i < n; i++)
				if (items[i] == value) return i;
		} else {
			for (int i = 0, n = this.size; i < n; i++)
				if (value.equals(items[i])) return i;
		}
		return -1;
	}

	/** Returns an index of last occurrence of value in array or -1 if no such value exists. Search is started from the end of an
	 * array.
	 * @param value May be null.
	 * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
	 * @return An index of last occurrence of value in array or -1 if no such value exists */
	public int lastIndexOf (T value, boolean identity) {
		T[] items = this.items;
		if (identity || value == null) {
			for (int i = this.size - 1; i >= 0; i--)
				if (items[i] == value) return i;
		} else {
			for (int i = this.size - 1; i >= 0; i--)
				if (value.equals(items[i])) return i;
		}
		return -1;
	}

	/** Removes the first instance of the specified value in the array.
	 * @param value May be null.
	 * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
	 * @return true if value was found and removed, false otherwise */
	public boolean removeValue (T value, boolean identity) {
		T[] items = this.items;
		if (identity || value == null) {
			for (int i = 0, n = this.size; i < n; i++) {
				if (items[i] == value) {
                    this.removeIndex(i);
					return true;
				}
			}
		} else {
			for (int i = 0, n = this.size; i < n; i++) {
				if (value.equals(items[i])) {
                    this.removeIndex(i);
					return true;
				}
			}
		}
		return false;
	}

	/** Removes and returns the item at the specified index. */
	public T removeIndex (int index) throws IndexOutOfBoundsException {
		if (index >= this.size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + this.size);
		T[] items = this.items;
		T value = items[index];
        this.size--;
		if (this.ordered)
			System.arraycopy(items, index + 1, items, index, this.size - index);
		else
			items[index] = items[this.size];
		items[this.size] = null;
		return value;
	}

	/** Removes the items between the specified indices, inclusive. */
	public void removeRange (int start, int end) throws IndexOutOfBoundsException {
		if (end >= this.size) throw new IndexOutOfBoundsException("end can't be >= size: " + end + " >= " + this.size);
		if (start > end) throw new IndexOutOfBoundsException("start can't be > end: " + start + " > " + end);
		T[] items = this.items;
		int count = end - start + 1;
		if (this.ordered)
			System.arraycopy(items, start + count, items, start, this.size - (start + count));
		else {
			int lastIndex = size - 1;
			for (int i = 0; i < count; i++)
				items[start + i] = items[lastIndex - i];
		}
        this.size -= count;
	}

	/** Removes from this array all of elements contained in the specified array.
	 * @param identity True to use ==, false to use .equals().
	 * @return true if this array was modified. */
	public boolean removeAll (Array<? extends T> array, boolean identity) {
		int size = this.size;
		int startSize = size;
		T[] items = this.items;
		if (identity) {
			for (int i = 0, n = array.size; i < n; i++) {
				T item = array.get(i);
				for (int ii = 0; ii < size; ii++) {
					if (item == items[ii]) {
                        this.removeIndex(ii);
						size--;
						break;
					}
				}
			}
		} else {
			for (int i = 0, n = array.size; i < n; i++) {
				T item = array.get(i);
				for (int ii = 0; ii < size; ii++) {
					if (item.equals(items[ii])) {
                        this.removeIndex(ii);
						size--;
						break;
					}
				}
			}
		}
		return size != startSize;
	}

	/** Removes and returns the last item. */
	public T pop () {
		if (this.size == 0) throw new IllegalStateException("Array is empty.");
		--this.size;
		T item = this.items[this.size];
        this.items[this.size] = null;
		return item;
	}

	/** Returns the last item. */
	public T peek () {
		if (this.size == 0) throw new IllegalStateException("Array is empty.");
		return this.items[this.size - 1];
	}

	/** Returns the first item. */
	public T first () {
		if (this.size == 0) throw new IllegalStateException("Array is empty.");
		return this.items[0];
	}

	public void clear () {
		T[] items = this.items;
		for (int i = 0, n = this.size; i < n; i++)
			items[i] = null;
        this.size = 0;
	}

	/** Reduces the size of the backing array to the size of the actual items. This is useful to release memory when many items
	 * have been removed, or if it is known that more items will not be added.
	 * @return {@link #items} */
	public T[] shrink () {
		if (this.items.length != this.size) this.resize(this.size);
		return this.items;
	}

	/** Increases the size of the backing array to accommodate the specified number of additional items. Useful before adding many
	 * items to avoid multiple backing array resizes.
	 * @return {@link #items} */
	public T[] ensureCapacity (int additionalCapacity) {
		int sizeNeeded = this.size + additionalCapacity;
		if (sizeNeeded > this.items.length) this.resize(Math.max(8, sizeNeeded));
		return this.items;
	}

	/** Creates a new backing array with the specified size containing the current items. */
	protected T[] resize (int newSize) {
		T[] items = this.items;
		T[] newItems = (T[])ArrayReflection.newInstance(items.getClass().getComponentType(), newSize);
		System.arraycopy(items, 0, newItems, 0, Math.min(this.size, newItems.length));
		this.items = newItems;
		return newItems;
	}

	/** Sorts this array. The array elements must implement {@link Comparable}. This method is not thread safe (uses
	 * {@link Sort#instance()}). */
	public void sort () {
		Sort.instance().sort(this.items, 0, this.size);
	}

	/** Sorts the array. This method is not thread safe (uses {@link Sort#instance()}). */
	public void sort (Comparator<? super T> comparator) {
		Sort.instance().sort(this.items, comparator, 0, this.size);
	}

	/** Selects the nth-lowest element from the Array according to Comparator ranking. This might partially sort the Array. The
	 * array must have a size greater than 0, or a {@link com.badlogic.gdx.utils.GdxRuntimeException} will be thrown.
	 * @see Select
	 * @param comparator used for comparison
	 * @param kthLowest rank of desired object according to comparison, n is based on ordinal numbers, not array indices. for min
	 *           value use 1, for max value use size of array, using 0 results in runtime exception.
	 * @return the value of the Nth lowest ranked object. */
	public T selectRanked (Comparator<T> comparator, int kthLowest) {
		if (kthLowest < 1) {
			throw new GdxRuntimeException("nth_lowest must be greater than 0, 1 = first, 2 = second...");
		}
		return Select.instance().select(this.items, comparator, kthLowest, this.size);
	}

	/** @see Array#selectRanked(Comparator, int)
	 * @param comparator used for comparison
	 * @param kthLowest rank of desired object according to comparison, n is based on ordinal numbers, not array indices. for min
	 *           value use 1, for max value use size of array, using 0 results in runtime exception.
	 * @return the index of the Nth lowest ranked object. */
	public int selectRankedIndex (Comparator<T> comparator, int kthLowest) {
		if (kthLowest < 1) {
			throw new GdxRuntimeException("nth_lowest must be greater than 0, 1 = first, 2 = second...");
		}
		return Select.instance().selectIndex(this.items, comparator, kthLowest, this.size);
	}

	public void reverse () {
		T[] items = this.items;
		for (int i = 0, lastIndex = this.size - 1, n = this.size / 2; i < n; i++) {
			int ii = lastIndex - i;
			T temp = items[i];
			items[i] = items[ii];
			items[ii] = temp;
		}
	}

	public void shuffle () {
		T[] items = this.items;
		for (int i = this.size - 1; i >= 0; i--) {
			int ii = MathUtils.random(i);
			T temp = items[i];
			items[i] = items[ii];
			items[ii] = temp;
		}
	}

	/** Returns an iterator for the items in the array. Remove is supported. Note that the same iterator instance is returned each
	 * time this method is called. Use the {@link Array.ArrayIterator} constructor for nested or multithreaded iteration. */
	@Override
    public Iterator<T> iterator () {
		if (this.iterable == null) this.iterable = new Array.ArrayIterable(this);
		return this.iterable.iterator();
	}

	/** Returns an iterable for the selected items in the array. Remove is supported, but not between hasNext() and next(). Note
	 * that the same iterable instance is returned each time this method is called. Use the {@link PredicateIterable}
	 * constructor for nested or multithreaded iteration. */
	public Iterable<T> select (Predicate<T> predicate) {
		if (this.predicateIterable == null)
            this.predicateIterable = new PredicateIterable<T>(this, predicate);
		else
            this.predicateIterable.set(this, predicate);
		return this.predicateIterable;
	}

	/** Reduces the size of the array to the specified size. If the array is already smaller than the specified size, no action is
	 * taken. */
	public void truncate (int newSize) {
		if (this.size <= newSize) return;
		for (int i = newSize; i < this.size; i++)
            this.items[i] = null;
        this.size = newSize;
	}

	/** Returns a random item from the array, or null if the array is empty. */
	public T random () {
		if (this.size == 0) return null;
		return this.items[MathUtils.random(0, this.size - 1)];
	}

	/** Returns the items as an array. Note the array is typed, so the {@link #Array(Class)} constructor must have been used.
	 * Otherwise use {@link #toArray(Class)} to specify the array type. */
	public T[] toArray () {
		return (T[]) this.toArray(this.items.getClass().getComponentType());
	}

	public <V> V[] toArray (Class type) {
		V[] result = (V[])ArrayReflection.newInstance(type, this.size);
		System.arraycopy(this.items, 0, result, 0, this.size);
		return result;
	}

	public int hashCode () {
		if (!this.ordered) return super.hashCode();
		Object[] items = this.items;
		int h = 1;
		for (int i = 0, n = this.size; i < n; i++) {
			h *= 31;
			Object item = items[i];
			if (item != null) h += item.hashCode();
		}
		return h;
	}

	public boolean equals (Object object) {
		if (object == this) return true;
		if (!this.ordered) return false;
		if (!(object instanceof Array)) return false;
		Array array = (Array)object;
		if (!array.ordered) return false;
		int n = this.size;
		if (n != array.size) return false;
		Object[] items1 = items;
		Object[] items2 = array.items;
		for (int i = 0; i < n; i++) {
			Object o1 = items1[i];
			Object o2 = items2[i];
			if (o1 == null ? o2 != null : !o1.equals(o2)) return false;
		}
		return true;
	}

	public String toString () {
		if (this.size == 0) return "[]";
		T[] items = this.items;
		StringBuilder buffer = new StringBuilder(32);
		buffer.append('[');
		buffer.append(items[0]);
		for (int i = 1; i < this.size; i++) {
			buffer.append(", ");
			buffer.append(items[i]);
		}
		buffer.append(']');
		return buffer.toString();
	}

	public String toString (String separator) {
		if (this.size == 0) return "";
		T[] items = this.items;
		StringBuilder buffer = new StringBuilder(32);
		buffer.append(items[0]);
		for (int i = 1; i < this.size; i++) {
			buffer.append(separator);
			buffer.append(items[i]);
		}
		return buffer.toString();
	}

	/** @see #Array(Class) */
    public static <T> Array<T> of (Class<T> arrayType) {
		return new Array<T>(arrayType);
	}

	/** @see #Array(boolean, int, Class) */
    public static <T> Array<T> of (boolean ordered, int capacity, Class<T> arrayType) {
		return new Array<T>(ordered, capacity, arrayType);
	}

	/** @see #Array(Object[]) */
    public static <T> Array<T> with (T... array) {
		return new Array(array);
	}

	public static class ArrayIterator<T> implements Iterator<T>, Iterable<T> {
		private final Array<T> array;
		private final boolean allowRemove;
		int index;
		boolean valid = true;

// ArrayIterable<T> iterable;

		public ArrayIterator (Array<T> array) {
			this(array, true);
		}

		public ArrayIterator (Array<T> array, boolean allowRemove) {
			this.array = array;
			this.allowRemove = allowRemove;
		}

		@Override
        public boolean hasNext () {
			if (!this.valid) {
// System.out.println(iterable.lastAcquire);
				throw new GdxRuntimeException("#iterator() cannot be used nested.");
			}
			return this.index < this.array.size;
		}

		@Override
        public T next () {
			if (this.index >= this.array.size) throw new NoSuchElementException(String.valueOf(this.index));
			if (!this.valid) {
// System.out.println(iterable.lastAcquire);
				throw new GdxRuntimeException("#iterator() cannot be used nested.");
			}
			return this.array.items[this.index++];
		}

		@Override
        public void remove () {
			if (!this.allowRemove) throw new GdxRuntimeException("Remove not allowed.");
            this.index--;
            this.array.removeIndex(this.index);
		}

		public void reset () {
            this.index = 0;
		}

		@Override
        public Iterator<T> iterator () {
			return this;
		}
	}

	public static class ArrayIterable<T> implements Iterable<T> {
		private final Array<T> array;
		private final boolean allowRemove;
		private Array.ArrayIterator iterator1, iterator2;

// java.io.StringWriter lastAcquire = new java.io.StringWriter();

		public ArrayIterable (Array<T> array) {
			this(array, true);
		}

		public ArrayIterable (Array<T> array, boolean allowRemove) {
			this.array = array;
			this.allowRemove = allowRemove;
		}

		@Override
        public Iterator<T> iterator () {
// lastAcquire.getBuffer().setLength(0);
// new Throwable().printStackTrace(new java.io.PrintWriter(lastAcquire));
			if (this.iterator1 == null) {
                this.iterator1 = new Array.ArrayIterator(this.array, this.allowRemove);
                this.iterator2 = new Array.ArrayIterator(this.array, this.allowRemove);
// iterator1.iterable = this;
// iterator2.iterable = this;
			}
			if (!this.iterator1.valid) {
                this.iterator1.index = 0;
                this.iterator1.valid = true;
                this.iterator2.valid = false;
				return this.iterator1;
			}
            this.iterator2.index = 0;
            this.iterator2.valid = true;
            this.iterator1.valid = false;
			return this.iterator2;
		}
	}
}

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


import java.util.Iterator;



/** Interface used to select items within an iterator against a predicate.
 * @author Xoppa */
public interface Predicate<T> {

	/** @return true if the item matches the criteria and should be included in the iterator's items */
	boolean evaluate (T arg0);

	class PredicateIterator<T> implements Iterator<T> {
		public Iterator<T> iterator;
		public Predicate<T> predicate;
		public boolean end;
		public boolean peeked;
		public T next;

		public PredicateIterator (Iterable<T> iterable, Predicate<T> predicate) {
			this(iterable.iterator(), predicate);
		}

		public PredicateIterator (Iterator<T> iterator, Predicate<T> predicate) {
            this.set(iterator, predicate);
		}

		public void set (Iterable<T> iterable, Predicate<T> predicate) {
            this.set(iterable.iterator(), predicate);
		}

		public void set (Iterator<T> iterator, Predicate<T> predicate) {
			this.iterator = iterator;
			this.predicate = predicate;
            this.end = this.peeked = false;
            this.next = null;
		}

		@Override
		public boolean hasNext () {
			if (this.end) return false;
			if (this.next != null) return true;
            this.peeked = true;
			while (this.iterator.hasNext()) {
				T n = this.iterator.next();
				if (this.predicate.evaluate(n)) {
                    this.next = n;
					return true;
				}
			}
            this.end = true;
			return false;
		}

		@Override
		public T next () {
			if (this.next == null && !this.hasNext()) return null;
			T result = this.next;
            this.next = null;
            this.peeked = false;
			return result;
		}

		@Override
		public void remove () {
			if (this.peeked) throw new GdxRuntimeException("Cannot remove between a call to hasNext() and next().");
            this.iterator.remove();
		}
	}

	class PredicateIterable<T> implements Iterable<T> {
		public Iterable<T> iterable;
		public Predicate<T> predicate;
		public Predicate.PredicateIterator<T> iterator;

		public PredicateIterable (Iterable<T> iterable, Predicate<T> predicate) {
            this.set(iterable, predicate);
		}

		public void set (Iterable<T> iterable, Predicate<T> predicate) {
			this.iterable = iterable;
			this.predicate = predicate;
		}

		/** Returns an iterator. Note that the same iterator instance is returned each time this method is called. Use the
		 * {@link PredicateIterator} constructor for nested or multithreaded iteration. */
		@Override
		public Iterator<T> iterator () {
			if (this.iterator == null)
                this.iterator = new Predicate.PredicateIterator<T>(this.iterable.iterator(), this.predicate);
			else
                this.iterator.set(this.iterable.iterator(), this.predicate);
			return this.iterator;
		}
	}
}
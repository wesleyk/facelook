package edu.cmu.cs214.hw9.db;

import java.util.*;

/**
 * Purpose: Cache structure extending the LinkedHashMap structure
 * @author Wesley, Jessica, Nikhil
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public class Cache<K,V> extends LinkedHashMap<K,V> {
	private final int capacity;
	
	/**
	 * Constructor
	 * @param capacity max capacity of the cache
	 */
	public Cache(int capacity) {
		super(capacity);
		this.capacity = capacity;
	}
	
	/**
	 * Override the removeEldestEntry method to specify
	 * that an element should be removed if the size is greater
	 * than the capacity. This is according to LRU policy for eviction.
	 */
	@Override
	protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
		return size() > capacity;
	}
}
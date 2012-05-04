package edu.cmu.cs214.hw9.db;

import java.util.*;

public class Cache<K,V> extends LinkedHashMap<K,V> {
	private final int capacity;
	
	public Cache(int capacity) {
		super(capacity);
		this.capacity = capacity;
	}
	
	protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
		return size() > capacity;
	}
}
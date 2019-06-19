package com.apptivedeals.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class InMemoryCache<K, T> {
	private long timeToLive;
	private Map<K, CacheObject> cacheMap;
	private Thread t;
	
	protected class CacheObject {
		public long accessed = System.currentTimeMillis();
		public T value;
		
		protected CacheObject(T value) {
			this.value = value;
		} 
	}
	
	public InMemoryCache(long timeToLive, final long timerInterval) {
		this.timeToLive = timeToLive;
		cacheMap = new HashMap<K, CacheObject>();
		
		if (timeToLive > 0 && timerInterval > 0) {
			t = new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						try {
							Thread.sleep(timerInterval);
						}
						catch (InterruptedException ex) {
							
						}
						cleanup();
					}
				}
			});
			
			t.setDaemon(true);
			t.start();
		}
	}
	
	public void put(K key, T value) {
		synchronized(cacheMap) {
			cacheMap.put(key, new CacheObject(value));
		}
	}
	
	public T get(K key) {
		synchronized(cacheMap) {
			CacheObject c = cacheMap.get(key);
			
			if (c == null) {
				return null;
			}
			else {
				c.accessed = System.currentTimeMillis();
				return c.value;
			}
		}
	}
	
	public boolean containsKey(K key) {
		synchronized(cacheMap) {
			return cacheMap.containsKey(key);
		}
	} 
	
	public void remove(K key) {
		synchronized(cacheMap) {
			cacheMap.remove(key);
		}
	}
	
	public long size() {
		synchronized(cacheMap) {
			return cacheMap.size();
		}
	}
	
	private void cleanup() {
		long now = System.currentTimeMillis();
		Set<K> deleteKey = null;
		
		synchronized(cacheMap) {
			Iterator<Map.Entry<K, InMemoryCache<K, T>.CacheObject>> it = cacheMap.entrySet().iterator();
			deleteKey = new HashSet<K>();
			CacheObject c = null;
			K key = null;
			
			while (it.hasNext()) {
				Map.Entry<K, CacheObject> pair = (Map.Entry<K, CacheObject>) it.next();
				key = (K) pair.getKey();
				c = (CacheObject) pair.getValue();
				
				if (c != null && (now > (timeToLive + c.accessed))) {
					deleteKey.add(key);
				}
			}
		}
		
		for (K key : deleteKey) {
			synchronized (cacheMap) {
				cacheMap.remove(key);
			}
			
			Thread.yield();
		}
	}
}

package edu.isi.bmkeg.utils;

import java.util.*;
import java.util.Iterator;

/**
 * 
 * @author tommying
 */
public class FrequencyCounter {

	private HashMap freq = new HashMap();

	public void add(FrequencyCounter fc) {
		Iterator it = this.freq.keySet().iterator();
		while (it.hasNext()) {
			Object o = it.next();
			int c1 = fc.getCount(o);
			int c2 = this.getCount(o);
			this.freq.put(o, new Integer(c1 + c2));
		}
	}

	public void add(Object o) {
		int c = this.getCount(o);
		Integer count = new Integer(c + 1);
		this.freq.put(o, count);
	}

	public Object getMostPopular() {
		int max = 0;
		Object mp = null;
		Iterator it = this.freq.keySet().iterator();
		while (it.hasNext()) {
			Object MP = it.next();
			int c = getCount(MP);
			if (c > max) {
				mp = MP;
				max = c;
			}
		}

		return mp;
	}

	public int getCount(Object o) {
		int c = 0;
		if (this.freq.containsKey(o)) {
			Integer count = (Integer) this.freq.get(o);
			c = count.intValue();
		}

		return c;
	}

	public int countOptions() {
		return this.freq.size();
	}

	public void reset() {
		this.freq.clear();

	}
}

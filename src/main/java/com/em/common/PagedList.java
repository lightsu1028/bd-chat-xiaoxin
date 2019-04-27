package com.em.common;

import java.util.Iterator;
import java.util.List;


public class PagedList<T> implements Iterable<T> {

	private List<T> rows;
	private long total;

	@SuppressWarnings("unchecked")
//	public PagedList(com.em.fx.core.dto.Page<?> page) {
//		if (null != page) {
//			this.rows = (List<T>) page.getData();
//			this.total = page.getRecordsTotal();
//		}
//	}
	
	public PagedList(List<T> rows, long total) {
		this.rows = rows;
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public T get(int index) {
		return rows.get(index);
	}

	public int size() {
		return rows.size();
	}

	public long getTotal() {
		return total;
	}

	@Override
	public Iterator<T> iterator() {
		return rows.iterator();
	}

}

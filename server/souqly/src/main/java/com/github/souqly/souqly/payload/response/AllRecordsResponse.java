package com.github.souqly.souqly.payload.response;

import java.util.List;

public class AllRecordsResponse<T> {
	private List<T> content;
	private long totalElements;

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

}

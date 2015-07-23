package io.pivotal.rentme.poc;

import java.util.HashSet;
import java.util.Set;

public class DataExportRequest {
	private Long startDate;
	private Long endDate;
	private Set<String> columns = new HashSet<String>();
	
	public Long getStartDate() {
		return startDate;
	}
	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}
	public Long getEndDate() {
		return endDate;
	}
	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}
	public Set<String> getColumns() {
		return columns;
	}
	public void setColumns(Set<String> columns) {
		this.columns = columns;
	}
}

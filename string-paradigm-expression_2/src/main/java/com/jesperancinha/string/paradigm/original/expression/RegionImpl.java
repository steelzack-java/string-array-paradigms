package com.jesperancinha.string.paradigm.original.expression;

import com.jesperancinha.string.paradigm.api.Region;

public class RegionImpl extends Region {
	public RegionImpl(String type, String size) {
		this.regionName = type;
		this.assignedNumber = size;
	}

	public String getRegionName() {
		return regionName;
	}

	public String getAssignedNumber() {
		return assignedNumber;
	}

	public String getDescription() {
		return regionName + ", " + assignedNumber;
	}

	@Override
	public String toString() {
		return getDescription();
	}

	@Override
	public boolean equals(Object obj) {
		return obj.toString().equals(this.toString());
	}
}

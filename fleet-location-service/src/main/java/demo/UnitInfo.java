/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package demo;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Embeddable
public class UnitInfo {

	private final String unitVin;
	private String engineMake;
	private String customerName;
	private String unitNumber;

	public UnitInfo(String unitVin) {
		this.unitVin = unitVin;
	}

	public UnitInfo(String unitVin, String engineMake, String customerName,
			String unitNumber) {
		this.unitVin = unitVin;
		this.engineMake = engineMake;
		this.customerName = customerName;
		this.unitNumber = unitNumber;
	}

	@SuppressWarnings("unused")
	private UnitInfo() {
		this.unitVin = "";
	}

	public String getUnitVin() {
		return unitVin;
	}

	public String getEngineMake() {
		return engineMake;
	}

	public void setEngineMake(String engineMake) {
		this.engineMake = engineMake;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UnitInfo unitInfo = (UnitInfo) o;

		if (unitVin != null ?
				!unitVin.equals(unitInfo.unitVin) :
				unitInfo.unitVin != null)
			return false;
		if (engineMake != null ?
				!engineMake.equals(unitInfo.engineMake) :
				unitInfo.engineMake != null) return false;
		if (customerName != null ?
				!customerName.equals(unitInfo.customerName) :
				unitInfo.customerName != null) return false;
		return !(unitNumber != null ?
				!unitNumber.equals(unitInfo.unitNumber) :
				unitInfo.unitNumber != null);
	}

	@Override public int hashCode() {
		int result = unitVin != null ? unitVin.hashCode() : 0;
		result = 31 * result + (engineMake != null ? engineMake.hashCode() : 0);
		result = 31 * result + (customerName != null ? customerName.hashCode() : 0);
		result = 31 * result + (unitNumber != null ? unitNumber.hashCode() : 0);
		return result;
	}

	@Override public String toString() {
		return "UnitInfo{" +
				"unitVin='" + unitVin + '\'' +
				", engineMake='" + engineMake + '\'' +
				", customerName='" + customerName + '\'' +
				", unitNumber='" + unitNumber + '\'' +
				'}';
	}
}

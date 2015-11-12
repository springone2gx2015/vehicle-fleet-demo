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
public class UnitFault {

	private final String vin;
	private Long spn;
	private Long fmi;

	public UnitFault(String vin) {
		this.vin = vin;
	}

	@SuppressWarnings("unused")
	private UnitFault() {
		this.vin = "";
	}

	public String getVin() {
		return vin;
	}

	public Long getSpn() {
		return spn;
	}

	public void setSpn(Long spn) {
		this.spn = spn;
	}

	public Long getFmi() {
		return fmi;
	}

	public void setFmi(Long fmi) {
		this.fmi = fmi;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UnitFault unitFault = (UnitFault) o;

		if (vin != null ? !vin.equals(unitFault.vin) : unitFault.vin != null)
			return false;
		if (spn != null ? !spn.equals(unitFault.spn) : unitFault.spn != null)
			return false;
		return !(fmi != null ? !fmi.equals(unitFault.fmi) : unitFault.fmi != null);

	}

	@Override public int hashCode() {
		int result = vin != null ? vin.hashCode() : 0;
		result = 31 * result + (spn != null ? spn.hashCode() : 0);
		result = 31 * result + (fmi != null ? fmi.hashCode() : 0);
		return result;
	}

	@Override public String toString() {
		return "UnitFault{" +
				"vin='" + vin + '\'' +
				", spn=" + spn +
				", fmi=" + fmi +
				'}';
	}
}
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

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Embeddable
public class FaultCode {
	private String engineMake;
	private String faultCode;
	private String faultCodeId;
	private String faultCodeClassification;
	private String description;
	@Column(length=1024)
	private String repairInstructions;
	private Long fmi;
	private String sa;
	private Long spn;

	public FaultCode() {}

	public FaultCode(String engineMake,
			String faultCode,
			String faultCodeId,
			String faultCodeClassification,
			String description,
			String repairInstructions,
			Long fmi,
			String sa,
			Long spn) {
		this.engineMake = engineMake;
		this.faultCode = faultCode;
		this.faultCodeId = faultCodeId;
		this.faultCodeClassification = faultCodeClassification;
		this.description = description;
		this.repairInstructions = repairInstructions;
		this.fmi = fmi;
		this.sa = sa;
		this.spn = spn;
	}

	public String getEngineMake() {
		return engineMake;
	}

	public void setEngineMake(String engineMake) {
		this.engineMake = engineMake;
	}

	public String getFaultCode() {
		return faultCode;
	}

	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}

	public String getFaultCodeId() {
		return faultCodeId;
	}

	public void setFaultCodeId(String faultCodeId) {
		this.faultCodeId = faultCodeId;
	}

	public String getFaultCodeClassification() {
		return faultCodeClassification;
	}

	public void setFaultCodeClassification(String faultCodeClassification) {
		this.faultCodeClassification = faultCodeClassification;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRepairInstructions() {
		return repairInstructions;
	}

	public void setRepairInstructions(String repairInstructions) {
		this.repairInstructions = repairInstructions;
	}

	public Long getFmi() {
		return fmi;
	}

	public void setFmi(Long fmi) {
		this.fmi = fmi;
	}

	public String getSa() {
		return sa;
	}

	public void setSa(String sa) {
		this.sa = sa;
	}

	public Long getSpn() {
		return spn;
	}

	public void setSpn(Long spn) {
		this.spn = spn;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FaultCode faultCode1 = (FaultCode) o;

		if (engineMake != null ?
				!engineMake.equals(faultCode1.engineMake) :
				faultCode1.engineMake != null) return false;
		if (faultCode != null ?
				!faultCode.equals(faultCode1.faultCode) :
				faultCode1.faultCode != null) return false;
		if (faultCodeId != null ?
				!faultCodeId.equals(faultCode1.faultCodeId) :
				faultCode1.faultCodeId != null) return false;
		if (faultCodeClassification != null ?
				!faultCodeClassification.equals(faultCode1.faultCodeClassification) :
				faultCode1.faultCodeClassification != null) return false;
		if (description != null ?
				!description.equals(faultCode1.description) :
				faultCode1.description != null) return false;
		if (repairInstructions != null ?
				!repairInstructions.equals(faultCode1.repairInstructions) :
				faultCode1.repairInstructions != null) return false;
		if (fmi != null ? !fmi.equals(faultCode1.fmi) : faultCode1.fmi != null)
			return false;
		if (sa != null ? !sa.equals(faultCode1.sa) : faultCode1.sa != null) return false;
		return !(spn != null ? !spn.equals(faultCode1.spn) : faultCode1.spn != null);

	}

	@Override public int hashCode() {
		int result = engineMake != null ? engineMake.hashCode() : 0;
		result = 31 * result + (faultCode != null ? faultCode.hashCode() : 0);
		result = 31 * result + (faultCodeId != null ? faultCodeId.hashCode() : 0);
		result = 31 * result + (faultCodeClassification != null ?
				faultCodeClassification.hashCode() :
				0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (repairInstructions != null ?
				repairInstructions.hashCode() :
				0);
		result = 31 * result + (fmi != null ? fmi.hashCode() : 0);
		result = 31 * result + (sa != null ? sa.hashCode() : 0);
		result = 31 * result + (spn != null ? spn.hashCode() : 0);
		return result;
	}

	@Override public String toString() {
		return "FaultCode{" +
				"engineMake='" + engineMake + '\'' +
				", faultCode='" + faultCode + '\'' +
				", faultCodeId='" + faultCodeId + '\'' +
				", faultCodeClassification='" + faultCodeClassification + '\'' +
				", description='" + description + '\'' +
				", repairInstructions='" + repairInstructions + '\'' +
				", fmi=" + fmi +
				", sa='" + sa + '\'' +
				", spn=" + spn +
				'}';
	}
}

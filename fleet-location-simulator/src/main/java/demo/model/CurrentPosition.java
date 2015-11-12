/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package demo.model;
/**
 * @author Gunnar Hillert
 * @author David Turanski
 */

public class CurrentPosition {

	private String vin;
	private Point location;
	private VehicleStatus vehicleStatus = VehicleStatus.NONE;
	private Double speed;
	private Double heading;
	private FaultCode faultCode;

	public CurrentPosition(String vin, Point location, VehicleStatus vehicleStatus,
			Double speed, Double heading, FaultCode faultCode) {
		this.vin = vin;
		this.location = location;
		this.vehicleStatus = vehicleStatus;
		this.speed = speed;
		this.heading = heading;
		this.faultCode = faultCode;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public VehicleStatus getVehicleStatus() {
		return vehicleStatus;
	}

	public void setVehicleStatus(VehicleStatus vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Double getHeading() {
		return heading;
	}

	public void setHeading(Double heading) {
		this.heading = heading;
	}

	public FaultCode getFaultCode() {
		return faultCode;
	}

	public void setFaultCode(FaultCode faultCode) {
		this.faultCode = faultCode;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CurrentPosition that = (CurrentPosition) o;

		if (vin != null ? !vin.equals(that.vin) : that.vin != null) return false;
		if (location != null ? !location.equals(that.location) : that.location != null)
			return false;
		if (vehicleStatus != that.vehicleStatus) return false;
		if (speed != null ? !speed.equals(that.speed) : that.speed != null) return false;
		if (heading != null ? !heading.equals(that.heading) : that.heading != null)
			return false;
		return !(faultCode != null ?
				!faultCode.equals(that.faultCode) :
				that.faultCode != null);

	}

	@Override public int hashCode() {
		int result = vin != null ? vin.hashCode() : 0;
		result = 31 * result + (location != null ? location.hashCode() : 0);
		result = 31 * result + (vehicleStatus != null ? vehicleStatus.hashCode() : 0);
		result = 31 * result + (speed != null ? speed.hashCode() : 0);
		result = 31 * result + (heading != null ? heading.hashCode() : 0);
		result = 31 * result + (faultCode != null ? faultCode.hashCode() : 0);
		return result;
	}

	@Override public String toString() {
		return "CurrentPosition{" +
				"vin='" + vin + '\'' +
				", location=" + location +
				", vehicleStatus=" + vehicleStatus +
				", speed=" + speed +
				", heading=" + heading +
				", faultCode=" + faultCode +
				'}';
	}
}

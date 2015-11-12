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
 * POJO to hold position of the vehiclqe and some other data.
 * @author faram
 * @author Gunnar Hillert
 * @author David Turanski
 */
public class PositionInfo {

	private String vin;
	private Point position;
	private VehicleStatus vehicleStatus = VehicleStatus.NONE;

	/**
	 * kml path is composed of a series of legs (line segments) 1 .. n.
	 * this member denotes the present leg (starting at leg 0)
	 */
	private Leg leg;

	/**
	 * Meters from start of leg
	 */
	private Double distanceFromStart;

	/**
	 * The speed in m/s
	 */
	private Double speed;

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public VehicleStatus getVehicleStatus() {
		return vehicleStatus;
	}

	public void setVehicleStatus(VehicleStatus vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}

	public Leg getLeg() {
		return leg;
	}

	public void setLeg(Leg leg) {
		this.leg = leg;
	}

	public Double getDistanceFromStart() {
		return distanceFromStart;
	}

	public void setDistanceFromStart(Double distanceFromStart) {
		this.distanceFromStart = distanceFromStart;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PositionInfo that = (PositionInfo) o;

		if (vin != null ? !vin.equals(that.vin) : that.vin != null) return false;
		if (position != null ? !position.equals(that.position) : that.position != null)
			return false;
		if (vehicleStatus != that.vehicleStatus) return false;
		if (leg != null ? !leg.equals(that.leg) : that.leg != null) return false;
		if (distanceFromStart != null ?
				!distanceFromStart.equals(that.distanceFromStart) :
				that.distanceFromStart != null) return false;
		return !(speed != null ? !speed.equals(that.speed) : that.speed != null);

	}

	@Override public int hashCode() {
		int result = vin != null ? vin.hashCode() : 0;
		result = 31 * result + (position != null ? position.hashCode() : 0);
		result = 31 * result + (vehicleStatus != null ? vehicleStatus.hashCode() : 0);
		result = 31 * result + (leg != null ? leg.hashCode() : 0);
		result = 31 * result + (distanceFromStart != null ?
				distanceFromStart.hashCode() :
				0);
		result = 31 * result + (speed != null ? speed.hashCode() : 0);
		return result;
	}

	@Override public String toString() {
		return "PositionInfo{" +
				"vin='" + vin + '\'' +
				", position=" + position +
				", vehicleStatus=" + vehicleStatus +
				", leg=" + leg +
				", distanceFromStart=" + distanceFromStart +
				", speed=" + speed +
				'}';
	}
}

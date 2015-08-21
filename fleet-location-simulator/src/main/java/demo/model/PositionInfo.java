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
 * POJO to hold position of the vehicle and some other data.
 * @author faram
 */
public class PositionInfo {

	private String vin;
	private Point position;
	private VehicleStatus vehicleStatus = VehicleStatus.NONE;

	//kml path is composed of a series of legs (line segments) 1 .. n.
	// this member denotes present leg (starting at leg 0)
	private Leg leg;
	//metres from start of leg
	private Double distanceFromStart;
	private Double speed;       // m/s

	/**
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Point position) {
		this.position = position;
	}

	/**
	 * @return the distanceFromStart
	 */
	public Double getDistanceFromStart() {
		return distanceFromStart;
	}

	/**
	 * @param distanceFromStart the distanceFromStart to set
	 */
	public void setDistanceFromStart(Double distanceFromStart) {
		this.distanceFromStart = distanceFromStart;
	}

	/**
	 * @return the leg
	 */
	public Leg getLeg() {
		return leg;
	}

	/**
	 * @param leg the leg to set
	 */
	public void setLeg(Leg leg) {
		this.leg = leg;
	}

	/**
	 * @return the speed
	 */
	public Double getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public VehicleStatus getVehicleStatus() {
		return vehicleStatus;
	}

	public void setVehicleStatus(VehicleStatus vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	@Override
	public String toString() {
		return "PositionInfo [position=" + position + ", leg=" + leg + ", distanceFromStart=" + distanceFromStart
				+ ", speed=" + speed + "]";
	}

	public enum VehicleStatus { NONE, SERVICE_INFO, SERVICE_SOON, SERVICE_NOW, STOP_NOW }

}

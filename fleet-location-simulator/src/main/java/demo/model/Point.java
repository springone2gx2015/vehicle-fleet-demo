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
 * Denotes a point on the globe.
 * @author faram
 * @author Gunnar Hillert
 * @author David Turanski
 */
public class Point {
	private Double latitude;
	private Double longitude;

	public Point(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Double getLatitude() {

		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Point point = (Point) o;

		if (latitude != null ? !latitude.equals(point.latitude) : point.latitude != null)
			return false;
		return !(longitude != null ?
				!longitude.equals(point.longitude) :
				point.longitude != null);

	}

	@Override public int hashCode() {
		int result = latitude != null ? latitude.hashCode() : 0;
		result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Point [lat/lang:" + latitude + "," + longitude + "]";
	}

}

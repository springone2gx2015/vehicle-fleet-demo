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

package demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * @author Dave Syer
 * @author Gunnar Hillert
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceLocation {

	private String id;
	private String address1;
	private String address2;
	private String city;
	@JsonUnwrapped
	private final Point point;
	private String location;
	private String state;
	private String zip;
	private String type;

	@SuppressWarnings("unused")
	private ServiceLocation() {
		this.point = new Point(0d, 0d);
	}

	@JsonCreator
	public ServiceLocation(@JsonProperty("latitude") double latitude, @JsonProperty("longitude") double longitude) {
		this.point = new Point(latitude, longitude);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Point getPoint() {
		return point;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ServiceLocation that = (ServiceLocation) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (address1 != null ? !address1.equals(that.address1) : that.address1 != null)
			return false;
		if (address2 != null ? !address2.equals(that.address2) : that.address2 != null)
			return false;
		if (city != null ? !city.equals(that.city) : that.city != null) return false;
		if (point != null ? !point.equals(that.point) : that.point != null) return false;
		if (location != null ? !location.equals(that.location) : that.location != null)
			return false;
		if (state != null ? !state.equals(that.state) : that.state != null) return false;
		if (zip != null ? !zip.equals(that.zip) : that.zip != null) return false;
		return !(type != null ? !type.equals(that.type) : that.type != null);

	}

	@Override public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (address1 != null ? address1.hashCode() : 0);
		result = 31 * result + (address2 != null ? address2.hashCode() : 0);
		result = 31 * result + (city != null ? city.hashCode() : 0);
		result = 31 * result + (point != null ? point.hashCode() : 0);
		result = 31 * result + (location != null ? location.hashCode() : 0);
		result = 31 * result + (state != null ? state.hashCode() : 0);
		result = 31 * result + (zip != null ? zip.hashCode() : 0);
		result = 31 * result + (type != null ? type.hashCode() : 0);
		return result;
	}

	@Override public String toString() {
		return "ServiceLocation{" +
				"id='" + id + '\'' +
				", address1='" + address1 + '\'' +
				", address2='" + address2 + '\'' +
				", city='" + city + '\'' +
				", point=" + point +
				", location='" + location + '\'' +
				", state='" + state + '\'' +
				", zip='" + zip + '\'' +
				", type='" + type + '\'' +
				'}';
	}
}

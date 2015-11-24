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
 * Path leg. A path is made up of a series of legs (line segments).
 *
 * @author faram
 * @author Gunnar Hillert
 * @author Gunnar Hillert
 */
public class Leg {
	private Integer id;
	private Point startPosition;
	private Point endPosition;
	private Double length;
	private Double heading;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Point getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(Point startPosition) {
		this.startPosition = startPosition;
	}

	public Point getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(Point endPosition) {
		this.endPosition = endPosition;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getHeading() {
		return heading;
	}

	public void setHeading(Double heading) {
		this.heading = heading;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Leg leg = (Leg) o;

		if (id != null ? !id.equals(leg.id) : leg.id != null) return false;
		if (startPosition != null ?
				!startPosition.equals(leg.startPosition) :
				leg.startPosition != null) return false;
		if (endPosition != null ?
				!endPosition.equals(leg.endPosition) :
				leg.endPosition != null) return false;
		if (length != null ? !length.equals(leg.length) : leg.length != null)
			return false;
		return !(heading != null ? !heading.equals(leg.heading) : leg.heading != null);

	}

	@Override public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (startPosition != null ? startPosition.hashCode() : 0);
		result = 31 * result + (endPosition != null ? endPosition.hashCode() : 0);
		result = 31 * result + (length != null ? length.hashCode() : 0);
		result = 31 * result + (heading != null ? heading.hashCode() : 0);
		return result;
	}

	@Override public String toString() {
		return "Leg{" +
				"id=" + id +
				", startPosition=" + startPosition +
				", endPosition=" + endPosition +
				", length=" + length +
				", heading=" + heading +
				'}';
	}
}

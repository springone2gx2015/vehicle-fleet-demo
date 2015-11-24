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
 *
 * @author Gunnar Hillert
 * @author David Turanski
 */
public class DirectionInput {

	private String from;
	private String to;

	public DirectionInput() {}

	public DirectionInput(String from, String to) {
		this.from = from;
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DirectionInput that = (DirectionInput) o;

		if (from != null ? !from.equals(that.from) : that.from != null) return false;
		return !(to != null ? !to.equals(that.to) : that.to != null);
	}

	@Override public int hashCode() {
		int result = from != null ? from.hashCode() : 0;
		result = 31 * result + (to != null ? to.hashCode() : 0);
		return result;
	}

	@Override public String toString() {
		return "DirectionInput{" +
				"from='" + from + '\'' +
				", to='" + to + '\'' +
				'}';
	}
}

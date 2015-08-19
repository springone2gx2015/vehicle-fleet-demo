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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Dave Syer
 *
 */
@Configuration
@EnableAutoConfiguration
public class FleetLocationRandomizer implements CommandLineRunner {

	private static final byte[] DEFAULT_CODEC = "1234567890".getBytes();
	private static Map<String, String> FAULT_DESCRIPTIONS = new HashMap<String, String>();
	private static Map<String, String> FAULT_REPAIRS = new HashMap<String, String>();
	private SecureRandom random = new SecureRandom();

	@Value("${companies.txt:classpath:/companies.txt}")
	private Resource companies;

	@Value("${fleet.json:classpath:/fleet.json}")
	private Resource fleet;

	static {
		FAULT_DESCRIPTIONS.put("MJR", "Engine Failure");
		FAULT_DESCRIPTIONS.put("FMW", "Firmware Upgrade Required");
		FAULT_DESCRIPTIONS.put("OHT", "Overheating Warning");
		FAULT_DESCRIPTIONS.put("LTP", "Low Tire Pressure Warning");
		FAULT_DESCRIPTIONS.put("LOP", "Low oil pressure warning");
	}

	static {
		FAULT_REPAIRS
		.put("MJR", "Tow or haul to nearest service station for replacement");
		FAULT_REPAIRS
		.put("FMW",
				"Verify Software update has been completed if available for this engine.");
		FAULT_REPAIRS.put("OHT", "Cool it down with some ice from freezer");
		FAULT_REPAIRS.put("LTP", "Re-inflate tires as soon as possible");
		FAULT_REPAIRS.put("LOP", "Refill oil and check for leaks");
	}

	@Autowired
	ObjectMapper mapper;
	private ArrayList<String> customerNames;

	@Override
	public void run(String... args) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> location : loadJson(this.fleet)) {
			list.add(randomize(location));
		}
		this.mapper.writeValue(
				new FileSystemResource("new_fleet.json").getOutputStream(), list);
	}

	private Map<String, Object> randomize(Map<String, Object> location) {
		String vin = UUID.randomUUID().toString();
		if (location.containsKey("vin")) {
			location.put("vin", vin);
		}
		if (location.get("unitInfo") != null) {
			@SuppressWarnings("unchecked")
			Map<String, Object> unitInfo = (Map<String, Object>) location.get("unitInfo");
			if (unitInfo.containsKey("unitVin")) {
				unitInfo.put("unitVin", vin);
			}
			if (unitInfo.containsKey("customerName")) {
				unitInfo.put("customerName", getRandomCustomerName(unitInfo));
			}
			if (unitInfo.containsKey("unitNumber")) {
				unitInfo.put("unitNumber", getRandomUnitNumber(unitInfo));
			}
		}
		if (location.get("unitFault") != null) {
			@SuppressWarnings("unchecked")
			Map<String, Object> unitFault = (Map<String, Object>) location
			.get("unitFault");
			if (unitFault.containsKey("vin")) {
				unitFault.put("vin", vin);
			}
		}
		if (location.get("faultCode") != null) {
			@SuppressWarnings("unchecked")
			Map<String, Object> faultCode = (Map<String, Object>) location
			.get("faultCode");
			if (faultCode.containsKey("faultCode")) {
				faultCode.putAll(getRandomFaultCode(faultCode));
			}
		}
		return location;
	}

	private Map<String, Object> getRandomFaultCode(Map<String, Object> unitFault) {
		Map<String, Object> codes = new LinkedHashMap<String, Object>();
		int next = this.random.nextInt(FAULT_DESCRIPTIONS.size());
		int i = 0;
		for (String code : FAULT_DESCRIPTIONS.keySet()) {
			if (i++ == next) {
				codes.put("faultCode", code);
				codes.put("description", FAULT_DESCRIPTIONS.get(code));
				codes.put("repairInstructions", FAULT_REPAIRS.get(code));
				return codes;
			}
		}
		return codes;
	}

	private Object getRandomUnitNumber(Map<String, Object> unitInfo) {
		byte[] chars = new byte[8];
		this.random.nextBytes(chars);
		for (int i = 0; i < chars.length; i++) {
			chars[i] = DEFAULT_CODEC[((chars[i] & 0xFF) % DEFAULT_CODEC.length)];
		}
		return new String(chars);

	}

	private Object getRandomCustomerName(Map<String, Object> unitInfo) {
		try {
			if (this.customerNames == null) {
				this.customerNames = new ArrayList<String>();
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						this.companies.getInputStream()));
				String line;
				line = reader.readLine();
				while (line != null) {
					this.customerNames.add(line);
					line = reader.readLine();
				}
			}
		}
		catch (IOException e) {
			throw new IllegalStateException("Cannot read company names", e);
		}
		return this.customerNames.get(this.random.nextInt(this.customerNames.size()));
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(FleetLocationRandomizer.class).web(false)
		.properties("spring.jackson.serialization.INDENT_OUTPUT=true").run(args);
	}

	private List<Map<String, Object>> loadJson(Resource resource) throws IOException,
	JsonParseException, JsonMappingException {
		return this.mapper.readValue(resource.getInputStream(),
				new TypeReference<List<Map<String, Object>>>() {
		});
	}

}

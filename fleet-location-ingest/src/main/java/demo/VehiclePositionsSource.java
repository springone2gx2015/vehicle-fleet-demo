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

import org.springframework.cloud.stream.annotation.EnableModule;
import org.springframework.cloud.stream.annotation.Source;

/**
 * Spring Cloud Stream {@link Source}, responsible for ingesting vehicle position
 * data.
 *
 * @author Gunnar Hillert
 *
 */
@EnableModule(Source.class)
public class VehiclePositionsSource {

}

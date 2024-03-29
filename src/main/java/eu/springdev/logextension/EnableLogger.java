/**
 * Copyright (C) 2018 Idan Rozenfeld the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.springdev.logextension;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Indicates that Logger support should be enabled.
 *
 * This should be applied to a Spring java config and should have an accompanying '@Configuration' annotation.
 * <p>
 * you can override the log message format by creating {@link LoggerFormats} bean.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(LoggerConfiguration.class)
public @interface EnableLogger {
}

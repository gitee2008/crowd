/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glaf.matrix.dataimport.parser;

import org.apache.commons.lang3.StringUtils;

public class ParserFactory {

	protected final static DataParser poiXlsParser = new POIXlsParser();

	protected final static DataParser poiXlsxParser = new POIXlsParser();

	private static class ParserFactoryHolder {
		public static ParserFactory instance = new ParserFactory();
	}

	public static ParserFactory getInstance() {
		return ParserFactoryHolder.instance;
	}

	private ParserFactory() {

	}

	public DataParser getParser(String fileType) {
		if (StringUtils.equalsIgnoreCase(fileType, "xls")) {
			return poiXlsParser;
		} else if (StringUtils.equalsIgnoreCase(fileType, "xlsx")) {
			return poiXlsxParser;
		}
		return null;
	}
}
package com.example.jwt_demo.common.utils;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	public static String toJson(Object o) {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			mapper.configure(Feature.ESCAPE_NON_ASCII, true);
			json = mapper.writeValueAsString(o);
		} catch (Exception e) {
		}
		return json;
	}

}

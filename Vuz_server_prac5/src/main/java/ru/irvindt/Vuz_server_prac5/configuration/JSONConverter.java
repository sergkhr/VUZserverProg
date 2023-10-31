package ru.irvindt.Vuz_server_prac5.configuration;

import org.springframework.stereotype.Component;
import com.google.gson.Gson;

@Component
public class JSONConverter {
	private final Gson gson = new Gson();

	public String toJSON(Object object) {
		return gson.toJson(object);
	}

	public <T> T fromJSON(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}
}

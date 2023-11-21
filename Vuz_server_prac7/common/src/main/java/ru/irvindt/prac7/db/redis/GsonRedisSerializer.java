package ru.irvindt.prac7.db.redis;

import com.google.gson.Gson;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class GsonRedisSerializer<T> implements RedisSerializer<T> {

	private final Class<T> cl;
	private final Gson gson;

	public GsonRedisSerializer(Class<T> cl, Gson gson) {
		this.cl = cl;
		this.gson = gson;
	}

	@Override
	public byte[] serialize(T t) throws SerializationException {
		return gson.toJson(t).getBytes();
	}

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		if (bytes == null) {
			return null;
		}
		return gson.fromJson(new String(bytes), cl);
	}
}

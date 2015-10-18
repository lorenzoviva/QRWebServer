package com.ogc.utility;

import java.lang.reflect.Type;
import java.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ogc.model.QRFreeDraw;

public class GsonHelper {
	public static final Gson customGson = new GsonBuilder().registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter()).registerTypeAdapter(QRFreeDraw.class, new QRFreeDrawTypeAdapter()).create();

	// Using Android's base64 libraries. This can be replaced with any base64
	// library.
	private static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
		  public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	            return Base64.getDecoder().decode(json.getAsString());
	        }

	        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
	            return new JsonPrimitive(Base64.getEncoder().encodeToString(src));
	        }
	}
	private static class QRFreeDrawTypeAdapter implements JsonSerializer<QRFreeDraw>, JsonDeserializer<QRFreeDraw> {
		

	        public JsonElement serialize(QRFreeDraw src, Type typeOfSrc, JsonSerializationContext context) {
	        	JsonElement json =  (new Gson()).toJsonTree(src);
	        	JsonObject asJsonObject = json.getAsJsonObject();
				asJsonObject.remove("img");
	        	JsonElement img = customGson.toJsonTree(src.getImg());
	        	asJsonObject.add("img", img);
	            return asJsonObject;
	        }

			@Override
			public QRFreeDraw deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
				JsonElement img = json.getAsJsonObject().get("img");
				QRFreeDraw qrfreedraw = (new Gson()).fromJson(json, QRFreeDraw.class);
				qrfreedraw.setImg(customGson.fromJson(img, byte[].class));
				return qrfreedraw;
			}
	}
}
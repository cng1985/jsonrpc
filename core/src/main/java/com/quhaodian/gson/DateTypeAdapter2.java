package com.quhaodian.gson;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DateTypeAdapter2 extends TypeAdapter<Date> {
	public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
		@SuppressWarnings("unchecked")
		// we use a runtime check to make sure the 'T's equal
		public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
			return typeToken.getRawType() == Date.class ? (TypeAdapter<T>) new DateTypeAdapter()
					: null;
		}
	};
	private final DateFormat chineseFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private final DateFormat enUsFormat = DateFormat.getDateTimeInstance(
			DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
	private final DateFormat localFormat = DateFormat.getDateTimeInstance(
			DateFormat.DEFAULT, DateFormat.DEFAULT);
	private final DateFormat iso8601Format = buildIso8601Format();

	private static DateFormat buildIso8601Format() {
		DateFormat iso8601Format = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
		iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
		return iso8601Format;
	}

	@Override
	public Date read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}
		return deserializeToDate(in.nextString());
	}

	private synchronized Date deserializeToDate(String json) {

		try {
			return chineseFormat.parse(json);
		} catch (ParseException ignored) {
		}
		try {
			return localFormat.parse(json);
		} catch (ParseException ignored) {
		}
		try {
			return enUsFormat.parse(json);
		} catch (ParseException ignored) {
		}
		try {
			return iso8601Format.parse(json);
		} catch (ParseException e) {
			// throw new JsonSyntaxException(json, e);
			// return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(
					"MMM dd, yyyy HH:mm:ss", Locale.US);
			return sdf.parse(json);
		} catch (ParseException e) {
			return new Date();
		}

	}

	@Override
	public synchronized void write(JsonWriter out, Date value)
			throws IOException {
		if (value == null) {
			out.nullValue();
			return;
		}
	    String dateFormatAsString = enUsFormat.format(value);
	    out.value(dateFormatAsString);
	}
}

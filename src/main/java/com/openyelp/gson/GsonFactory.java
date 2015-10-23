package com.openyelp.gson;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonFactory {

	public Gson gson() {
		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
				new DateTypeAdapter2()).create();
		return gson;

	}
}

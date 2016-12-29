package com.quhaodian.server;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.quhaodian.annotation.NoGson;

public class NoGsonExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		NoGson gson=	f.getAnnotation(NoGson.class);
		if(gson!=null){
			return true;
		}else{
			return false;

		}
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

}

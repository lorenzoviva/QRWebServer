package com.ogc.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;


public class QRSquareFactory {
	private static QRSquareFactory instance = null;

	private QRSquareFactory() {

	}

	public static QRSquareFactory getInstance() {

		if (instance == null) {
			instance = new QRSquareFactory();
		}

		return instance;
	}

	public QRSquare getNewQRSquare(String className, Map<String, Object> parameters) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, NoSuchFieldException, SecurityException {
		Class<?> classforName = Class.forName("com.ogc.model."+className);
		QRSquare qrsquare = (QRSquare) classforName.newInstance();
		List<String> fields = new ArrayList<String>();
		while(classforName.getSuperclass()!=Object.class){
			for (Field field : classforName.getDeclaredFields()) {
				fields.add(field.getName());
			}
			classforName = classforName.getSuperclass();
		}
		List<String> qrsquarefields = new ArrayList<String>();
		for (Field field : QRSquare.class.getDeclaredFields()) {
			qrsquarefields.add(field.getName());
		}
		
		for (String param : parameters.keySet()) {
			System.out.println("param name :" + param);
			if (fields.contains(param) || qrsquarefields.contains(param)) {
				System.out.println("param name:" + parameters.get(param).getClass().getName());
				FieldUtils.writeField(qrsquare, param, parameters.get(param), true);
			}
		}
		qrsquare.setCreationDate(new Date());
		qrsquare.setVisit(1L);
		return qrsquare;
	}
}

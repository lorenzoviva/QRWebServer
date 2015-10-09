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
		QRSquare qrsquare = (QRSquare) Class.forName(className).newInstance();
		List<String> fields = new ArrayList<String>();
		for (Field field : Class.forName(className).getDeclaredFields()) {
			fields.add(field.getName());
		}
		List<String> qrsquarefields = new ArrayList<String>();
		for (Field field : QRSquare.class.getDeclaredFields()) {
			qrsquarefields.add(field.getName());
		}
		
		for (String param : parameters.keySet()) {
			System.out.println("param name :" + param);
			if (fields.contains(param) || qrsquarefields.contains(param)) {
				FieldUtils.writeField(qrsquare, param, parameters.get(param), true);
			}
		}
		qrsquare.setCreationDate(new Date());
		qrsquare.setVisit(1L);
		return qrsquare;
	}
}

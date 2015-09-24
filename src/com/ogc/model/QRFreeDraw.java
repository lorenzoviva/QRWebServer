package com.ogc.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.codehaus.jettison.json.JSONObject;


@Entity
@DiscriminatorValue(value = "QRFreeDraw")
public class QRFreeDraw extends QRSquare{
	
	
	@Override
	public boolean canBeCreated(JSONObject parameters){
		return true;
	}
//	private Bitmap representation;
}

package com.ogc.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.codehaus.jettison.json.JSONObject;


@Entity
@DiscriminatorValue(value = "QRFreeDraw")
public class QRFreeDraw extends QRSquare{
	
	@Column(length = 5000000)
	private byte[] img;
	@Column(length = 200)
	private String name;
	
	@Override
	public boolean canBeCreated(JSONObject parameters){
		return true;
	}
//	private Bitmap representation;

	/**
	 * @return the img
	 */
	public byte[] getImg() {
		return img;
	}

	/**
	 * @param img the img to set
	 */
	public void setImg(byte[] img) {
		this.img = img;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
}

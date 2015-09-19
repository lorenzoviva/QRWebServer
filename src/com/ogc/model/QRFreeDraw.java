package com.ogc.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue(value = "QRFreeDraw")
public class QRFreeDraw extends QRSquare{
	
//	private Bitmap representation;
}

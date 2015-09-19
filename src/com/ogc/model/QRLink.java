package com.ogc.model;

import static javax.persistence.GenerationType.AUTO;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class QRLink {
	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private LinkType linktype;
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
	private QRSquare fromqr;
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
	private QRSquare toqr;
	
	
	
	
	public QRLink() {
	}
	public QRLink(LinkType linktype, QRSquare fromqr, QRSquare toqr) {
		this.linktype = linktype;
		this.fromqr = fromqr;
		this.toqr = toqr;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LinkType getLinktype() {
		return linktype;
	}
	public void setLinktype(LinkType linktype) {
		this.linktype = linktype;
	}
	public QRSquare getFromqr() {
		return fromqr;
	}
	public void setFromqr(QRSquare fromqr) {
		this.fromqr = fromqr;
	}
	public QRSquare getToqr() {
		return toqr;
	}
	public void setToqr(QRSquare toqr) {
		this.toqr = toqr;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QRLink other = (QRLink) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}

package com.ogc.facades;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.ogc.model.ACL;

public class ACLFacade {
	private EntityManagerFactory emf;
	private EntityManager em;
	boolean embedded = false;

	public ACLFacade() {
		emf = Persistence.createEntityManagerFactory("QRWebService");
		em = emf.createEntityManager();
	}
	public ACLFacade(EntityManagerFactory emf,EntityManager em){
		this.emf = emf;
		this.em = em;
		embedded = true; 
	}
	public void saveACL(ACL acl){
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		em.persist(acl);
		transaction.commit();
		if(!embedded){
			em.close();
			emf.close();
		}
	}
	
	public ACL createACL(boolean read,boolean write){
		ACL acl = new ACL(read,write);
		
		
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		em.persist(acl);
		transaction.commit();
		if(!embedded){
			em.close();
			emf.close();
		}
		
		return acl;
	}
	public ACL createACL(JSONObject jsonObject) throws JSONException {
		createACL(jsonObject.getBoolean("read"),jsonObject.getBoolean("write"));
		return null;
	}
}

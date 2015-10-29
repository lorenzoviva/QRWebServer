package com.ogc.facades;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ogc.model.ACL;
import com.ogc.model.QRUser;
import com.ogc.model.QRUserMenager;

public class QRUserFacade {
	@PersistenceContext(unitName="QRWebService")
	private EntityManagerFactory emf;
	private EntityManager em;
	boolean embedded = false;
	
	public QRUserFacade() {
		emf = Persistence.createEntityManagerFactory("QRWebService");
		em = emf.createEntityManager();
	}
	public QRUserFacade(EntityManagerFactory emf, EntityManager em) {
		this.emf = emf;
		this.em = em;
		embedded = true;
	}
	public QRUser createQRUser(String firstName, String lastName,String password,String text) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, NoSuchFieldException, SecurityException {
		
		QRUser qruser = new QRUser(firstName, lastName);
		
		ACLFacade aclfacade = new ACLFacade(emf,em);
		ACL usermenageracl = aclfacade.createACL(false, false);
		
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		em.persist(qruser);
		
		transaction.commit();
		
		QRSquareFacade squarefacade= new QRSquareFacade(emf,em);
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("html", "welcome " + firstName +" "+lastName);
		parameters.put("acl", usermenageracl);
		parameters.put("text", text);
		parameters.put("password", password);
		try {
			squarefacade.createNewQRSquare(QRUserMenager.class.getSimpleName(), parameters, qruser);
		} catch (InvalidRoleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		if(!embedded){
			em.close();
			emf.close();
		}
		return qruser;
	}
	public QRUser getUserFromId(long id){
		Query query = em.createQuery("SELECT u FROM QRUser u WHERE u.id LIKE :id").setParameter("id", id);
		@SuppressWarnings("unchecked")
		List<QRUser> users = query.getResultList();
		if (users == null || users.equals(null) || users.isEmpty()) {
			return null;
		} else {
			return users.get(0);
		}
	}
	public QRUser checkQRUserMenager(String text, String password) {
		QRSquareFacade squarefacade= new QRSquareFacade(emf,em);
		QRUserMenager qrUserMenager = (QRUserMenager) squarefacade.getQRFromText(text);
		if(qrUserMenager != null && qrUserMenager.getPassword().equals(password)){
			QRSquareUserFacade squareuserfacade = new QRSquareUserFacade(emf, em);
			long userid = squareuserfacade.getQRSquareOwnerId(qrUserMenager);
			return getUserFromId(userid);
		}else{
			return null;
		}
		
	}
	public EntityManagerFactory getEmf() {
		return emf;
	}
	public void setEmf(EntityManagerFactory emf) {
		this.emf = emf;
	}
	public EntityManager getEm() {
		return em;
	}
	public void setEm(EntityManager em) {
		this.em = em;
	}
	

}

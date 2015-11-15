package com.ogc.facades;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import com.ogc.model.QRSquare;
import com.ogc.model.QRUserMenager;
import com.ogc.model.QRWebPage;
import com.ogc.model.RoleType;

public class RoleTypeFacade {
	@PersistenceUnit(unitName="QRWebService")
	public EntityManagerFactory emf;
	public EntityManager em;
	boolean embedded = false;

	public RoleTypeFacade() {
		emf = Persistence.createEntityManagerFactory("QRWebService");
		em = emf.createEntityManager();
	}
	public RoleTypeFacade(EntityManagerFactory emf,EntityManager em) {
		this.emf = emf;
		this.em = em;
		embedded = true;
		populateRoleTypeTable();

	}
	public void populateRoleTypeTable(){
		if(getRoleType("request") == null){
			
			RoleType owner = new RoleType("owner");
			owner.addQRSquareClass(QRSquare.class.getSimpleName());
			owner.addQRSquareClass(QRUserMenager.class.getSimpleName());
			owner.addQRSquareClass(QRWebPage.class.getSimpleName());
			
			RoleType editor = new RoleType("editor");
			editor.addQRSquareClass(QRSquare.class.getSimpleName());
			editor.addQRSquareClass(QRWebPage.class.getSimpleName());

			
			RoleType reader = new RoleType("reader");
			reader.addQRSquareClass(QRSquare.class.getSimpleName());
			reader.addQRSquareClass(QRWebPage.class.getSimpleName());

			
			RoleType request = new RoleType("request");
			request.addQRSquareClass(QRSquare.class.getSimpleName());
			reader.addQRSquareClass(QRWebPage.class.getSimpleName());
			reader.addQRSquareClass(QRUserMenager.class.getSimpleName());

			RoleType friend = new RoleType("friend");
			friend.addQRSquareClass(QRUserMenager.class.getSimpleName());
			
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();
			em.persist(owner);
			em.persist(editor);
			em.persist(reader);
			em.persist(request);
			em.persist(friend);
			transaction.commit();
			
			if(!embedded){
				em.close();
				emf.close();
			}
		}
	}
	public void close(){
		if(!embedded){
			em.close();
			emf.close();
		}
	}
	
	public RoleType getRoleType(String name){
		Query query = em.createQuery("SELECT r FROM RoleType r WHERE r.name LIKE :name").setParameter("name", name);
		RoleType singleResult = null;
		try{
			singleResult = (RoleType) query.getSingleResult();
		}catch (javax.persistence.NoResultException e){
			singleResult = null;
		}
		return  singleResult;
	}
	
}

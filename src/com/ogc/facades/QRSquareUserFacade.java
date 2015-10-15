package com.ogc.facades;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.apache.openjpa.persistence.EntityManagerImpl;

import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareUser;
import com.ogc.model.QRUser;
import com.ogc.model.RoleType;

public class QRSquareUserFacade {
	@PersistenceUnit(unitName="QRWebService")
	private EntityManagerFactory emf;
	private EntityManager em;
	boolean embedded = false;

	public QRSquareUserFacade() {
		emf = Persistence.createEntityManagerFactory("QRWebService");
		em = emf.createEntityManager();
	}
	public QRSquareUserFacade(EntityManagerFactory emf,EntityManager em){
		this.emf = emf;
		this.em = em;
		embedded = true; 
	}
	public QRSquareUser createQRSquareUser(QRSquare square, QRUser user, RoleType role) throws InvalidRoleException{
		if (!role.getQrSquareClasses().contains(QRSquare.class.getSimpleName())){
			throw new InvalidRoleException();
		}
		
		QRSquareUser qrSquareUser = new QRSquareUser(square,user,role);
		
		
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		((EntityManagerImpl)em).getBroker().setAllowReferenceToSiblingContext(true);

		em.persist(qrSquareUser);
		

		transaction.commit();
		if(!embedded){
			em.close();
			emf.close();
		}
		return qrSquareUser;
	}
	
	public long getQRSquareOwnerId(QRSquare square){
		Query query = em.createQuery("SELECT su.user.id FROM QRSquareUser su WHERE su.square.text LIKE :text AND su.role.name LIKE :role").setParameter("text", square.getText()).setParameter("role", "owner");
//		@SuppressWarnings("unchecked")
//		List<QRSquare> users = query.getResultList();
		long id = (long) query.getSingleResult();
		return id;
		
	}
	public void addQRSquareUser(QRSquare qrsquare,QRUser user,String role) throws InvalidRoleException{
		RoleTypeFacade roletypefacade = new RoleTypeFacade(emf,em);
		RoleType roleType = roletypefacade.getRoleType(role);
		QRSquareUser qrSquareUser;
		
			qrSquareUser = createQRSquareUser(qrsquare, user, roleType);
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();
			em.persist(qrSquareUser);
			transaction.commit();
			if(!embedded){
				em.close();
				emf.close();
			}
		
		
	}
	public List<RoleType> getQRSquareUserRoles(QRSquare square, long userid) {
		Query query = em.createQuery("SELECT su.role FROM QRSquareUser su WHERE su.square.text LIKE :text AND su.user.id LIKE :userid").setParameter("text", square.getText()).setParameter("userid", userid);
		@SuppressWarnings("unchecked")
		List<RoleType> roles = query.getResultList();
		return roles;
		
	}
	public List<QRSquareUser> getSquareUsers(String text) {
		Query query = em.createQuery("SELECT su FROM QRSquareUser su WHERE su.square.text LIKE :text").setParameter("text", text);
		@SuppressWarnings("unchecked")
		List<QRSquareUser> squareUsers = query.getResultList();
		return squareUsers;
	}
	public QRSquareUser requestSquareUser(String text, long userid) {
		
		RoleTypeFacade roletypefacade = new RoleTypeFacade(emf,em);
		RoleType role = roletypefacade.getRoleType("request");
		QRSquareFacade squarefacade = new QRSquareFacade(emf, em);
		QRSquare square = squarefacade.getQRFromText(text);
		QRUserFacade userfacade = new QRUserFacade(emf,em);
		QRUser user = userfacade.getUserFromId(userid);
		
		
		try {
			return createQRSquareUser(square, user, role);
		} catch (InvalidRoleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public List<QRSquareUser> getQRSquareUser(String text, long userid) throws NoResultException{
		Query query = em.createQuery("SELECT su FROM QRSquareUser su WHERE su.square.text LIKE :text AND su.user.id LIKE :userid").setParameter("text", text).setParameter("userid", userid);
		@SuppressWarnings("unchecked")
		List<QRSquareUser> squareUser = (List<QRSquareUser>) query.getResultList();
		return squareUser;
	}
}

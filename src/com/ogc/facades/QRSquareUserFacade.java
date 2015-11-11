package com.ogc.facades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.ogc.model.QRUserMenager;
import com.ogc.model.RoleType;

public class QRSquareUserFacade {
	@PersistenceUnit(unitName = "QRWebService")
	private EntityManagerFactory emf;
	private EntityManager em;
	boolean embedded = false;

	public QRSquareUserFacade() {
		emf = Persistence.createEntityManagerFactory("QRWebService");
		em = emf.createEntityManager();
	}

	public QRSquareUserFacade(EntityManagerFactory emf, EntityManager em) {
		this.emf = emf;
		this.em = em;
		embedded = true;
	}

	public QRSquareUser createQRSquareUser(QRSquare square, QRUser user, RoleType role) throws InvalidRoleException {
		if (!role.getQrSquareClasses().contains(QRSquare.class.getSimpleName())) {
			throw new InvalidRoleException();
		}

		QRSquareUser qrSquareUser = new QRSquareUser(square, user, role);

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		((EntityManagerImpl) em).getBroker().setAllowReferenceToSiblingContext(true);

		em.persist(qrSquareUser);

		transaction.commit();
		if (!embedded) {
			em.close();
			emf.close();
		}
		return qrSquareUser;
	}

	public QRUserMenager getQRUserMenager(long userid) {
		Query query = em.createQuery("SELECT su.square FROM QRSquareUser su WHERE TYPE(su.square) = :type AND su.user.id LIKE :userid AND su.role.name LIKE :owner").setParameter("type", QRUserMenager.class).setParameter("userid", userid).setParameter("owner", "owner");

		// Query query =
		// em.createQuery("SELECT s FROM QRSQUaQRSquare s WHERE s.text LIKE :text").setParameter("text",
		// text);
		@SuppressWarnings("unchecked")
		List<QRUserMenager> squares = query.getResultList();
		if (squares == null || squares.equals(null) || squares.isEmpty()) {
			return null;
		} else {
			return squares.get(0);
		}
	}

	public long getQRSquareOwnerId(QRSquare square) {
		Query query = em.createQuery("SELECT su.user.id FROM QRSquareUser su WHERE su.square.text LIKE :text AND su.role.name LIKE :role").setParameter("text", square.getText()).setParameter("role", "owner");
		// @SuppressWarnings("unchecked")
		// List<QRSquare> users = query.getResultList();
		long id = (long) query.getSingleResult();
		return id;

	}

	public void addQRSquareUser(QRSquare qrsquare, QRUser user, String role) throws InvalidRoleException {
		RoleTypeFacade roletypefacade = new RoleTypeFacade(emf, em);
		RoleType roleType = roletypefacade.getRoleType(role);
		createQRSquareUser(qrsquare, user, roleType);
//		EntityTransaction transaction = em.getTransaction();
//		transaction.begin();
//		em.persist(qrSquareUser);
//		transaction.commit();
//		if (!embedded) {
//			em.close();
//			emf.close();
//		}

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
	public List<QRSquareUser> getUserSquares(long userid) {
		Query query = em.createQuery("SELECT su FROM QRSquareUser su WHERE su.user.id LIKE :userid").setParameter("userid", userid);
		@SuppressWarnings("unchecked")
		List<QRSquareUser> squareUsers = query.getResultList();
		return squareUsers;
	}
	public List<QRUserMenager> getSquareUsersMenager(List<QRSquareUser> squareUsers) {
		List<QRUserMenager> qrUsersMenagers = new ArrayList<QRUserMenager>();

		for (QRSquareUser squareUser : squareUsers) {
			qrUsersMenagers.add(getQRUserMenager(squareUser.getUser().getId()));

		}
		return qrUsersMenagers;
	}
	public List<QRSquare> getSquareUsersSquares(List<QRSquareUser> squareUsers) {
		List<QRSquare> qrUsersMenagers = new ArrayList<QRSquare>();
		QRSquareFacade squareFacade = new QRSquareFacade(emf, em);
		for (QRSquareUser squareUser : squareUsers) {
			qrUsersMenagers.add(squareFacade.getQRFromText(squareUser.getSquare().getText()));
		}
		return qrUsersMenagers;
	}
	public QRSquareUser requestSquareUser(String text, long userid) {

		RoleTypeFacade roletypefacade = new RoleTypeFacade(emf, em);
		RoleType role = roletypefacade.getRoleType("request");
		QRSquareFacade squarefacade = new QRSquareFacade(emf, em);
		QRSquare square = squarefacade.getQRFromText(text);
		QRUserFacade userfacade = new QRUserFacade(emf, em);
		QRUser user = userfacade.getUserFromId(userid);

		try {
			return createQRSquareUser(square, user, role);
		} catch (InvalidRoleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<QRSquareUser> getQRSquareUser(String text, long userid) throws NoResultException {
		Query query = em.createQuery("SELECT su FROM QRSquareUser su WHERE su.square.text LIKE :text AND su.user.id LIKE :userid").setParameter("text", text).setParameter("userid", userid);
		@SuppressWarnings("unchecked")
		List<QRSquareUser> squareUser = (List<QRSquareUser>) query.getResultList();
		return squareUser;
	}

	public QRUser getQRUser(long userid) {
		QRUserFacade userfacade = new QRUserFacade(emf, em);
		QRUser user = userfacade.getUserFromId(userid);
		return user;
	}
	public QRSquare getQRSquare(String text) {
		QRSquareFacade userfacade = new QRSquareFacade(emf, em);
		QRSquare user = userfacade.getQRFromText(text);
		return user;
	}

	public Map<String, QRSquareUser> getQRSquareUsers(List<QRSquare> squares, long userid) {
		Map<String, QRSquareUser> map=	new HashMap<String,QRSquareUser>();
		for(QRSquare square : squares){
			List<QRSquareUser> qrSquareUsers = getQRSquareUser(square.getText(), userid);
			if(qrSquareUsers!=null && !qrSquareUsers.isEmpty() && qrSquareUsers.get(0).getRole()!=null && !qrSquareUsers.get(0).getRole().getName().toLowerCase().startsWith("request")){
				map.put(square.getText(), qrSquareUsers.get(0));
			}else{
				QRSquareUser qrSquareUser = new QRSquareUser();
				qrSquareUser.setIsnew(true);
				map.put(square.getText(), qrSquareUser);
			}
		}
		return map;
	}

	public Map<String, QRSquareUser> getQRMenagerUsers(List<QRUserMenager> squareUsersMenagers, long userid) {
		Map<String, QRSquareUser> map=	new HashMap<String,QRSquareUser>();
		for(QRSquare square : squareUsersMenagers){
			List<QRSquareUser> qrSquareUsers = getQRSquareUser(square.getText(), userid);
			if(qrSquareUsers!=null && !qrSquareUsers.isEmpty() && qrSquareUsers.get(0).getRole()!=null && !qrSquareUsers.get(0).getRole().getName().toLowerCase().startsWith("request")){
				map.put(square.getText(), qrSquareUsers.get(0));
			}else{
				QRSquareUser qrSquareUser = new QRSquareUser();
				qrSquareUser.setIsnew(true);
				map.put(square.getText(), qrSquareUser);
			}
		}
		return map;
	}

	public void addQRSquareUserRead(QRSquare square, long userid) {
		QRUserFacade userFacade = new QRUserFacade(emf, em);
		QRUser user = userFacade.getUserFromId(userid);
		try {
			addQRSquareUser(square, user, "reader");
		} catch (InvalidRoleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package com.ogc.facades;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import com.ogc.model.QRSquare;
import com.ogc.model.QRSquareFactory;
import com.ogc.model.QRSquareUser;
import com.ogc.model.QRUser;
import com.ogc.model.RoleType;

public class QRSquareFacade {
	@PersistenceUnit(unitName="QRWebService")
	private EntityManagerFactory emf;
	private EntityManager em;
	boolean embedded = false;

	public QRSquareFacade() {
		emf = Persistence.createEntityManagerFactory("QRWebService");
		em = emf.createEntityManager();
	}

	public QRSquareFacade(EntityManagerFactory emf, EntityManager em) {
		this.emf = emf;
		this.em = em;
		embedded = true;
	}

	public String getFirstFreeText() {
		String s = UUID.randomUUID().toString();
		while (!isTextAvaliable(s)) {
			s = UUID.randomUUID().toString();
		}
		return s;
	}

	public boolean isTextAvaliable(String text) {
		Query query = em.createQuery("SELECT s FROM QRSquare s WHERE s.text LIKE :text").setParameter("text", text);
		@SuppressWarnings("unchecked")
		List<QRSquare> users = query.getResultList();
		return (users == null || users.equals(null) || users.isEmpty());
	}

	public QRSquare getQRFromText(String text) {
		Query query = em.createQuery("SELECT s FROM QRSquare s WHERE s.text LIKE :text").setParameter("text", text);
		@SuppressWarnings("unchecked")
		List<QRSquare> users = query.getResultList();
		if (users == null || users.equals(null) || users.isEmpty()) {
			return null;
		} else {
			return users.get(0);
		}
	}

	/**
	 * create a new QRSquare subclass object (one of QRUserMenager, QRDraw...)
	 * and set the parameters map.
	 * 
	 * @param className
	 *            the class name of the subclass object
	 * @param parameters
	 *            : a Map<Field name, Object to set> of the class fields to set
	 * @return the new QRSquare
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public QRSquare createNewQRSquare(String className, Map<String, Object> parameters) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, NoSuchFieldException, SecurityException {
		QRSquare qrsquare = QRSquareFactory.getInstance().getNewQRSquare(className, parameters);

		if (qrsquare.getText() == null || qrsquare.getText().equals(null) || qrsquare.getText().equals("") || !isTextAvaliable(qrsquare.getText())) {
			qrsquare.setText(getFirstFreeText());
		}

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		em.persist(qrsquare);
		transaction.commit();
		if (!embedded) {
			em.close();
			emf.close();
		}

		return qrsquare;
	}

	/**
	 * create a new QRSquare subclass object (one of QRUserMenager, QRDraw...)
	 * and set the parameters map. It also add an owner link between QRuser and
	 * QRSquare
	 * 
	 * @param className
	 *            the class name of the subclass object
	 * @param parameters
	 *            : a Map<Field name, Object to set> of the class fields to set
	 * @param owner
	 *            : the owner of the new qr square
	 * @return the new QRSquare
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws InvalidRoleException 
	 */
	public QRSquare createNewQRSquare(String className, Map<String, Object> parameters, QRUser owner) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, NoSuchFieldException, SecurityException, InvalidRoleException {
		QRSquare qrsquare = QRSquareFactory.getInstance().getNewQRSquare(className, parameters);
		
		if (qrsquare.getText() == null || qrsquare.getText().equals(null) || qrsquare.getText().equals("") || !isTextAvaliable(qrsquare.getText())) {
			qrsquare.setText(getFirstFreeText());
		}
		QRSquareUserFacade qrSquareUserFacade = new QRSquareUserFacade(emf, em);
		qrSquareUserFacade.addQRSquareUser(qrsquare, owner, "owner");

		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		em.persist(qrsquare);
		transaction.commit();
		if (!embedded) {
			em.close();
			emf.close();
		}
		return qrsquare;
	}

//	public QRSquare checkQRSquare(String text) {
//		QRSquare square = getQRFromText(text);
//		if (square == null) {
//			return "";
//		} else {
//			
//		}
//	}

}

/**
 * 
 */
package com.sokoban.db;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * @author שדמה
 * Add/get methods for interacting with the SQL Server DB
 */
public class DBHandler {
	private SessionFactory factory;
	
	public DBHandler() {
		Configuration configuration = new Configuration();
		configuration.configure();
		factory = configuration.buildSessionFactory();
	}
	
	public void addSolution(SokobanSolution solution) {
		Session session = null;
		Transaction tx = null;
		
		try {
			session = factory.openSession();
			tx = session.beginTransaction();
			
			session.save(solution);
			tx.commit();			
		}
		catch (HibernateException ex) {
			if (tx != null)
				tx.rollback();
			System.out.println(ex.getMessage());
		}
		finally {
			if (session != null)
				session.close();
		}		
	}
	
	public String getSolution(String name) {
		Session session = null;		
		
		try {
			session = factory.openSession();			
			
			SokobanSolution sol = session.get(SokobanSolution.class, name);
			if (sol != null) {
				return sol.getSolution();
			}			
		}
		catch (HibernateException ex) {			
			System.out.println(ex.getMessage());
		}
		finally {
			if (session != null)
				session.close();
		}
		return null;
	}
}

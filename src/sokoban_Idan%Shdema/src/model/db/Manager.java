package model.db;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * In charge for all queries to the DB.
 * @author ����
 *
 */
public class Manager {
	private static SessionFactory factory;
	private User user;

	public Manager() {
		Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
		Configuration configuration = new Configuration();
		configuration.configure();
		factory = configuration.buildSessionFactory();
	}

	public int addUser(String fName, String lName, int id) {
		user = new User(fName, lName, id);
		Transaction tx = null;
		int userID = 0;
		List<User> users = null;
		Session session = factory.openSession();
		try {
			tx = session.beginTransaction();
			
			//if the user exists (duplicate id)
			
			Query<User> q = session.createQuery("from Users where ID like ?");
			q.setParameter(0, id);
			users = q.list();
			
			if (users.size() > 0)
			{}
			else
			{
				userID = (int) session.save(user);
				tx.commit();
			}
			
		} 
		catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} 
		finally {
			session.close();
		}
		return userID;
	}

	public void addLevel(commons.Level l) {
		Transaction tx1 = null;
		List<Level> levels = null;
		Session session = factory.openSession();
		try {
			tx1 = session.beginTransaction();			
			//if the user exists (duplicate id)
			
			Query<Level> q = session.createQuery("from Levels where Name like ?");
			q.setParameter(0, l.getName());
			levels = q.list();
			
			if (levels.size() > 0)
			{}
			else
			{
				session.save(l);
				tx1.commit();
			}
		} catch (HibernateException e) {
			if (tx1 != null)
				tx1.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	// adds the current level to the db
	public void addScore(User user, commons.Level l) {
		Score s = new Score(user.getId(), l.getName(), l.getSteps(), l.getMyTimer().convert());
		Transaction tx2 = null;
		List<Score> scores = null;
		Session scoreSession = factory.openSession();
		try {
			tx2 = scoreSession.beginTransaction();

			
			Query<Score> q = scoreSession.createQuery("from Scoreboard where UserID like ? and LevelID like ?");
			q.setParameter(0, user.getId());
			q.setParameter(1, l.getName());
			scores = q.list();
			
			if (scores.size()>0) //update record based on steps, if equals then by time
			{
				Score same = scores.get(0);
				if (s.getSteps() < same.getSteps())
				{
					updateScore(user, l, s, scoreSession, tx2);
				}
				else if (s.getSteps() == same.getSteps())
				{
					if (s.getTime().before(same.getTime()))
					{
						updateScore(user, l, s, scoreSession, tx2);
					}
				}				
			}
			
			else
			{			
				scoreSession.save(s);
				tx2.commit();
			}
			
		} catch (HibernateException e) {
			if (tx2 != null)
				tx2.rollback();
			e.printStackTrace();
		} finally {
			scoreSession.close();
		}

	}

	private void updateScore(User user, commons.Level l, Score s, Session scoreSession, Transaction tx)
	{
		//update the record
		Query<Score> q1 = scoreSession.createQuery("update Scoreboard set Steps = ? where UserID like ? and LevelID like ? ");
		q1.setParameter(0, s.getSteps());
		q1.setParameter(1, user.getId());
		q1.setParameter(2, l.getName());
		Query<Score> q2 = scoreSession.createQuery("update Scoreboard set Time = ? where UserID like ? and LevelID like ?");
		q2.setParameter(0, s.getTime());
		q2.setParameter(1, user.getId());
		q2.setParameter(2, l.getName());
		q1.executeUpdate();
		q2.executeUpdate();
		tx.commit();
	}
	
	public LinkedList<Score> pullData()
	{
		ArrayList<Score> scoreArrayList = new ArrayList<>();
		LinkedList<Score> scoreList = new LinkedList<>();
		Transaction tx = null;
		Session session = factory.openSession();
		tx = session.beginTransaction();
		Query<Score> q = session.createQuery("from Scoreboard order by Steps asc").setMaxResults(15);
		tx.commit();
		scoreArrayList = (ArrayList<Score>) q.list();
		scoreList.addAll(scoreArrayList);
			
		return scoreList;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LinkedList<Score> pullDataUser(String string) {
		int id = Integer.parseInt(string);
		ArrayList<Score> scoreArrayList = new ArrayList<>();
		LinkedList<Score> scoreList = new LinkedList<>();
		Transaction tx3 = null;
		Session session = factory.openSession();
		tx3 = session.beginTransaction();
		Query<Score> q = session.createQuery("from Scoreboard where UserID like ? order by Steps asc").setMaxResults(15);
		q.setParameter(0, id);
		tx3.commit();
		scoreArrayList = (ArrayList<Score>) q.list();
		scoreList.addAll(scoreArrayList);
			
		return scoreList;
	}

	public LinkedList<Score> pullDataLevel(String string) {
		ArrayList<Score> scoreArrayList = new ArrayList<>();
		LinkedList<Score> scoreList = new LinkedList<>();
		Transaction tx4 = null;
		Session session = factory.openSession();
		tx4 = session.beginTransaction();
		Query<Score> q = session.createQuery("from Scoreboard where LevelID like ? order by Steps asc").setMaxResults(15);
		q.setParameter(0, string);
		tx4.commit();
		scoreArrayList = (ArrayList<Score>) q.list();
		scoreList.addAll(scoreArrayList);
			
		return scoreList;
	}
}

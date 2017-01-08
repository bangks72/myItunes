package application;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class CreateStudentTest {

	public static void main(String[] args) {
		
		SessionFactory factory = new Configuration()
								.configure("hibernate.cfg.xml")
								.addAnnotatedClass(Audio.class)
								.buildSessionFactory();
		
		Session session = factory.getCurrentSession();
		
		try {

			
			//start a transaction
			session.beginTransaction();
			
			//query students
			List<Audio> theStus = session.createQuery("from Audio order by id").list();
			
			//display them
			//displayStudents(theStus);
			
			//query students 
			//theStus = session.createQuery("from Audio s where" 
				//				+ " s.id = 24 ").list();
			
			//displayStudents(theStus);
			
			//System.out.println("The first element is:" +theStus.remove(0));
			
			displayStudents(theStus);
			
			String str="e:/a";
			int n = session.createQuery("update Audio set "
										+"path = :str, "
										+"name = 'haha' where aid = :int ")
					.setString("str", str)
					.setInteger("int", 1)
					.executeUpdate();
			
			//commit transaction
			session.getTransaction().commit();
			
			System.out.println("Done!");
		}
		finally {
			factory.close();
		}
	}
	private static void displayStudents(List<Audio> theStus) {
		for(Audio stu : theStus){
			System.out.println(stu);
		}
	}

}

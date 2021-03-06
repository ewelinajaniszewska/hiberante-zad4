import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {

	Session session;

	public static void main(String[] args) {
		Main main = new Main();
		//main.addData();
		main.printSchools();
		//main.executeQueries();
		//main.executeQuery1();
		//main.executeQuery2();
		//main.executeQuery3();
		//main.executeQuery4();
		//main.executeQuery5();
		//main.executeQuery6();
		//main.executeQuery7();
		main.close();
	}

	

	public Main() {
		session = HibernateUtil.getSessionFactory().openSession();
	}

	public void close() {
		session.close();
		HibernateUtil.shutdown();
	}
	
	private void executeQueries() {
        String hql = "FROM School";
        Query query = session.createQuery(hql);
        List results = query.list();
        System.out.println(results);
}
	
	private void executeQuery1() {
        String hql = "FROM School S where S.name = 'AE'";
        Query query = session.createQuery(hql);
        List results = query.list();
        System.out.println(results);
	}

	private void executeQuery2(){
		String hql = "FROM School S where S.name = 'AWF'";
		Query query = session.createQuery(hql);
		List<School> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (School school: results ){
		session.delete(school); 
		}
		transaction.commit();	
	}
	
	private void executeQuery3(){
		String hql = "Select COUNT(s) from School s ";
		Query query = session.createQuery(hql);
        List results = query.list();
        System.out.println(results);
	}
	
	private void executeQuery4(){
		String hql = "Select COUNT(s) from Student s ";
		Query query = session.createQuery(hql);
        List results = query.list();
        System.out.println(results);
	}
	
	private void executeQuery5(){
		String hql = "Select Count(s) from School s where  size (s.classes) >= 2";
		Query query = session.createQuery(hql);
        List results = query.list();
        System.out.println(results);		
	}
	
	private void executeQuery6(){
		String hql = "SELECT s FROM School s INNER JOIN s.classes classes WHERE classes.profile = 'mat-fiz' and classes.currentYear>=2008";
		Query query = session.createQuery(hql);
        List results = query.list();
        System.out.println(results);		
	}
	
	private void executeQuery7(){
	Query query = session.createQuery("from School where id= :id");
	query.setLong("id", 2);
	School school = (School) query.uniqueResult();
	school.setAddress("ul Wisniowa 45");
	Transaction transaction = session.beginTransaction();
	session.save(school); 
	transaction.commit();
	}
	
	private void addData(){
		School s1 = new School();
		s1.setName("AWF");
		s1.setAddress("Nowa 2, Krakow");
		
		SchoolClass sc1 = new SchoolClass();
		sc1.setProfile("sportowy");
		sc1.setStartYear(2012);
		sc1.setCurrentYear(3);
		
		Student st1 = new Student();
		st1.setName("Joanna");
		st1.setSurname("Nowak");
		st1. setPesel("99999999");
		
		sc1.addStudent(st1);
		s1.addClasses(sc1);
		
		Transaction transaction = session.beginTransaction();
		session.save(s1); 
		transaction.commit();
		
	}
	

	private void printSchools() {
		Criteria crit = session.createCriteria(School.class);
		List<School> schools = crit.list();

		System.out.println("### Schools and classes");
		for (School s : schools) {
		System.out.println(s);
		System.out.println("   Klasy:  ");
		for (SchoolClass schoolClass : s.getClasses()){
			System.out.println("     "+ schoolClass);
			
			for (Student student : schoolClass.getStudents()) {
				System.out.print("Student:");
				System.out.print("            " + student.getName());
				System.out.print(" " + student.getSurname());
				System.out.println(" (" + student.getPesel() + ")");
		}
		}
		}
	}
	

	private void jdbcTest() {
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("org.sqlite.JDBC");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection("jdbc:sqlite:school.db", "", "");

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM schools";
			ResultSet rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set
			while (rs.next()) {
				// Retrieve by column name
				String name = rs.getString("name");
				String address = rs.getString("address");

				// Display values
				System.out.println("Name: " + name);
				System.out.println(" address: " + address);
			}
			// STEP 6: Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		System.out.println("Goodbye!");
	}// end jdbcTest

}

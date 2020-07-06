package britenet.task;
import java.io.BufferedReader;
import java.sql.*;
import java.io.FileReader;  
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

public class CSV {

private String makeQueryCustomers(int id, String name, String surname, String age) {
		
		String sqlQueryCustomers = "INSERT INTO cc.CUSTOMERS " + 
				" (ID, NAME, SURNAME, AGE) " +
				" VALUES ("+Integer.toString(id)+", '"+name+"', '"+surname +"', '"+age+"');";	
		return sqlQueryCustomers;
}	
	
	
private String makeQueryContacts(int contactId, int id, int type, String contact) {
		
		String sqlQueryContacts = "INSERT INTO cc.CONTACTS " + 
				" (ID, ID_CUSTOMER, TYPE, CONTACT) " +
				" VALUES ("+Integer.toString(contactId)+", '"+Integer.toString(id)+"', '"+Integer.toString(type)+"', '"+contact+"');";
			
		return sqlQueryContacts;
}
	

public void readAndSave(String filename) {  
	String line = ""; 
	int contactId = 0;	
try   
	{  
		Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cc", "root", "hyfeb");
		Statement myStmt = myConn.createStatement();

		BufferedReader br = new BufferedReader(new FileReader("files\\"+filename));  
		int id = 0;	
		while ((line = br.readLine()) != null)   
				{  
					String[] customer = line.split(",");    
					String sqlQueryCustomers = makeQueryCustomers(id, customer[0], customer[1], customer[2]);
					System.out.println(sqlQueryCustomers);
					myStmt.executeUpdate(sqlQueryCustomers);
					System.out.println("DODANO REKORD do tabeli CUSTOMERS od ID: " + id);
						
					for (int i=4; i <customer.length; i++) {
							int type = 0;
							boolean numeric = true;
							if(customer[i].indexOf("@")!=-1) {    				//Email -  "@" - type 1
								type = 1;
							}
					
							if(customer[i].indexOf("/")!=-1) {   				//Jabber -  "/" - type 3
								type = 3;
							}
					
							try {                                               // - type 2
								Double num = Double.parseDouble(customer[i]);
							} catch (NumberFormatException e) {
								numeric = false;
							}

							if(numeric) {
								type = 2;
							}
					
							String sqlQueryContacts = makeQueryContacts(contactId, id, type, customer[i]);
							myStmt.executeUpdate(sqlQueryContacts);
							System.out.println("DODANO REKORD do tabeli CONTACTS o ID: " + contactId);
						
					contactId++; 
					} //END OF FOR
					id++; 
				} //END OF WHILE 
			
	br.close();	
	}   
	catch (Exception exc)   
	{  
		System.out.println("Plik nie istnieje lub te dane zostaly juz wprowadzone do bazy danych!!!!");
		exc.printStackTrace(); 
		System.out.println("Plik nie istnieje lub te dane zostaly juz wprowadzone do bazy danych!!!!");
	}  
} 

}
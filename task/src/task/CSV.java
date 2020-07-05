package task;
import java.io.BufferedReader;
import java.sql.*;
import java.io.FileReader;  
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;



public class CSV {

public void readAndSave(String filename) {  
	
	String line = ""; 
	ArrayList<String[]> records = new ArrayList<String[]>(); //Lista tablic Stringow, gdzie kazda tablica zawiera jeden wiersz
	int contactId = 0;
try   
	{  
	
		Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cc", "root", "***");
		Statement myStmt = myConn.createStatement();

		BufferedReader br = new BufferedReader(new FileReader("files\\"+filename));  
		int id = 0;	
		while ((line = br.readLine()) != null)   //Zwraca Boolean - przejscie przez petle dla kazdej linii
				{  
					
					String[] customer = line.split(",");    // oddziela dane z linii uzywajac przecinka jako separatora   
					
				
					//Zapytanie do bazy - wprowadzanie info o klientach
					String sqlQueryCustomers = "INSERT INTO cc.CUSTOMERS " + 
							" (ID, NAME, SURNAME, AGE) " +
							" VALUES ("+Integer.toString(id)+", '"+customer[0]+"', '"+customer[1] +"', '"+customer[2]+"');";
					
					System.out.println(sqlQueryCustomers);
					myStmt.executeUpdate(sqlQueryCustomers);
					System.out.println("DODANO REKORD do tabeli CUSTOMERS od ID: " + id);
					
					
						
					//ZAPISYWANIE DO LISTY TABLIC STRINGOW
					records.add(customer);
						
					//Sprawdzanie typow kontaktow; zapytania do bazy - wprowadzanie info o kontaktach
					
					
					
					for (int i=4; i <customer.length; i++) {
						int type = 0;
						boolean numeric = true;
						if(customer[i].indexOf("@")!=-1) {    //Email - adresy zawieraj¹ "@" - type 1
							type = 1;
						}
					
						if(customer[i].indexOf("/")!=-1) {   //Jabber - adresy osobiste zawieraja znak "/" - type 3
							type = 3;
						}
					

						try {                                                 ///Czy jest to numer - type 2
							Double num = Double.parseDouble(customer[i]);
							} catch (NumberFormatException e) {
								numeric = false;
							}

						if(numeric) {
							type = 2;
						}
					
					
						
						String sqlQueryContacts = "INSERT INTO cc.CONTACTS " + 
								" (ID, ID_CUSTOMER, TYPE, CONTACT) " +
								" VALUES ("+Integer.toString(contactId)+", '"+Integer.toString(id)+"', '"+Integer.toString(type)+"', '"+customer[i]+"');";
							myStmt.executeUpdate(sqlQueryContacts);
							System.out.println("DODANO REKORD do tabeli CONTACTS o ID: " + contactId);
						
						
						
						
					contactId++; //Inkrementacja id kontaktu
					} //END OF FOR
					/*******************************************************/
					
				
					
					id++; //Inkrementacja id klienta
				} //END OF WHILE 
		
		
		
		
		
		
			System.out.println("\nWYDRUK Z LISTY STRINGOW:");
			String print;
			for(int i = 0; i < records.size(); i++) {
				System.out.println("\n**************************");
				for(int j=0; j < records.get(i).length; j++) {
						print = records.get(i)[j];
						System.out.print(print + " | ");
				}
			}
			
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

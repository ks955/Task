package britenetTask;

import java.io.File;
import org.w3c.dom.*; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
  
public class XML {
		
private String makeQueryContacts(int contactId, int i, int type, String contact) {
	
	String sqlQueryContact = "INSERT INTO cc.CONTACTS " + 
			" (ID, ID_CUSTOMER, TYPE, CONTACT) " +
			" VALUES ("+Integer.toString(contactId)+", '"+Integer.toString(i)+"', '"+Integer.toString(type)+"', '"+contact+"');";
		return sqlQueryContact;
}
	
private String makeQueryCustomers(String id, String name, String surname, String age) {
	String sqlQueryCustomer = "INSERT INTO cc.CUSTOMERS " + 
			" (ID, NAME, SURNAME, AGE) " +
			" VALUES ("+id+", '"+name+"', '"+surname +"', '"+age+"');";
		return sqlQueryCustomer;
}

public void readAndSave(String filename){
	try {
			int contactId = 0;
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cc", "root", "hyfeb");
			Statement myStmt = myConn.createStatement();
		
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (new File("files\\"+filename));

			doc.getDocumentElement ().normalize ();
			System.out.println ("ROOT: " + doc.getDocumentElement().getNodeName());

			NodeList listOfPersons = doc.getElementsByTagName("person");
			int totalPersons = listOfPersons.getLength();
			System.out.println("Ilosc osob o ktorych dane zapisane sa w pliku: " + totalPersons);
			
			for(int i=0; i<totalPersons ; i++){
				Node firstPersonNode = listOfPersons.item(i);
				
			if(firstPersonNode.getNodeType() == Node.ELEMENT_NODE){
					Element firstPersonElement = (Element)firstPersonNode;
					String[] customer = new String[4]; //tablica stringow;
					customer[0] = Integer.toString(i); //Zapis id z iteratora petli		
			
			NodeList firstNameList = firstPersonElement.getElementsByTagName("name");
			if(firstNameList.getLength()>0) {
					Element firstNameElement = (Element)firstNameList.item(0);  
					NodeList textFNList = firstNameElement.getChildNodes();
					System.out.println("Imie : " + ((Node)textFNList.item(0)).getNodeValue().trim());
					customer[1] = ((Node)textFNList.item(0)).getNodeValue().trim();
			}

			NodeList lastNameList = firstPersonElement.getElementsByTagName("surname");
			if(lastNameList.getLength()>0) {
					Element lastNameElement = (Element)lastNameList.item(0);
					NodeList textLNList = lastNameElement.getChildNodes();
					System.out.println("Nazwisko : " + ((Node)textLNList.item(0)).getNodeValue().trim());
					customer[2] = ((Node)textLNList.item(0)).getNodeValue().trim();
			}

			NodeList ageList = firstPersonElement.getElementsByTagName("age");
			if (ageList.getLength()>0) {
					Element ageElement = (Element)ageList.item(0);
					NodeList textAgeList = ageElement.getChildNodes();
					System.out.println("Wiek : " + ((Node)textAgeList.item(0)).getNodeValue().trim()); 
					customer[3] = ((Node)textAgeList.item(0)).getNodeValue().trim();
			}
//CONTACTS
			NodeList listOfContacts = firstPersonElement.getElementsByTagName("contacts");
			Node contactNode = listOfContacts.item(0);
			if(contactNode.getNodeType() == Node.ELEMENT_NODE){
			Element contactElement = (Element)contactNode;	
//PHONES
				NodeList phonesList = contactElement.getElementsByTagName("phone");
					if(phonesList.getLength()>0) {
						System.out.println("Ilosc TELEFONOW dla OSOBY o ID nr: " + i + " WYNOSI: " + phonesList.getLength());
						for(int q = 0; q < phonesList.getLength(); q++) {
							contactId++;
							Element phoneElement = (Element)phonesList.item(q);
							NodeList textPHist = phoneElement.getChildNodes();
							System.out.println("Telefon NR: " + q + " to: " + ((Node)textPHist.item(0)).getNodeValue().trim());

							String sqlQueryContacts = makeQueryContacts(contactId, i, 2, ((Node)textPHist.item(0)).getNodeValue().trim());
							myStmt.executeUpdate(sqlQueryContacts);
							System.out.println("DODANO REKORD do tabeli CONTACTS o ID: " + contactId);
						}
					}	
//EMAILS
				NodeList emailsList = contactElement.getElementsByTagName("email");
					if(emailsList.getLength()>0) {
						System.out.println("Ilosc EMAILi dla OSOBY o ID nr: " + i + " WYNOSI: " + emailsList.getLength());
						for(int q = 0; q < emailsList.getLength(); q++) {
							contactId++;
							Element emailElement = (Element)emailsList.item(q);
							NodeList textEMist = emailElement.getChildNodes();
							System.out.println("Email NR: " + q + " to: " + ((Node)textEMist.item(0)).getNodeValue().trim());

							String sqlQueryContacts = makeQueryContacts(contactId, i, 1, ((Node)textEMist.item(0)).getNodeValue().trim());
							myStmt.executeUpdate(sqlQueryContacts);
							System.out.println("DODANO REKORD do tabeli CONTACTS o ID: " + contactId);
						}
					}
					
				//JABBER
					NodeList jabberList = contactElement.getElementsByTagName("jabber");
					if(jabberList.getLength()>0) {
						System.out.println("Ilosc AD.JABBER dla OSOBY o ID nr: " + i + " WYNOSI: " + emailsList.getLength());
						for(int q = 0; q < jabberList.getLength(); q++) {
							contactId++;
							Element jabberElement = (Element)jabberList.item(q);
							NodeList textJBist = jabberElement.getChildNodes();
							System.out.println("Jabber NR : " + q + " to: " + ((Node)textJBist.item(0)).getNodeValue().trim());

							String sqlQueryContacts = makeQueryContacts(contactId, i, 3, ((Node)textJBist.item(0)).getNodeValue().trim());
							System.out.println("DODANO REKORD do tabeli CONTACTS o ID: " + contactId);			
						}
					}
				
			} //END OF if FOR CONTACTS
				String sqlQueryCustomer = makeQueryCustomers(customer[0], customer[1], customer[2], customer[3]);
				System.out.println("\n" + sqlQueryCustomer);
				myStmt.executeUpdate(sqlQueryCustomer);
				System.out.println("DODANO REKORD o ID do tabeli CUSTOMERS: " + i + "\n");
		}//end of if	
	}//end of for 
	  
	}catch (SAXParseException err) {
		System.out.println ("** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
		System.out.println(" " + err.getMessage ());
	  
	}catch (SAXException e) {
		Exception x = e.getException ();
		((x == null) ? e : x).printStackTrace ();
	  
	}catch (Throwable t) {
		System.out.println("Plik nie istnieje lub te dane zostaly juz wprowadzone do bazy danych!!!!");
		t.printStackTrace ();
		System.out.println("Plik nie istnieje lub te dane zostaly juz wprowadzone do bazy danych!!!!");
	}
	
	  
	}//end of main	

}
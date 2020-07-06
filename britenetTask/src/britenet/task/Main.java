package britenet.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	public static void main(String[] args) {
	
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String name;
	
		System.out.println("Podaj nazwe pliku wraz z rozszerzeniem:\n");
      
			try {
				name = reader.readLine();
				System.out.println(name);
				if(name.indexOf("csv", name.length()-3)!=-1){
					System.out.println("Plik typu CSV");
					CSV csv = new CSV();
					csv.readAndSave(name);
					}else if(name.indexOf("xml", name.length()-3)!=-1) {
													System.out.println("Plik typu XML");
													XML xml = new XML();
													xml.readAndSave(name);	
					}else {
							System.out.println("Nieprawid³owy format pliku!");
					}		
			}
			catch(IOException e) {
				System.out.println("WYSTAPIL BLAD");
    	 }
	}
}

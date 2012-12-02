package entry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class NameDiscrimination {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//readHTML();
		readXML();
	}

	private static void readHTML() {

		File f = new File("webps/web_pages/Abby_Watkins/raw/001/index.html");
		if (f.exists()) {
			System.out.println("exists");

			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader(
						new FileInputStream(f)));
				String s = null;
				while ((s = br.readLine()) != null) {
					System.out.println(s);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}

	private static void readXML() {

		File f = new File("webps/truth_files/Abby_Watkins.clust.xml");
		if (f.exists()) {
			System.out.println("exists");

			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader(
						new FileInputStream(f)));
				String s = null;
				while ((s = br.readLine()) != null) {
					System.out.println(s);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}
}

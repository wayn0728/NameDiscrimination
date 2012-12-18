package entry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import model.*;

import parser.HTMLParser;

public class NameDiscrimination {
	
	static HashMap<String, List> fileMap;
	static String path = "webps/web_pages";
	
	
	NameDiscrimination() {
		fileMap = new HashMap<String,List>();
	}
	public static void main(String[] args) {
		fileMap = readHTML();
		BaseLineModel model = new BaseLineModel();
		TFIDFModel tfidfModel = new TFIDFModel();
		SuffixTree suffixTree = new SuffixTree();
		
		//SVDModel svdMode = new SVDModel();
		//model.cluster(fileMap);
		//svdMode.cluster(fileMap);
		suffixTree.cluster(fileMap);
		//readXML();
	}

	private static HashMap<String, List> readHTML() {
		HTMLParser htmlParser = new HTMLParser();
		return htmlParser.parse(path);
		
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

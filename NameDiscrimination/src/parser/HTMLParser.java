package parser;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.htmlparser.jericho.*;



public class HTMLParser {
	
	public static void main(String[] args) throws IOException {
//		File rawfile = new File("webps/web_pages/Abby_Watkins/raw/001/index.html");
//		TextExtractor te = new TextExtractor(new Source(new FileInputStream(rawfile))); 
//		String temp = te.toString();
//		//StringBuffer temp = null;
//		//te.appendTo(temp);
//		System.out.println(temp);
		HashMap<String, List> map = parse("webps/web_pages");
	}
	
	private static List<String> parseOne(File f) {
		TextExtractor te;
		List<String> resList = new ArrayList<String>();
		try {
			te = new TextExtractor(new Source(new FileInputStream(f)));
			String temp = te.toString();
			resList = new ArrayList<String>();
			String[] sentences = temp.split(" ");
			for (String sentence: sentences) {
				if (!sentence.equals(" "))
					resList.add(sentence);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally {
			return resList;	
		}
		
	}
	
	public static HashMap<String, List> parse(String path) {
		HashMap<String, List> map = new HashMap<String, List>();
		File root = new File(path);
		if (root.isDirectory()) {
			File[] nameFiles = root.listFiles();
			for (File nameFile: nameFiles) {
				List<List<String>> oneName = new ArrayList<List<String>>();
				File raw = new File(nameFile.getAbsoluteFile()+"/raw");
				File[] rawFiles = raw.listFiles();
				for (File rawFile: rawFiles) {
					File file = new File(rawFile.getAbsoluteFile()+"/index.html");
					List<String> oneFile = new ArrayList<String>();
					if (!rawFile.getName().equals(".DS_Store")) {
						oneFile.add(rawFile.getName());
						oneFile.addAll(parseOne(file));
						oneName.add(oneFile);
					}
				}
				map.put(nameFile.getName(), oneName);
			}
		}
		return map;
	}

}

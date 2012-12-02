package parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {
	public void parsersXml() {
		// 实例化一个文档构建器工厂
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			File f = new File("webps/truth_files/Abby_Watkins.clust.xml");
			InputStream fs = new FileInputStream(f);
			Document document = db.parse(fs);
			Element element = document.getDocumentElement();  
	        NodeList entityList = element.getElementsByTagName("entity");  
			for (int i = 0; i < entityList.getLength(); i++) {
				Element entityItem = (Element) entityList.item(i);  
				System.out.println("id = " + entityItem.getAttribute("id"));
				NodeList docList = entityItem.getElementsByTagName("doc");
				for(int j=0;j<docList.getLength();j++){  
					Element docItem = (Element) docList.item(j);
					System.out.println("rank = " + docItem.getAttribute("rank"));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		XMLParser domxml = new XMLParser();
		domxml.parsersXml();
	}
}

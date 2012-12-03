package parser;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {
	public void parsersXml() {
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
				for (int j = 0; j < docList.getLength(); j++) {
					Element docItem = (Element) docList.item(j);
					System.out
							.println("rank = " + docItem.getAttribute("rank"));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		XMLParser domxml = new XMLParser();
		// domxml.parsersXml();
		domxml.generateXml("test", null);
	}

	public void generateXml(String name, HashMap<Integer, Integer> oneRes) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.newDocument();

			Element root = document.createElement("clustering");
			root.setAttribute("name", name);
			document.appendChild(root);

			HashMap<Integer, Set<Integer>> entityMap = new HashMap<Integer, Set<Integer>>();
			Iterator iter = oneRes.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				int rank = (Integer) entry.getKey();
				int entity = (Integer) entry.getValue();
				if (entityMap.containsKey(entity)) {
					Set<Integer> temp = entityMap.get(entity);
					temp.add(rank);
				} else {
					Set<Integer> temp = new HashSet();
					temp.add(rank);
					entityMap.put(entity, temp);
				}
			}
			iter = entityMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				int entityNum = (Integer) entry.getKey();
				Element entity = document.createElement("entity");
				entity.setAttribute("id", String.valueOf(entityNum));
				root.appendChild(entity);

				HashSet<Integer> set = (HashSet) entry.getValue();
				Iterator<Integer> it = set.iterator();
				while (it.hasNext()) {
					Integer rank = it.next();
					Element doc = document.createElement("doc");
					doc.setAttribute("rank", String.valueOf(rank));
					entity.appendChild(doc);
				}
			}

			File f = new File("result/" + name + ".clust.xml");
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty("indent", "yes");
			t.transform(new DOMSource(document), new StreamResult(
					new FileOutputStream(f)));

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

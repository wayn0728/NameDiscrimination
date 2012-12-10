package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import adapter.SVDAdapter;

import parser.XMLParser;

import cluster.Kmeans;


public class SVDModel {

	HashMap<List<String>, Integer> features;
	double[][] docsArray;
	HashMap<String,HashMap<Integer,Integer>> res;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void cluster(HashMap<String, List> fileMap) {
		Iterator iter = fileMap.entrySet().iterator(); 
		String name = null;
		
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    name = (String)entry.getKey(); 
		    List<List<String>> docs = (List<List<String>>)entry.getValue(); 
		    clusterOneName(name,docs);
		} 
	}

	private void generateXML(String name, HashMap<Integer, Integer> oneRes) {
		XMLParser xmlParser = new XMLParser();
		xmlParser.generateXml(name, oneRes);
	}

	private  void clusterOneName(String name, List<List<String>> docs) {
		
		extractFeature(docs);
		
		repFeature(docs);
		
		Kmeans kmeans= new Kmeans();
		HashMap<Integer,Integer> oneRes = new HashMap<Integer,Integer>();
		oneRes = kmeans.cluster(docsArray,docs);
		
		generateXML(name, oneRes);
	}

	private void extractFeature(List<List<String>> docs) {
		features = new HashMap<List<String>, Integer>();
		int indice = 0;
		for (List<String> doc: docs) {
			for (int i = 1; i< doc.size()-1; i++) {
				String first = doc.get(i);
				String second = doc.get(i+1);
				List<String> bigram = new ArrayList<String>();
				bigram.add(first);
				bigram.add(second);
				if (!features.containsKey(bigram))
					features.put(bigram, indice++);
			}
		}
	}
	
	private void repFeature(List<List<String>> docs) {
		docsArray = new double[docs.size()+1][features.size()];
//		for (int i = 0; i < docsArray.length; i++)
//				docsArray[i][0] = -1;
		int count = 0;
		for (List<String> doc : docs) {
			System.out.println(count++);
			try {
				int docPos = Integer.parseInt(doc.get(0));
				for (int i = 1; i< doc.size()-1; i++) {
					String first = doc.get(i);
					String second = doc.get(i+1);
					List<String> bigram = new ArrayList<String>();
					bigram.add(first);
					bigram.add(second);
					int featurePos = features.get(bigram);
					docsArray[docPos][featurePos] = 1;
				}
			}
			catch(Exception e) {
				String temp = doc.get(0);
				e.printStackTrace();
				System.out.print("stop");
			}
		}
		SVDAdapter svdAdapter = new SVDAdapter();
		docsArray = svdAdapter.decompose(docsArray);
	}

}

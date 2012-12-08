package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.math.*;

import parser.XMLParser;

import cluster.Kmeans;


public class TFIDFModel {

	HashMap<List<String>, int[]> features;
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
		List<List<List<String>>> biDocs=new ArrayList<List<List<String>>>();
		biDocs=extractFeature(docs);
		
		//Get the max count of bigram for each article
		int[] maxCount= new int[docs.size()];
		for (int docIndex=0;docIndex< biDocs.size();docIndex++) {
			for(List<String> bigram : biDocs.get(docIndex)){
				if(features.get(bigram)[docIndex+1]>maxCount[docIndex]){
					maxCount[docIndex]=features.get(bigram)[docIndex+1];
				}
			}
		}
		
		//Assign TFIDF weight
		HashMap<List<String>, double[] >tfidf= new HashMap<List<String>,double[]>();
		for (int docIndex=0;docIndex< biDocs.size();docIndex++) {
			for(List<String> bigram : biDocs.get(docIndex)){
				double[] tfidfValue = new double[2];
				int[] idAndCount = new int[docs.size()+1];
				idAndCount=features.get(bigram);
				tfidfValue[0]=idAndCount[0];
				double presentDocCount=0.0;
				for(int count : features.get(bigram)){
					if(count!=0)presentDocCount+=1.0;
				}
				//									TF						IDF
				tfidfValue[1]=((double)idAndCount[docIndex+1]/(double)maxCount[docIndex])*Math.log((double)docs.size()/presentDocCount);
				tfidf.put(bigram, tfidfValue);
			}
		}
		/*
		//visualize tfidf map
		int count=0;
		for(List<String> bigram : tfidf.keySet()){
			if(tfidf.get(bigram)[1]>1){
				System.out.print(bigram);
				count++;
			}
		}
		System.out.println(count);
		*/
		
		//PCA and building kmeans Array start from HERE!!!
		//filling in docarray
		docsArray=new double[docs.size()][tfidf.size()];
		for (int i = 0; i<biDocs.size();i++) {
			List<List<String>> biDoc=new ArrayList<List<String>>();
			biDoc=biDocs.get(i);
			for(List<String> bigram : biDoc){
				int featurePos = features.get(bigram)[0];
				double tfidfValue=tfidf.get(bigram)[1];
				docsArray[i][featurePos] = tfidfValue;
			}			
		}
		
		Kmeans kmeans= new Kmeans();
		HashMap<Integer,Integer> oneRes = new HashMap<Integer,Integer>();
		oneRes = kmeans.cluster(docsArray,docs);
		
		generateXML(name, oneRes);
	}

	private List<List<List<String>>> extractFeature(List<List<String>> docs) {
		List<List<List<String>>> biDocs=new ArrayList<List<List<String>>>();
		features = new HashMap<List<String>, int[]>();
		int indice = 0;
		int docIndex = 1;
		for (List<String> doc: docs) {			
			List<List<String>> biDoc=new ArrayList<List<String>>();
			for (int i = 1; i< doc.size() - 1; i++) {
				String first = doc.get(i);
				String second = doc.get(i+1);
				List<String> bigram = new ArrayList<String>();
				bigram.add(first);
				bigram.add(second);
				biDoc.add(bigram);
				int[] idAndCount = new int[docs.size()+1];
				if(!features.containsKey(bigram)){
					idAndCount[0]=indice++;
					idAndCount[docIndex]=1;			
					features.put(bigram,idAndCount);
				}else{
					idAndCount=features.get(bigram);
					idAndCount[docIndex]+=1;
					features.put(bigram,idAndCount);
				}
			}
			biDocs.add(biDoc);
			docIndex++;
		}
		/*
		//visualize non-zero features
		int all=0;
		for(List<String> bigram : features.keySet()){
			int[] idAndCount = new int[docs.size()+1];
			idAndCount=features.get(bigram);
			int sum=0;
			for(int i = 1; i<idAndCount.length;i++){
				sum+=idAndCount[i];
			}
			if(sum>2){
				//System.out.print(bigram);
				//System.out.println(sum);
				all++;
			}
		}
		System.out.println(all);
		*/
		return biDocs;
	}
	
	private void repFeature(List<List<List<String>>> biDocs) {
		docsArray = new double[biDocs.size()+1][features.size()];
//		for (int i = 0; i < docsArray.length; i++)
//				docsArray[i][0] = -1;
		
		
	}
}
package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.math.*;
import java.util.Collection;

import parser.XMLParser;

import cluster.Kmeans;


public class SuffixTree {

	List<List<String>> docs;
	Node root=null;
	List<List<Integer>> rawClusters=new ArrayList<List<Integer>>();
	List<List<Integer>> similarClusters=new ArrayList<List<Integer>>();
	List<List<Integer>> resultClusters=new ArrayList<List<Integer>>();
	//ArrayList<ArrayList<Integer>> resultCluster=new ArrayList<ArrayList<Integer>>();
	//ArrayList<ArrayList<Integer>> rawCluster=new ArrayList<ArrayList<Integer>>();
	//int totalGroup=0;
	

	public void cluster(HashMap<String, List> fileMap) {
		Iterator iter = fileMap.entrySet().iterator(); 
		String name = null;
		
		while (iter.hasNext()) {
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    name = (String)entry.getKey(); 
		    docs = (List<List<String>>)entry.getValue();
		    
		    //totalGroup=0;
		    resultClusters=new ArrayList<List<Integer>>();
		    similarClusters=new ArrayList<List<Integer>>();
		    rawClusters=new ArrayList<List<Integer>>();
		    clusterOneName(name);
		    //clusterOneName();
		} 
		System.out.println("hi");
	}
	

	private void generateXML(String name, HashMap<Integer, Integer> oneRes) {
		XMLParser xmlParser = new XMLParser();
		xmlParser.generateXml(name, oneRes);
	}

	private void clusterOneName(String name) {
		/*
		List<List<String>> miniTest=new ArrayList<List<String>>();
		ArrayList<String> s1=new ArrayList<String>(Arrays.asList("cat","ate","cheese"));
		ArrayList<String> s2=new ArrayList<String>(Arrays.asList("cat","ate","cat","and","cheese"));
		ArrayList<String> s3=new ArrayList<String>(Arrays.asList("mouse","ate","cheese"));
		miniTest.add(s1);
		miniTest.add(s2);
		miniTest.add(s3);
		docs=miniTest;*/
		
		
		root = new Node();
		for (int i=0; i<docs.size();i++){
			root.addSuffix(docs.get(i),i);
			//System.out.println(root);
		}
		
		nodeCluster(root);
		//extractCluster();
		for(int i =0 ;i<rawClusters.size();i++){
			List<Integer> baseCluster=rawClusters.get(i);
			similarClusters.add(new ArrayList(Arrays.asList(-1)));
			for(int j=i+1;j<rawClusters.size();j++){
				float count=0;
				List<Integer> compareCluster=rawClusters.get(j);
				for(int docIndex:baseCluster){
					if(compareCluster.contains(docIndex)){
						count++;
					}
				}
				if(count/baseCluster.size()>0.4 && count/compareCluster.size()>0.4){
					List<Integer> similarCluster=similarClusters.get(i);
					similarCluster.add(j);
					similarClusters.set(i,similarCluster);
				}
			}			
		}
		
		for(int i = 0 ;i<similarClusters.size();i++){
			if(similarClusters.get(i).get(0)==-1){
				if(similarClusters.get(i).size()==1){
					similarClusters.get(i).set(0, 0);
					resultClusters.add(new ArrayList<Integer>(Arrays.asList(i)));
					continue;
				}
				List<Integer> resultCluster=similarClusters.get(i);
				resultCluster=resultCluster.subList(1, resultCluster.size());
				//System.out.println(resultCluster);
				for(int j=0;j<resultCluster.size();j++){
					int docIndex=resultCluster.get(j);
					//System.out.println(docIndex);
					List<Integer> appendCluster=similarClusters.get(docIndex);
					for(int appendIndex: appendCluster){
						if(!resultCluster.contains(appendIndex) && appendIndex!=-1){
							resultCluster.add(appendIndex);
						}
					}
					similarClusters.get(docIndex).set(0, 0);
				}
				resultCluster.add(0,i);
				resultClusters.add(resultCluster);
			}
			//resultClusters.add(similarClusters.get(i));
		}
		for(int i =0; i<resultClusters.size();i++){
			List<Integer> rawList=resultClusters.get(i);
			resultClusters.set(i, new ArrayList<Integer>());
			for(int j=0; j<rawList.size(); j++){
				int rawIndex=rawList.get(j);
				for(int docIndex:rawClusters.get(rawIndex)){
					if(!resultClusters.get(i).contains(docIndex)){
						resultClusters.get(i).add(docIndex);
					}
				}
			}
		}
		
		
		HashMap<Integer,Integer> oneRes= new HashMap<Integer,Integer>();
		for (int i=0; i<resultClusters.size();i++){
			for(int j: resultClusters.get(i)){
				for(int k : rawClusters.get(j)){
					if(!oneRes.keySet().contains(k)){
						oneRes.put(k, i);
					}
				}
			}
		}
		generateXML(name, oneRes);
		
		System.out.println(name);
		//generateXML(name, oneRes);
	}
	/*
	private void extractCluster(){
		ArrayList<Integer> mergeCluster = new ArrayList<Integer>();
		//initial
		for(ArrayList<Integer> node: rawCluster){
			if(totalGroup==0){
				totalGroup++;
			}
			node.add(0, -1);
			for(int i = 0 ; i < rawCluster.size() ; i++){
				//when coming node haven't been assigned to any group
				ArrayList<Integer> cluster = rawCluster.get(i);
				if(node.get(0)==-1){
					float count=0;
					
					for(int j=1;j<cluster.size();j++){
						if(node.contains(cluster.get(j))) count++;
					}
					//similar group discovered
					if(count/node.size()>0.5 && count/cluster.size()>0.5){
						node.set(0, cluster.get(0));
						rawCluster.add(node);
					}
				}else{//node has been assigned to a group skip same group if similar with other group merge all groups
					if (!(node.get(0)==cluster.get(0) || mergeCluster.contains(cluster.get(0)))){
						float count=0;
						for(int j=1;j<cluster.size();j++){
							if(node.contains(cluster.get(j))) count++;
						}
						//similar group discovered
						if(count/node.size()>0.5 && count/cluster.size()>0.5){
							if(!mergeCluster.contains(node.get(0))){
								mergeCluster.add(node.get(0));
							}
							mergeCluster.add(cluster.get(0));
						}
					}
				}
			}
			//no similar clusters
			if(node.get(0)==-1){
				totalGroup++;
				node.set(0,totalGroup);
				rawCluster.add(node);
			}
			//merge clusters
			if(mergeCluster.size()>0){
				for(ArrayList<Integer>cluster:rawCluster){
					if(mergeCluster.contains(cluster.get(0))){
						cluster.set(0, mergeCluster.get(0));
					}
				}
			}
		}
	}*/
	private void nodeCluster(Node current){
		if(current.children!=null){
			//boolean notFound=true;
			for(Node child : current.children){
				if(child.docList.size()>4){
					//notFound=false;
					nodeCluster(child);
				}
			}
			//if(notFound){
			//current.docList.add(0, -1);
			rawClusters.add(current.docList);
			//}
		}else if(current.docList.size()>4) {
			//current.docList.add(0, -1);
			rawClusters.add(current.docList);
		}
	}
	
	static class Node{
		//words this node represent
		List<String> content;
		List<Node> children = new ArrayList<Node>();
		Node parent = null;
		//all doc that bypass this node
		ArrayList<Integer> docList = new ArrayList<Integer>();
		
		public Node(Node parent, List<String> substring,int docIndex) {
		    this.parent = parent;
		    this.content= substring;
		    //?
		    this.docList.add(docIndex);
		}
		public Node(){
		}
		//leaf suffix < content
		private Node extendNode(Node current,List<String> suffix, int matchIndex, int docIndex){
			Node parent=current.parent;
			Node insert=new Node(parent,current.content.subList(0,matchIndex),docIndex);
			insert.children.add(current);
			insert.docList.addAll(0,current.docList);
			if(current.docList.contains(docIndex)) insert.docList.remove(insert.docList.size()-1);
			
			//Node suffixNode=new Node(insert,suffix.subList(matchIndex, suffix.size()),docIndex);
			//insert.children.add(suffixNode);
			
			int position=parent.children.indexOf(current);
			parent.children.remove(position);
			parent.children.add(position,insert);
			
			current.parent=insert;
			current.content=current.content.subList(matchIndex,current.content.size());
			//if(current.docList.contains(docIndex))current.docList.remove(new Integer(docIndex));

			return insert;
		}
		private Node divideNode(Node current,List<String> suffix, int matchIndex, int docIndex){
			Node parent=current.parent;
			Node insert=new Node(parent,current.content.subList(0,matchIndex),docIndex);
			insert.children.add(current);
			insert.docList.addAll(0,current.docList);
			if(current.docList.contains(docIndex)) insert.docList.remove(insert.docList.size()-1);
			
			Node suffixNode=new Node(insert,suffix.subList(matchIndex, suffix.size()),docIndex);
			insert.children.add(suffixNode);
			
			int position=parent.children.indexOf(current);
			parent.children.remove(position);
			parent.children.add(position,insert);
			
			current.parent=insert;
			current.content=current.content.subList(matchIndex,current.content.size());
			//if(current.docList.contains(docIndex))current.docList.remove(new Integer(docIndex));

			return insert;
		}
		
		public void addSuffix(List<String> suffix,int docIndex){ 
		    for(int i = 0 ; i<suffix.size();i++){
		    	//!!!!change this
		    	Node insertAt = this;
		    	insert(insertAt, suffix.subList(i, suffix.size()),docIndex);
		    }   
		}
		
		private void insert(Node insertAt, List<String> suffix, int docIndex) {
			boolean notFound=true;
			//Iterator<Node> iter = insertAt.children.iterator();
			//while (iter.hasNext()) {	
			for(int i=0; i<insertAt.children.size();i++){
				//Node child=iter.next();
				Node child = insertAt.children.get(i);
				List<String> nodeContent = child.content;
				if(nodeContent.get(0).equals(suffix.get(0))){
					notFound=false;
					//search for maxium match
					int matchIndex=0;
					for(int j=1;j<Math.min(nodeContent.size(),suffix.size());j++){
						if(!nodeContent.get(j).equals(suffix.get(j))){
							//sublist exclusive toIndex
							matchIndex=j;
							break;
						}
					}
					//this node is leaf insert new node as children
					if(child.isLeaf()){
						if(matchIndex==0){
							//add new node
							if(nodeContent.size()<suffix.size()){
								child.addDoc(docIndex);
								Node n = new Node(child,suffix.subList(nodeContent.size(), suffix.size()), docIndex);
								child.children.add(n);						
							}else if(suffix.size() < nodeContent.size()){
								child.extendNode(child, suffix, suffix.size(),docIndex);
							}else child.addDoc(docIndex);
						}else{
							child.divideNode(child, suffix, matchIndex, docIndex);
						}
					}else{//this is a node in the middle
						//node full match recall itself again for deeper search
						if(matchIndex==0){
							if(nodeContent.size()<suffix.size()){
								child.addDoc(docIndex);
								insert(child,suffix.subList(nodeContent.size(), suffix.size()),docIndex);
							}else if(suffix.size() < nodeContent.size()){
								child.extendNode(child, suffix, suffix.size(),docIndex);
							}else child.addDoc(docIndex);
						}else{//node part match build new branches
							child.divideNode( child, suffix, matchIndex, docIndex);
						}	
					}	
				}
			}
			if(notFound){
				Node child=new Node(insertAt,suffix,docIndex);
				insertAt.children.add(child);
				//System.out.println(insertAt.children);
			}
		}
		private void addDoc(int docIndex){
			if(!this.docList.contains(docIndex)) this.docList.add(docIndex);
		}
		
		public String toString() {
		    return this.content.toString();
		}
		public boolean isRoot() {
		    return this.parent == null;
		}
		public boolean isLeaf() {
		    return children.size() == 0;
		}
	}
}
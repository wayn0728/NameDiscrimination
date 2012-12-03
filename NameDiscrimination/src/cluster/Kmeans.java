package cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kmeans.*;

public class Kmeans {
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double[][] points = { { 0, 0 }, { 4, 10 }, { 1, 1 }, { 5, 8 },}; // 测试数据，四个二维的点
		kmeans_data data = new kmeans_data(points, 4, 2); // 初始化数据结构
		kmeans_param param = new kmeans_param(); // 初始化参数结构
		param.initCenterMehtod = kmeans_param.CENTER_RANDOM; // 设置聚类中心点的初始化模式为随机模式

		// 做kmeans计算，分两类
		kmeans.doKmeans(2, data, param);

		// 查看每个点的所属聚类标号
		System.out.print("The labels of points is: ");
		for (int lable : data.labels) {
			System.out.print(lable + "  ");
		}

	}

	public HashMap<Integer,Integer> cluster(double[][] docsArray, List<List<String>> docs) {
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		kmeans_data data = new kmeans_data(docsArray, docsArray.length, docsArray[0].length); // 初始化数据结构
		kmeans_param param = new kmeans_param(); // 初始化参数结构
		param.initCenterMehtod = kmeans_param.CENTER_RANDOM; // 设置聚类中心点的初始化模式为随机模式

		// 做kmeans计算，分两类
		kmeans.doKmeans(2, data, param);

		// 查看每个点的所属聚类标号
		System.out.print("The labels of points is: ");
		
		for (int i =0; i<data.labels.length; i++) {
			try {
				int docPos = Integer.parseInt(docs.get(i).get(0));
				if (i == docPos)
					map.put(i, data.labels[i]);
			}
			catch(Exception e) {
				//String temp = doc.get(0);
				System.out.print("error");
			}
		}
		
		
		for (int i =0; i<data.labels.length; i++) {
			System.out.print(i +":" + data.labels[i]+"    ");
		}
		for (int lable : data.labels) {
			System.out.print(lable + "  ");
		}		
		System.out.print("stop");
		return map;
	}
}

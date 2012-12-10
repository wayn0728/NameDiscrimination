package adapter;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class SVDAdapter {
	

	
	public static void main(String[] args) {
		
	}

	public double[][] decompose(double[][] rawFeature) {
		Matrix A = new Matrix(rawFeature);
		int reducedDimension = 0;
		if (A.getColumnDimension() > 3000)
			reducedDimension = 300;
		else reducedDimension /= 10;
		int[] k = new int[reducedDimension];
		for (int i = 0; i< reducedDimension; i++)
			k[i] = i;
		
		SingularValueDecomposition s = A.svd();
		Matrix U = s.getU();
		Matrix reducedU = U.getMatrix(0,U.getRowDimension()-1,k);
		Matrix S = s.getS();
		Matrix reducedS = S.getMatrix(k, k);
		Matrix V = s.getV();
		Matrix reducedV = S.getMatrix(k,0,V.getColumnDimension()-1);
		Matrix newM = reducedU.times(reducedS).times(reducedV);
		newM = newM.getMatrix(0,U.getRowDimension()-1,k);
		double[][] res = newM.getArray();
//		double[] sum = new double[newM.getColumnDimension()];
//		for (int i = 0; i < res.length; i++) {
//			for (int j = 0; j < res[i].length; j++) {
//				sum[j] += res[i][j];
//			}
//		}
//		for (int i= 0; i<sum.length; i++)
//			sum[i] /= res.length;
		return res;
	}

}

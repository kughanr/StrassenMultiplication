import java.util.ArrayList;
import java.util.Vector;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.*;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.data.xy.XYSeriesCollection; 
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import java.awt.Color; 
import java.awt.BasicStroke; 

import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.data.xy.XYDataset; 
import org.jfree.data.xy.XYSeries; 
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.ChartFactory;

import java.io.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ChartUtils;; 


/**
 * This class offers different algorithms for matrix multiplication.
 *
 * @author Martin Thoma
 */
public class strassen {
    static int LEAF_SIZE = 1;

    public static int[][] normalMultiply(int[][] A, int[][] B){
		int n = A.length;
		
		int [][] output = new int[n][n];
		
		for(int i=0; i<n; i++){
			for(int j=0; j<n; j++){
				output[i][j] = 0; //initalize a value
				for(int k=0; k<n; k++){
					output[i][j] += A[i][k]*B[k][j];
				}
			}
		}
		
		return output;
	}
    
    //function to add two arrays
    private static int[][] add(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] + B[i][j];
            }
        }
        return C;
    }

    //function to subtract two arrays
    private static int[][] subtract(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] - B[i][j];
            }
        }
        return C;
    }

    private static int nextPowerOfTwo(int n) {
        return (int) Math.pow(2, (int) Math.ceil(Math.log(n) / Math.log(2)));
    }

    public static int[][] StrassenMultiply(int[][] A, int[][] B) {
       //strassen requires the matrix to be a power of 2
        int n = A.length;
        int m = nextPowerOfTwo(n);
        int[][] APrep = new int[m][m];
        int[][] BPrep = new int[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
            	if(i<n && j<n){
            		APrep[i][j] = A[i][j];
            		BPrep[i][j] = B[i][j];
            	} else {
            		APrep[i][j] = 0;
            		BPrep[i][j] = 0;
            		
            	}
            }
        }

        int[][] CPrep = StrassenRecursive(APrep, BPrep);
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = CPrep[i][j];
            }
        }
        return C;
    }

    private static int[][] StrassenRecursive(int[][] A, int[][] B) {
        int n = A.length;
        
        if(n==1){
        	return normalMultiply(A,B);
        } else {
        
            // initializing the new sub-matrices
            //Matrix A
            int[][] a11 = new int[n/2][n/2];
            int[][] a12 = new int[n/2][n/2];
            int[][] a21 = new int[n/2][n/2];
            int[][] a22 = new int[n/2][n/2];
            //Matrix B
            int[][] b11 = new int[n/2][n/2];
            int[][] b12 = new int[n/2][n/2];
            int[][] b21 = new int[n/2][n/2];
            int[][] b22 = new int[n/2][n/2];

            int[][] aResult = new int[n/2][n/2];
            int[][] bResult = new int[n/2][n/2];

            // dividing the matrices in 4 sub-matrices:
            for (int i = 0; i < n/2; i++) {
                for (int j = 0; j < n/2; j++) {
                    a11[i][j] = A[i][j]; // top left
                    a12[i][j] = A[i][j + n/2]; // top right
                    a21[i][j] = A[i + n/2][j]; // bottom left
                    a22[i][j] = A[i + n/2][j + n/2]; // bottom right

                    b11[i][j] = B[i][j]; // top left
                    b12[i][j] = B[i][j + n/2]; // top right
                    b21[i][j] = B[i + n/2][j]; // bottom left
                    b22[i][j] = B[i + n/2][j + n/2]; // bottom right
                }
            }

            // Calculating s1 to s7:
            aResult = add(a11, a22);
            bResult = add(b11, b22);
            int[][] s1 = StrassenRecursive(aResult, bResult); // s1 = (a11+a22) * (b11+b22)
            
            aResult = add(a21, a22);
            bResult = b11;
            int[][] s2 = StrassenRecursive(aResult, bResult); // s2 = (a21+a22) * (b11)
            
            aResult = a11;
            bResult = subtract(b12, b22);
            int[][] s3 = StrassenRecursive(aResult, bResult); // s3 = (a11) * (b12 - b22)
            
            aResult = a22;
            bResult = subtract(b21,b11);
            int[][] s4 = StrassenRecursive(aResult, bResult); // s4 = (a22) * (b21 - b11)
            
            aResult = add(a11, a12);
            bResult = b22;
            int[][] s5 = StrassenRecursive(aResult, bResult); // s5 = (a11+a12) * (b22)
            
            aResult = subtract(a21, a11); 
            bResult = add(b11, b12);
            int[][] s6 = StrassenRecursive(aResult, bResult); // s6 = (a21-a11) * (b11+b12)
            
            aResult = subtract(a12, a22);
            bResult = add(b21, b22);
            int[][] s7 = StrassenRecursive(aResult, bResult); // s7 = (a12-a22) * (b21+b22)

            // calculating sub matrices of c:
            int[][] c11 = subtract(add(add(s1,s4),s7), s5); // c11 = s1 + s4 - s5 + s7
            int[][] c12 = add(s3, s5); // c12 = s3 + s5
            int[][] c21 = add(s2, s4); // c21 = s2 + s4
            int[][] c22 = subtract(add(add(s1,s3),s6), s2); // c22 = s1 + s3 - s2 + s6

            // Grouping the results obtained in a single matrix:
            int[][] C = new int[n][n];
            for (int i = 0; i < n/2; i++) {
                for (int j = 0; j < n/2; j++) {
                    C[i][j] = c11[i][j];
                    C[i][j + n/2] = c12[i][j];
                    C[i + n/2][j] = c21[i][j];
                    C[i + n/2][j + n/2] = c22[i][j];
                }
            }
            return C;
        }
            
        
    }
    
    public static int[][] randomMatrix(int n) {
        
  	  int r = (int) Math.random();
  	  int adder = (int) Math.random();
  	  int[][] a=new int[n][n];
  	  int current = r;
  	  for(int i=0;i<n;i++)
  	  {
  	      for(int j=0;j<n;j++)
  	      {
  	         a[i][j] = current + adder;
  	         current += adder;
  	      }
  	  }
  	  return a;
    }
    
   
    
    public static void main(String[] args){
    	
    	
    	int start = 2;
    	int end = 512;
    	int length = end-start+1;
    	long[] timeTakenStrassen = new long[length];
    	long [] timeTakenNormal = new long[length];
    	//initialize arrays for storing time taken
    	for(int i = 0; i<timeTakenStrassen.length; i++){
    		timeTakenStrassen[i] = 0;
    		timeTakenNormal[i] = 0;
    	}
    	
    	int[][][][] randomMatrices = new int[length][2][][];
    	
    	//generate matrix
    	for (int m = start; m<=end; m++){
    		int i = m - start; // index for randomMatrices array
    		//int n = (int) Math.pow(2,m);
        	int [][] big = randomMatrix(m);
        	int [][] big2 = randomMatrix(m);
        	randomMatrices[i][0] = big;
        	randomMatrices[i][1] = big2;
    	}
 
    	for(int m=start; m<=end; m++){
    		int i = m - start; //index for  randomMatrices array
    		int[][] big = randomMatrices[i][0];
    		int[][] big2= randomMatrices[i][1];
    		long start2 = System.nanoTime();
    		StrassenMultiply(big,big2);
    		long end2 = System.nanoTime();
    		long strassenTime = end2 - start2;
    		timeTakenStrassen[i] = strassenTime;
    		//System.out.println("check");
    		
    	}
    	
    	for(int m=start; m<=end; m++){
    		int i = m - start; //index for  randomMatrices array
    		int[][] big = randomMatrices[i][0];
    		int[][] big2= randomMatrices[i][1];
    		long start2 = System.nanoTime();
    		normalMultiply(big,big2);
    		long end2 = System.nanoTime();
    		long normalTime = end2 - start2;
    		timeTakenNormal[i] = normalTime;
    	}
    	
    	final XYSeries strassenRuntime = new XYSeries("Strassen");
    	final XYSeries normalRuntime = new XYSeries("Normal");
    	final XYSeries fracRuntime = new XYSeries("Frac");
    	XYSeriesCollection strassenData = new XYSeriesCollection();
    	XYSeriesCollection normalData = new XYSeriesCollection();
    	XYSeriesCollection fracData = new XYSeriesCollection();
    	
    	for(int i=0; i<length; i++){
    		long strassenT = timeTakenStrassen[i];
    		long normalT = timeTakenNormal[i];
    		if(normalT != 0){
    			int j = i + start;
    			strassenRuntime.add(j,strassenT);
    			normalRuntime.add(j, normalT);
    			fracRuntime.add(j, strassenT/normalT);
    			System.out.println("n=" + j + " Strassen: " + strassenT + ", Normal: " + normalT + ", Frac: " + strassenT/normalT );
    		}
    	}
    	
    	strassenData.addSeries(strassenRuntime);
    	normalData.addSeries(normalRuntime);
    	fracData.addSeries(fracRuntime);
    	
    	//plot strassen
    	JFreeChart chart = ChartFactory.createXYLineChart(
    	        "Strassen Chart", // Chart title
    	        "Run Time", // X-Axis Label
    	        "Dimension of Matrix", // Y-Axis Label
    	        strassenData,
    	        PlotOrientation.VERTICAL ,
    	        true , true , false);
    	
    	int width = 640;   /* Width of the image */
        int height = 480;  /* Height of the image */ 
        File StrassenChart = new File( "StrassenChart.jpeg" ); 
       try {
        ChartUtils.saveChartAsJPEG(StrassenChart, chart, width, height);
       } catch (IOException e) {
    	   
       }
    	
        
    	        
    	//plot Normal
    	JFreeChart chartNorm = ChartFactory.createXYLineChart(
    	        "Normal Chart", // Chart title
    	        "X-Axis", // X-Axis Label
    	        "Y-Axis", // Y-Axis Label
    	        normalData,
    	        PlotOrientation.VERTICAL ,
    	        true , true , false);
    	
    	
        File NormalChart = new File( "NormChart2.jpeg" ); 
       try {
        ChartUtils.saveChartAsJPEG(NormalChart, chartNorm, width, height);
       } catch (IOException e) {
    	   
       }
    
    	    
       //plot frac
       JFreeChart chartFrac = ChartFactory.createXYLineChart(
   	        "Normal Chart", // Chart title
   	        "X-Axis", // X-Axis Label
   	        "Y-Axis", // Y-Axis Label
   	        fracData,
   	        PlotOrientation.VERTICAL ,
   	        true , true , false);
   	
   	
       File FracChart = new File( "FracChart.jpeg" ); 
      try {
       ChartUtils.saveChartAsJPEG(FracChart, chartFrac, width, height);
      } catch (IOException e) {
   	   
      }
    	
    	
    	
    }
}
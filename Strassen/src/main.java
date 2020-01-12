import java.util.Arrays;

public class main {
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
	
	private static int[][] StrassenMultiply(int[][] A, int[][] B) {

        int n = A.length;

        int[][] output = new int[n][n];

        //for 1x1 matrices
        if (n == 1) {
            output[0][0] = A[0][0] * B[0][0];
        } else {

            // decomposition of first matrix
            int[][] a = new int[n/2][n/2];
            int[][] b = new int[n/2][n/2];
            int[][] c = new int[n/2][n/2];
            int[][] d = new int[n/2][n/2];
            
            // decomposition second matrix
            int[][] e = new int[n/2][n/2];
            int[][] f = new int[n/2][n/2];
            int[][] g = new int[n/2][n/2];
            int[][] h = new int[n/2][n/2];
            
            // split the first matrix (A) into 4 parts
            splitMatrix(A, a, 0, 0);
            splitMatrix(A, b, 0, n / 2);
            splitMatrix(A, c, n / 2, 0);
            splitMatrix(A, d, n / 2, n / 2);

            // split the second matrix (B) into 4 parts
            splitMatrix(B, e, 0, 0);
            splitMatrix(B, f, 0, n / 2);
            splitMatrix(B, g, n / 2, 0);
            splitMatrix(B, h, n / 2, n / 2);
            
            /* Strassen's rules
              m1 = (a + d)(e + h)
              m2 = (c + d)e
              m3 = a(f - h)
              m4 = d(g - e)
              m5 = (a + b)h
              m6 = (c - a) (e + f)
              m7 = (b - d) (g + h)
            */
           
            int[][] m1 = StrassenMultiply(addMatrices(a, d), addMatrices(e, h));
            int[][] m2 = StrassenMultiply(addMatrices(c,d),e);
            int[][] m3 = StrassenMultiply(a, subtractMatrices(f, h));           
            int[][] m4 = StrassenMultiply(d, subtractMatrices(g, e));
            int[][] m5 = StrassenMultiply(addMatrices(a,b), h);
            int[][] m6 = StrassenMultiply(subtractMatrices(c, a), addMatrices(e, f));
            int[][] m7 = StrassenMultiply(subtractMatrices(b, d), addMatrices(g, h));

            
           /*
              C11 = p1 + p4 - p5 + p7
              C12 = p3 + p5
              C21 = p2 + p4
              C22 = p1 - p2 + p3 + p6
            */
           
            int[][] C11 = addMatrices(subtractMatrices(addMatrices(m1, m4), m5), m7);
            int[][] C12 = addMatrices(m3, m5);
            int[][] C21 = addMatrices(m2, m4);
            int[][] C22 = addMatrices(subtractMatrices(addMatrices(m1, m3), m2), m6);

            // merging sub matrices back into one matrix
            copySubMatrix(C11, output, 0, 0);
            copySubMatrix(C12, output, 0, n / 2);
            copySubMatrix(C21, output, n / 2, 0);
            copySubMatrix(C22, output, n / 2, n / 2);
        }
        return output;
    }
	
	//splits large matrix into smaller matrices
    public static void splitMatrix(int[][] input, int[][] subMatrix, int i, int j) 
    {
        for(int i1 = 0, i2 = i; i1 < subMatrix.length; i1++, i2++)
            for(int j1 = 0, j2 = j; j1 < subMatrix.length; j1++, j2++)
                subMatrix[i1][j1] = input[i2][j2];
    }
    
    //adds matrices together
    public static int[][] addMatrices(int[][] a, int[][] b) {
        int n = a.length;
        int[][] output = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                output[i][j] = a[i][j] + b[i][j];
            }
        }
        return output;
    }
    
    // Subtracting 2 matrices
    public static int[][] subtractMatrices(int[][] a, int[][] b) {
        int n = a.length;
        int[][] output = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                output[i][j] = a[i][j] - b[i][j];
            }
        }
        return output;
    }
    
    // fills matrix
    public static void copySubMatrix(int[][] subMatrix, int[][] outputMatrix, int i, int j) 
    {
        for(int i1 = 0, i2 = i; i1 < subMatrix.length; i1++, i2++)
            for(int j1 = 0, j2 = j; j1 < subMatrix.length; j1++, j2++)
                outputMatrix[i2][j2] = subMatrix[i1][j1];
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
//    	int[][][] matrices = new int[1000][][];
//    	int[][][] matrices2 = new int[1000][][];
//    	for(int l=0; l<10;l++){
//    		matrices[l] = randomMatrix((int)Math.pow(2, l));
//    		matrices2[l] = randomMatrix((int)Math.pow(2, l));
//    	}
//    	long[] timeTakenNormal = new long[1000]; 
//    	for(int i=0; i<10; i++){
//    		long startTime = System.nanoTime();
//    		normalMultiply(matrices[i], matrices2[i]);
//    		long endTime = System.nanoTime();
//    		timeTakenNormal[i] = endTime - startTime;
//    		
//    	}
//    	int[][] a = {{1,1,3,4}, {1,1,2,5}, {4,2,1,3}, {1,6,1,3}};
//    	int[][] b = {{1,1,2,4}, {1,3,3,2}, {4,2,5,3}, {1,1,5,3}};
//    	int[][] c = normalMultiply(a,b);
//    	int[][] d = StrassenMultiply(a,b);
//    	System.out.println("NORMAL");
//    	for(int i = 0; i<a.length; i++){
//    		System.out.println(Arrays.toString(c[i]));
//    	}
//    	
//    	System.out.println("STRASSEN");
//    	for(int i = 0; i<a.length; i++){
//    		System.out.println(Arrays.toString(d[i]));
//    	}
    	int n = 8;
    	int [][] big = randomMatrix((int)Math.pow(2, n));
    	int [][] big2 = randomMatrix((int)Math.pow(2, n));
    	long[] timeTakenStrassen = new long[1000];
//    	for(int i=0; i<10; i++){
//    		long startTime = System.nanoTime();
//    		StrassenMultiply(matrices[i], matrices2[i]);
//    		long endTime = System.nanoTime();
//    		timeTakenStrassen[i] = endTime - startTime;
//    	}
//    	for(int i=0; i<10; i++){
//    		System.out.println((double) timeTakenNormal[i] / (double) timeTakenStrassen[i]);
//    	}
    	long start = System.nanoTime();
    	StrassenMultiply(big,big2);
    	long end = System.nanoTime();
    	long start2 = System.nanoTime();
    	normalMultiply(big,big2);
    	long end2 = System.nanoTime();
    	System.out.println((end-start)/(end2-start2));
   
    }

    
    
}

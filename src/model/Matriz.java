/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

/**
 *
 * @author edd
 */
public class Matriz {
    
    private double [][] matriz;
    private int fila;
    private int columna;
    
    public Matriz(int fila, int columna){
        this.fila = fila;
        this.columna = columna;
        matriz = new double[fila][columna];
    }

    public double[][] getMatriz() {
        return matriz;
    }

    public void setMatriz(double[][] matriz) {
        this.matriz = matriz;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }
    
    public double det(){
        return det(0,this.matriz);
    }
    
    public double det(int i, double[][] m){
        
        
        if (m.length == 2) {
                return(m[0][0]*m[1][1]) - (m[1][0] * m[0][1]);
        }else{

            double det = 0.0;

            for (int j = 0; j < m.length; j++) {
                double [][] mTemp = subMatriz(i, j, m);
                det = det + Math.pow(-1, j + i) * m[i][j]*det(i,mTemp);
            }

            return det;
        }
        
    }
    
    private double[][] subMatriz(int i, int j, double [][] m){
        
        double[][] temp = new double[m.length - 1][m.length -1];
        
        int c;
        int f = 0;
        
        for (int k = 0; k < m.length; k++) {
            if (k!=i) {
                c = 0;
                for (int l = 0; l < m.length; l++) {
                    if (l!=j) {
                        temp[f][c] = m[k][l];
                        c++;
                    }
                } 
                f++;
            }
            
        }
        return temp;
        
    }
    
    
    
    
    
    
    
    ////////////////////////
    
    
    public static double matrixDeterminant (double[][] matrix) {
		double temporary[][];
		double result = 0;

		if (matrix.length == 1) {
			result = matrix[0][0];
			return (result);
		}

		if (matrix.length == 2) {
			result = ((matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]));
			return (result);
		}

		for (int i = 0; i < matrix[0].length; i++) {
			temporary = new double[matrix.length - 1][matrix[0].length - 1];

			for (int j = 1; j < matrix.length; j++) {
				for (int k = 0; k < matrix[0].length; k++) {
					if (k < i) {
						temporary[j - 1][k] = matrix[j][k];
					} else if (k > i) {
						temporary[j - 1][k - 1] = matrix[j][k];
					}
				}
			}

			result += matrix[0][i] * Math.pow (-1, (double) i) * matrixDeterminant (temporary);
		}
		return (result);
}
    
    
     private double[][] cofactor (double a[][], int n, int p, int q)
    {
        double cofac[][] = new double [n-1][n-1];
        int x = 0, y = 0;
        for (int i = 0; i < n; ++i)
        {
            for (int j = 0; j < n; ++j)
                if (i != p && j != q)
                {
                    cofac[x][y] = a[i][j];
                    y++;
                }
            if (y > n-2)  { ++x; y = 0; }
        }
        return cofac;
    }

    private double determinant (double a[][], int n)
    {
        if (n == 1)  return a[0][0];
        else
        {
            int res = 0;
            for (int i = 0; i < n; ++i)
                    res += ((int)Math.pow(-1, i+0)) * a[i][0] * determinant(cofactor(a, n, i, 0), n-1);
            return res;
        }
    }


    
}

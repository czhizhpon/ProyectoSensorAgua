/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Matriz;
import java.util.Arrays;

/**
 *
 * @author edd
 */
public class RegresionPolinomial {
    private Matriz matriz;
    private int grado;
    private double[] resultados;
    private double[] coef;

    public RegresionPolinomial(double[] x, double[] y, int grado) {
        this.grado = grado;
        this.matriz = new Matriz(grado + 1, grado + 1);
        this.coef = new double[grado + 1];
        calcCoef(x, y);
        
    }
    
    public Matriz getMatriz() {
        return matriz;
    }

    public void setMatriz(Matriz matriz) {
        this.matriz = matriz;
    }

    public int getGrado() {
        return grado;
    }

    public void setGrado(int grado) {
        this.grado = grado;
    }
    
    public double[] getCoef() {
        return coef;
    }

    public void setCoef(double[] coef) {
        this.coef = coef;
    }
    
    private double sum(double[] v, int n){
        int nv = v.length;
        double r = 0;
        for (int i = 0; i < nv; i++) {
            r = (Math.pow(v[i], n)) + r;
        }
        return r;
    }
    
    private void llenar(double[] x){
        int g;
        
        for (int i = 0; i < matriz.getFila(); i++) {
            g = i;
            for (int j = 0; j < matriz.getColumna(); j++) {
                matriz.getMatriz()[i][j] = sum(x, g);
                g++;
            }
        }
    }
    
    private void calcResul(double[] x, double[] y){
        resultados = new double[grado + 1];
        double aux;
        
        for (int i = 0; i < resultados.length; i++) {
            aux = 0;
            for (int j = 0; j < x.length; j++) {
                aux = aux + (Math.pow(x[j], i)) * y[j];
            }
            resultados[i] = aux;
        }
        
    }
    
    private void interCol(double[][] m, int c){
        for (int i = 0; i < matriz.getFila(); i++) {
            m[i][c] = resultados[i];
        }
    }
    
    private void recuperar(double[][] aux){
        for (int i = 0; i < aux.length; i++) {
            for (int j = 0; j < aux[0].length; j++) {
                aux[i][j] = matriz.getMatriz()[i][j];
            }
        }
    }
    
    private void p(){
        for (int i = 0; i < matriz.getMatriz().length; i++) {
            for (int j = 0; j < matriz.getMatriz()[0].length; j++) {
                System.out.print(matriz.getMatriz()[i][j] + "\t");
            }
            System.out.println("");
        }
    }
    
    private void calcCoef(double[] x, double[] y){
        
        llenar(x);
        calcResul(x, y);
        
        Matriz aux = new Matriz(grado + 1, grado + 1);
        
        for (int i = 0; i < aux.getFila(); i++) {
            for (int j = 0; j < aux.getColumna(); j++) {
                aux.getMatriz()[i][j] = matriz.getMatriz()[i][j];
            }
        }
        
        double detMatriz = matriz.det();
        for (int i = 0; i < coef.length; i++) {
            recuperar(aux.getMatriz());
            interCol(aux.getMatriz(), i);
            coef[i] = aux.det() / detMatriz;
        }
    }
    
    public double eval(double x){
        float resul = 0;
        
        for (int i = 0; i < coef.length; i++) {
            resul = (float)(resul + coef[i]*Math.pow(x, i));
        }
        
        return resul;
    }
    
}

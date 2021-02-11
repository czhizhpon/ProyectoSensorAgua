/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.panamahitek.ArduinoException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author edd
 */
public class GestionSensor {
    
    private RegresionPolinomial regresion;
    private Arduino arduino;
    
    private double[] datosX;
    private double[] datosY;
    
    private double[] evalX;
    private double[] evalY;
    
    private String nombreX;
    private String nombreY;

    private boolean state;

    public GestionSensor() {
        state = false;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
    
    public String getNombreX() {
        return nombreX;
    }

    public void setNombreX(String nombreX) {
        this.nombreX = nombreX;
    }

    public String getNombreY() {
        return nombreY;
    }

    public void setNombreY(String nombreY) {
        this.nombreY = nombreY;
    }

    public double[] getDatosX() {
        return datosX;
    }

    public void setDatosX(double[] datosX) {
        this.datosX = datosX;
    }

    public double[] getDatosY() {
        return datosY;
    }

    public void setDatosY(double[] datosY) {
        this.datosY = datosY;
    }

    public double[] getEvalX() {
        return evalX;
    }

    public void setEvalX(double[] evalX) {
        this.evalX = evalX;
    }

    public double[] getEvalY() {
        return evalY;
    }

    public void setEvalY(double[] evalY) {
        this.evalY = evalY;
    }
    
    public boolean conectarArduino(){
        
        arduino = new Arduino();
        
        try {
            arduino.conectar();
            this.state = true;
            return true;
        } catch (Exception ex) {
            System.out.println("ex \n" + ex.getMessage());
            return false;
        }
    }
    
    public void leerArchivo(String ruta) throws IOException{
            this.nombreX = "X";
            this.nombreY = "Y";
            String linea = "";
            List<Double> datosX = new ArrayList<>();
            List<Double> datosY = new ArrayList<>();
            FileReader file = null;
        try {
            
            file = new FileReader(ruta);
            BufferedReader read = new BufferedReader(file);
            boolean con = false;
            while(linea != null){
                linea = read.readLine();
                
                if (linea!=null && con) {
                    String [] a = linea.split(",");
                    datosX.add(Double.parseDouble(a[0]));
                    datosY.add(Double.parseDouble(a[1]));
                }else{
                    String [] n = linea.split(",");
                    nombreX = n[0];
                    nombreY = n[1];
                    con = true;
                }
                
            }
            
        } catch (NullPointerException ex) {
            
        }catch (ArrayIndexOutOfBoundsException e){
            this.nombreX = "X";
            this.nombreY = "Y";
        }finally{
            file.close();
            
        }
        
            this.datosX = new double[datosX.size()];
            this.datosY = new double[datosY.size()];
            this.evalX = new double[this.datosX.length];
            this.evalY = new double[this.datosY.length];
            convDat(datosX, this.datosX);
            convDat(datosY, this.datosY);
    }
    
    public void guardarArchivo(String ruta) throws IOException{
        FileWriter guardar = null;
        
        try {
            guardar = new FileWriter(ruta);
            guardar.write("Segundos[s],Caudal[L/m]");
            for (int i = 0; i < this.datosX.length; i++) {
                guardar.write("" + this.datosX[i] + "," + this.datosY[i]);
            }
            
        } catch (IOException ex) {
            
        }finally{
            guardar.close();
        }
    }
    
    private void convDat(List<Double> l, double[] d){
        int i = 0;
        for (double e : l) {
            d[i] = e;
            i++;
        }
        
    }
    
    public void evalEcuacion(int g){
        
        double x[] = new double[]{1,2,3,4};
        double y[] = new double[]{-2,3,1,2};
        
        this.regresion = new RegresionPolinomial(datosX, datosY, g);
        
        //System.out.println(Arrays.toString(regresion.getCoef()));
        for (int i = 0; i < datosX.length; i++) {
            evalX[i] = datosX[i];
            evalY[i] = regresion.eval(evalX[i]);
        }
        
    }
    
    public void getArduinoDatos() throws ArduinoException{
        
        List<String> dat = this.arduino.getDatos();
        List<Double> datosX = new ArrayList<>();
        List<Double> datosY = new ArrayList<>();
        
        for(String d:dat){
            String [] a = d.split(",");
            datosX.add(Double.parseDouble(a[0]));
            datosY.add(Double.parseDouble(a[1]));
        }
        this.datosX = new double[datosX.size()];
        this.datosY = new double[datosY.size()];
        this.evalX = new double[this.datosX.length];
        this.evalY = new double[this.datosY.length];
        convDat(datosX, this.datosX);
        convDat(datosY, this.datosY);
        
        
        if (this.datosX.length > 1) {
            this.nombreX = "Segundos[s]";
            this.nombreY = "Caudal[L/m]";
        }else{
            this.nombreX = "X";
            this.nombreY = "Y";
        }
        if (!state) {
            this.arduino.desconectar();
        }
    }
    
    public void impX(){
        for (double datosX1 : datosX) {
            System.out.println(datosX1 + "\t");
        }
    }
    
    public void impY(){
        for (double datosX1 : datosY) {
            System.out.println("|" + datosX1);
        }
    }
    
}

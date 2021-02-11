/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import com.panamahitek.PanamaHitek_MultiMessage;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 *
 * @author edd
 */
public class Arduino implements SerialPortEventListener{
    
    private PanamaHitek_Arduino arduino;
    private SerialPortEventListener serial;
    private InputStream input;
    private OutputStream output;
    private SerialPort port;
    private List<String> datos;
    
    public Arduino(){
        arduino = new PanamaHitek_Arduino();
        datos = new ArrayList<>();
    }

    public List<String> getDatos() {
        return datos;
    }

    public void setDatos(List<String> datos) {
        this.datos = datos;
    }
    
    public void conectar() throws Exception{
        int a = arduino.getPortsAvailable();
        List<String> puertos = arduino.getSerialPorts();
        
        System.out.println(puertos.toString());
        
        for (String puerto:puertos) {
            if (conectar(puerto)){
                return;
            }
        }
        throw new Exception();
        
    }
    
    private boolean conectar(String puerto){
        
        try{
            arduino.arduinoRX(puerto, 9600, this);
        }catch(Exception ex){
            return false;
        }
        
        return true;
    }
    
    public void desconectar() throws ArduinoException{
         arduino.killArduinoConnection();
    }
    
    public void pausar(){
        
    }
    
    public void reiniciar() throws ArduinoException, Exception{
        desconectar();
        conectar();
        this.datos = new ArrayList<>();
    }
    
    public String dato(String dato){
        
        return dato;
    }

    @Override
    public void serialEvent(SerialPortEvent spe) {
        try {
            
            if (arduino.isMessageAvailable()) {
                datos.add(arduino.printMessage());
            }
            if (arduino.receiveData()!=null) {
                String dato = this.arduino.receiveData().toString();
                
            }
            
        } catch (ArduinoException ex) {
            
        } catch (SerialPortException ex) {
            
        } catch (Exception ex) {
            
        }
    }
    
}

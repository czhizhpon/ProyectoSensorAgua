package view;

import com.panamahitek.ArduinoException;
import controller.GestionSensor;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author edd
 */
public class UpdateData extends Thread {
    
    GestionSensor gestion;
    MainWindow mw;
    
    public UpdateData(GestionSensor gestion, MainWindow mw){
        this.gestion = gestion;
        this.mw = mw;
    }
    
    @Override
    public void run(){
        while(this.gestion.isState()){
            try {
                Thread.sleep(1000);
                this.gestion.getArduinoDatos();
                this.mw.updateTable();
            } catch (Exception ex) {
            }
        }
        
    }
    
}

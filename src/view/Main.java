package view;

//import com.mathworks.engine.MatlabEngine;
import javax.lang.model.SourceVersion;
import javax.swing.UIManager;

public class Main {
	public static void main(String [] args) {
		
		try {
                    
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
		} catch (Exception e) {
                    System.out.println(" e \n" + e);
		}
		
                 
		MainWindow mw = new MainWindow();
		mw.setVisible(true);
	}
}
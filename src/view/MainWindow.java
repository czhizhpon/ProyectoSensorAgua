package view;

import com.panamahitek.ArduinoException;
import controller.GestionSensor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class MainWindow extends JFrame implements ActionListener{

    private Grafica grafica;
    private GestionSensor gs;
    private UpdateData data;
    
    private JButton plotBtn;
    private JTable table;
    private DefaultTableModel model;
    private JTextField gradoTxt;
    
    private String file;
    
    
    public MainWindow () {
        gs = new GestionSensor();
        data = new UpdateData(this.gs, this);
        
        try{
            initComponents();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void initComponents() {

        setTitle("Proyecto Sensor de Presión");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.NONE;
        
        /*Panel Arriba*/
        
        JPanel upPanel = new JPanel(new GridBagLayout());
        
        JButton upFile = new JButton("Abrir Archivo");
        upFile.addActionListener(this);
        upFile.setActionCommand("upfile");
        
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.anchor = GridBagConstraints.WEST;
        upPanel.add(upFile, gbc);
        
        JButton conect = new JButton("Conectar");
        conect.addActionListener(this);
        conect.setActionCommand("conect");
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 0;
            gbc.anchor = GridBagConstraints.EAST;
        upPanel.add(conect, gbc);
        
        
        JButton term = new JButton("Terminar");
        term.addActionListener(this);
        term.setActionCommand("term");
            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.weightx = 0;
            gbc.anchor = GridBagConstraints.EAST;
        upPanel.add(term, gbc);
        
        JButton save = new JButton("Guardar");
        save.addActionListener(this);
        save.setActionCommand("save");
            gbc.gridx = 3;
            gbc.gridy = 0;
            gbc.weightx = 0;
            gbc.anchor = GridBagConstraints.EAST;
        //upPanel.add(save, gbc);
        
         
//        upPanel.setBorder(BorderFactory.createEtchedBorder(Color.lightGray, Color.yellow));
        /*Panel Central*/
        
        JPanel graficaPanel = new JPanel();
        graficaPanel.setLayout(new BorderLayout());

            grafica = new Grafica();
            graficaPanel.add(grafica, BorderLayout.CENTER);
            grafica.setReescalar(200);
            grafica.setMoveX(-275);
            grafica.setMoveY(250);
            grafica.setColorFondo(Color.DARK_GRAY);
            grafica.setColorLineas(Color.lightGray);
            grafica.setColorNumeros(Color.white);
         
            
            
        JPanel tablePanel = new JPanel(new BorderLayout());
        table = new JTable();
        JScrollPane src = new JScrollPane(table);
        
        tablePanel.add(src, BorderLayout.CENTER);
        
        plotBtn = new JButton("PLOT");
        plotBtn.addActionListener(this);
        plotBtn.setActionCommand("plot");
        tablePanel.add(plotBtn, BorderLayout.SOUTH);
        
            /**/ 
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        centerPanel.add(graficaPanel, BorderLayout.CENTER);
        
        /*Rigth Panel*/
        JPanel rigthPanel = new JPanel(new BorderLayout());
        centerPanel.add(tablePanel, BorderLayout.EAST);
        
        
        
        /*Down Panel*/
        JPanel downPanel = new JPanel(new GridBagLayout());
        
        gbc = new GridBagConstraints();
            gbc.insets = new Insets(2, 2, 2, 2);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            JLabel gradoLb = new JLabel("Grado:", 11);
                gbc.gridx = 0;
                gbc.gridy = 0;
            downPanel.add(gradoLb, gbc);
            
            gradoTxt = new JTextField(20);
                gbc.gridx = 1;
                gbc.gridy = 0;
            downPanel.add(gradoTxt, gbc); 
            
            JButton evalBtn = new JButton("Evaluar");
            evalBtn.addActionListener(this);
            evalBtn.setActionCommand("eval");
                gbc.gridx = 2;
                gbc.gridy = 0;
            downPanel.add(evalBtn, gbc);
            
        
        
        
        Container c = getContentPane();

        c.setLayout(new BorderLayout());
        c.add(upPanel, BorderLayout.NORTH);
        c.add(centerPanel, BorderLayout.CENTER);
        c.add(rigthPanel, BorderLayout.EAST);
        c.add(downPanel, BorderLayout.SOUTH);
        
    }

    public void plot(){
        
        try{
            grafica.cleanStem();
            grafica.cleanPlot();
            grafica.addStem(gs.getDatosX(), gs.getDatosY(), null,Color.cyan, 10);
            
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Seleccione un archivo valido");
            
        }
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String c = e.getActionCommand();
        
        switch(c){
            case "plot":
                plot();
                break;
            case "eval":
                evaluar();
                break;
            case "upfile":
                upFile();
                break;
            case "conect":
                
                if (!this.gs.conectarArduino()){
                    JOptionPane.showMessageDialog(null, "No se pudo conectar");
                }else{
                    data.start();
                }
                break;
                
            case "term":
                terminar();
                break;
            case "save":
                save();
                break;
            default:
            
        }
    }

    private void evaluar() {
        try{
            int g = Integer.parseInt(gradoTxt.getText());
            if (g>9)
                return;
            gs.evalEcuacion(g);
            
//            if (grafica.plotSize() > 0) {
//                grafica.deletePlot(0);
//            }
            grafica.cleanPlot();
            System.out.println("Plot " + grafica.plotSize());
            grafica.addPlot(gs.getEvalX(), gs.getEvalY(), null, Color.red, 7);
            

        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
    }

    private void upFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(fileChooser);
        
        try{
            this.file = fileChooser.getSelectedFile().getAbsolutePath();
            gs.leerArchivo(file);
            updateTable();
            grafica.cleanPlot();
        }catch (Exception e) {
            
        }
    }
    
    public void updateTable(){
        
        String datos [][] = new String[gs.getDatosX().length]
                [gs.getDatosY().length];

        for (int i = 0; i < gs.getEvalX().length; i++) {
            datos[i][0] = "" + gs.getDatosX()[i];
            datos[i][1] = "" + gs.getDatosY()[i];
        }
        
        
        model = new DefaultTableModel(datos, new String[]{this.gs.getNombreX(),this.gs.getNombreY()});
        
        this.table.setModel(model);
        
    }

    private void terminar() {
        
        try {
            this.gs.getArduinoDatos();
            updateTable();
            this.gs.setState(false);
        } catch (ArduinoException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
        
    }

    private void save() {
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showSaveDialog(fileChooser);
        
        try{
            this.file = fileChooser.getSelectedFile().getAbsolutePath();
            this.gs.guardarArchivo(file);
            
        }catch (Exception ex){
            
        }
        
    }

}

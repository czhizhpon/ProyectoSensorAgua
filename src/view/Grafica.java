/*
 * NuvArmi S.A 
 */
package view;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author EddZ, BearKarian, RobySerpa, Ranmoon
 */
public class Grafica extends Canvas{
    
    private Color colorFondo;
    private Color colorLineas;
    private Color colorNumeros;
    
    private int medioW;
    private int medioH;
    
    private int moveX;
    private int moveY;
    
    private Double zoom;
    private double intervaloX;
    private double intervaloY;
    
    private double maxX;
    private double maxY;
    private int reescalar;
    
    /*Valores para grficar el plano*/
    private List<Double> listaPuntosX;
    private List<Double> listaPuntosY;
    private List<Double> listaPuntosXN;
    private List<Double> listaPuntosYN;
    
    /*Valores para plot*/
    private List<double[]> valoresX;
    private List<double[]> valoresY;
    private List<String[]> valoresZ;
    private List<Color> colores;
    private List<BasicStroke> sizeP;
    
    /*Valores para stem*/
    private List<double[]> valoresXS;
    private List<double[]> valoresYS;
    private List<String[]> valoresZS;
    private List<Color> coloresS;
    private List<Integer> sizeS;
    
    private boolean stem;
    private boolean plot;
    
    public Grafica (){
        
        listaPuntosX = new ArrayList<>();
        listaPuntosY = new ArrayList<>();
        listaPuntosXN = new ArrayList<>();
        listaPuntosYN = new ArrayList<>();
        
        moveX = 0;
        moveY = 0;
        maxX = 0;
        maxY = 0;
        reescalar = -50;
        
        stem = false;
        plot = false;
        
        zoom = 0.0;
        intervaloX = 50;
        intervaloY = 50;
        
        colorFondo = Color.BLACK;
        colorLineas = Color.WHITE;
        colorNumeros = Color.GREEN;
        
        setBackground(colorFondo);
        
        super.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() > 0) {
                    zoom += 10;
                    int w = (getWidth()/2)-100 + reescalar;
                    int h = (getHeight()/2)-100 + reescalar;
                    if (zoom > w||zoom > h) {
                        if (w<h) {
                            zoom = (double)w;
                        }else{
                            zoom = (double)h;
                        }
                    }
                    repaint();
                }else if (e.getWheelRotation() < 0) {
                    zoom -= 10;
                    if (zoom < -5000) {
                    zoom =-5000.0 ;
                    }
                    repaint();
                }
            }
        });
        
        super.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                //System.out.println(" 1: " + e.toString() + "\n");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_RIGHT)
                    moveX-=10;
                else if(e.getKeyCode()==KeyEvent.VK_DOWN)
                    moveY-=10;
                else if(e.getKeyCode() ==KeyEvent.VK_LEFT)
                    moveX+=10;
                else if(e.getKeyCode() ==KeyEvent.VK_UP)
                    moveY+=10;
                repaint();
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println(" 3: " + e.getKeyCode() + "\n");
            }
        });
        
    }

    public int getMoveX() {
        return moveX;
    }

    public void setMoveX(int moveX) {
        this.moveX = moveX;
    }

    public int getMoveY() {
        return moveY;
    }

    public void setMoveY(int moveY) {
        this.moveY = moveY;
    }

    public int getReescalar() {
        return reescalar;
    }

    public void setReescalar(int reescalar) {
        this.reescalar = reescalar;
    }

    public Color getColorFondo() {
        return colorFondo;
    }

    public void setColorFondo(Color colorFondo) {
        this.colorFondo = colorFondo;
        setBackground(colorFondo);
    }

    public Color getColorLineas() {
        return colorLineas;
    }

    public void setColorLineas(Color colorLineas) {
        this.colorLineas = colorLineas;
    }

    public Color getColorNumeros() {
        return colorNumeros;
    }

    public void setColorNumeros(Color colorNumeros) {
        this.colorNumeros = colorNumeros;
    }
    
    public int plotSize() {
        if (valoresX == null) {
            return -1;
        }
        return valoresX.size();
    }
    
    public int stemSize() {
        if (valoresXS == null) {
            return -1;
        }
        return valoresXS.size();
    }
    
    public void addPlot(double[] x, double[] y, String[] z, Color c, int s){
        if (valoresX == null) {
            valoresX = new ArrayList<>();
            valoresY = new ArrayList<>();
            valoresZ = new ArrayList<>();
            colores = new ArrayList<>();
            sizeP = new ArrayList<>();
        }
        this.valoresX.add(x);
        this.valoresY.add(y);
        this.valoresZ.add(z);
        this.colores.add(c);
        sizeP.add(new BasicStroke(s));
        
        maxX = calcMax(valoresX, valoresX);
        maxY = calcMax(valoresY, valoresYS);
        
        plot = true;
        repaint();
    }
    
    public void deletePlot(int id){
        if (id >= this.valoresX.size()) {
            throw new IndexOutOfBoundsException("id fuera del rango");
        }
        this.valoresX.remove(id);
        this.valoresY.remove(id);
        this.valoresZ.remove(id);
        this.colores.remove(id);
        this.sizeP.remove(id);
        
        maxX = calcMax(valoresX, valoresXS);
        maxY = calcMax(valoresY, valoresYS);
        if (valoresX.size() <=0) {
            plot = false;
        }
        repaint();
    }
    
    public void cleanPlot(){
        this.valoresX = null;
        this.valoresY = null;
        this.valoresZ = null;
        this.colores = null;
        this.sizeP = null;
        
        this.maxX = 0;
        this.maxY = 0;
        
        plot = false;
        repaint();
        
    }
    
    public void addStem(double[] x, double[] y, String[] z, Color c, int s){
        if (valoresXS == null) {
            valoresXS = new ArrayList<>();
            valoresYS = new ArrayList<>();
            valoresZS = new ArrayList<>();
            coloresS = new ArrayList<>();
            sizeS = new ArrayList<>();
        }
        this.valoresXS.add(x);
        this.valoresYS.add(y);
        this.valoresZS.add(z);
        this.coloresS.add(c);
        sizeS.add(s);
        
        maxX = calcMax(valoresX, valoresXS);
        maxY = calcMax(valoresY, valoresYS);
        stem = true;
        repaint();
    }
    
    public void deleteStem(int id){
        if (id >= this.valoresXS.size()) {
            throw new IndexOutOfBoundsException("id fuera del rango");
        }
        this.valoresXS.remove(id);
        this.valoresYS.remove(id);
        this.valoresZS.remove(id);
        this.coloresS.remove(id);
        this.sizeS.remove(id);
        
        maxX = calcMax(valoresX, valoresXS);
        maxY = calcMax(valoresY, valoresYS);
        
        if (valoresXS.size() <=0) {
            stem = false;
        }
        
    }
    
    public void cleanStem(){
        this.valoresXS = null;
        this.valoresYS = null;
        this.valoresZS = null;
        this.coloresS = null;
        this.sizeS = null;
        
        this.maxX = 0;
        this.maxY = 0;
        
        stem = false;
        repaint();
    }
    
    /**
     * 
     * @param zoom 
     */
    public void setZoom(Double zoom) {
        this.zoom = zoom;
    }
    
    private double regla(double v, double l, int t){
        t= t + reescalar;
        return (v*t)/l;
    }
    
    private double calcMax(List<double[]> valoresP, List<double[]> valoresS){
        double maxP = 0;
        double maxPAnterior = 0;
        double maxS = 0;
        double maxSAnterior = 0;
        int n;
        try{
            for(double[] i:valoresP){
                n = i.length;
                maxP = Math.abs(i[0]);
                for(int j = 1; j < n; j++){
                    if(maxP < Math.abs(i[j]))
                        maxP = Math.abs(i[j]);
                }
                if (maxPAnterior > maxP) {
                    maxP = maxPAnterior;
                }
                maxPAnterior = maxP;
            }
            
        }catch (NullPointerException e){
            maxP = 0;
        }
        
        try{
            for(double[] i:valoresS){
                n = i.length;
                maxS = Math.abs(i[0]);
                for(int j = 1; j < n; j++){
                    if(maxS < Math.abs(i[j]))
                        maxS = Math.abs(i[j]);
                }
                if (maxSAnterior > maxS) {
                    maxS = maxSAnterior;
                }
                maxSAnterior = maxS;

            }
        }catch (NullPointerException e){
            maxS = 0;
        }
        if (maxP < maxS) {
           return maxS;
        }
        return maxP;
    }
    
    /**
     * 
     * @param g 
     */
    private void dibujarLinea(Graphics2D g){
        
       g.setColor(colorLineas);
        
       g.drawLine(-(super.getWidth() + moveX), 0, super.getWidth() - moveX, 0);
       g.drawLine(0, medioH - moveY, 0, -(medioH + moveY));
    }
    
    
    public void crearPuntos(){
        
        listaPuntosX = null;
        listaPuntosXN = null;
        listaPuntosY = null;
        listaPuntosYN = null;
        
        listaPuntosX = new ArrayList<>();
        listaPuntosXN = new ArrayList<>();
        listaPuntosY = new ArrayList<>();
        listaPuntosYN = new ArrayList<>();
        try{
            
            intervaloX = maxX/5;
            intervaloY = maxY/5;

            double inter = regla(intervaloX, maxX, medioW - zoom.intValue());
            for (double i = inter; i < -( -medioW + moveX); i = i + (inter)) {
                listaPuntosX.add(i);
            }
            for (double i = inter; i < medioW + moveX; i = i + inter) {
                listaPuntosXN.add(-i);
            }

            inter = regla(intervaloY, maxY, medioH - zoom.intValue());

            for (double i = inter; i < medioH  + moveY; i = i + inter) {
                listaPuntosY.add(-i);
            }
            for (double i = inter; i < -1*(-medioH + moveY); i = i + inter) {
                listaPuntosYN.add(i);
            }
       }catch(Exception ex){
           
       }
    }
    
    /**
     * 
     * @param g 
     */
    private void dibujarPuntos(Graphics2D g, List<Double> listaPuntosX,
            List<Double> listaPuntosXN, List<Double> listaPuntosY,
            List<Double> listaPuntosYN){
        
        double val = 0.0;
        int p;
        int n = listaPuntosX.size();
        for(int i = 0; i < n; i++){
            g.setColor(colorLineas);
            val = val + intervaloX;
            p = listaPuntosX.get(i).intValue();
            g.drawLine(p, 5, 
                    p,- 5);
            g.setColor(colorNumeros);
            g.setFont( new Font( "Arial", Font.BOLD, 12 ));
            g.drawString(String.format("%.2f", val), p - 10, 30);
        }
        val = 0.0;
        n = listaPuntosXN.size();
        for(int i = 0; i < n ; i++){
            g.setColor(colorLineas);
            val = val + intervaloX;
            p = listaPuntosXN.get(i).intValue();
            g.drawLine(p, 5, 
                    p, -5);
            g.setColor(colorNumeros);
            g.setFont( new Font( "Arial", Font.BOLD, 12 ));
            g.drawString(String.format("%.2f", -val), p - 10, 30);
        }
        n = listaPuntosY.size();
        val = 0.0;
        for (int i = 0; i < n; i++) {
            g.setColor(colorLineas);
            val = val + intervaloY;
            p = listaPuntosY.get(i).intValue();
            g.drawLine(5, p, 
                    -5, p);
            
            g.setColor(colorNumeros);
            g.setFont( new Font( "Arial", Font.BOLD, 12 ));
            g.drawString(String.format("%.2f", val), -50, p + 5);
            
        }
        n = listaPuntosYN.size();
        val = 0.0;
        for (int i = 0; i < n; i++) {
            g.setColor(colorLineas);
            val = val + intervaloY;
            p = listaPuntosYN.get(i).intValue();
            g.drawLine(5, p, 
                    -5, p);
            
            g.setColor(colorNumeros);
            g.setFont( new Font( "Arial", Font.BOLD, 12 ));
            g.drawString(String.format("%.2f", -val), -50, p + 5);
            
        }
    }
    
    /**
     * 
     * @param g
     * @return 
     */
    private Graphics2D graficarPlot(Graphics2D g){
        int puntoY1;
        int puntoX1;
        int puntoY2;
        int puntoX2;
        int n;
        
        int i;
        int nv = valoresX.size();
        for (int j = 0; j < nv; j++){
            g.setColor(colores.get(j));
            g.setStroke(this.sizeP.get(j));
            i=0;
            n = valoresX.get(j).length;
            puntoX1 = (int)regla(valoresX.get(j)[i],maxX,medioW - zoom.intValue());
            puntoY1 = (int)regla(valoresY.get(j)[i],maxY,medioH - zoom.intValue());
            i++;
            while(i<n){

                puntoX2 = (int)regla(valoresX.get(j)[i],maxX, medioW - zoom.intValue());
                puntoY2 = (int)regla(valoresY.get(j)[i],maxY,medioH - zoom.intValue());

                g.drawLine(puntoX1, -puntoY1, puntoX2, -puntoY2);

                puntoX1 = puntoX2;
                puntoY1 = puntoY2;
                i++;
            }
        }
        return g;
    }
    
    /**
     * 
     * @param g
     * @return 
     */
    private Graphics2D graficarStem(Graphics2D g){
        int puntoY1;
        int puntoX1;
        int n;
        
        g.setColor(Color.blue);
        int i;
        int nv = valoresXS.size();
        for (int j = 0; j < nv; j++){
            g.setColor(coloresS.get(j));
            i=0;
            n = valoresXS.get(j).length;
            while(i<n){

                puntoX1 = (int)regla(valoresXS.get(j)[i],maxX,medioW - zoom.intValue());
                puntoY1 = (int)regla(valoresYS.get(j)[i],maxY,medioH - zoom.intValue());

                g.fillOval(puntoX1 - (int)this.sizeS.get(j)/2, 
                        -(puntoY1 + (int)this.sizeS.get(j)/2), 
                        this.sizeS.get(j), this.sizeS.get(j));
                i++;
            }
        //stem = false;
        }
        return g;
    }
    
    /**
     * IMPRIME LOS ARRAYS
     * METODO PARA PRUEBAS
     * @param l 
     */
    public void imprimir(List<Double> l){
        for (double p:l) {
            System.out.print(" | " + p + " | ");        
        }
        System.out.println("\n\n");
    }
    
    public void update(){
        this.medioW = (int)((super.getWidth())/2);
        this.medioH = (int)((super.getHeight())/2);
        
        
        int w = (medioW)-100 + reescalar;
        int h = (medioH)-100 + reescalar;
        
        if (zoom > w||zoom > h) {
            if (w<h) {
                zoom = (double)w;
            }else{
                zoom = (double)h;
            }
        }
    }
    
    /**
     * 
     * @param g 
     */
    @Override
    public void paint(Graphics g) {
        
        BufferStrategy buffer = getBufferStrategy();
	if (buffer == null) {
	    createBufferStrategy(2);
	    return;
	}
        Graphics gb = buffer.getDrawGraphics();
        //Graphics gb = g;
        update();
        
        super.paint(gb);
       
        Graphics2D g2d = (Graphics2D) gb;
       
        g2d.translate(medioW + moveX, medioH + moveY);
        dibujarLinea(g2d);
       
        crearPuntos();
        try {
            dibujarPuntos(g2d, listaPuntosX, listaPuntosXN, listaPuntosY, 
                    listaPuntosYN);
            if (plot)
                graficarPlot(g2d);
        
            if (stem)
                graficarStem(g2d);
        } catch (Exception ex) {
        }
        
        buffer.show();
        gb.dispose();
        g2d.dispose();
    }
    
    
}

//Emilio Zetina 
//ID: 0055615
//Alfredo Vieto
//ID: 00558680
//Ing. TI

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class PanelArbol extends JPanel {
    private Arbol arbol;
    private String tipoRecorrido = "";
    private List<Nodo> recorrido = new ArrayList<>();
    private int indiceAnimacion = 0;
    
    private class NodoArea {
        Rectangle area;
        Nodo nodoRef;
        NodoArea(Rectangle r, Nodo n) { area = r; nodoRef = n; }
    }
    
    private List<NodoArea> ubicacionesNodos = new ArrayList<>();

    public PanelArbol(Arbol arbol) {
        this.arbol = arbol;
        setBackground(Color.WHITE);
        setToolTipText(""); 
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                manejarTooltip(e.getPoint());
            }
        });
    }
    
    public void actualizarAnimacion(String tipo, List<Nodo> lista, int indice) {
        this.tipoRecorrido = tipo;
        this.recorrido = lista;
        this.indiceAnimacion = indice;
        repaint();
    }
    
    public void limpiar() {
        this.tipoRecorrido = "";
        this.recorrido.clear();
        this.indiceAnimacion = 0;
        repaint();
    }
    
    public void setArbol(Arbol nuevoArbol) {
        this.arbol = nuevoArbol;
        repaint();
    }

    private void manejarTooltip(Point p) {
        boolean found = false;
        for (NodoArea na : ubicacionesNodos) {
            if (na.area.contains(p)) {
                Nodo n = na.nodoRef;
                if (n != null) {
                    int h = arbol.calcularAltura(n);
                    int niv = arbol.calcularNivel(n, n.costo);
                    int g = arbol.calcularGrado(n);
                    
                    setToolTipText("<html><div style='padding:5px; background-color:#ffffcc; border:1px solid black'>" +
                                   "<b>Producto:</b> " + n.nombre + "<br>" +
                                   "<b>Costo:</b> $" + String.format("%.2f", n.costo) + "<br>" +
                                   "<i>Altura: " + h + " | Nivel: " + niv + " | Grado: " + g + "</i>" +
                                   "</div></html>");
                    found = true;
                }
                break;
            }
        }
        if (!found) setToolTipText(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ubicacionesNodos.clear(); 
        
        if (arbol.raiz != null) {
            dibujarArbol(g, arbol.raiz, getWidth() / 2, 40, getWidth() / 4);
        }
        if (!tipoRecorrido.isEmpty() && !recorrido.isEmpty()) {
            dibujarRecorridoAnimado(g);
        }
    }

    private void dibujarArbol(Graphics g, Nodo nodo, int x, int y, int separacion) {
        if (nodo == null) return;
        
        g.setFont(new Font("Arial", Font.BOLD, 12));
        
        g.setColor(Color.BLACK);
        if (nodo.nodoIzquierdo != null) {
            g.drawLine(x, y, x - separacion, y + 80);
            dibujarArbol(g, nodo.nodoIzquierdo, x - separacion, y + 80, separacion / 2);
        }
        if (nodo.nodoDerecho != null) {
            g.drawLine(x, y, x + separacion, y + 80);
            dibujarArbol(g, nodo.nodoDerecho, x + separacion, y + 80, separacion / 2);
        }
        
        g.setColor(new Color(255, 220, 180)); 
        g.fillOval(x - 25, y - 25, 50, 50);
        g.setColor(Color.BLACK);
        g.drawOval(x - 25, y - 25, 50, 50);
        
        String label = nodo.nombre;
        if(label.length() > 5) label = label.substring(0, 5) + "..";
        
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(label);
        g.drawString(label, x - (textWidth / 2), y + 5);
        
        ubicacionesNodos.add(new NodoArea(new Rectangle(x - 25, y - 25, 50, 50), nodo));
    }

    private void dibujarRecorridoAnimado(Graphics g) {
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Recorrido " + tipoRecorrido + ":", 30, getHeight() - 100);
        
        int x = 30;
        int y = getHeight() - 60;
        
        for (int i = 0; i < indiceAnimacion && i < recorrido.size(); i++) {
            Nodo n = recorrido.get(i);
            
            g.setColor(new Color(200, 255, 200));
            g.fillOval(x, y - 25, 50, 50);
            g.setColor(Color.BLACK);
            g.drawOval(x, y - 25, 50, 50);
            
            String label = n.nombre;
            if(label.length() > 5) label = label.substring(0, 5) + "..";
            g.drawString(label, x + 5, y + 5);
            
            g.setFont(new Font("Arial", Font.PLAIN, 10));
            g.drawString("$" + (int)n.costo, x + 10, y + 20);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            
            x += 60;
            if(x > getWidth() - 50) {
                x = 30;
                y += 60;
            }
        }
    }
}


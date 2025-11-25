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

public class Interface extends JFrame {

    Arbol arbol = new Arbol();
    private DrawPanel panel;
    private JButton botonConstruir;
    private JButton botonInsertar;
    private JButton botonEliminar; // Nuevo
    private JButton botonBuscar;
    private JButton botonContar;
    private JButton botonBalancear; // Nuevo
    private JButton botonInorden;
    private JButton botonPreorden;
    private JButton botonPostorden;
    
    private String tipoRecorrido = "";
    private List<Integer> recorrido = new ArrayList<>();
    
    // Para la animación
    private Timer timerAnimacion;
    private int indiceAnimacion = 0;

    public Interface() {
        super("Tienda - Visualizador de Árboles");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        botonConstruir = new JButton("Crear Árbol");
        botonInsertar = new JButton("Agregar Nodo");
        botonEliminar = new JButton("Eliminar Nodo");
        botonBuscar = new JButton("Buscar Nodo");
        botonContar = new JButton("Info Global");
        botonBalancear = new JButton("Balancear Árbol");
        
        botonInorden = new JButton("Inorden");
        botonPreorden = new JButton("Preorden");
        botonPostorden = new JButton("Postorden");
        
        panel = new DrawPanel();

        // Panel de controles superior
        JPanel top = new JPanel(new GridLayout(2, 1));
        
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(botonConstruir);
        row1.add(botonInsertar);
        row1.add(botonEliminar);
        row1.add(botonBuscar);
        row1.add(botonBalancear);
        row1.add(botonContar);
        
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(new JLabel("Recorridos: "));
        row2.add(botonInorden);
        row2.add(botonPreorden);
        row2.add(botonPostorden);

        top.add(row1);
        top.add(row2);

        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        botonConstruir.addActionListener(e -> construirArbol());
        botonInsertar.addActionListener(e -> agregarNodo());
        botonEliminar.addActionListener(e -> eliminarNodo());
        botonBuscar.addActionListener(e -> buscarNodo());
        botonContar.addActionListener(e -> contarNodos());
        botonBalancear.addActionListener(e -> balancearArbol());
        
        botonInorden.addActionListener(e -> iniciarAnimacion("Inorden", arbol.obtenerInorden()));
        botonPreorden.addActionListener(e -> iniciarAnimacion("Preorden", arbol.obtenerPreorden()));
        botonPostorden.addActionListener(e -> iniciarAnimacion("Postorden", arbol.obtenerPostorden()));
    }

    private void construirArbol() {
        // Opciones para el usuario
        Object[] options = {"Aleatorio", "Manual"};
        
        int choice = JOptionPane.showOptionDialog(this,
                "¿Cómo deseas crear el árbol?",
                "Seleccionar Método de Creación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == JOptionPane.CLOSED_OPTION) return;

        arbol = new Arbol(); 

        if (choice == 0) { 
            arbol.generarCodigoAleatorio(7);
        } else { 
            String cantidadStr = JOptionPane.showInputDialog(this, "¿Cuántos nodos deseas ingresar?");
            int cantidadNodos = 0;
            try {
                if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
                    cantidadNodos = Integer.parseInt(cantidadStr.trim());
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida.");
                return;
            }

            if (cantidadNodos <= 0) return;

            for (int i = 0; i < cantidadNodos; i++) {
                String input = JOptionPane.showInputDialog(this, "Ingrese el valor para el nodo " + (i + 1) + " de " + cantidadNodos + ":");
                if (input != null && !input.trim().isEmpty()) {
                    try {
                        int valor = Integer.parseInt(input.trim());
                        arbol.insertar(valor);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Valor inválido. Ingrese un entero.");
                        i--; 
                    }
                } else {
                    if (input == null) break; 
                    i--; 
                }
            }
        }
        limpiarVisualizacion();
    }

    private void agregarNodo() {
        String input = JOptionPane.showInputDialog(this, "Ingrese el valor del nuevo nodo:");
        if (input == null || input.trim().isEmpty()) return;
        try {
            int valor = Integer.parseInt(input.trim());
            arbol.insertar(valor);
            limpiarVisualizacion();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un número entero válido.");
        }
    }

    private void eliminarNodo() {
        String input = JOptionPane.showInputDialog(this, "Ingrese el valor del nodo a eliminar:");
        if (input == null || input.trim().isEmpty()) return;
        try {
            int valor = Integer.parseInt(input.trim());
            if (arbol.buscar(valor)) {
                arbol.eliminar(valor);
                limpiarVisualizacion();
                JOptionPane.showMessageDialog(this, "Nodo " + valor + " eliminado.");
            } else {
                JOptionPane.showMessageDialog(this, "El nodo no existe en el árbol.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Entrada inválida.");
        }
    }

    private void balancearArbol() {
        arbol.balancearArbol();
        limpiarVisualizacion();
        JOptionPane.showMessageDialog(this, "Árbol balanceado correctamente.");
    }

    private void contarNodos() {
        int total = arbol.contarNodos();
        JOptionPane.showMessageDialog(this, "Cantidad total de nodos: " + total);
    }

    private void buscarNodo() {
        String valor = JOptionPane.showInputDialog(this, "¿Qué valor deseas buscar?");
        if (valor == null || valor.trim().isEmpty()) return;
        try {
            int numero = Integer.parseInt(valor.trim());
            boolean encontrado = arbol.buscar(numero);
            if (encontrado)
                JOptionPane.showMessageDialog(this, "El valor " + numero + " EXISTE en el árbol.");
            else
                JOptionPane.showMessageDialog(this, "El valor " + numero + " NO existe.");
        } catch (Exception e) {}
    }

    // Método unificado para iniciar animaciones
    private void iniciarAnimacion(String nombre, List<Integer> listaRecorrido) {
        if (timerAnimacion != null && timerAnimacion.isRunning()) timerAnimacion.stop();
        
        tipoRecorrido = nombre;
        recorrido = listaRecorrido;
        indiceAnimacion = 0; // Empezar desde 0
        
        // Timer para añadir nodos uno por uno
        timerAnimacion = new Timer(300, e -> {
            if (indiceAnimacion < recorrido.size()) {
                indiceAnimacion++;
                panel.repaint();
            } else {
                ((Timer)e.getSource()).stop();
                JOptionPane.showMessageDialog(this, "Recorrido " + nombre + " finalizado.");
            }
        });
        timerAnimacion.start();
        panel.repaint();
    }

    private void limpiarVisualizacion() {
        if (timerAnimacion != null) timerAnimacion.stop();
        tipoRecorrido = "";
        recorrido.clear();
        indiceAnimacion = 0;
        panel.repaint();
    }

    private class DrawPanel extends JPanel {
        // Clase auxiliar para guardar posición y valor de nodos dibujados
        private class NodoArea {
            Rectangle area;
            int valor;
            NodoArea(Rectangle r, int v) { area = r; valor = v; }
        }
        
        private List<NodoArea> ubicacionesNodos = new ArrayList<>();

        public DrawPanel() {
            setBackground(Color.WHITE);
            // Añadir listener para Tooltips
            setToolTipText(""); // Habilitar tooltips
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    manejarTooltip(e.getPoint());
                }
            });
        }

        private void manejarTooltip(Point p) {
            boolean found = false;
            for (NodoArea na : ubicacionesNodos) {
                if (na.area.contains(p)) {
                    Nodo n = arbol.obtenerNodo(na.valor);
                    if (n != null) {
                        int h = arbol.calcularAltura(n);
                        int niv = arbol.calcularNivel(n, n.datos);
                        int g = arbol.calcularGrado(n);
                        
                        // HTML en el tooltip permite multilínea
                        setToolTipText("<html><b>Nodo: " + n.datos + "</b><br>" +
                                       "Altura: " + h + "<br>" +
                                       "Nivel: " + niv + "<br>" +
                                       "Grado: " + g + "</html>");
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
            ubicacionesNodos.clear(); // Limpiar ubicaciones anteriores
            
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
            
            // Dibujar líneas hacia los hijos
            g.setColor(Color.BLACK);
            if (nodo.nodoIzquierdo != null) {
                g.drawLine(x, y, x - separacion, y + 80);
                dibujarArbol(g, nodo.nodoIzquierdo, x - separacion, y + 80, separacion / 2);
            }
            if (nodo.nodoDerecho != null) {
                g.drawLine(x, y, x + separacion, y + 80);
                dibujarArbol(g, nodo.nodoDerecho, x + separacion, y + 80, separacion / 2);
            }
            
            // Dibujar el nodo
            g.setColor(new Color(180, 220, 255));
            g.fillOval(x - 20, y - 20, 40, 40);
            g.setColor(Color.BLACK);
            g.drawOval(x - 20, y - 20, 40, 40);
            g.drawString(String.valueOf(nodo.datos), x - 10, y + 5);
            
            // Guardar ubicación para el tooltip
            ubicacionesNodos.add(new NodoArea(new Rectangle(x - 20, y - 20, 40, 40), nodo.datos));
        }

        private void dibujarRecorridoAnimado(Graphics g) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Recorrido " + tipoRecorrido + ":", 30, getHeight() - 80);
            
            int x = 30;
            int y = getHeight() - 50;
            
            // Dibujar solo hasta donde vaya la animación
            for (int i = 0; i < indiceAnimacion && i < recorrido.size(); i++) {
                int valor = recorrido.get(i);
                g.setColor(new Color(255, 200, 200));
                g.fillOval(x, y - 25, 40, 40);
                g.setColor(Color.BLACK);
                g.drawOval(x, y - 25, 40, 40);
                g.drawString(String.valueOf(valor), x + 5, y);
                x += 60;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Interface f = new Interface();
            f.setVisible(true);
        });
    }
}

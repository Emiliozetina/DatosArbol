//Emilio Zetina 
//ID: 0055615
//Alfredo Vieto
//ID: 00558680
//Ing. TI

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Interface extends JFrame {

    private Arbol arbol = new Arbol();
    private PanelArbol panelDibujo; // Usamos el nuevo componente
    
    // Botones superiores
    private JButton botonConstruir;
    private JButton botonInsertar;
    private JButton botonEliminar;
    private JButton botonBuscar;
    private JButton botonContar;
    private JButton botonBalancear;
    private JButton botonLimpiar;
    
    // Botones de recorrido
    private JButton botonInorden;
    private JButton botonPreorden;
    private JButton botonPostorden;
    
    // Panel lateral
    private JList<Producto> listaProductos; // Usamos Producto
    private DefaultListModel<Producto> modeloLista;
    private JButton botonAgregarSeleccionados;

    // Variables para animación controladas por Interface (Controlador)
    private Timer timerAnimacion;
    private int indiceAnimacion = 0;
    private List<Nodo> recorridoActual = new ArrayList<>();
    private String nombreRecorridoActual = "";

    // Lista predefinida de 20 productos
    private final List<Producto> inventarioBase = new ArrayList<>();

    public Interface() {
        super("Supermercado - Árbol de Productos (Ordenado por Costo)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 800);
        setLocationRelativeTo(null);
        
        cargarInventarioBase();
        initComponents();
    }
    
    private void cargarInventarioBase() {
        inventarioBase.add(new Producto("Chicle", 5.0));
        inventarioBase.add(new Producto("Paleta", 8.0));
        inventarioBase.add(new Producto("Agua", 12.0));
        inventarioBase.add(new Producto("Refresco", 18.0));
        inventarioBase.add(new Producto("Papas", 22.0));
        inventarioBase.add(new Producto("Galletas", 25.0));
        inventarioBase.add(new Producto("Jugo", 28.0));
        inventarioBase.add(new Producto("Leche", 32.0));
        inventarioBase.add(new Producto("Yogurt", 35.0));
        inventarioBase.add(new Producto("Pan", 38.0));
        inventarioBase.add(new Producto("Huevo", 42.0));
        inventarioBase.add(new Producto("Arroz", 45.0));
        inventarioBase.add(new Producto("Frijol", 48.0));
        inventarioBase.add(new Producto("Azucar", 52.0));
        inventarioBase.add(new Producto("Aceite", 65.0));
        inventarioBase.add(new Producto("Cereal", 75.0));
        inventarioBase.add(new Producto("Cafe", 85.0));
        inventarioBase.add(new Producto("Atun", 90.0));
        inventarioBase.add(new Producto("Jamon", 110.0));
        inventarioBase.add(new Producto("Queso", 140.0));
    }

    private void initComponents() {
        // --- Panel Superior (Botones) ---
        botonConstruir = new JButton("Crear Tienda");
        botonInsertar = new JButton("Nuevo Producto Custom");
        botonEliminar = new JButton("Eliminar (Nombre)");
        botonBuscar = new JButton("Buscar (Nombre)");
        botonContar = new JButton("Info Global");
        botonBalancear = new JButton("Balancear Árbol");
        botonLimpiar = new JButton("Limpiar Todo");
        
        botonInorden = new JButton("Inorden");
        botonPreorden = new JButton("Preorden");
        botonPostorden = new JButton("Postorden");
        
        JPanel top = new JPanel(new GridLayout(2, 1));
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.add(botonConstruir);
        row1.add(botonInsertar);
        row1.add(botonEliminar);
        row1.add(botonBuscar);
        row1.add(botonBalancear);
        row1.add(botonContar);
        row1.add(botonLimpiar);
        
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.add(new JLabel("Recorridos: "));
        row2.add(botonInorden);
        row2.add(botonPreorden);
        row2.add(botonPostorden);

        top.add(row1);
        top.add(row2);

        // --- Panel Central (Dibujo) ---
        panelDibujo = new PanelArbol(arbol); // Inicializamos el panel separado

        // --- Panel Derecho (Lista de Productos) ---
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBorder(BorderFactory.createTitledBorder("Inventario Disponible"));
        panelDerecho.setPreferredSize(new Dimension(250, 0));
        
        modeloLista = new DefaultListModel<>();
        for (Producto p : inventarioBase) {
            modeloLista.addElement(p);
        }
        
        listaProductos = new JList<>(modeloLista);
        listaProductos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaProductos.setToolTipText("Mantén presionada la tecla CTRL para seleccionar varios productos");
        
        botonAgregarSeleccionados = new JButton("Agregar Seleccionados al Árbol");
        
        // Etiqueta de ayuda
        JLabel labelAyuda = new JLabel("<html><center><small>Usa <b>Ctrl+Click</b> para<br>seleccionar varios</small></center></html>", SwingConstants.CENTER);
        labelAyuda.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panelDerecho.add(labelAyuda, BorderLayout.NORTH);
        panelDerecho.add(new JScrollPane(listaProductos), BorderLayout.CENTER);
        panelDerecho.add(botonAgregarSeleccionados, BorderLayout.SOUTH);

        // --- Layout Principal ---
        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(panelDibujo, BorderLayout.CENTER);
        add(panelDerecho, BorderLayout.EAST);

        // --- Eventos ---
        botonConstruir.addActionListener(e -> construirArbolDialogo());
        botonAgregarSeleccionados.addActionListener(e -> agregarDesdeSeleccion());
        botonLimpiar.addActionListener(e -> limpiarArbolCompleto());
        
        botonInsertar.addActionListener(e -> agregarNodoCustom());
        botonEliminar.addActionListener(e -> eliminarNodo());
        botonBuscar.addActionListener(e -> buscarNodo());
        botonContar.addActionListener(e -> contarNodos());
        botonBalancear.addActionListener(e -> balancearArbol());
        
        botonInorden.addActionListener(e -> iniciarAnimacion("Inorden", arbol.obtenerInorden()));
        botonPreorden.addActionListener(e -> iniciarAnimacion("Preorden", arbol.obtenerPreorden()));
        botonPostorden.addActionListener(e -> iniciarAnimacion("Postorden", arbol.obtenerPostorden()));
    }
    
    private void limpiarArbolCompleto() {
        if (JOptionPane.showConfirmDialog(this, "¿Estás seguro de borrar toda la tienda?", "Confirmar Limpieza", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            arbol = new Arbol();
            panelDibujo.setArbol(arbol); // Actualizar referencia en el panel
            limpiarVisualizacion();
            JOptionPane.showMessageDialog(this, "Tienda vaciada.");
        }
    }
    
    private void agregarDesdeSeleccion() {
        List<Producto> seleccionados = listaProductos.getSelectedValuesList();
        if (seleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona al menos un producto de la lista derecha.");
            return;
        }
        
        int agregados = 0;
        for (Producto p : seleccionados) {
            if (arbol.insertar(p.nombre, p.costo)) {
                agregados++;
            }
        }
        
        limpiarVisualizacion();
        if (agregados < seleccionados.size()) {
            JOptionPane.showMessageDialog(this, "Se agregaron " + agregados + " productos.\n(Algunos ya existían y se omitieron).");
        }
        listaProductos.clearSelection();
    }

    private void construirArbolDialogo() {
        Object[] options = {"Usar Selección del Panel", "Aleatorio (de la lista)"};
        
        int choice = JOptionPane.showOptionDialog(this,
                "Selecciona método de creación:",
                "Configurar Tienda",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == JOptionPane.CLOSED_OPTION) return;
        
        if (arbol.contarNodos() > 0) {
             if (JOptionPane.showConfirmDialog(this, "¿Borrar productos actuales antes de agregar nuevos?", "Nuevo Inventario", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                arbol = new Arbol();
                panelDibujo.setArbol(arbol);
             }
        }

        if (choice == 0) { // Selección Panel
            agregarDesdeSeleccion();
        } else if (choice == 1) { // Aleatorio
            String input = JOptionPane.showInputDialog(this, "¿Cuántos productos aleatorios (máx 20)?");
            if (input != null) {
                try {
                    int cant = Integer.parseInt(input.trim());
                    arbol.generarProductosAleatorios(cant, inventarioBase);
                    limpiarVisualizacion();
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(this, "Cantidad inválida");
                }
            }
        }
    }

    private void agregarNodoCustom() {
        JTextField nombreField = new JTextField();
        JTextField costoField = new JTextField();
        Object[] message = {
            "Nombre del Producto:", nombreField,
            "Costo:", costoField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Producto Personalizado", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String nombre = nombreField.getText().trim();
                if (nombre.isEmpty()) throw new Exception("Nombre vacío");
                double costo = Double.parseDouble(costoField.getText());
                
                if (arbol.insertar(nombre, costo)) {
                    agregarAlInventarioSiNoExiste(nombre, costo);
                    limpiarVisualizacion();
                } else {
                    JOptionPane.showMessageDialog(this, "El producto '" + nombre + "' ya existe.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Datos inválidos: " + e.getMessage());
            }
        }
    }
    
    private void agregarAlInventarioSiNoExiste(String nombre, double costo) {
        boolean existe = false;
        for (int i = 0; i < modeloLista.size(); i++) {
             if (modeloLista.get(i).nombre.equalsIgnoreCase(nombre)) {
                 existe = true;
                 break;
             }
        }
        
        if (!existe) {
            Producto nuevo = new Producto(nombre, costo);
            inventarioBase.add(nuevo);
            modeloLista.addElement(nuevo);
            listaProductos.ensureIndexIsVisible(modeloLista.getSize() - 1);
        }
    }

    private void eliminarNodo() {
        String input = JOptionPane.showInputDialog(this, "Ingrese el NOMBRE del producto a eliminar:");
        if (input == null || input.trim().isEmpty()) return;
        
        String nombre = input.trim();
        if (arbol.eliminar(nombre)) {
            limpiarVisualizacion();
            JOptionPane.showMessageDialog(this, "Producto '" + nombre + "' eliminado.");
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el producto '" + nombre + "'.");
        }
    }

    private void balancearArbol() {
        arbol.balancearArbol();
        limpiarVisualizacion();
        JOptionPane.showMessageDialog(this, "Inventario balanceado por costo.");
    }

    private void contarNodos() {
        int total = arbol.contarNodos();
        JOptionPane.showMessageDialog(this, "Total de productos en inventario: " + total);
    }

    private void buscarNodo() {
        String nombre = JOptionPane.showInputDialog(this, "¿Qué producto deseas buscar (NOMBRE)?");
        if (nombre == null || nombre.trim().isEmpty()) return;
        
        Nodo n = arbol.obtenerNodo(nombre.trim());
        if (n != null) {
            JOptionPane.showMessageDialog(this, "ENCONTRADO:\nProducto: " + n.nombre + "\nCosto: $" + n.costo);
        } else {
            JOptionPane.showMessageDialog(this, "No existe el producto '" + nombre + "'");
        }
    }

    private void iniciarAnimacion(String nombreRecorrido, List<Nodo> listaRecorrido) {
        if (timerAnimacion != null && timerAnimacion.isRunning()) timerAnimacion.stop();
        
        nombreRecorridoActual = nombreRecorrido;
        recorridoActual = listaRecorrido;
        indiceAnimacion = 0; 
        
        // Estado inicial
        panelDibujo.actualizarAnimacion(nombreRecorridoActual, recorridoActual, indiceAnimacion);
        
        timerAnimacion = new Timer(500, e -> {
            if (indiceAnimacion < recorridoActual.size()) {
                indiceAnimacion++;
                panelDibujo.actualizarAnimacion(nombreRecorridoActual, recorridoActual, indiceAnimacion);
            } else {
                ((Timer)e.getSource()).stop();
                JOptionPane.showMessageDialog(this, "Recorrido " + nombreRecorrido + " finalizado.");
            }
        });
        timerAnimacion.start();
    }

    private void limpiarVisualizacion() {
        if (timerAnimacion != null) timerAnimacion.stop();
        nombreRecorridoActual = "";
        recorridoActual.clear();
        indiceAnimacion = 0;
        panelDibujo.limpiar(); // Delegamos la limpieza visual
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) { }
            Interface f = new Interface();
            f.setVisible(true);
        });
    }
}

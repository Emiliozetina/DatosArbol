import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Interfaz extends JFrame {

    private JTextField tfExpresion;
    private JButton btnConstruir;
    private JTextArea taSalida;
    private DrawPanel panelDibujo;

    // raíz del árbol actual
    private Nodo raiz;

    public Interfaz() {
        super("Visualizador de Árbol Aritmético");
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        tfExpresion = new JTextField("( 3 + 5 ) * ( 2 - 4 )"); // ejemplo con tokens separados por espacios
        btnConstruir = new JButton("Construir Árbol");
        taSalida = new JTextArea(4, 30);
        taSalida.setEditable(false);
        panelDibujo = new DrawPanel();

        JPanel top = new JPanel(new BorderLayout(6, 6));
        top.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        top.add(new JLabel("Expresión:"), BorderLayout.NORTH);
        top.add(tfExpresion, BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.add(btnConstruir);
        top.add(controls, BorderLayout.SOUTH);

        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createTitledBorder("Salida"));
        right.add(new JScrollPane(taSalida), BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(panelDibujo, BorderLayout.CENTER);
        getContentPane().add(right, BorderLayout.EAST);

        // Listeners
        btnConstruir.addActionListener(e -> construirAccion());
    }

    private void construirAccion() {
        String expr = tfExpresion.getText().trim();
        if (expr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una expresión.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            java.util.List<String> postfija = ArbolAritmetico.infijaAPostfija(expr);
            taSalida.setText("Postfija: " + postfija + "\n");
            raiz = ArbolAritmetico.construirArbol(postfija);
            panelDibujo.setRoot(raiz);
            panelDibujo.revalidate();
            panelDibujo.repaint();
            int resultado = ArbolAritmetico.evaluar(raiz);
            taSalida.append("Resultado: " + resultado + "\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al construir: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Panel que dibuja el árbol. Calcula posiciones con un recorrido inOrden para espaciar nodos.
    private static class DrawPanel extends JPanel {
        private Nodo root;
        private Map<Nodo, Point> posicion = new HashMap<>();
        private int nodeRadius = 24;
        private int nivelAltura = 80;
        private int xCounter;

        public DrawPanel() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(600, 400));
        }

        public void setRoot(Nodo r) {
            this.root = r;
            posicion.clear();
            xCounter = 0;
            if (r != null) {
                computePositions(r, 0);
                // ajustar preferencia de tamaño según la cantidad de nodos horizontales
                int width = Math.max(600, (xCounter + 1) * (nodeRadius * 2));
                int height = Math.max(400, (maxDepth(root) + 2) * nivelAltura);
                setPreferredSize(new Dimension(width, height));
            } else {
                setPreferredSize(new Dimension(600, 400));
            }
            revalidate();
            repaint();
        }

        // asigna coordenadas x en orden ascendente y y por profundidad
        private void computePositions(Nodo nodo, int depth) {
            if (nodo == null) return;
            computePositions(nodo.izquierdo, depth + 1);
            int x = xCounter * (nodeRadius * 3) + nodeRadius + 20;
            int y = depth * nivelAltura + 40;
            posicion.put(nodo, new Point(x, y));
            xCounter++;
            computePositions(nodo.derecho, depth + 1);
        }

        private int maxDepth(Nodo nodo) {
            if (nodo == null) return 0;
            return 1 + Math.max(maxDepth(nodo.izquierdo), maxDepth(nodo.derecho));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (root == null) {
                g.setColor(Color.GRAY);
                g.drawString("No hay árbol. Presione 'Construir Árbol'.", 20, 20);
                return;
            }
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // dibujar aristas primero
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.DARK_GRAY);
            for (Map.Entry<Nodo, Point> e : posicion.entrySet()) {
                Nodo n = e.getKey();
                Point p = e.getValue();
                if (n.izquierdo != null) {
                    Point q = posicion.get(n.izquierdo);
                    if (q != null) g2.drawLine(p.x, p.y, q.x, q.y);
                }
                if (n.derecho != null) {
                    Point q = posicion.get(n.derecho);
                    if (q != null) g2.drawLine(p.x, p.y, q.x, q.y);
                }
            }

            // dibujar nodos
            for (Map.Entry<Nodo, Point> e : posicion.entrySet()) {
                Nodo n = e.getKey();
                Point p = e.getValue();
                int x = p.x - nodeRadius;
                int y = p.y - nodeRadius;
                // círculo
                g2.setColor(new Color(135, 206, 250));
                g2.fillOval(x, y, nodeRadius * 2, nodeRadius * 2);
                g2.setColor(Color.BLUE.darker());
                g2.drawOval(x, y, nodeRadius * 2, nodeRadius * 2);
                // texto centrado
                String text = n.valor;
                FontMetrics fm = g2.getFontMetrics();
                int tx = p.x - fm.stringWidth(text) / 2;
                int ty = p.y + fm.getAscent() / 2 - 2;
                g2.setColor(Color.BLACK);
                g2.drawString(text, tx, ty);
            }
        }
    }

    // Lanzador simple
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Interfaz f = new Interfaz();
            f.setVisible(true);
        });
    }
}
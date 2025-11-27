//Emilio Zetina 
//ID: 0055615
//Alfredo Vieto
//ID: 00558680
//Ing. TI

public class Producto {
    String nombre;
    double costo;

    public Producto(String nombre, double costo) {
        this.nombre = nombre;
        this.costo = costo;
    }

    @Override
    public String toString() {
        return nombre + " - $" + costo;
    }
}


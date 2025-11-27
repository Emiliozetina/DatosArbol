//Emilio Zetina 
//ID: 0055615
//Alfredo Vieto
//ID: 00558680
//Ing. TI


class Nodo {
    //Atributos
    double costo; // Precio del producto (clave de ordenamiento)
    String nombre; // Nombre del producto
    Nodo nodoIzquierdo;
    Nodo nodoDerecho;

    //Constructor
    public Nodo(String nombre, double costo) {
        this.nombre = nombre;
        this.costo = costo;
        this.nodoIzquierdo = null;
        this.nodoDerecho = null;
    }

}

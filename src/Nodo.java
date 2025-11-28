//Emilio Zetina 
//ID: 0055615
//Alfredo Vieto
//ID: 00558680
//Ing. TI


class Nodo {

    double costo; 
    String nombre;
    Nodo nodoIzquierdo;
    Nodo nodoDerecho;

    public Nodo(String nombre, double costo) {
        this.nombre = nombre;
        this.costo = costo;
        this.nodoIzquierdo = null;
        this.nodoDerecho = null;
    }

}

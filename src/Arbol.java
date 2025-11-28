//Emilio Zetina 
//ID: 0055615
//Alfredo Vieto
//ID: 00558680
//Ing. TI

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Arbol {
    
    Nodo raiz;

    Arbol(){
        this.raiz = null;
    }

    boolean insertar(String nombre, double costo) {
        // Verificar si ya existe un producto con ese nombre
        if (buscar(nombre)) {
            System.out.println("Error: El producto '" + nombre + "' ya existe.");
            return false;
        }
        raiz = insertarRecursivo(raiz, nombre, costo);
        return true;
    }

    Nodo insertarRecursivo(Nodo nodo, String nombre, double costo) {
        if (nodo == null){
            return new Nodo(nombre, costo);
        }
        else{
            // Ordenar por costo
            if(costo < nodo.costo){
                nodo.nodoIzquierdo = insertarRecursivo(nodo.nodoIzquierdo, nombre, costo);
            }
            else{
                nodo.nodoDerecho = insertarRecursivo(nodo.nodoDerecho, nombre, costo);
            }
        }
        return nodo;
    }

    void eliminar(double costo) {
        raiz = eliminarRecursivo(raiz, costo);
    }
    
    boolean eliminar(String nombre) {
        Nodo nodo = obtenerNodo(nombre);
        if (nodo != null) {
            eliminar(nodo.costo);
            return true;
        }
        return false;
    }

    Nodo eliminarRecursivo(Nodo nodo, double costo) {
        if (nodo == null) return null;

        if (costo < nodo.costo) {
            nodo.nodoIzquierdo = eliminarRecursivo(nodo.nodoIzquierdo, costo);
        } else if (costo > nodo.costo) {
            nodo.nodoDerecho = eliminarRecursivo(nodo.nodoDerecho, costo);
        } else {
            
            //Max un hijo
            if (nodo.nodoIzquierdo == null) return nodo.nodoDerecho;
            if (nodo.nodoDerecho == null) return nodo.nodoIzquierdo;

            //Dos hijos
            Nodo temp = valorMinimo(nodo.nodoDerecho);
            nodo.costo = temp.costo;
            nodo.nombre = temp.nombre;
            // Eliminar el sucesor encontrado
            nodo.nodoDerecho = eliminarRecursivo(nodo.nodoDerecho, temp.costo);
        }
        return nodo;
    }

    Nodo valorMinimo(Nodo nodo) {
        Nodo current = nodo;
        while (current.nodoIzquierdo != null) {
            current = current.nodoIzquierdo;
        }
        return current;
    }

    void balancearArbol() {
        List<Nodo> nodosOrdenados = new ArrayList<>();
        llenarInordenNodos(raiz, nodosOrdenados);
        raiz = construirBalanceado(nodosOrdenados, 0, nodosOrdenados.size() - 1);
    }

    private Nodo construirBalanceado(List<Nodo> lista, int inicio, int fin) {
        if (inicio > fin) return null;
        int medio = (inicio + fin) / 2;
        Nodo temp = lista.get(medio);
        Nodo nodo = new Nodo(temp.nombre, temp.costo);
        nodo.nodoIzquierdo = construirBalanceado(lista, inicio, medio - 1);
        nodo.nodoDerecho = construirBalanceado(lista, medio + 1, fin);
        return nodo;
    }

    Nodo obtenerNodo(double costo) {
        return buscarNodoRecursivo(raiz, costo);
    }
    
    Nodo obtenerNodo(String nombre) {
        return buscarNodoPorNombreRecursivo(raiz, nombre);
    }
    
    private Nodo buscarNodoRecursivo(Nodo nodo, double costo) {
        if (nodo == null || nodo.costo == costo) return nodo;
        if (costo < nodo.costo) return buscarNodoRecursivo(nodo.nodoIzquierdo, costo);
        return buscarNodoRecursivo(nodo.nodoDerecho, costo);
    }
    
    private Nodo buscarNodoPorNombreRecursivo(Nodo nodo, String nombre) {
        if (nodo == null) return null;
        if (nodo.nombre.equalsIgnoreCase(nombre)) return nodo;
        
        Nodo izquierdo = buscarNodoPorNombreRecursivo(nodo.nodoIzquierdo, nombre);
        if (izquierdo != null) return izquierdo;
        
        return buscarNodoPorNombreRecursivo(nodo.nodoDerecho, nombre);
    }

    int calcularAltura(Nodo nodo) {
        if (nodo == null) return 0;
        return 1 + Math.max(calcularAltura(nodo.nodoIzquierdo), calcularAltura(nodo.nodoDerecho));
    }

    int calcularNivel(Nodo nodo, double costo) {
        return obtenerNivelRecursivo(raiz, costo, 1);
    }

    private int obtenerNivelRecursivo(Nodo nodo, double costo, int nivel) {
        if (nodo == null) return 0;
        if (nodo.costo == costo) return nivel;
        int abajo = obtenerNivelRecursivo(nodo.nodoIzquierdo, costo, nivel + 1);
        if (abajo != 0) return abajo;
        return obtenerNivelRecursivo(nodo.nodoDerecho, costo, nivel + 1);
    }
    
    int calcularGrado(Nodo nodo) {
        if (nodo == null) return 0;
        int count = 0;
        if (nodo.nodoIzquierdo != null) count++;
        if (nodo.nodoDerecho != null) count++;
        return count;
    }


    void mostrarArbol(Nodo nodo, int nivel) {
        if (nodo != null) {
            mostrarArbol(nodo.nodoDerecho, nivel + 1);
            for (int i = 0; i < nivel; i++) {
                System.out.print("    ");
            }
            System.out.println(nodo.nombre + " ($" + nodo.costo + ")");
            mostrarArbol(nodo.nodoIzquierdo, nivel + 1);
        }
    }

    void generarProductosAleatorios(int cantidad, List<Producto> disponibles){
        if (disponibles.isEmpty()) return;
        
        List<Producto> copia = new ArrayList<>(disponibles);
        Collections.shuffle(copia);
        
        int insertados = 0;
        for (Producto prod : copia) {
            if (insertados >= cantidad) break;
            if (insertar(prod.nombre, prod.costo)) {
                insertados++;
            }
        }
    }

    boolean buscar(double costo) {
        boolean encontrado = buscarRecursivo(raiz, costo);
        return encontrado;
    }
    
    boolean buscar(String nombre) {
        return obtenerNodo(nombre) != null;
    }

    boolean buscarRecursivo(Nodo nodo, double costo) {
        if (nodo == null) {
            return false;
        } else if (nodo.costo == costo) {
            return true;
        } else if (costo < nodo.costo) {
            return buscarRecursivo(nodo.nodoIzquierdo, costo);
        } else {
            return buscarRecursivo(nodo.nodoDerecho, costo);
        }
    }

    int contarNodos() {
        return contarRecursivo(raiz);
    }

    int contarRecursivo(Nodo nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contarRecursivo(nodo.nodoIzquierdo) + contarRecursivo(nodo.nodoDerecho);
    }

    List<Nodo> obtenerPreorden() {
        List<Nodo> lista = new ArrayList<>();
        llenarPreorden(raiz, lista);
        return lista;
    }
    private void llenarPreorden(Nodo nodo, List<Nodo> lista) {
        if (nodo != null) {
            lista.add(nodo);
            llenarPreorden(nodo.nodoIzquierdo, lista);
            llenarPreorden(nodo.nodoDerecho, lista);
        }
    }
    
    List<Nodo> obtenerInorden() {
        List<Nodo> lista = new ArrayList<>();
        llenarInordenNodos(raiz, lista);
        return lista;
    }
    private void llenarInordenNodos(Nodo nodo, List<Nodo> lista) {
        if (nodo != null) {
            llenarInordenNodos(nodo.nodoIzquierdo, lista);
            lista.add(nodo);
            llenarInordenNodos(nodo.nodoDerecho, lista);
        }
    }

    List<Nodo> obtenerPostorden() {
        List<Nodo> lista = new ArrayList<>();
        llenarPostorden(raiz, lista);
        return lista;
    }
    private void llenarPostorden(Nodo nodo, List<Nodo> lista) {
        if (nodo != null) {
            llenarPostorden(nodo.nodoIzquierdo, lista);
            llenarPostorden(nodo.nodoDerecho, lista);
            lista.add(nodo);
        }
    }

    void imprimir() {
        mostrarArbol(raiz, 0);
    }

    public static void main(String[] args) {
        Arbol obj =  new Arbol();
        obj.insertar("Test", 10);
        obj.imprimir();
    }
}

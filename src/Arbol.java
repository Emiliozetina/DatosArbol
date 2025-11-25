//Alfredo Vieto
//ID: 00558680
//Ing. TI

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arbol {
    
    Nodo raiz;

    Arbol(){
        this.raiz = null;
    }

    void insertar(int datos) {
        raiz = insertarRecursivo(raiz, datos);
    }

    Nodo insertarRecursivo(Nodo nodo, int datos) {
        if (nodo == null){
            return new Nodo(datos);
        }
        else{
            if(datos < nodo.datos){
                nodo.nodoIzquierdo = insertarRecursivo(nodo.nodoIzquierdo, datos);
            }
            else{
                nodo.nodoDerecho = insertarRecursivo(nodo.nodoDerecho, datos);
            }
        }
        return nodo;
    }

    // --- NUEVO: Eliminar Nodo ---
    void eliminar(int valor) {
        raiz = eliminarRecursivo(raiz, valor);
    }

    Nodo eliminarRecursivo(Nodo nodo, int valor) {
        if (nodo == null) return null;

        if (valor < nodo.datos) {
            nodo.nodoIzquierdo = eliminarRecursivo(nodo.nodoIzquierdo, valor);
        } else if (valor > nodo.datos) {
            nodo.nodoDerecho = eliminarRecursivo(nodo.nodoDerecho, valor);
        } else {
            // Encontrado: nodo a eliminar
            
            // Caso 1: Sin hijos o solo uno
            if (nodo.nodoIzquierdo == null) return nodo.nodoDerecho;
            if (nodo.nodoDerecho == null) return nodo.nodoIzquierdo;

            // Caso 2: Dos hijos
            // Buscar sucesor (menor del subárbol derecho)
            nodo.datos = valorMinimo(nodo.nodoDerecho);
            // Eliminar el sucesor encontrado
            nodo.nodoDerecho = eliminarRecursivo(nodo.nodoDerecho, nodo.datos);
        }
        return nodo;
    }

    int valorMinimo(Nodo nodo) {
        int minv = nodo.datos;
        while (nodo.nodoIzquierdo != null) {
            minv = nodo.nodoIzquierdo.datos;
            nodo = nodo.nodoIzquierdo;
        }
        return minv;
    }

    // --- NUEVO: Balancear Árbol ---
    void balancearArbol() {
        List<Integer> nodosOrdenados = obtenerInorden();
        raiz = construirBalanceado(nodosOrdenados, 0, nodosOrdenados.size() - 1);
    }

    private Nodo construirBalanceado(List<Integer> lista, int inicio, int fin) {
        if (inicio > fin) return null;
        int medio = (inicio + fin) / 2;
        Nodo nodo = new Nodo(lista.get(medio));
        nodo.nodoIzquierdo = construirBalanceado(lista, inicio, medio - 1);
        nodo.nodoDerecho = construirBalanceado(lista, medio + 1, fin);
        return nodo;
    }

    // --- NUEVO: Info de Nodos ---
    Nodo obtenerNodo(int valor) {
        return buscarNodoRecursivo(raiz, valor);
    }
    
    private Nodo buscarNodoRecursivo(Nodo nodo, int valor) {
        if (nodo == null || nodo.datos == valor) return nodo;
        if (valor < nodo.datos) return buscarNodoRecursivo(nodo.nodoIzquierdo, valor);
        return buscarNodoRecursivo(nodo.nodoDerecho, valor);
    }

    int calcularAltura(Nodo nodo) {
        if (nodo == null) return 0;
        return 1 + Math.max(calcularAltura(nodo.nodoIzquierdo), calcularAltura(nodo.nodoDerecho));
    }

    int calcularNivel(Nodo nodo, int valor) {
        return obtenerNivelRecursivo(raiz, valor, 1);
    }

    private int obtenerNivelRecursivo(Nodo nodo, int valor, int nivel) {
        if (nodo == null) return 0;
        if (nodo.datos == valor) return nivel;
        int abajo = obtenerNivelRecursivo(nodo.nodoIzquierdo, valor, nivel + 1);
        if (abajo != 0) return abajo;
        return obtenerNivelRecursivo(nodo.nodoDerecho, valor, nivel + 1);
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
            System.out.println(nodo.datos);
            mostrarArbol(nodo.nodoIzquierdo, nivel + 1);
        }
    }

    //Genera codigos aleatorios y los inserta al arbol
    void generarCodigoAleatorio(int cantidad){
        Random random = new Random();
        int generados = 0;
        while(generados < cantidad){
            int codigo = 1000 + random.nextInt(9000);
            insertar(codigo);
            generados++;
        }
    }

    //Buscar un valor en el arbol
    boolean buscar(int valor) {
        boolean encontrado = buscarRecursivo(raiz, valor);
        if (encontrado) {
            System.out.println("El codigo " + valor + " fue encontrado");
        } else {
            System.out.println("No existe el codigo " + valor);
        }
        return encontrado;
    }

    boolean buscarRecursivo(Nodo nodo, int valor) {
        if (nodo == null) {
            return false;
        } else if (nodo.datos == valor) {
            return true;
        } else if (valor < nodo.datos) {
            return buscarRecursivo(nodo.nodoIzquierdo, valor);
        } else {
            return buscarRecursivo(nodo.nodoDerecho, valor);
        }
    }

    //Contar los nodos del arbol
    int contarNodos() {
        return contarRecursivo(raiz);
    }

    int contarRecursivo(Nodo nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contarRecursivo(nodo.nodoIzquierdo) + contarRecursivo(nodo.nodoDerecho);
    }

    // --- LÓGICA DE RECORRIDOS OPTIMIZADA ---
    // Ahora los métodos imprimir reutilizan los métodos de obtener lista.
    // Esto evita tener código duplicado para la misma lógica de recorrido.

    // Preorden
    List<Integer> obtenerPreorden() {
        List<Integer> lista = new ArrayList<>();
        llenarPreorden(raiz, lista);
        return lista;
    }
    private void llenarPreorden(Nodo nodo, List<Integer> lista) {
        if (nodo != null) {
            lista.add(nodo.datos);
            llenarPreorden(nodo.nodoIzquierdo, lista);
            llenarPreorden(nodo.nodoDerecho, lista);
        }
    }
    void imprimirPreorden(){
        List<Integer> lista = obtenerPreorden();
        for(int dato : lista) System.out.print(dato + ", ");
        System.out.println();
    }

    // Inorden
    List<Integer> obtenerInorden() {
        List<Integer> lista = new ArrayList<>();
        llenarInorden(raiz, lista);
        return lista;
    }
    private void llenarInorden(Nodo nodo, List<Integer> lista) {
        if (nodo != null) {
            llenarInorden(nodo.nodoIzquierdo, lista);
            lista.add(nodo.datos);
            llenarInorden(nodo.nodoDerecho, lista);
        }
    }
    void imprimirInorden(){
        List<Integer> lista = obtenerInorden();
        for(int dato : lista) System.out.print(dato + ", ");
        System.out.println();
    }

    // Postorden
    List<Integer> obtenerPostorden() {
        List<Integer> lista = new ArrayList<>();
        llenarPostorden(raiz, lista);
        return lista;
    }
    private void llenarPostorden(Nodo nodo, List<Integer> lista) {
        if (nodo != null) {
            llenarPostorden(nodo.nodoIzquierdo, lista);
            llenarPostorden(nodo.nodoDerecho, lista);
            lista.add(nodo.datos);
        }
    }
    void imprimirPostorden(){
        List<Integer> lista = obtenerPostorden();
        for(int dato : lista) System.out.print(dato + ", ");
        System.out.println();
    }

    //Impresion grafica del arbol en consola
    void imprimir(){
        mostrarArbol(raiz, 0);
    }

    public static void main(String[] args) {
        Arbol obj =  new Arbol();
        
        System.out.println("\nArbol con códigos aleatorios:");
        obj.generarCodigoAleatorio(4);
        obj.imprimir();

        System.out.println("\nRecorrido en Preorden:");
        obj.imprimirPreorden();

        System.out.println("Recorrido en Inorden:");
        obj.imprimirInorden();

        System.out.println("Recorrido en Postorden:");
        obj.imprimirPostorden();

        System.out.println("Cantidad total de nodos: " + obj.contarNodos());
    }
}

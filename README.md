Proyecto final Ordinario Algoritmos
-------------------------------------

Emilio Zetina Sánchez (ID: 00556615)  
Alfredo Vieto Solis (ID: 00558680)  
Ingeniería en TI — Anáhuac Mayab  
Estructura de Datos y Algoritmos

Descripción del proyecto
-----------------------
Este proyecto busca simular el almacen de una tienda mediante arboles binarios. Incluye una interfaz gráfica (desarrollada en Apache NetBeans) que permite introducir expresiones en notación infija/prefija/postfija, convertir entre notaciones, evaluar resultados y mostrar el árbol de expresión con sus recorridos.

Requisitos
----------
- Java JDK 8 en adelante

Funciones principales
---------------------
- Recorrido de expresiones con soporte de paréntesis y precedencia de operadores.  
- Conversiones: infijo <-> prefijo <-> postfijo.  
- Evaluación de expresiones en cualquier notación.  
- Visualización gráfica del árbol de expresión y despliegue de recorridos (Preorden, Inorden, Postorden).

Definiciones de notación
------------------------
- Infija: Notación habitual en matemáticas; el operador va entre operandos. Ejemplo: 1 + 2  
- Prefija: El operador va antes de sus operandos. Ejemplo: + 1 2  
- Postfija: El operador va después de sus operandos. Ejemplo: 1 2 +

¿Por qué se usan estas notaciones?
----------------------------------
- Las notaciones prefija y postfija eliminan la ambigüedad de paréntesis si se interpretan con la estructura de árbol correcta, simplificando la evaluación mediante pilas o recorridos del árbol.  
- En notación infija se requieren reglas de precedencia y/o paréntesis para preservar el orden deseado de operaciones.  
- Para evaluación automática y conversión, las notaciones prefija/postfija permiten algoritmos eficientes (por ejemplo, Shunting-yard para convertir infija a postfija y evaluación por pila para postfija).

Relación con recorridos de árboles (Preorden, Inorden, PostOrden)
-----------------------------------------------------------------
- Preorden (Root, Left, Right)  → Representa la notación prefija cuando se recorre el árbol de expresión.  
- Inorden (Left, Root, Right)  → Representa la notación infija (mantiene la estructura con paréntesis).  
- Postorden (Left, Right, Root) → Representa la notación postfija; su evaluación con pila produce el resultado sin necesidad de paréntesis.  
- Convertir una expresión a un árbol y aplicar estos recorridos genera las notaciones equivalentes y muestra la correspondencia directa entre expresiones y recorridos.

Ejemplos rápidos del funcionamiento ideal del proyecto
--------------------------------------------------------
- Infija: (3 + 4) * 5  
- Prefija: * + 3 4 5  
- Postfija: 3 4 + 5 *




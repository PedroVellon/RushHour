# 🚗 Rush Hour

[![Java](https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=java&logoColor=white)]()
[![Search Algorithms](https://img.shields.io/badge/Algorithms-A%2A%20%7C%20BFS%20%7C%20DFS-blue?style=flat-square)]()
[![Optimization](https://img.shields.io/badge/Focus-High%20Performance-success?style=flat-square)]()

*Read this in other languages: [English](#english) | [Español](#español)*

<a id="english"></a>
An advanced, highly-optimized algorithmic solver for the classic **Rush Hour** puzzle game. Modeled as a state-space search problem, this engine explores thousands of potential board configurations to find the optimal sequence of moves to free the red car ('A') from a traffic jam.

🏆 **Achievement:** This implementation achieved **3rd place in the global efficiency and execution time ranking** of my university cohort, only surpassed by two legacy implementations written in low-level languages by the teaching staff.

## 🧠 The Problem
Rush Hour is played on a 6x6 grid. Vehicles (cars of length 2 and trucks of length 3) are placed horizontally or vertically. They can only move forward and backward along their axis. The goal is to clear a path so the red car ('A'), starting in a central row, can exit through the right edge of the board.

The board is serialized as a 36-character string where 'A'-'Z' represent vehicles and 'o' represents empty spaces. 
Example: `IBBoooIoooDDJAAoooJoKEEMooKooMGGHHHM`

## ⚙️ Features & Commands
The program runs via CLI and supports multiple operations to verify, query, and solve boards.

* **Verify:** Validates the structural integrity of a level string.
  `java RushHour verify -s <level>`
* **Successors:** Generates all valid next possible states from a given board.
  `java RushHour successors -s <level>`
* **Solver:** The core search engine. Solves the puzzle using different algorithms.
  `java RushHour solver -s <level> --strategy <STRATEGY> [--heuristic <0|1|2>] [--stats]`

### 🔍 Board Queries (`question` command)
You can query specific details about any given board state or simulate interactions using the following flags:
* `--whereis <vehicle>`: Returns the exact grid coordinates (row,col) occupied by a specific vehicle. Spaces are omitted in the output.
* `--what <row,col>`: Identifies what is currently occupying the specified cell.
* `--size <vehicle>`: Returns the size (length) of the specified vehicle.
* `--howmany`: Returns the total count of vehicles currently on the board.
* `--goal`: Evaluates the board and returns `TRUE` if the red car has reached the exit, or `FALSE` otherwise.
* `--move <move_list>`: Simulates a comma-separated list of consecutive moves (e.g., `A+1,B-1`) and returns the resulting board state string.

### Supported Search Strategies
* **Uninformed Search:** `BFS` (Breadth-First), `DFS` (Depth-First), `UCS` (Uniform Cost Search).
* **Informed (Heuristic) Search:** `GBF` (Greedy Best-First), `AStar` (A* Search).

**Custom Heuristics implemented:**
* `H0`: Distance (number of empty cells) between the red car and the exit.
* `H1`: Number of blocking vehicles in front of the red car.
* `H2`: Combined approach (H0 + H1) for optimal A* performance.

## 🚀 Performance & Optimizations
To achieve top-tier execution times, several micro-optimizations were implemented, drifting from standard object-oriented conveniences to favor raw computational speed:

1. **1D Array Board Representation:** Instead of using a traditional 2D array (`char[][]`), the 6x6 grid is flattened into a single 1D array (`char[36]`). This vastly improves CPU Cache locality and eliminates the overhead of multiple array object instantiations during the thousands of state cloning operations required during node expansion.
2. **Loop Optimization:** `while` loops were heavily favored over `for` loops in critical path methods (like successor generation and board iteration) to minimize loop control variable overhead.
3. **Efficient Data Structures:** * `PriorityQueue` for the search frontier to guarantee O(log n) insertions while maintaining ordered nodes by cost/heuristic.
   * `HashMap` for the closed list (visited states) mapping serialized strings to values, allowing O(1) expected time complexity to prune redundant paths.
   * `HashSet` used during the initial O(n) verification phase to quickly detect duplicate identifiers.

### Examples of usage

> java RushHour question -s BBDDCoEoooCKEoAACKGoooooGoooooGoHHHo --whereis C
  (0,4)(1,4)(2,4)

> question -s IBBBoDIooJoDoAAJoooooJoMKKoooMooHHHo --what 3,5
  M

> java RushHour question -s IoBBCCIDooooIDoAAoooJooMKKJooMoooHHH --size M
  2

> java RushHour question -s IBBoooIoooDDJAAoooJoKEEMooKooMGGHHHM --howmany
  10

> java RushHour question -s IBBoooIoooDDJoAAooJoKEEMooKooMGGHHHM --goal
  FALSE

> java RushHour question -s IBBoooIoooDDJoAAooJoKEEMooKooMGGHHHM --move A+1,A+1
  IBBoooIoooDDJoooAAJoKEEMooKooMGGHHHM

> java RushHour solver -s HBBCCCHDDKoMAAJKoMEEJFFMoIooLooIGGLo --strategy AStar --heuristic 2  --stats

  [0,none,___,HBBCCCHDDKoMAAJKoMEEJFFMoIooLooIGGLo,0,0,7,7]
  [1,0,J-1,HBBCCCHDDKoMAAoKoMEEJFFMoIJoLooIGGLo,5,1,6,11]
  [7,1,A+1,HBBCCCHDDKoMoAAKoMEEJFFMoIJoLooIGGLo,10,2,5,15]
  [19,7,H-1,oBBCCCHDDKoMHAAKoMEEJFFMoIJoLooIGGLo,15,3,5,20]
  [30,19,B-1,BBoCCCHDDKoMHAAKoMEEJFFMoIJoLooIGGLo,20,4,5,25]
  [43,30,C-1,BBCCCoHDDKoMHAAKoMEEJFFMoIJoLooIGGLo,25,5,5,30]
  [55,43,M+1,BBCCCMHDDKoMHAAKoMEEJFFooIJoLooIGGLo,30,6,5,35]
  [66,55,F+1,BBCCCMHDDKoMHAAKoMEEJoFFoIJoLooIGGLo,35,7,5,40]
  [76,66,K-2,BBCCCMHDDooMHAAooMEEJKFFoIJKLooIGGLo,39,8,4,43]
  [78,76,A+2,BBCCCMHDDooMHooAAMEEJKFFoIJKLooIGGLo,43,9,2,45]
  [90,78,D+2,BBCCCMHooDDMHooAAMEEJKFFoIJKLooIGGLo,47,10,2,49]
  [114,90,J+2,BBCCCMHoJDDMHoJAAMEEoKFFoIoKLooIGGLo,51,11,2,53]
  [134,114,E+1,BBCCCMHoJDDMHoJAAMoEEKFFoIoKLooIGGLo,56,12,2,58]
  [147,134,H-3,BBCCCMooJDDMooJAAMoEEKFFHIoKLoHIGGLo,59,13,2,61]
  [148,147,E-1,BBCCCMooJDDMooJAAMEEoKFFHIoKLoHIGGLo,64,14,2,66]
  [160,148,J-2,BBCCCMoooDDMoooAAMEEJKFFHIJKLoHIGGLo,68,15,2,70]
  [166,160,D-3,BBCCCMDDoooMoooAAMEEJKFFHIJKLoHIGGLo,71,16,2,73]
  [176,166,A-3,BBCCCMDDoooMAAoooMEEJKFFHIJKLoHIGGLo,74,17,5,79]
  [255,176,J+2,BBCCCMDDJooMAAJooMEEoKFFHIoKLoHIGGLo,78,18,6,84]
  [336,255,K+2,BBCCCMDDJKoMAAJKoMEEooFFHIooLoHIGGLo,82,19,7,89]
  [441,336,F-2,BBCCCMDDJKoMAAJKoMEEFFooHIooLoHIGGLo,86,20,7,93]
  [547,441,L+3,BBCCCMDDJKLMAAJKLMEEFFooHIooooHIGGoo,89,21,8,97]
  [687,547,F+2,BBCCCMDDJKLMAAJKLMEEooFFHIooooHIGGoo,93,22,8,101]
  [926,687,G+2,BBCCCMDDJKLMAAJKLMEEooFFHIooooHIooGG,97,23,8,105]
  [1323,926,J-3,BBCCCMDDoKLMAAoKLMEEooFFHIJoooHIJoGG,100,24,7,107]
  [1658,1323,K-3,BBCCCMDDooLMAAooLMEEooFFHIJKooHIJKGG,103,25,6,109]
  [2011,1658,A+2,BBCCCMDDooLMooAALMEEooFFHIJKooHIJKGG,107,26,4,111]
  [2525,2011,D+2,BBCCCMooDDLMooAALMEEooFFHIJKooHIJKGG,111,27,4,115]
  [3958,2525,E+2,BBCCCMooDDLMooAALMooEEFFHIJKooHIJKGG,115,28,4,119]
  [6186,3958,H+3,BBCCCMHoDDLMHoAALMooEEFFoIJKoooIJKGG,118,29,4,122]
  [8268,6186,I+3,BBCCCMHIDDLMHIAALMooEEFFooJKooooJKGG,121,30,4,125]
  [10475,8268,E-2,BBCCCMHIDDLMHIAALMEEooFFooJKooooJKGG,125,31,4,129]
  [13679,10475,J+1,BBCCCMHIDDLMHIAALMEEJoFFooJKoooooKGG,130,32,4,134]
  [16824,13679,K+1,BBCCCMHIDDLMHIAALMEEJKFFooJKooooooGG,135,33,4,139]
  [19078,16824,G-4,BBCCCMHIDDLMHIAALMEEJKFFooJKooGGoooo,137,34,4,141]
  [19959,19078,J-1,BBCCCMHIDDLMHIAALMEEoKFFooJKooGGJooo,142,35,4,146]
  [21383,19959,K-1,BBCCCMHIDDLMHIAALMEEooFFooJKooGGJKoo,147,36,4,151]
  [22465,21383,F-2,BBCCCMHIDDLMHIAALMEEFFooooJKooGGJKoo,151,37,4,155]
  [23199,22465,L-3,BBCCCMHIDDoMHIAAoMEEFFooooJKLoGGJKLo,154,38,3,157]
  [23466,23199,M-3,BBCCCoHIDDooHIAAooEEFFoMooJKLMGGJKLM,157,39,2,159]
  [23735,23466,A+2,BBCCCoHIDDooHIooAAEEFFoMooJKLMGGJKLM,161,40,0,161]
  
  ET: XXXXX
  TN: 23977
  EN: 3062
  CN: 20329
  DF: 40
  
---

<a id="español"></a>
# 🇪🇸 🚗 Rush Hour (Español)

Un motor de resolución algorítmica avanzado y altamente optimizado para el clásico rompecabezas **Rush Hour**. Modelado mediante búsqueda en espacio de estados, este motor explora miles de configuraciones de tablero para encontrar la secuencia óptima de movimientos para liberar el coche rojo ('A') del atasco.

🏆 **Logro destacado:** Esta implementación alcanzó el **3.er puesto en el ranking global de eficiencia y tiempo de ejecución** de mi promoción universitaria, superado únicamente por dos implementaciones docentes desarrolladas en lenguajes de bajo nivel (COBOL).

## 🧠 El Problema
Rush Hour se juega en una cuadrícula de 6x6. Los vehículos (coches de longitud 2 y camiones de longitud 3) están colocados horizontal o verticalmente y solo pueden moverse hacia adelante o hacia atrás en su eje. El objetivo es despejar el camino para que el coche rojo ('A'), que siempre empieza en una fila central, pueda salir por el borde derecho.

El tablero se serializa como una cadena de 36 caracteres donde 'A'-'Z' representan vehículos y 'o' espacios vacíos.
Ejemplo: `IBBoooIoooDDJAAoooJoKEEMooKooMGGHHHM`

## ⚙️ Características y Comandos
El programa opera mediante línea de comandos (CLI) y permite verificar, analizar y resolver tableros.

* **Verify:** Valida la integridad estructural del tablero (tamaño, caracteres válidos, duplicados, posición del coche A, etc.). 
  `java RushHour verify -s <nivel>`
* **Successors:** Genera la lista ordenada de los siguientes estados posibles desde un tablero dado. 
  `java RushHour successors -s <nivel>`
* **Solver:** Motor de búsqueda principal. Resuelve el nivel utilizando diferentes estrategias algorítmicas. 
  `java RushHour solver -s <nivel> --strategy <ESTRATEGIA> [--heuristic <0|1|2>] [--stats]`

### 🔍 Consultas del tablero (comando `question`)
Permite extraer información específica de un estado del tablero o simular acciones:
* `--whereis <vehículo>`: Devuelve la lista de casillas (fila,columna) ocupadas por el vehículo sin espacios en blanco.
* `--what <f,c>`: Muestra qué hay en la celda indicada (fila, columna).
* `--size <vehículo>`: Devuelve el tamaño (longitud) del vehículo indicado.
* `--howmany`: Devuelve la cantidad total de vehículos presentes en el tablero.
* `--goal`: Evalúa el tablero y devuelve `TRUE` si el coche rojo ha alcanzado la casilla de salida (2,5), o `FALSE` en caso contrario.
* `--move <lista_movimientos>`: Simula una lista de movimientos consecutivos separados por comas (ej. `A+1,B-1`) y devuelve la cadena del estado resultante.

*Ejemplos de uso:* > java RushHour question -s IBBoooIoooDDJoAAooJoKEEMooKooMGGHHHM --goal
    FALSE

    > java RushHour question -s IBBoooIoooDDJoAAooJoKEEMooKooMGGHHHM --move A+1,A+1
    IBBoooIoooDDJoooAAJoKEEMooKooMGGHHHM

### Estrategias de Búsqueda Soportadas
* **Búsqueda no informada:** `BFS` (Anchura), `DFS` (Profundidad), `UCS` (Costo Uniforme).
* **Búsqueda informada (Heurística):** `GBF` (Voraz), `AStar` (A*).

**Heurísticas personalizadas implementadas:**
* `H0`: Número de casillas a la derecha del coche rojo 'A' (distancia a la salida).
* `H1`: Número de coches que bloquean el camino y que hay que mover para despejar la fila de salida.
* `H2`: Aproximación combinada (H0 + H1) para optimizar el rendimiento del algoritmo A*.

## 🚀 Rendimiento y Optimizaciones
Para alcanzar tiempos de ejecución de primer nivel, se sacrificaron ciertas comodidades de la programación orientada a objetos en favor de la velocidad de cálculo pura a bajo nivel:

1. **Representación en Array Unidimensional:** En lugar de utilizar una matriz bidimensional tradicional (`char[][]`), la cuadrícula de 6x6 se aplanó en un único array unidimensional (`char[36]`). Esto mejora drásticamente la localidad de caché de la CPU y elimina la sobrecarga computacional de instanciar múltiples arrays durante las miles de operaciones de clonación de estados que requiere la expansión de nodos.
2. **Optimización de Bucles:** Se priorizó el uso de bucles `while` frente a bucles `for` en los métodos de la ruta crítica (como la generación de sucesores) para reducir al mínimo la sobrecarga de evaluación de variables de control.
3. **Estructuras de Datos de Alto Rendimiento:**
   * Uso de `PriorityQueue` (inserción ordenada) en la frontera de búsqueda para mantener los nodos ordenados por coste y heurística con una eficiencia de O(log n).
   * Uso de `HashMap` para la lista de nodos cerrados (visitados), mapeando las cadenas serializadas a sus valores, permitiendo podar caminos redundantes en un tiempo esperado de O(1).
   * Uso de `HashSet` en la fase de verificación inicial para detectar vehículos duplicados en tiempo O(n).


### Ejemplos de uso

> java RushHour question -s BBDDCoEoooCKEoAACKGoooooGoooooGoHHHo --whereis C
  (0,4)(1,4)(2,4)

> question -s IBBBoDIooJoDoAAJoooooJoMKKoooMooHHHo --what 3,5
  M

> java RushHour question -s IoBBCCIDooooIDoAAoooJooMKKJooMoooHHH --size M
  2

> java RushHour question -s IBBoooIoooDDJAAoooJoKEEMooKooMGGHHHM --howmany
  10

> java RushHour question -s IBBoooIoooDDJoAAooJoKEEMooKooMGGHHHM --goal
  FALSE

> java RushHour question -s IBBoooIoooDDJoAAooJoKEEMooKooMGGHHHM --move A+1,A+1
  IBBoooIoooDDJoooAAJoKEEMooKooMGGHHHM

> java RushHour solver -s HBBCCCHDDKoMAAJKoMEEJFFMoIooLooIGGLo --strategy AStar --heuristic 2  --stats

  [0,none,___,HBBCCCHDDKoMAAJKoMEEJFFMoIooLooIGGLo,0,0,7,7]
  [1,0,J-1,HBBCCCHDDKoMAAoKoMEEJFFMoIJoLooIGGLo,5,1,6,11]
  [7,1,A+1,HBBCCCHDDKoMoAAKoMEEJFFMoIJoLooIGGLo,10,2,5,15]
  [19,7,H-1,oBBCCCHDDKoMHAAKoMEEJFFMoIJoLooIGGLo,15,3,5,20]
  [30,19,B-1,BBoCCCHDDKoMHAAKoMEEJFFMoIJoLooIGGLo,20,4,5,25]
  [43,30,C-1,BBCCCoHDDKoMHAAKoMEEJFFMoIJoLooIGGLo,25,5,5,30]
  [55,43,M+1,BBCCCMHDDKoMHAAKoMEEJFFooIJoLooIGGLo,30,6,5,35]
  [66,55,F+1,BBCCCMHDDKoMHAAKoMEEJoFFoIJoLooIGGLo,35,7,5,40]
  [76,66,K-2,BBCCCMHDDooMHAAooMEEJKFFoIJKLooIGGLo,39,8,4,43]
  [78,76,A+2,BBCCCMHDDooMHooAAMEEJKFFoIJKLooIGGLo,43,9,2,45]
  [90,78,D+2,BBCCCMHooDDMHooAAMEEJKFFoIJKLooIGGLo,47,10,2,49]
  [114,90,J+2,BBCCCMHoJDDMHoJAAMEEoKFFoIoKLooIGGLo,51,11,2,53]
  [134,114,E+1,BBCCCMHoJDDMHoJAAMoEEKFFoIoKLooIGGLo,56,12,2,58]
  [147,134,H-3,BBCCCMooJDDMooJAAMoEEKFFHIoKLoHIGGLo,59,13,2,61]
  [148,147,E-1,BBCCCMooJDDMooJAAMEEoKFFHIoKLoHIGGLo,64,14,2,66]
  [160,148,J-2,BBCCCMoooDDMoooAAMEEJKFFHIJKLoHIGGLo,68,15,2,70]
  [166,160,D-3,BBCCCMDDoooMoooAAMEEJKFFHIJKLoHIGGLo,71,16,2,73]
  [176,166,A-3,BBCCCMDDoooMAAoooMEEJKFFHIJKLoHIGGLo,74,17,5,79]
  [255,176,J+2,BBCCCMDDJooMAAJooMEEoKFFHIoKLoHIGGLo,78,18,6,84]
  [336,255,K+2,BBCCCMDDJKoMAAJKoMEEooFFHIooLoHIGGLo,82,19,7,89]
  [441,336,F-2,BBCCCMDDJKoMAAJKoMEEFFooHIooLoHIGGLo,86,20,7,93]
  [547,441,L+3,BBCCCMDDJKLMAAJKLMEEFFooHIooooHIGGoo,89,21,8,97]
  [687,547,F+2,BBCCCMDDJKLMAAJKLMEEooFFHIooooHIGGoo,93,22,8,101]
  [926,687,G+2,BBCCCMDDJKLMAAJKLMEEooFFHIooooHIooGG,97,23,8,105]
  [1323,926,J-3,BBCCCMDDoKLMAAoKLMEEooFFHIJoooHIJoGG,100,24,7,107]
  [1658,1323,K-3,BBCCCMDDooLMAAooLMEEooFFHIJKooHIJKGG,103,25,6,109]
  [2011,1658,A+2,BBCCCMDDooLMooAALMEEooFFHIJKooHIJKGG,107,26,4,111]
  [2525,2011,D+2,BBCCCMooDDLMooAALMEEooFFHIJKooHIJKGG,111,27,4,115]
  [3958,2525,E+2,BBCCCMooDDLMooAALMooEEFFHIJKooHIJKGG,115,28,4,119]
  [6186,3958,H+3,BBCCCMHoDDLMHoAALMooEEFFoIJKoooIJKGG,118,29,4,122]
  [8268,6186,I+3,BBCCCMHIDDLMHIAALMooEEFFooJKooooJKGG,121,30,4,125]
  [10475,8268,E-2,BBCCCMHIDDLMHIAALMEEooFFooJKooooJKGG,125,31,4,129]
  [13679,10475,J+1,BBCCCMHIDDLMHIAALMEEJoFFooJKoooooKGG,130,32,4,134]
  [16824,13679,K+1,BBCCCMHIDDLMHIAALMEEJKFFooJKooooooGG,135,33,4,139]
  [19078,16824,G-4,BBCCCMHIDDLMHIAALMEEJKFFooJKooGGoooo,137,34,4,141]
  [19959,19078,J-1,BBCCCMHIDDLMHIAALMEEoKFFooJKooGGJooo,142,35,4,146]
  [21383,19959,K-1,BBCCCMHIDDLMHIAALMEEooFFooJKooGGJKoo,147,36,4,151]
  [22465,21383,F-2,BBCCCMHIDDLMHIAALMEEFFooooJKooGGJKoo,151,37,4,155]
  [23199,22465,L-3,BBCCCMHIDDoMHIAAoMEEFFooooJKLoGGJKLo,154,38,3,157]
  [23466,23199,M-3,BBCCCoHIDDooHIAAooEEFFoMooJKLMGGJKLM,157,39,2,159]
  [23735,23466,A+2,BBCCCoHIDDooHIooAAEEFFoMooJKLMGGJKLM,161,40,0,161]
  
  ET: XXXXX
  TN: 23977
  EN: 3062
  CN: 20329
  DF: 40
  

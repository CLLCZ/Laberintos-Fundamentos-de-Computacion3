# Laberintos: BFS, DFS y Dijkstra

Programa en Java que resuelve laberintos con tres algoritmos de búsqueda
y muestra el recorrido animado en la terminal.
Además cuenta con interfaz realizada en Swing.



![Animación de BFS](docs/bfs.gif)
![AnimacionGrafica.png](img.png)


## Características

- Algoritmos: BFS, DFS y Dijkstra
- Animación del recorrido en la terminal
- Estadísticas: longitud del camino, vértices visitados y tiempo
- No requiere dependencias externas



## Cómo ejecutar

```bash
git clone https://github.com/CLLCZ/Laberintos-Fundamentos-de-Computacion3
cd Laberintos-Fundamentos-de-Computacion3\src\main\java
```

### Con Maven
```bash
mvn compile
java -cp target/classes laberinto.Main        
java -cp target/classes laberinto.MainGrafico   
```

### Sin Maven
```bash
javac -encoding UTF-8 -d out src/main/java/laberinto/*.java
java -cp out laberinto.Main
```

En el directorio [Ejemplos Laberintos](Ejemplos%20Laberintos) se encuentran 3 laberintos de distintos casos, pruebalos.

Genera tus propios laberintos: https://www.delorie.com/game-room/mazes/genmaze.cgi





## Autores

- Sebastian Gastelum (gadfaeds): Menu principal, y animacion de despliegue de laberinto.
- - Camila Zazueta (CLLCZ): Aspecto gráfico de la solución del laberinto.
- <Compañero>: <qué hizo>
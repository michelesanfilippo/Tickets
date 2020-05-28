/*
* Terza prova in itinere laboratorio di algoritmi A.A 2019/2020
* soluzione di Michele Sanfilippo matricola 0664184
*/



import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
/*
* Classe Graph di supporto
* ci consentirà di lavorare con una struttura a grafo
*/

public class Graph<T> {
    // Classe interna per lavorare con archi pesati e diretti
    static class WeightedDirectedEdge<T> {
        // vertice 1
        Vertex<T> vertex1;
        // vertice 2
        Vertex<T> vertex2;
        // peso dell'arco tra i due vertici
        int weight;
        // inizializzo
        WeightedDirectedEdge(Vertex<T> vertex1, Vertex<T> vertex2, int weight) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
            this.weight = weight;
        }
        /*
        * Serie di metodi pubblici utili per ottenere informazioni
        */

        // ottengo il vertice numero 1
        public Vertex<T> getVertex1() {
            return vertex1;
        }
        // ottengo il vertice numero 2
        public Vertex<T> getVertex2() {
            return vertex2;
        }

        // il peso dell'arco tra i due vertici
        public int getWeight() {
            return weight;
        }

    }
    // Classe che definisce come dovrà essere composto ogni vertice
    static class Vertex<T>{
        // sapremo se è stato visitato in base a visited = true or false
        boolean visited;
        // conterrà il dato
        private T data;
        Vertex(T data) {
            this.data = data;
        }
        // ottengo l'informazione contenuta 
        private T getData() {
            return data;
        }
        // posso settare l'informazione
        private void setData(T data) {
            this.data = data;
        }

        // Override del metodo tostring
        public String toString() {
            return getData().toString();
        }

        // Override per verificare se due vertici sono uguali
        public boolean equals(Object obj) {
            if (obj instanceof Vertex) {
                Vertex ver = (Vertex) obj;
                return getData().equals(ver.getData());
            } else return false;
        }
    }

    // gli attributi della classe Graph
    private ArrayList<Vertex<T>> vertices;
    private ArrayList<WeightedDirectedEdge<T>> edges;

    // inizializzo
    public Graph() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
    }

    private Graph(ArrayList<Vertex<T>> vertices, ArrayList<WeightedDirectedEdge<T>> edges) {
        this.vertices = new ArrayList<>(vertices);
        this.edges = new ArrayList<>(edges);
    }
    /*
    * Serie di metodi public utili
    */

    // ottengo una copia del grafo
    public Graph<T> getCopy() {
        return new Graph<>(vertices, edges);
    }

    // ottengo il numero dei vertici
    public int numberOfVertices() {
        return vertices.size();
    }

    // posso aggiungere un vertice passando a parametro l'informazione
    public void addVertex(T data) {
        vertices.add(new Vertex<>(data));
    }

    // ricerca di un nodo data l'informazione passata a parametro
    private Vertex<T> findNode(T data) {
        for (Vertex<T> allVertex : vertices) {
            if (allVertex.getData().equals(data)) {
                return allVertex;
            }
        }
        return null;
    }

    // Controllo dati v1 e v2 se esiste un arco tra di essi
    public boolean checkIfThereIsAnEdgeBetween(T vertex1, T vertex2) {
        // controllo se i vertici esistono
        Vertex<T> node1 = findNode(vertex1);
        Vertex<T> node2 = findNode(vertex2);
        if (node1 == null || node2 == null)
            return false;
        // cerco l'arco
        for(WeightedDirectedEdge<T> edge : edges) {
            if (edge.vertex1.equals(node1) && edge.vertex2.equals(node2)) {
                return true;
            }
        }
        return false;
    }

    //Restituisco l'insieme dei pesi degli archi
    public Set<Integer> getEdgeWeights (T vertex) {
        Set<Integer> set = new HashSet<>();
        Vertex<T> node1 = findNode(vertex);
        for (WeightedDirectedEdge<T> edge : edges) {
            if (edge.vertex2.equals(node1))
                set.add(edge.weight);
        }
        return set;
    }

    // Prendo il peso a parameto e rimuovo gli archi con tale peso
    public void removeEdgesWithWeightOf(int weight) {
        for (int i = 0; i < edges.size();) {
            if (edges.get(i).weight == weight) {
                edges.remove(edges.get(i));
            } else {
                i++;
            }
        }
    }

    // Inserisco direttamente un arco diretto e pesato con parametro v1 , v2 e peso
    public boolean addDirectedEdge(T origin, T destination, int weight) {
        Vertex<T> node1 = findNode(origin);
        Vertex<T> node2 = findNode(destination);
        if (node1 == null || node2 == null)
            return false;
        edges.add(new WeightedDirectedEdge<>(node1, node2, weight));
        return true;
    }


    // Override di utilità
    public String toString() {
        return vertices.toString();
    }
}

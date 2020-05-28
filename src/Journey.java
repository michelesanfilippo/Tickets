/*
* Terza prova in itinere laboratorio di algoritmi A.A 2019/2020
* soluzione di Michele Sanfilippo matricola 0664184
*/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
/*
* Classe Journey che conterrà la soluzione finale.
* Tale classe è la classe principale che dovrà essere chiamata attraverso java Journey dopo aver compilato i file java
*/
public class Journey {
    // Classe risultato che servirà per memorizzare i vari risultati per ogni itinerario
    static class Result {
        // itinerario
        Itinerary itinerary;
        // i ticket usati
        ArrayList<Ticket> tickets;
        // se ho finito oppure no
        boolean finished;
        Result(Itinerary itinerary) {
            this.itinerary = itinerary;
            tickets = new ArrayList<>();
        }

        private Result(Itinerary itinerary, ArrayList<Ticket> tickets) {
            this.itinerary = itinerary;
            this.tickets = new ArrayList<>(tickets);
        }
        // ottengo una copia
        Result getCopy() {
            return new Result(itinerary, tickets);
        }
        // aumento il costo
        int cost() {
            int total = 0;
            for(Ticket ticket : tickets) {
                total += ticket.price;
            }
            return total;
        }
        // aggiungo un ticket 
        void addTicket(Ticket ticket) {
            this.tickets.add(ticket);
            int currentPlace = 0;
            for (Ticket ticket1 : tickets){
                currentPlace = ticket1.useTicket(itinerary, currentPlace);
            }
            if (currentPlace == itinerary.places.length)
                finished = true;
        }

        // Override utile per la stampa finale
        public String toString() {
            int totalPrice = 0;
            for (Ticket ticket : tickets) {
                totalPrice += ticket.price;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Costo totale : ").append(totalPrice);
            stringBuilder.append(String.format(" Itinerario numero: ", itinerary.id+1));
            for (Ticket ticket : tickets) {
                stringBuilder.append(String.format("(Ticket numero %d prezzo %d) + ",ticket.ticketNumber, ticket.price));
            }
            if (tickets.size() == 0)
                stringBuilder.append("Itinerario non possibile");
            else
                stringBuilder.setLength(stringBuilder.length()-2);
            return stringBuilder.toString();

        }
    }
    // attributi della classe Journey

    // quali sono i ticket disponibili
    private static ArrayList<Ticket> tickets;
    // quali sono quelli usati
    private static ArrayList<Ticket> usedTickets;
    // quali sono gli itinerari da effettuare
    private static ArrayList<Itinerary> itineraries;
    // aereoporti visti come aereoporti delle città attraversate
    private static Set<Integer> airports;
    public static void main(String[] args) {
        //leggo l'input e lo salvo
        airports = new HashSet<>();
        usedTickets = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int numberOfTickets = scanner.nextInt();
        tickets = new ArrayList<>();
        for (int i = 0; i < numberOfTickets; i++) {
            int price = scanner.nextInt();
            int numOfDestinations = scanner.nextInt();
            int[] destinations = new int[numOfDestinations];
            for (int j = 0; j < numOfDestinations; j ++) {
                destinations[j] = scanner.nextInt();
                airports.add(destinations[j]);
            }
            tickets.add(new Ticket(price, destinations));
        }
        int numberOfItineraries = scanner.nextInt();
        itineraries = new ArrayList<>();
        for (int i = 0; i < numberOfItineraries; i++) {
            int numOfDestinations = scanner.nextInt();
            int[] destinations = new int[numOfDestinations];
            for (int j = 0; j < numOfDestinations; j ++) {
                destinations[j] = scanner.nextInt();
            }
            itineraries.add(new Itinerary(destinations, i));
        }
        helperFunction();
    }

    /*
     * Creiamo un grafo con archi diretti e pesati inserendo come peso il numero del ticket che stiamo usando per 
     * sapere in qualsiasi momento se stiamo cambiando ticket.
     * Useremo queste notazioni:
     *      A[] sarà la lista delle destinazioni del ticket
     *      Ci sarà un arco tra A[n] e A[n+1]
     *      Per ogni A nella lista ticket, ci sarà un arco tra A[0] e tutti i vertici
     */
    private static void helperFunction () {
        Graph<Integer> graph = new Graph<>();
        for (Integer integer : airports)
            graph.addVertex(integer);
        for (Ticket ticket: tickets) {
            //Aggiungo gli archi su cui posso andare per il ticket
            for (int i = 0; i < ticket.destinations.length-1; i++) {
                graph.addDirectedEdge(ticket.destinations[i], ticket.destinations[i+1], ticket.ticketNumber);
            }
            //Aggiungo gli archi dagli altri aereoporti che possono andare al ticket
            for (Integer integer : airports) {
                graph.addDirectedEdge(integer, ticket.destinations[0], ticket.ticketNumber);
            }
        }
        for (Itinerary itinerary : itineraries) {
            // cerco il risultato
            Result result = findResult(itinerary, graph, 0, null);
            System.out.println(result);
            for (Ticket ticket : result.tickets){
                graph.removeEdgesWithWeightOf(ticket.ticketNumber);
            }
        }
    }

    /*
    * Questo sarà il metodo che ci porterà alla soluzione finale.
    * Tale metodo è stato pensato sulla base delle visite DFS per grafi ed è stato adattato al nostro problema.
    * Proprio per questo dovremo fare una ricerca esaustiva ogni volta che torniamo indietro e per questo motivo la complessità
    * di tempo non sarà quella di una semplica visita DFS ma sarà maggiore.
    * È stato visto che la complessità di tempo è all'incirca O(V!)
    */
    private static Result findResult(Itinerary itinerary, Graph<Integer> graph, int nextPlaceWeNeedToGoIndex, Result result) {
        // Iniziamo dal caso base, ovvero quando l'indice è quello dell'ultima città
        if (nextPlaceWeNeedToGoIndex == itinerary.places.length) return result;

        ArrayList<Ticket> tickets2 = new ArrayList<>();
        //Prendo i pesi degli archi
        Set<Integer> integers = graph.getEdgeWeights(itinerary.places[nextPlaceWeNeedToGoIndex]);
         //Dato che i pesi erano il numero dei ticket allora:
        for(Integer integer: integers) {
            for (Ticket ticket : tickets) {
                //Possiamo muoverci se esiste l'arco e aggiorno
                if (integer.equals(ticket.ticketNumber)) {
                    tickets2.add(ticket);
                }
            }
        }
        //se  è null creo una copia dei risultati
        if (result == null) result = new Result(itinerary);
        //salvo il risultato come minimo inizialmente(a prima iterata sarà null), se il risultato ha 0 ticket invece
        //non esiste un possibile path
        Result lowest = result; 
        for (Ticket ticket : tickets2) {
            //se true possiamo usare il ticket
            if (graph.checkIfThereIsAnEdgeBetween(itinerary.places[nextPlaceWeNeedToGoIndex], ticket.destinations[0])) {
                int nextPlace = ticket.useTicket(itinerary, nextPlaceWeNeedToGoIndex);
                //creo una copia del grafo dato che rimuoviamo l'arco con il numero del ticket
                Graph<Integer> copiedGraph = graph.getCopy();
                copiedGraph.removeEdgesWithWeightOf(ticket.ticketNumber);
                //creo una copia dei risultati dato che possono essere differenti ad ogni iterata
                Result resultCopy = result.getCopy();
                // aggiungo il ticket
                resultCopy.addTicket(ticket);
                //Continuo fino al caso base
                Result returned = findResult(itinerary, copiedGraph, nextPlace, resultCopy);
                // salvo realmente il minimo risultato per l'itinerario desiderato
                if (!lowest.finished && returned.finished || lowest.cost() == 0 && returned.finished) {
                    lowest = returned;
                } else {
                    if (lowest.cost() > returned.cost() && returned.finished) {
                        lowest = returned;
                    }
                }
            }
        }
        return lowest;
    }
}
/*
* Terza prova in itinere laboratorio di algoritmi A.A 2019/2020
* soluzione di Michele Sanfilippo matricola 0664184
*/

/*
* Classe Ticket di supporto
* per tenere traccia dei biglietti inseriti con i propri attrbuti
*/
public class Ticket {
    // il prezzo
    public int price;
    // le destinazioni
    public int[] destinations;
    // il numero del ticket
    public int ticketNumber;
    // un contatore per aggiornare il numero del ticket
    static int ticketCounter = 1;
    // inserisco il prezzo per quelle destinazioni presenti nel ticket
    Ticket(int price, int[] destinations) {
        this.price = price;
        this.destinations = destinations;
        ticketNumber = ticketCounter++;
    }

    /*
     * Partendo dal posto iniziale e prendendo
     * in considerazione l'itinerario restituisce il costo
     * se non esiste percorso allora restituirà MAX_VALUE
     */
    double getWorth (Itinerary itinerary, int startingPlace) {
        int num = (useTicket(itinerary, startingPlace)-startingPlace);
        if (num != 0)
            return (price/num)*price;
        else
            return Integer.MAX_VALUE;
    }

    /*
     * Restituisce il posto in cui finiremo usando il ticket 
     */
    int useTicket(Itinerary itinerary, int startingPlace) {
        for (int i = startingPlace, j =0; i < itinerary.places.length && j < destinations.length; j++) {
            if (itinerary.places[i] == destinations[j]) {
                i++;
                startingPlace++;
            }
        }
        return startingPlace;
    }

    /*
    * Override di utilità del metodo tostring per eventuale stampa
    */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(price).append(": ");
        for (int i : destinations) {
            stringBuilder.append(i).append(" ");
        }
        return stringBuilder.toString();
    }

    /*
    * Override di equals per confrontare i ticket
    */
    public boolean equals(Object obj) {
        if (obj instanceof Ticket) {
            Ticket ticket = (Ticket) obj;
            if (ticket.price != price)
                return false;
            if (ticket.destinations.length != destinations.length)
                return false;
            for (int i = 0; i < destinations.length; i ++) {
                if (destinations[i] != ticket.destinations[i])
                    return false;
            }
            return true;
        }
        return false;
    }
}

/*
* Terza prova in itinere laboratorio di algoritmi A.A 2019/2020
* soluzione di Michele Sanfilippo matricola 0664184
*/

/*
* Classe Itinerary di supporto
* per tenere traccia degli itinerari inseriti con i propri attrbuti
*/
public class Itinerary {
    // posti da visitare
    int[] places;
    // id itinerario
    int id;
    Itinerary(int[] places, int id) {
        this.places = places;
        this.id = id;
    }

    // Override di utilità per eventuale stampa
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i : places) {
            stringBuilder.append(i).append(" ");
        }
        return stringBuilder.toString();
    }
}
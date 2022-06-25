package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	private YelpDao dao;
	private Graph<Business, DefaultWeightedEdge> grafo;
	private Map<String, Business> idMap;
	
	private List<Business> best;
	
	public Model() {
		super();
		this.dao = new YelpDao();
	}
	
	public List<String> getAllCities(){
		return this.dao.getAllCities();
	}
	
	public void creaGrafo(String city) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMap = new HashMap<>();
		
		this.dao.getVertici(city, idMap);
		
		//Aggiungo i vertici
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		//Aggiungo gli archi
		for(Adiacenza a: this.dao.getAdiacenze(city, idMap)) {
		   if(this.grafo.containsVertex(a.getB1()) && this.grafo.containsVertex(a.getB2())){
			   Graphs.addEdgeWithVertices(this.grafo, a.getB1(), a.getB2(), a.getPeso());
		   }
		}
		 System.out.println("Grafo creato!");
		 System.out.println("#VERTICI: "+ this.grafo.vertexSet().size());
		 System.out.println("#ARCHI: "+ this.grafo.edgeSet().size());
	}
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public boolean grafoCreato() {
		if(this.grafo == null)
			return false;
		else
			return true;
	}
	
	public List<Business> getVertici(){
		List<Business> vertici = new ArrayList<>(this.grafo.vertexSet());
		return vertici;
	}
	
	public Vicino getPiuDistante(Business business) {
		Vicino piuDistante = null;
		Double pesoMax = 0.0;
		for(Business b: Graphs.neighborListOf(this.grafo, business)) {
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(business, b)) > pesoMax) {
				pesoMax = this.grafo.getEdgeWeight(this.grafo.getEdge(business, b));
			}
		}
		
		for(Business b: Graphs.neighborListOf(this.grafo, business)) {
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(business, b)) == pesoMax) {
				piuDistante = new Vicino(b, pesoMax);
			}
		}
	   return piuDistante;
	}
	
	public List<Business> trovaPercorso(Business partenza, Business arrivo, Double soglia){
		this.best = new ArrayList<>();
		
		List<Business> parziale = new ArrayList<>();
		
		parziale.add(partenza);
		
		cerca(parziale, arrivo, soglia);
		
		return best;
	}

	private void cerca(List<Business> parziale, Business arrivo, Double soglia) {
		
		Business ultimo = parziale.get(parziale.size()-1);
		
		if(ultimo.equals(arrivo)) {
			if(this.best.isEmpty()) {
				this.best = new ArrayList<>(parziale);
			    return;
			}else if(parziale.size() > best.size()) {
				this.best = new ArrayList<>(parziale);
			    return;
			}
		}
		
		for(Business b: Graphs.neighborListOf(this.grafo, ultimo)) {
			if(b.getStars() > soglia) {
				if(!parziale.contains(b)) {
					parziale.add(b);
					cerca(parziale, arrivo, soglia);
					parziale.remove(parziale.size()-1);
				}
			}
		}
	}
	
	public Double getKmTotali(List<Business> percorso) {
		Double kmTotali = 0.0;
		
		for(int i = 0; i< percorso.size()-1; i++) {
	    	   kmTotali += this.grafo.getEdgeWeight(this.grafo.getEdge(percorso.get(i), percorso.get(i+1)));
	       }
		return kmTotali;
	}
}

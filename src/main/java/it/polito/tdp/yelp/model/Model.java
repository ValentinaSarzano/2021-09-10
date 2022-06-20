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
	
	
	
	public Model() {
		super();
		this.dao = new YelpDao();
	}

	public List<String> getAllCities(){
		return this.dao.getAllCities();
	}
	
	public void creGrafo(String city) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMap = new HashMap<>();
		
		this.dao.getVertici(city, idMap);
		
		//Aggiunta vertici
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		//Aggiunta archi
	    for(Adiacenza a: this.dao.getAdiacenze(city, idMap)) {
	    	if(this.grafo.containsVertex(a.getB1()) && this.grafo.containsVertex(a.getB2())) {
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
	
	public List<Business> getVertici(){
		List<Business> vertici = new ArrayList<>(this.grafo.vertexSet());
		return vertici;
	}
	
	public Vicino getPiuDistante(Business b1) {
		Vicino piuDistante = null;
		double distanzaMax = 0.0;
		for(Business b: Graphs.neighborListOf(this.grafo, b1)) {
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(b1, b)) > distanzaMax) {
				distanzaMax = this.grafo.getEdgeWeight(this.grafo.getEdge(b1, b));
			}
		}
		for(Business b: Graphs.neighborListOf(this.grafo, b1)) {
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(b1, b)) == distanzaMax) {
				piuDistante = new Vicino(b, distanzaMax);
			}
		}
		
		return piuDistante;
		
	}
}

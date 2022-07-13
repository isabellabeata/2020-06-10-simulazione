package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Map<Integer, Actor> idMap;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private List<Actor> attori;
	
	
	public Model() {
		this.dao= new ImdbDAO();
		this.idMap= new HashMap<>();
		dao.listAllActors(idMap);
	}
	
	public List<String> popolaCmbGeneri(){
		return dao.popolaCmbGeneri();
	}
	
	public void creaGrafo(String genere) {
		
		this.grafo= new SimpleWeightedGraph<Actor, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.attori= new ArrayList<>(dao.getVertici(genere, idMap));
		Graphs.addAllVertices(this.grafo, attori);
		
		for(Arco ai: this.dao.getArco(genere, idMap)) {
			if(!this.grafo.containsEdge(ai.getA1(), ai.getA2())) {
			Graphs.addEdgeWithVertices(this.grafo, ai.getA1(), ai.getA2(), ai.getPeso());
			}
		}
		
	}
	
	public String nVertici() {
		return "Grafo creato!"+"\n"+"#verici: "+ this.grafo.vertexSet().size()+"\n";
	}
	
	public String nArchi() {
		return "#archi: "+ this.grafo.edgeSet().size()+"\n";
	}

	public List<Actor> getAttori() {
		Collections.sort(attori);
		return attori;
	}

	public void setAttori(List<Actor> attori) {
		
		this.attori = attori;
	}
	
	public List<Actor> attoriSimili(Actor a){
		ConnectivityInspector<Actor, DefaultWeightedEdge> ci= new ConnectivityInspector<>(grafo);
		List<Actor> actors = new ArrayList<>(ci.connectedSetOf(a));
		actors.remove(a);
		Collections.sort(actors);
		return actors;
		
	}
}

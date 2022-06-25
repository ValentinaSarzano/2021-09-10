package it.polito.tdp.yelp.model;

public class Vicino {
	private Business business;
	private double peso;
	
	public Vicino(Business business, double peso) {
		super();
		this.business = business;
		this.peso = peso;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}
	

}

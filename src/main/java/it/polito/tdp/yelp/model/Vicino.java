package it.polito.tdp.yelp.model;

public class Vicino {
	private Business business;
	private double distanza;
	
	public Vicino(Business business, double distanza) {
		super();
		this.business = business;
		this.distanza = distanza;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public double getDistanza() {
		return distanza;
	}

	public void setDistanza(double distanza) {
		this.distanza = distanza;
	}
	

}

package model.presentie;

import java.util.Date;

import model.persoon.Student;

import model.vakGegevens.Les;


public class Presentie {
	private boolean isAanwezig;
	private String username;
	private Date startDatum;
	private Date eindDatum;
	private String vak;
	private String klas;
	private String lokaal;
	private String docent;
	
	public Presentie(String username, Date startDatum, Date eindDatum, String vak, String klas, String lokaal, String docent){
		this.username = username;
		this.startDatum = startDatum;
		this.eindDatum = eindDatum;
		this.vak = vak;
		this.klas = klas;
		this.lokaal = lokaal;
		this.docent = docent;
	}
	
	
	


	public boolean equals(Object obj){
		boolean gelijk = false;
		
		if (obj instanceof Presentie){
			Presentie anderePresentie = (Presentie) obj;
  		if (this.username.equals(anderePresentie.username) &&	
  				this.startDatum.equals(anderePresentie.startDatum)	&&
  				this.eindDatum.equals(anderePresentie.eindDatum) &&	
  				this.vak.equals(anderePresentie.vak) &&	
  				this.klas.equals(anderePresentie.klas) &&	
  				this.lokaal.equals(anderePresentie.lokaal) &&
  				this.docent.equals(anderePresentie.docent)				
					){
				gelijk = true;
			}
		}
		return gelijk;		
	}





	@Override
	public String toString() {
		return username + "," + startDatum + "," + eindDatum + "," + vak + "," + klas + "," + lokaal + "," + docent;
	}





	public boolean isAanwezig() {
		return isAanwezig;
	}





	public void setAanwezig(boolean isAanwezig) {
		this.isAanwezig = isAanwezig;
	}





	public String getUsername() {
		return username;
	}





	public void setUsername(String username) {
		this.username = username;
	}





	public Date getStartDatum() {
		return startDatum;
	}





	public void setStartDatum(Date startDatum) {
		this.startDatum = startDatum;
	}





	public Date getEindDatum() {
		return eindDatum;
	}





	public void setEindDatum(Date eindDatum) {
		this.eindDatum = eindDatum;
	}





	public String getVak() {
		return vak;
	}





	public void setVak(String vak) {
		this.vak = vak;
	}





	public String getKlas() {
		return klas;
	}





	public void setKlas(String klas) {
		this.klas = klas;
	}





	public String getLokaal() {
		return lokaal;
	}





	public void setLokaal(String lokaal) {
		this.lokaal = lokaal;
	}





	public String getDocent() {
		return docent;
	}





	public void setDocent(String docent) {
		this.docent = docent;
	}






}

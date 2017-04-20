package model.vakGegevens;



import java.util.Date;
import java.util.Date;

import model.klas.Klas;
import model.persoon.Docent;

public class Les {
	private Date startDate;
	private Date eindDate;
	private String locatie;
	private Vak hetVak;
	private Klas deKlas; 
	private Docent deDocent;
	
	public Les(
			Date startDate, 
			Date eindDate, 
			String locatie, 
			Vak hetVak, 
			Klas deKlas, 
			Docent deDocent){
		this.startDate = startDate;
		this.eindDate = eindDate; 
		this.locatie = locatie;
		this.hetVak = hetVak;
		this.deKlas = deKlas;
		this.deDocent = deDocent;
	}

	public Docent getDocent(){
		return deDocent;
	}
	
	public void setDocent(Docent deDocent){
		this.deDocent = deDocent;
	}
	
	public Vak getVak(){
		return hetVak;
	}
	
	public void setVak(Vak hetVak){
		this.hetVak = hetVak;
	}
	
	public Klas getKlas(){
		return deKlas;
	}
	
	public void setKlas(Klas deKlas){
		this.deKlas = deKlas;
	}
	
	public Date getStartDateTime(){
		return startDate;
	}
	
	public void setStartDateTime(Date startDate){
		this.startDate = startDate;
	}
	
	public Date getEindDateTime(){
		return eindDate;
	}
	
	public void setEindDateTime(Date eindDate){
		this.eindDate = eindDate;
	}
	
	public String getLocatie(){
		return locatie;
	}
	
	public void setLocatie(String locatie){
		this.locatie = locatie;
	}
	
}

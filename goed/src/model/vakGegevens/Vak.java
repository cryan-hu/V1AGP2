package model.vakGegevens;

public class Vak {
	private String vakCode; 
	private String naam;
	
	public Vak(String vakCode, String naam){
		this.vakCode = vakCode;
		this.naam = naam;
	}
	
	public String getVakCode(){
		return vakCode;
	}
	
	public void setVakCode(String vakCode){
		this.vakCode = vakCode;
	}
	
	public String getNaam(){
		return naam;
	}
	
	public void setNaam(String naam){
		this.naam = naam;
	}
	
	public boolean equals(Object andereObject){
		boolean gelijkObj = false;
		
		if(andereObject instanceof Vak){
			Vak andereVak = (Vak) andereObject;
			if(this.naam.equals(andereVak.naam)&&
					this.vakCode.equals(andereVak.vakCode)){
				gelijkObj = true;
			}
		}
	return gelijkObj;
	}
}

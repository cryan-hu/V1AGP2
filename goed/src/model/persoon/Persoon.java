package model.persoon;



public abstract class Persoon {

	private String voornaam;
	private String tussenvoegsel;
	private String achternaam;
	private String wachtwoord;
	private String gebruikersnaam;

	public Persoon(String voornaam, 
			String tussenvoegsel, 
			String achternaam, 
			String wachtwoord, 
			String gebruikersnaam) {
		this.voornaam = voornaam;
		this.tussenvoegsel = tussenvoegsel;
		this.achternaam = achternaam;
		this.wachtwoord = wachtwoord;
		this.gebruikersnaam = gebruikersnaam;
	}

	public String getVoornaam() {
		return this.voornaam;
	}
	
	public void setVoornaam(String voornaam){
		this.voornaam = voornaam;
	}

	private String getAchternaam() {
		return this.achternaam;
	}
	
	public void setAchternaam(String achternaam){
		this.achternaam = achternaam;
	}

	protected String getWachtwoord() {
		return this.wachtwoord;
	}
	
	public void setWachtwoord(String wachtwoord){
		this.wachtwoord = wachtwoord;
	}

	public String getGebruikersnaam() {
		return this.gebruikersnaam;
	}
	
	public void setGebruikersnaam(String gebruikersnaam ){
		this.gebruikersnaam = gebruikersnaam;
	}

	public String getVolledigeAchternaam() {
		String lVolledigeAchternaam="";
		if (this.tussenvoegsel != null && this.tussenvoegsel != "" && this.tussenvoegsel.length() > 0) {
			lVolledigeAchternaam += this.tussenvoegsel + " ";
		}
		lVolledigeAchternaam += this.getAchternaam();
		return lVolledigeAchternaam;
	}
	
	public String getVolledigeNaam(){
		String volledigeNaam = voornaam;
		if (this.tussenvoegsel != null && this.tussenvoegsel != "" && this.tussenvoegsel.length() > 0) {
			volledigeNaam += " " + this.tussenvoegsel;
		}
		volledigeNaam += " " + this.getAchternaam();
		return volledigeNaam;		
	}

	public boolean komtWachtwoordOvereen(String pWachtwoord) {
		boolean lStatus = false;
		if (this.getWachtwoord().equals(pWachtwoord)) {
			lStatus = true;
		}
		return lStatus;
	}
	
	public boolean equals(Object andereObject){
		boolean gelijkeObj = false;
		
		if (andereObject instanceof Persoon){
			Persoon anderePersoon = (Persoon) andereObject;
			if(anderePersoon.voornaam.equals(this.voornaam)&&
					anderePersoon.achternaam.equals(this.achternaam)&&
					anderePersoon.tussenvoegsel.equals(this.tussenvoegsel)&&
					anderePersoon.wachtwoord.equals(this.wachtwoord)&&
					anderePersoon.gebruikersnaam.equals(this.gebruikersnaam)){
				gelijkeObj = true;
			}
		}
		return gelijkeObj;
	}

}

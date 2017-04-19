package model.presentie;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;


public class Afwezigheid {
	private Date beginTijd;
	private Date eindTijd;
	private String useCase;
	private String username;
	private String vak;
	private String klas;
	private String docent;
	

	public Afwezigheid(String useCase, Date beginTijd, Date eindTijd, String username, String vak, String klas, String docent) {
		this.useCase = useCase;
		this.beginTijd= beginTijd;
		this.eindTijd = eindTijd;
		this.username = username;
		this.vak = vak;
		this.klas = klas;
		this.docent = docent;
	}

	public Date geteindTijd() { //aanpassen
		return this.eindTijd;
	}
	
	public Date getBeginTijd() { //aanpassen
		return this.beginTijd;
	}

	public String getUseCase() {
		return useCase;
	}
	
	public String getUsername() {
		return username;
	}

	public String getAfwezigheid() {
		String s= "De afwezigheid is van " + this.beginTijd + " tot " + this.eindTijd; 
		return s; // datum + eindTijd;
	}

	public boolean isActieveZiektemelding() {
		Calendar nowDate = Calendar.getInstance(); 
		if (nowDate.after(this.beginTijd) && nowDate.before(eindTijd)) { // begin<huidigedatum<eind
			return true;
		} else {
			return false; // hier moet met huidige datum gekeken worden of dat wel in
										// de range van begin en eind datum zit. zoja returnt True,
										// anders false.
		}
	}

	public boolean isZiektemelding(){
		
		if(this.useCase.contains("ziek")){
			return true;
		}else{
			return false;
		}
		
		
		
		
			/*if (datum!=null && eindTijd!=null){
				return true;
			} else{
			return false; // kijken of er begin en eind datum zijn ingesteld zoja dan returnt True anders false;
			}
		}*/
	}

	
	public boolean equals(Object obj){
		boolean gelijkeObjecten = false; 
		if (obj instanceof Afwezigheid) {
		Afwezigheid andereAfwezigheid = (Afwezigheid) obj;
		if (this.useCase.equals(andereAfwezigheid.useCase) &&	
				this.beginTijd.equals(andereAfwezigheid.beginTijd)	&&
				this.eindTijd.equals(andereAfwezigheid.eindTijd) &&	
				this.username.equals(andereAfwezigheid.username) &&	
				this.vak.equals(andereAfwezigheid.vak) &&	
				this.klas.equals(andereAfwezigheid.klas) &&	
				this.docent.equals(andereAfwezigheid.docent)
				
				) {
			gelijkeObjecten = true;
			}
		}
		return gelijkeObjecten;
	}
	private void setBeginTijd(Date beginTijd) {
		this.beginTijd = beginTijd;
	}
	
	public void seteindTijd(Date eindTijd){
		this.eindTijd = eindTijd;
	}

	private void setuseCase(String useCase) {
		this.useCase = useCase;
	}
	
	public String toString(){
		return  this.useCase + "," + this.beginTijd + "," + this.eindTijd + "," + this.username + "," + this.vak + "," + this.klas + "," + this.docent;
				
				
	}

}

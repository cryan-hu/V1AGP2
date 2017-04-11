package model.presentie;

import java.util.Calendar;

public class Afwezigheid {
	private Calendar beginDatum;
	private Calendar eindDatum;
	private String type;

	public Afwezigheid(String type, Calendar beginDatum, Calendar eindDatum) {
		this.type = type;
		this.beginDatum = beginDatum;
		this.eindDatum = eindDatum;
	}

	public Calendar getBeginDatum() {
		return beginDatum;
	}

	public Calendar getEindDatum() {
		return eindDatum;
	}

	private String getType() {
		return type;
	}

	public String getVolledigeAfwezigheid() {
		String s= "De Volledige afwezigheid is van: "+beginDatum+" tot "+eindDatum+"."; 
		return s; // beginDatum + eindDatum;
	}

	public boolean isActieveZiektemelding() {
		Calendar nowDate = Calendar.getInstance(); 
		if (nowDate.after(beginDatum) && nowDate.before(eindDatum)) { // begin<huidigedatum<eind
			return true;
		} else {
			return false; // hier moet met huidige datum gekeken worden of dat wel in
										// de range van begin en eind datum zit. zoja returnt True,
										// anders false.
		}
	}

	public boolean isZiektemelding(){
		
		if(this.type.contains("ziek")){
			return true;
		}else{
			return false;
		}
		
		
		
		
			/*if (beginDatum!=null && eindDatum!=null){
				return true;
			} else{
			return false; // kijken of er begin en eind datum zijn ingesteld zoja dan returnt True anders false;
			}
		}*/
	}

	private void setBeginDatum(Calendar beginDatum) {
		this.beginDatum = beginDatum;
	}
	
	public void setEindDatum(Calendar eindDatum){
		this.eindDatum = eindDatum;
	}

	private void setType(String type) {
		this.type = type;
	}

}

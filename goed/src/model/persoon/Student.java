//checked
package model.persoon;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.presentie.Afwezigheid;

public class Student extends Persoon {

	private int studentNummer;
	private String groepId;
	private ArrayList<Afwezigheid> afwezigheden;
	private String klas;

	public Student(
		String pVoornaam, 
		String pTussenvoegsel, 
		String pAchternaam, 
		String pWachtwoord, 
		String pGebruikersnaam,
		int pStudentNummer){
		super(
			pVoornaam, 
			pTussenvoegsel, 
			pAchternaam, 
			pWachtwoord, 
			pGebruikersnaam);
		this.setGroepId("");
		this.setStudentNummer(pStudentNummer);
		this.afwezigheden = new ArrayList<Afwezigheid>();
	}
	
	public Student(
			String pVoornaam, 
			String pTussenvoegsel, 
			String pAchternaam, 
			String pWachtwoord, 
			String pGebruikersnaam,
			int pStudentNummer,
			String klas){
			super(
				pVoornaam, 
				pTussenvoegsel, 
				pAchternaam, 
				pWachtwoord, 
				pGebruikersnaam);
			this.setGroepId("");
			this.setStudentNummer(pStudentNummer);
			this.afwezigheden = new ArrayList<Afwezigheid>();
			this.klas = klas;
		}

	public Afwezigheid getActieveZiektemelding(){
		Date nowDate = new Date();
		for (Afwezigheid a: afwezigheden){
			if(nowDate.before(a.getBeginTijd())){
				return a;
			}
		} // returnt actieve melding die open staat.
		return null;
	}
	
	public ArrayList<Afwezigheid> getAfwezigheden(){
		return afwezigheden;
	}
	
	public int getStudentNummer() {
		return this.studentNummer;
	}

	private void setStudentNummer(int pStudentNummer) {
		this.studentNummer = pStudentNummer;
	}
	
	public void voegAfwezigheidToe(Afwezigheid afwezigheid){
		afwezigheden.add(afwezigheid);
	}
	
	public String getGroepId() {
    return this.groepId;	
  }
 
  public void setGroepId(String pGroepId) {
    this.groepId= pGroepId;	
  }

	public void setKlas(String klas) {
		this.klas = klas;
	}
	public String getKlas() {
		return this.klas;
	}
	
	
	
	
}

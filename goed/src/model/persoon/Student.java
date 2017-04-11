//checked
package model.persoon;

import java.util.ArrayList;
import java.util.Calendar;

import model.presentie.Afwezigheid;

public class Student extends Persoon {

	private int studentNummer;
	private String groepId;
	private ArrayList<Afwezigheid> afwezigheden;

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

	public Afwezigheid getActieveZiektemelding(){
		Calendar nowDate = Calendar.getInstance(); 
		for (Afwezigheid a: afwezigheden){
			if(nowDate.before(a.getEindDatum())){
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

	
	
	
	
	
	
}

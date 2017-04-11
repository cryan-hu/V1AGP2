package model.klas;

import java.util.ArrayList;
import model.persoon.Student;

public class Klas {

	private String klasCode;
	private String naam;
	private ArrayList<Student> deStudenten;

	public Klas(String klasCode, String naam) {
		this.klasCode = klasCode;
		this.naam = naam;
		deStudenten = new ArrayList<Student>();
	}
	
	public String getKlasCode() {
		return klasCode;
	}
	
	public void setKlasCode(String klasCode){
		this.klasCode = klasCode;
	}
	
	public String getNaam() {
		return naam;
	}
	
	public void setNaam(String naam){
		this.naam = naam;
	}
	public ArrayList<Student> getStudenten() {
		return this.deStudenten;
	}
	
	public void setStudenten(ArrayList<Student> deStudenten){
		this.deStudenten = deStudenten;
	}
	
	public boolean bevatStudent(Student student) {
		if(deStudenten.contains(student)){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean equals(Object obj){	
		if (obj instanceof Klas){
			Klas andereKlas = (Klas) obj;
			if (this.klasCode.equals(andereKlas.klasCode)&&
					(this.naam.equals(andereKlas.naam))){
				return true;
			}
		}return false;
	}
	
	public int aantalStudenten(){
		return deStudenten.size();
	}

	public void voegStudentToe(Student student) {
		if (!deStudenten.contains(student)) {
			deStudenten.add(student);
		}
	}
	
	public void verwijderStudent(Student student){
		if(deStudenten.contains(student)){
			deStudenten.remove(student);
		}
	}

}

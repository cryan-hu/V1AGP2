package model;

import java.util.ArrayList;

import model.klas.Klas;
import model.persoon.Docent;
import model.persoon.Student;
import model.presentie.Presentie;
import model.vakGegevens.Les;
import model.vakGegevens.Vak;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PrIS {
	private ArrayList<Docent> deDocenten;
	private ArrayList<Student> deStudenten;
	private ArrayList<Klas> deKlassen;
	private ArrayList<Presentie> dePresenties;
	private ArrayList<Vak> deVakken;
	private ArrayList<Les> deLessen;

	
	/**
	 * De constructor maakt een set met standaard-data aan. Deze data
	 * moet nog uitgebreidt worden met rooster gegevens die uit een bestand worden
	 * ingelezen, maar dat is geen onderdeel van deze demo-applicatie!
	 * 
	 * De klasse PrIS (PresentieInformatieSysteem) heeft nu een meervoudige
	 * associatie met de klassen Docent, Student, Vakken en Klassen Uiteraard kan dit nog veel
	 * verder uitgebreid en aangepast worden! 
	 * 
	 * De klasse fungeert min of meer als ingangspunt voor het domeinmodel. Op
	 * dit moment zijn de volgende methoden aanroepbaar:
	 * 
	 * String login(String gebruikersnaam, String wachtwoord)
	 * Docent getDocent(String gebruikersnaam)
	 * Student getStudent(String gebruikersnaam)
	 * ArrayList<Student> getStudentenVanKlas(String klasCode)
	 * 
	 * Methode login geeft de rol van de gebruiker die probeert in te loggen,
	 * dat kan 'student', 'docent' of 'undefined' zijn! Die informatie kan gebruikt 
	 * worden om in de Polymer-GUI te bepalen wat het volgende scherm is dat getoond 
	 * moet worden.
	 * 
	 */
	public PrIS() {
		deDocenten = new ArrayList<Docent>();
		deStudenten = new ArrayList<Student>();
		deKlassen = new ArrayList<Klas>();
		deVakken = new ArrayList<Vak>();
		deLessen = new ArrayList<Les>();
		dePresenties = new ArrayList<Presentie>();

		//vulLessen(deLessen); // CODE VOOR vulLessen CHECKEN!!! is het nodig!!??
		
		vulVakken(deVakken);

		// Inladen klassen
		vulKlassen(deKlassen);

		// Inladen studenten in klassen
		vulStudenten(deStudenten, deKlassen);

		// Inladen docenten
		vulDocenten(deDocenten);
	
	} //Einde Pris constructor
	
	//deze method is static onderdeel van PrIS omdat hij als hulp methode 
	//in veel controllers gebruikt wordt
	//een standaardDatumString heeft formaat YYYY-MM-DD
	public static Calendar standaardDatumStringToCal(String pStadaardDatumString) {
		Calendar lCal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			lCal.setTime(sdf.parse(pStadaardDatumString));
		}  catch (ParseException e) {
			e.printStackTrace();
			lCal=null;
		}
		return lCal;
	}
	//deze method is static onderdeel van PrIS omdat hij als hulp methode 
	//in veel controllers gebruikt wordt
	//een standaardDatumString heeft formaat YYYY-MM-DD
	public static String calToStandaardDatumString (Calendar pCalendar) {
		int lJaar = pCalendar.get(Calendar.YEAR);
		int lMaand= pCalendar.get(Calendar.MONTH) + 1;
		int lDag  = pCalendar.get(Calendar.DAY_OF_MONTH);

		String lMaandStr = Integer.toString(lMaand);
		if (lMaandStr.length() == 1) {
			lMaandStr = "0"+ lMaandStr;
		}
		String lDagStr = Integer.toString(lDag);
		if (lDagStr.length() == 1) {
			lDagStr = "0"+ lDagStr;
		}
		String lString = 
				Integer.toString(lJaar) + "-" +
				lMaandStr + "-" +
				lDagStr;
		return lString;
	}

	private Vak getVak(String vakCode){
		for (Vak lVak : deVakken) {
			if (lVak.getVakCode().equals(vakCode)){
				return lVak;
			}
		}
		return null;
	}
	
	public Docent getDocent(String gebruikersnaam) {
		Docent resultaat = null;
		
		for (Docent d : deDocenten) {
			if (d.getGebruikersnaam().equals(gebruikersnaam)) {
				resultaat = d;
				break;
			}
		}
		
		return resultaat;
	}
	
	public ArrayList<Vak> getDocentVakken(Docent docent){
		ArrayList<Vak> docentVakken = new ArrayList<Vak>() ;
		for(Les l : deLessen){
			if(l.getDocent().equals(docent)){
				docentVakken.add(l.getVak());
			}
		}return docentVakken;
	}
	
	public ArrayList<Les> getDocentLessen(Docent docent){
		ArrayList<Les> docentLessen = new ArrayList<Les>();
		for(Les l : deLessen){
			if(l.getDocent().equals(docent)){
				docentLessen.add(l);
			}
		}return docentLessen;
	}
	
	public Klas getKlasVanStudent(Student pStudent) {
	  //used
		for (Klas lKlas : deKlassen) {
			if (lKlas.bevatStudent(pStudent)){
				return (lKlas);
			}
		}
		return null;
	}
	
	public Les getLes(String les){
		for(Les l : deLessen){
			if(l.equals(les)){
				return l;
			}
		}return null;
	}
	
	public Presentie getPresentie(Les les, Student student){
		for(Presentie p : dePresenties){
			if(p.getStudent().equals(student)&&
					p.getLes().equals(les)){
				return p;
			}
		}return null;
	}
	
	//public int getPresentieType(Les les, Student student){
	//	return (Integer) null;
	//}
	
	public ArrayList<Les> getLessen(){
		return deLessen;
	}
	
	public ArrayList<Les> getLessenVanKlas(String klas){
		ArrayList<Les> lessenVanKlas	 = new ArrayList<Les>();
		for (Les l : deLessen){
			if (l.getKlas().getNaam().equals(klas)){
				lessenVanKlas.add(l);
			}
		}return lessenVanKlas;
	}
	
	public ArrayList<Presentie> getPresentie(){
		return dePresenties;
	}
		
	public ArrayList<Presentie> getPresentieDocent(Docent docent){
		ArrayList<Presentie> presentieDocent = new ArrayList<Presentie>();
		for (Presentie p : dePresenties){
			if(p.getLes().getDocent().equals(docent)){
				presentieDocent.add(p);
			}
		}return presentieDocent;
	}
	
public ArrayList<Presentie> getPresentieStudent(String student){
		ArrayList<Presentie> presentieStudent = new ArrayList<Presentie>();
		for (Presentie p : dePresenties){
			if(p.getStudent().equals(student)){
				presentieStudent.add(p);
			}
		}return presentieStudent;
	}
	
	public Student getStudent(String pGebruikersnaam) {
		// used
		Student lGevondenStudent = null;
		
		for (Student lStudent : deStudenten) {
			if (lStudent.getGebruikersnaam().equals(pGebruikersnaam)) {
				lGevondenStudent = lStudent;
				break;
			}
		}
		
		return lGevondenStudent;
	}
	
	public Student getStudent(int pStudentNummer) {
		// used
		Student lGevondenStudent = null;
		
		for (Student lStudent : deStudenten) {
			if (lStudent.getStudentNummer()==(pStudentNummer)) {
				lGevondenStudent = lStudent;
				break;
			}
		}
		
		return lGevondenStudent;
	}

	public boolean inPresentie(Student student, Les les){
		boolean status = false;
		for (Presentie p : dePresenties){
			if (p.getStudent().equals(student)&& p.getLes().equals(les)){
				status = true;
			}
		}
		return status;
	}
	
	public String login(String gebruikersnaam, String wachtwoord) {
		for (Docent d : deDocenten) {
			if (d.getGebruikersnaam().equals(gebruikersnaam)) {
				if (d.komtWachtwoordOvereen(wachtwoord)) {
					return "docent";
				}
			}
		}
		
		for (Student s : deStudenten) {
			if (s.getGebruikersnaam().equals(gebruikersnaam)) {
				if (s.komtWachtwoordOvereen(wachtwoord)) {
					return "student";
				}
			}
		}
		
		return "undefined";
	}
	
	public void nieuwePresentie(boolean isAanwezig, Les les, boolean opnameDoorDocent, Student student){
		Presentie nieuwePresentie = new Presentie(isAanwezig, student, les, opnameDoorDocent);
		dePresenties.add(nieuwePresentie);
		}
	
	public void verwijderPresentie(Student student, Les les){
		for (Presentie p : dePresenties){
			if (p.getLes().equals(les)&& 
					p.getStudent().equals(student)){
				dePresenties.remove(p);
			}
		}
	}
	
	private void vulVakken(ArrayList<Vak> pVakken){
		String csvFile = "././CSV/vakken.csv";
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		
		try{
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null){
				
				String[] element = line.split(csvSplitBy);
				String vakcode = element[0];
				String vaknaam = element[1];
				pVakken.add(new Vak(vakcode, vaknaam));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
		
	private void vulDocenten(ArrayList<Docent> pDocenten) {
		String csvFile = "././CSV/docenten.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
			
		try {
	
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
	
			        // use comma as separator
				String[] element = line.split(cvsSplitBy);
				String gebruikersnaam = element[0].toLowerCase();
				String voornaam = element[1];
				String tussenvoegsel = element[2];
				String achternaam = element[3];
				pDocenten.add(new Docent(voornaam, tussenvoegsel, achternaam, "geheim", gebruikersnaam, 1));
				
				//System.out.println(gebruikersnaam);
		
			}
	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void vulKlassen(ArrayList<Klas> pKlassen) {
		//TICT-SIE-VIA is de klascode die ook in de rooster file voorkomt
		//V1A is de naam van de klas die ook als file naam voor de studenten van die klas wordt gebruikt
		Klas k1 = new Klas("TICT-SIE-V1A", "V1A");
		Klas k2 = new Klas("TICT-SIE-V1B", "V1B");
		Klas k3 = new Klas("TICT-SIE-V1C", "V1C");
		Klas k4 = new Klas("TICT-SIE-V1D", "V1D");
		Klas k5 = new Klas("TICT-SIE-V1E", "V1E");
		Klas k6 = new Klas("TICT-SIE-V1F", "V1F");
		
		pKlassen.add(k1);
		pKlassen.add(k2);
		pKlassen.add(k3);
		pKlassen.add(k4);
		pKlassen.add(k5);
		pKlassen.add(k6);
	}	
	private void vulStudenten(
			ArrayList<Student> pStudenten,
			ArrayList<Klas> pKlassen) {
		Student lStudent;
		for (Klas k : pKlassen) {			
			String csvFile = "././CSV/" + k.getNaam() + ".csv";
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = ",";

			try {

				br = new BufferedReader(new FileReader(csvFile));
				
				while ((line = br.readLine()) != null) {

				    // use comma as separator
					String[] element = line.split(cvsSplitBy);
					String gebruikersnaam = (element[3] + "." + element[2] + element[1] + "@student.hu.nl").toLowerCase();
					// verwijder spaties tussen  dubbele voornamen en tussen bv van der 
					gebruikersnaam = gebruikersnaam.replace(" ","");
					String lStudentNrString  = element[0];
					int lStudentNr = Integer.parseInt(lStudentNrString);
					lStudent = new Student(element[3], element[2], element[1], "geheim", gebruikersnaam, lStudentNr); //Volgorde 3-2-1 = voornaam, tussenvoegsel en achternaam
					pStudenten.add(lStudent);
					k.voegStudentToe(lStudent);
					
					//System.out.println(gebruikersnaam);
			
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}	
			
		}
	}	

	private void vulLessen( ArrayList<Les> lessen){
		String csvFile = "././CSV/rooster.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		try {
			
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
	
			        // use comma as separator
				String[] element = line.split(cvsSplitBy);
				String lesDatum = element[0];
				String lesBeginDatumTijdString = lesDatum+" "+element[1];
				String lesEindDatumTijdString = lesDatum+" "+element[2];
				String lesVakCode = element[3];
				String lesDocentEmail = element[4];
				String lesLocatie = element[5];
				String lesKlasCode = element[6];
				Calendar lesBeginDatumTijdCal = null;
				Calendar lesEindDatumTijdCal = null;
				
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:");
				lesBeginDatumTijdCal.setTime(sdf.parse(lesBeginDatumTijdString));
				lesEindDatumTijdCal.setTime(sdf.parse(lesEindDatumTijdString));
				
				
				Vak lesVak = null;
				Klas lesKlas = null;
				Docent lesDocent = null;
						
				for( Docent d: deDocenten){
					if(d.getGebruikersnaam().equals(lesDocentEmail)){
						lesDocent = d;
					}
				}
				for(Vak v: deVakken){
					if(v.getVakCode().equals(lesVakCode)){
						lesVak = v;
					}
				}
				for(Klas k: deKlassen){
					if(k.getKlasCode().equals(lesKlasCode)){
						lesKlas = k;
					}
				}		
				
				lessen.add(new Les(
						lesBeginDatumTijdCal, 
						lesEindDatumTijdCal, 
						lesLocatie, 
						lesVak, 
						lesKlas, 
						lesDocent));
				
				//System.out.println(gebruikersnaam);
		
			}
	
		}catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	



}




package model;

import java.util.ArrayList;

import model.klas.Klas;
import model.persoon.Docent;
import model.persoon.Student;
import model.presentie.Afwezigheid;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class PrIS {
	private ArrayList<Docent> deDocenten;
	private ArrayList<Student> deStudenten;
	private ArrayList<Klas> deKlassen;
	private ArrayList<Presentie> dePresenties;
	private ArrayList<Vak> deVakken;
	private ArrayList<Les> deLessen;
	private ArrayList<Afwezigheid> deAfwezigheden;

	
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
		deAfwezigheden = new ArrayList<Afwezigheid>();


		// Inladen klassen
		vulKlassen(deKlassen);

		// Inladen studenten in klassen
		vulStudenten(deStudenten, deKlassen);

		// Inladen docenten
		vulDocenten(deDocenten);
		
		vulVakken(deVakken);

		vulLessen(deLessen);
		
	}

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
	
	public ArrayList<Afwezigheid> getAfwezigheden() throws IOException, ParseException{
		FileReader fr = new FileReader("CSV/afwezigheden.csv");
		BufferedReader br = new BufferedReader(fr);
  	ArrayList<Afwezigheid> afwezigBestaand = new ArrayList<Afwezigheid>();
  	String regel = br.readLine();
		while(regel != null){
			String[] values = regel.split(",");
			Date lesBeginTijd = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.UK).parse(values[1]);
			Date lesEindTijd = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.UK).parse(values[2]);
			Afwezigheid b = new Afwezigheid(values[0],lesBeginTijd,lesEindTijd,values[3],values[4],values[5],values[6],values[7]);
			afwezigBestaand.add(b);
			regel = br.readLine();
		}
		br.close();
		return afwezigBestaand;
	}
	
	public ArrayList<Presentie> getPresenties() throws ParseException, IOException {
		FileReader fr = new FileReader("CSV/absenties.csv");
		BufferedReader br = new BufferedReader(fr);
		ArrayList<Presentie> presentieBestaand = new ArrayList<Presentie>();
		String regel = br.readLine();
		while (regel != null) {
			String[] values = regel.split(",");
			Date lesBeginTijd = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.UK).parse(values[1]);
			Date lesEindTijd = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.UK).parse(values[2]);
			Presentie b = new Presentie(values[0], lesBeginTijd, lesEindTijd, values[3], values[4], values[5], values[6]);
			presentieBestaand.add(b);
			regel = br.readLine();
		}
		br.close();
		return presentieBestaand;
	}
	
	public Les getLes(String les){
		for(Les l : deLessen){
			if(l.equals(les)){
				return l;
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
		

	


public ArrayList<Klas> getKlassenDocent(String username){
  	String lGebruikersnaam = username;
  	String csvFile2 = "././CSV/rooster.csv";
  	BufferedReader br2 = null;
  	String line2 = "";
  	ArrayList<Klas> klassen = new ArrayList<Klas>();
  	try {
  		br2 = new BufferedReader(new FileReader(csvFile2));
  		while ((line2 = br2.readLine()) != null) {
  				String[] element = line2.split(",");
  				String docentcsv = element[4];
  				String klascsv = element[6];
  				String klasnaam = klascsv.substring(klascsv.length() - 3);
  				Klas klas = new Klas(klascsv,klasnaam);
  				if(docentcsv.equals(lGebruikersnaam)){
  					if(!klassen.contains(klas)){
  						klassen.add(klas);
  					}
  				}
  
  		}
  	} catch (FileNotFoundException e1) {
  		// TODO Auto-generated catch block
  		e1.printStackTrace();
  	} catch (IOException e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  	}finally {
  		if (br2 != null) {
  			try {
  				br2.close();
  			} catch (IOException e) {
  				e.printStackTrace();
  			}
  		}
  	}	
  	
		Collections.sort(klassen, new Comparator<Klas>() {
	    public int compare(Klas k1, Klas k2) {
	        return k1.getNaam().compareTo(k2.getNaam());
	    }
		});
  	return klassen;
  }
public ArrayList<Student> getStudentenKlassen(ArrayList<Klas> klassen){
	Student lStudent;
	ArrayList <Student> alleStudenten = new ArrayList<Student>();
	for (Klas k : klassen) {			
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
				String klas = k.getNaam();
				// verwijder spaties tussen  dubbele voornamen en tussen bv van der 
				gebruikersnaam = gebruikersnaam.replace(" ","");
				String lStudentNrString  = element[0];
				int lStudentNr = Integer.parseInt(lStudentNrString);
				lStudent = new Student(element[3], element[2], element[1], "geheim", gebruikersnaam, lStudentNr,klas); //Volgorde 3-2-1 = voornaam, tussenvoegsel en achternaam
				alleStudenten.add(lStudent);
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
	return alleStudenten;
	
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
		
		Vak lesVak = null;
		Klas lesKlas = null ;
		Docent lesDocent = null;
		
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
				Calendar lesBeginDatumTijdCal = Calendar.getInstance();
				Calendar lesEindDatumTijdCal = Calendar.getInstance();
				
				Date lesBeginDatum = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(lesBeginDatumTijdString);
				Date lesEindDatum = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(lesEindDatumTijdString);
				

				
				lesBeginDatumTijdCal.setTime(lesBeginDatum);
				lesEindDatumTijdCal.setTime(lesEindDatum);
						
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
			

			}
	
		}catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	



}




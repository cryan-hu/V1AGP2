package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.PrIS;
import model.persoon.Student;
import model.klas.Klas;

import server.Conversation;
import server.Handler;

public class StudentenController implements Handler {
	private PrIS informatieSysteem;

/**
	 * Deze methode haalt eerst de opgestuurde JSON-data op. Daarna worden
	 * de benodigde gegevens uit het domeinmodel gehaald. Deze gegevens worden
	 * dan weer omgezet naar JSON en teruggestuurd naar de Polymer-GUI!
	 * 
	 * @param conversation - alle informatie over het request
	 */
	
	public StudentenController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}
	private void ophalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String lGebruikersnaam = lJsonObjectIn.getString("username");
		Student lStudentZelf = informatieSysteem.getStudent("jorrit.strikwerda@student.hu.nl");
		Student lStudentZelf1 = informatieSysteem.getStudent("boris.blom@student.hu.nl");
		Student lStudentZelf2 = informatieSysteem.getStudent("fabio.boer@student.hu.nl");
		Student lStudentZelf3 = informatieSysteem.getStudent("owen.ashby@student.hu.nl");
		Student lStudentZelf4= informatieSysteem.getStudent("martijn.bakker@student.hu.nl");
		Student lStudentZelf5 = informatieSysteem.getStudent("ties.brouwer@student.hu.nl");
	
		//csv elementen uitlezen
			Klas lKlas1 = informatieSysteem.getKlasVanStudent(lStudentZelf);
			Klas lKlas2 = informatieSysteem.getKlasVanStudent(lStudentZelf1);
			Klas lKlas3 = informatieSysteem.getKlasVanStudent(lStudentZelf2);
			Klas lKlas4 = informatieSysteem.getKlasVanStudent(lStudentZelf3);
			Klas lKlas5 = informatieSysteem.getKlasVanStudent(lStudentZelf4);
			Klas lKlas6 = informatieSysteem.getKlasVanStudent(lStudentZelf5);
			ArrayList <Student> alleStudenten = new ArrayList<Student>();
			ArrayList <Klas> alleKlassen = new ArrayList <Klas>(); 
			alleKlassen.add(lKlas1);
			alleKlassen.add(lKlas2);
			alleKlassen.add(lKlas3);
			alleKlassen.add(lKlas4);
			alleKlassen.add(lKlas5);
			alleKlassen.add(lKlas6);
			
			Student lStudent;
			for (Klas k : alleKlassen) {			
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
		
		JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();						// Uiteindelijk gaat er een array...
		
		
		for (Student lMedeStudent : alleStudenten) {									        // met daarin voor elke medestudent een JSON-object... 
				JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder(); // maak het JsonObject voor een student
				String lLastName = lMedeStudent.getVolledigeAchternaam();
				String klas2 = lMedeStudent.getKlas();
				lJsonObjectBuilderVoorStudent
					.add("id", lMedeStudent.getStudentNummer())																	//vul het JsonObject		     
					.add("firstName", lMedeStudent.getVoornaam())	
					.add("lastName", lLastName)
					.add("klas", klas2);
			  
			  lJsonArrayBuilder.add(lJsonObjectBuilderVoorStudent);													//voeg het JsonObject aan het array toe				     
		}
		
    String lJsonOutStr = lJsonArrayBuilder.build().toString();												// maak er een string van
    System.out.println(lJsonOutStr);
		conversation.sendJSONMessage(lJsonOutStr);																				// string gaat terug naar de Polymer-GUI!
	}

	/**
	 * Deze methode haalt eerst de opgestuurde JSON-data op. Op basis van deze gegevens 
	 * het domeinmodel gewijzigd. Een eventuele errorcode wordt tenslotte
	 * weer (als JSON) teruggestuurd naar de Polymer-GUI!
	 * 
	 * @param conversation - alle informatie over het request
	 */
	private void opslaan(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String lGebruikersnaam = lJsonObjectIn.getString("username");
		Student lStudent = informatieSysteem.getStudent(lGebruikersnaam);
		
		//Het lJsonObjectIn bevat niet enkel strings maar ook een heel (Json) array van Json-objecten.
		// in dat Json-object zijn telkens het studentnummer en een indicatie of de student 
		// tot het zelfde team hoort opgenomen.
		
		//Het Json-array heeft als naam: "groupMembers"
  	JsonArray lGroepMembers_jArray = lJsonObjectIn.getJsonArray("groupMembers"); 
    if (lGroepMembers_jArray != null) { 
    	// bepaal op basis van de huidige tijd een unieke string
    	Calendar lCal = Calendar.getInstance();
    	long lMilliSeconds = lCal.getTimeInMillis();
    	String lGroepId = String.valueOf(lMilliSeconds);   
    	
    	lStudent.setGroepId(lGroepId);
    	for (int i=0;i<lGroepMembers_jArray.size();i++){
    		JsonObject lGroepMember_jsonObj = lGroepMembers_jArray.getJsonObject(i );
    		int lStudentNummer = lGroepMember_jsonObj.getInt("id");
    		boolean lZelfdeGroep = lGroepMember_jsonObj.getBoolean("sameGroup");
    		if (lZelfdeGroep) {
    			Student lGroepStudent = informatieSysteem.getStudent(lStudentNummer);
     		  lGroepStudent.setGroepId(lGroepId);
    		}
    	}
    }
    
  	JsonObjectBuilder lJob =	Json.createObjectBuilder(); 
  	lJob.add("errorcode", 0);
   	//nothing to return use only errorcode to signal: ready!
  	String lJsonOutStr = lJob.build().toString();
 		conversation.sendJSONMessage(lJsonOutStr);					// terug naar de Polymer-GUI!
	}

	@Override
	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/student/studenten/ophalen")) {
			ophalen(conversation);
		}else{
		opslaan(conversation);
		}
	}
}
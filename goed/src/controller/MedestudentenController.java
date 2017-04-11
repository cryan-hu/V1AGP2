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

public class MedestudentenController implements Handler {
	private PrIS informatieSysteem;
	
	/**
	 * De StudentController klasse moet alle student-gerelateerde aanvragen
	 * afhandelen. Methode handle() kijkt welke URI is opgevraagd en laat
	 * dan de juiste methode het werk doen. Je kunt voor elke nieuwe URI
	 * een nieuwe methode schrijven.
	 * 
	 * @param infoSys - het toegangspunt tot het domeinmodel
	 */
	public MedestudentenController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}

	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/student/medestudenten/ophalen")) {
			ophalen(conversation);
		} else if(conversation.getRequestedURI().startsWith("/student/rooster/ophalen")){
			JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
			String lGebruikersnaam = lJsonObjectIn.getString("username");
			String selectedDatum = lJsonObjectIn.getString("maandagVanDeWeek");
			
			System.out.println(selectedDatum);
			Student lStudentZelf = informatieSysteem.getStudent(lGebruikersnaam);
			String  lGroepIdZelf = lStudentZelf.getGroepId();
			
			Klas lKlas = informatieSysteem.getKlasVanStudent(lStudentZelf);		// klas van de student opzoeken
			String klasCode = lKlas.getKlasCode(); // code van de klas

			JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();						// Uiteindelijk gaat er een array...

			JsonObjectBuilder lJsonObjectBuilderVoorRooster = Json.createObjectBuilder(); // maak het JsonObject voor rooster
			
			String csvFile = "././CSV/rooster.csv";
			BufferedReader br = null;
			String line = "";
			try {
				br = new BufferedReader(new FileReader(csvFile));
				while ((line = br.readLine()) != null) {
						String[] element = line.split(",");
						String klas = element[6];
						Date yourDate = new SimpleDateFormat("yyyy-MM-dd").parse(element[0]);
						Calendar c = Calendar.getInstance();
						c.setTime(yourDate);
						int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
						if(klasCode.equals(klas)){
							lJsonObjectBuilderVoorRooster.add("datum", element[0]);
							
							lJsonObjectBuilderVoorRooster.add("startTijd", element[1]);
							lJsonObjectBuilderVoorRooster.add("eindTijd", element[2]);
							lJsonObjectBuilderVoorRooster.add("klas", element[3]);
							lJsonObjectBuilderVoorRooster.add("docent", element[4]);
							lJsonObjectBuilderVoorRooster.add("locatie", element[5]);
							lJsonObjectBuilderVoorRooster.add("klasCode", element[6]);
						lJsonArrayBuilder.add(lJsonObjectBuilderVoorRooster);
						}
						
				}	
			   String lJsonOutStr = lJsonArrayBuilder.build().toString();												// maak er een string van
			   System.out.println(lJsonOutStr);
			   conversation.sendJSONMessage(lJsonOutStr);		
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}	
			
			

			
			
		} else{
			opslaan(conversation);
		}
	}

	private void add(String string, String string2) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Deze methode haalt eerst de opgestuurde JSON-data op. Daarna worden
	 * de benodigde gegevens uit het domeinmodel gehaald. Deze gegevens worden
	 * dan weer omgezet naar JSON en teruggestuurd naar de Polymer-GUI!
	 * 
	 * @param conversation - alle informatie over het request
	 */
	private void ophalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String lGebruikersnaam = lJsonObjectIn.getString("username");
		Student lStudentZelf = informatieSysteem.getStudent(lGebruikersnaam);
		String  lGroepIdZelf = lStudentZelf.getGroepId();
		
		Klas lKlas = informatieSysteem.getKlasVanStudent(lStudentZelf);		// klas van de student opzoeken

    ArrayList<Student> lStudentenVanKlas = lKlas.getStudenten();	// medestudenten opzoeken
		
		JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();						// Uiteindelijk gaat er een array...
		
		for (Student lMedeStudent : lStudentenVanKlas) {									        // met daarin voor elke medestudent een JSON-object... 
			if (lMedeStudent == lStudentZelf) 																			// behalve de student zelf...
				continue;
			else {
				String lGroepIdAnder = lMedeStudent.getGroepId();
				boolean lZelfdeGroep = ((lGroepIdZelf != "") && (lGroepIdAnder==lGroepIdZelf));
				JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder(); // maak het JsonObject voor een student
				String lLastName = lMedeStudent.getVolledigeAchternaam();
				lJsonObjectBuilderVoorStudent
					.add("id", lMedeStudent.getStudentNummer())																	//vul het JsonObject		     
					.add("firstName", lMedeStudent.getVoornaam())	
					.add("lastName", lLastName)				     
				  .add("sameGroup", lZelfdeGroep);	
					System.out.println(lLastName);
			  
			  lJsonArrayBuilder.add(lJsonObjectBuilderVoorStudent);													//voeg het JsonObject aan het array toe				     
			}
		}
		
    String lJsonOutStr = lJsonArrayBuilder.build().toString();												// maak er een string van
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
}
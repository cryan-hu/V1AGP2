package controller;

import java.io.IOException;
import java.text.DateFormat;
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
import model.presentie.Afwezigheid;
import model.presentie.Presentie;
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
		ArrayList<Klas> klassen = informatieSysteem.getKlassenDocent(lGebruikersnaam);
		ArrayList<Student> alleStudenten = informatieSysteem.getStudentenKlassen(klassen);			

		
		JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();						// Uiteindelijk gaat er een array...		
		for (Student student : alleStudenten) {									        // met daarin voor elke medestudent een JSON-object... 
				JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder(); // maak het JsonObject voor een student
				String lLastName = student.getVolledigeAchternaam();
				String klas2 = student.getKlas();
				lJsonObjectBuilderVoorStudent
					.add("id", student.getStudentNummer())																	//vul het JsonObject		     
					.add("firstName", student.getVoornaam())	
					.add("lastName", lLastName)
					.add("klas", klas2);
			  
			  lJsonArrayBuilder.add(lJsonObjectBuilderVoorStudent);													//voeg het JsonObject aan het array toe				     
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
	 * @throws IOException 
	 * @throws ParseException 
	 */
private void ophalenklas(Conversation conversation) throws ParseException, IOException {		
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String sKlas = lJsonObjectIn.getString("klas");
		JsonArray les = lJsonObjectIn.getJsonArray("les");
		String klasnaam = sKlas.substring(sKlas.length() - 3);
		Klas klas = new Klas(sKlas,klasnaam);
		ArrayList<Klas> klassen2 = new ArrayList<Klas>();
		klassen2.add(klas);
		
		ArrayList<Student> alleStudenten = informatieSysteem.getStudentenKlassen(klassen2);		
		
		
		ArrayList<String> lesInfo = new ArrayList<String>();
		for (Object o : les) {
			lesInfo.add(o.toString());
		}

		
		JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();						// Uiteindelijk gaat er een array...		
		for (Student student : alleStudenten) {									        // met daarin voor elke medestudent een JSON-object... 
				JsonObjectBuilder lJsonObjectBuilderVoorStudent = Json.createObjectBuilder(); // maak het JsonObject voor een student
				String lLastName = student.getVolledigeAchternaam();
				String klas2 = student.getKlas();
				Boolean in = inAbsenties(student,lesInfo);
				lJsonObjectBuilderVoorStudent
					.add("id", student.getStudentNummer())																	//vul het JsonObject		     
					.add("firstName", student.getVoornaam())	
					.add("lastName", lLastName)
					.add("klas", klas2)
					.add("sameGroup", in);
			  
			  lJsonArrayBuilder.add(lJsonObjectBuilderVoorStudent);													//voeg het JsonObject aan het array toe				     
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
	 * @throws IOException 
	 * @throws ParseException 
	 */

	private boolean inAbsenties(Student s,ArrayList<String> lesInfo) throws ParseException, IOException{
		DateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm"); 
		ArrayList<Presentie> presenties = informatieSysteem.getPresenties();
		String lesDatum = lesInfo.get(0).replace("\"", "");
		String startDatum = lesInfo.get(1).replace("\"", "");
		String eindDatum = lesInfo.get(2).replace("\"", "");
		String klas = lesInfo.get(3).replace("\"", "");
		String vak = lesInfo.get(4).replace("\"", "");
		String lokaal = lesInfo.get(5).replace("\"", "");
		String docent = lesInfo.get(6).replace("\"", "");
  	String lesBeginDatumTijdString = lesDatum+" "+startDatum;
		String lesEindDatumTijdString = lesDatum+" "+eindDatum;	
		Date lesBeginTijd = format1.parse(lesBeginDatumTijdString);
		Date lesEindTijd = format1.parse(lesEindDatumTijdString);
		Presentie q = new Presentie(s.getGebruikersnaam(), lesBeginTijd, lesEindTijd, vak, klas,lokaal, docent);
		
		Boolean terug = false;
		if(presenties.contains(q)){
			terug = true;
		}
		return terug;

		

	}
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
		}else if(conversation.getRequestedURI().startsWith("/student/studentenklas/ophalen")) {
			try {
				ophalenklas(conversation);
			} catch (ParseException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
		opslaan(conversation);
		}
	}
}
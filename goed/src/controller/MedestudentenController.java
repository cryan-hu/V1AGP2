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
		SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
		if (conversation.getRequestedURI().startsWith("/student/medestudenten/ophalen")) {
			ophalen(conversation);
		} else if(conversation.getRequestedURI().startsWith("/student/rooster/ophalen")){
			JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
			String lGebruikersnaam = lJsonObjectIn.getString("username");
			Date selectedDatum =new Date();
			try {
				selectedDatum = new SimpleDateFormat("dd-MM-yyyy").parse(lJsonObjectIn.getString("maandagVanDeWeek")); //de zondag van de week
			} catch (ParseException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(selectedDatum);
			cal.add(Calendar.DATE, 1);
			Date maandag = cal.getTime();
			cal.setTime(selectedDatum);
			cal.add(Calendar.DATE, 2);
			Date dinsdag = cal.getTime();
			cal.setTime(selectedDatum);
			cal.add(Calendar.DATE, 3);
			Date woensdag = cal.getTime();
			cal.setTime(selectedDatum);
			cal.add(Calendar.DATE, 4);
			Date donderdag = cal.getTime();
			cal.setTime(selectedDatum);
			cal.add(Calendar.DATE, 5);
			Date vrijdag = cal.getTime();
			
			
			
			
			
			Student lStudentZelf = informatieSysteem.getStudent(lGebruikersnaam);
			String  lGroepIdZelf = lStudentZelf.getGroepId();
			
			Klas lKlas = informatieSysteem.getKlasVanStudent(lStudentZelf);		// klas van de student opzoeken
			String klasCode = lKlas.getKlasCode(); // code van de klas

			JsonArrayBuilder lJsonArrayBuilderMa = Json.createArrayBuilder();						// Uiteindelijk gaat er een array...
			JsonArrayBuilder lJsonArrayBuilderDi = Json.createArrayBuilder();						// Uiteindelijk gaat er een array...
			JsonArrayBuilder lJsonArrayBuilderWo = Json.createArrayBuilder();						// Uiteindelijk gaat er een array...
			JsonArrayBuilder lJsonArrayBuilderDo = Json.createArrayBuilder();						// Uiteindelijk gaat er een array...
			JsonArrayBuilder lJsonArrayBuilderVr = Json.createArrayBuilder();						// Uiteindelijk gaat er een array...
			
			JsonObjectBuilder lJsonTotaal = Json.createObjectBuilder();						// Uiteindelijk gaat er een array...

			JsonObjectBuilder lJsonObjectBuilderVoorLesMa = Json.createObjectBuilder(); // maak het JsonObject voor rooster
			JsonObjectBuilder lJsonObjectBuilderVoorLesDi = Json.createObjectBuilder(); // maak het JsonObject voor rooster
			JsonObjectBuilder lJsonObjectBuilderVoorLesWo = Json.createObjectBuilder(); // maak het JsonObject voor rooster
			JsonObjectBuilder lJsonObjectBuilderVoorLesDo = Json.createObjectBuilder(); // maak het JsonObject voor rooster
			JsonObjectBuilder lJsonObjectBuilderVoorLesVr = Json.createObjectBuilder(); // maak het JsonObject voor rooster
			
			JsonObjectBuilder lJsonObjectBuilderLesMa = Json.createObjectBuilder(); // maak het JsonObject voor rooster
			JsonObjectBuilder lJsonObjectBuilderLesDi = Json.createObjectBuilder(); // maak het JsonObject voor rooster
			JsonObjectBuilder lJsonObjectBuilderLesWo = Json.createObjectBuilder(); // maak het JsonObject voor rooster
			JsonObjectBuilder lJsonObjectBuilderLesDo = Json.createObjectBuilder(); // maak het JsonObject voor rooster
			JsonObjectBuilder lJsonObjectBuilderLesVr = Json.createObjectBuilder(); // maak het JsonObject voor rooster
			
			JsonObjectBuilder lJsonMaandag = Json.createObjectBuilder(); // maak het JsonObject voor rooster
			JsonObjectBuilder lJsonDinsdag = Json.createObjectBuilder(); // maak het JsonObject voor rooster
			JsonObjectBuilder lJsonWoensdag = Json.createObjectBuilder(); // maak het JsonObject voor rooster
			JsonObjectBuilder lJsonDonderdag = Json.createObjectBuilder(); // maak het JsonObject voor rooster
			JsonObjectBuilder lJsonVrijdag = Json.createObjectBuilder(); // maak het JsonObject voor rooster
			
			String csvFile = "././CSV/rooster.csv";
			BufferedReader br = null;
			String line = "";
			ArrayList data = new ArrayList<Date>();
			try {
				br = new BufferedReader(new FileReader(csvFile));
				while ((line = br.readLine()) != null) {
						String[] element = line.split(",");
						String klas = element[6];
						Date yourDate = new SimpleDateFormat("yyyy-MM-dd").parse(element[0]);
						if(!data.contains(yourDate)){
							data.add(yourDate);
						}
						if(yourDate.equals(maandag)){
							if(klasCode.equals(klas)){							
								lJsonObjectBuilderVoorLesMa.add("startTijd", element[1]);
								lJsonObjectBuilderVoorLesMa.add("eindTijd", element[2]);
								lJsonObjectBuilderVoorLesMa.add("vak", element[3]);
								lJsonObjectBuilderVoorLesMa.add("emailDocent", element[4]);
								lJsonObjectBuilderVoorLesMa.add("locatie", element[5]);
								lJsonObjectBuilderVoorLesMa.add("klas", element[6]);
							lJsonObjectBuilderLesMa.add("les",lJsonObjectBuilderVoorLesMa);
							lJsonArrayBuilderMa.add(lJsonObjectBuilderLesMa);
							}
						}
						if(yourDate.equals(dinsdag)){
							if(klasCode.equals(klas)){							
								lJsonObjectBuilderVoorLesDi.add("startTijd", element[1]);
								lJsonObjectBuilderVoorLesDi.add("eindTijd", element[2]);
								lJsonObjectBuilderVoorLesDi.add("vak", element[3]);
								lJsonObjectBuilderVoorLesDi.add("emailDocent", element[4]);
								lJsonObjectBuilderVoorLesDi.add("locatie", element[5]);
								lJsonObjectBuilderVoorLesDi.add("klas", element[6]);
							lJsonObjectBuilderLesDi.add("les",lJsonObjectBuilderVoorLesDi);
							lJsonArrayBuilderDi.add(lJsonObjectBuilderLesDi);
							}
						}
						if(yourDate.equals(woensdag)){
							if(klasCode.equals(klas)){							
								lJsonObjectBuilderVoorLesWo.add("startTijd", element[1]);
								lJsonObjectBuilderVoorLesWo.add("eindTijd", element[2]);
								lJsonObjectBuilderVoorLesWo.add("vak", element[3]);
								lJsonObjectBuilderVoorLesWo.add("emailDocent", element[4]);
								lJsonObjectBuilderVoorLesWo.add("locatie", element[5]);
								lJsonObjectBuilderVoorLesWo.add("klas", element[6]);
							lJsonObjectBuilderLesWo.add("les",lJsonObjectBuilderVoorLesWo);
							lJsonArrayBuilderWo.add(lJsonObjectBuilderLesWo);
							}
						}
						if(yourDate.equals(donderdag)){
							if(klasCode.equals(klas)){							
								lJsonObjectBuilderVoorLesDo.add("startTijd", element[1]);
								lJsonObjectBuilderVoorLesDo.add("eindTijd", element[2]);
								lJsonObjectBuilderVoorLesDo.add("vak", element[3]);
								lJsonObjectBuilderVoorLesDo.add("emailDocent", element[4]);
								lJsonObjectBuilderVoorLesDo.add("locatie", element[5]);
								lJsonObjectBuilderVoorLesDo.add("klas", element[6]);
							lJsonObjectBuilderLesDo.add("les",lJsonObjectBuilderVoorLesDo);
							lJsonArrayBuilderDo.add(lJsonObjectBuilderLesDo);
							}
						}
						if(yourDate.equals(vrijdag)){
							if(klasCode.equals(klas)){							
								lJsonObjectBuilderVoorLesVr.add("startTijd", element[1]);
								lJsonObjectBuilderVoorLesVr.add("eindTijd", element[2]);
								lJsonObjectBuilderVoorLesVr.add("vak", element[3]);
								lJsonObjectBuilderVoorLesVr.add("emailDocent", element[4]);
								lJsonObjectBuilderVoorLesVr.add("locatie", element[5]);
								lJsonObjectBuilderVoorLesVr.add("klas", element[6]);
							lJsonObjectBuilderLesVr.add("les",lJsonObjectBuilderVoorLesVr);
							lJsonArrayBuilderVr.add(lJsonObjectBuilderLesVr);
							}
						}
							
						
						
						

						
				}	
				 lJsonMaandag.add("datum", format1.format(maandag));
				 lJsonMaandag.add("lesgegevens", lJsonArrayBuilderMa);
				 
				 lJsonDinsdag.add("datum", format1.format(dinsdag));
				 lJsonDinsdag.add("lesgegevens", lJsonArrayBuilderDi);
				 
				 lJsonWoensdag.add("datum", format1.format(woensdag));
				 lJsonWoensdag.add("lesgegevens", lJsonArrayBuilderWo);
				 
				 lJsonDonderdag.add("datum", format1.format(donderdag));
				 lJsonDonderdag.add("lesgegevens", lJsonArrayBuilderDo);
				 
				 lJsonVrijdag.add("datum", format1.format(vrijdag));
				 lJsonVrijdag.add("lesgegevens", lJsonArrayBuilderVr);
				 
				 lJsonTotaal.add("maandag", lJsonMaandag);
				 lJsonTotaal.add("dinsdag", lJsonDinsdag);
				 lJsonTotaal.add("woensdag", lJsonWoensdag);
				 lJsonTotaal.add("donderdag", lJsonDonderdag);
				 lJsonTotaal.add("vrijdag", lJsonVrijdag);
				 String jsonmaandag = lJsonMaandag.build().toString();	
				 String jsondinsdag = lJsonDinsdag.build().toString();	
			   String lJsonOutStr = lJsonTotaal.build().toString();												// maak er een string van
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

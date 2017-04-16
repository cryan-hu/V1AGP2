package controller;

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

import server.Conversation;
import server.Handler;

public class AfwezigheidsController implements Handler {
	private PrIS informatieSysteem;

/**
	 * Deze methode haalt eerst de opgestuurde JSON-data op. Daarna worden
	 * de benodigde gegevens uit het domeinmodel gehaald. Deze gegevens worden
	 * dan weer omgezet naar JSON en teruggestuurd naar de Polymer-GUI!
	 * 
	 * @param conversation - alle informatie over het request
	 */
	
	public AfwezigheidsController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}
	private void opslaan(Conversation conversation) throws ParseException {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();		
    JsonArray lineItems = lJsonObjectIn.getJsonArray("afwezigheden");
    String useCase = lJsonObjectIn.getString("useCase");
    String username = lJsonObjectIn.getString("username");
    
    Student student = informatieSysteem.getStudent(username);
    
    ArrayList<String> lessenAS = new ArrayList<String>();
    
    for (Object o : lineItems) { 						 // dit moet Afwezigheid o worden , op de juiste manier casten.
    	lessenAS.add(o.toString());
    }
    System.out.println(useCase + " voor " + username);
    System.out.println(lessenAS);
    JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();	
    for(String s : lessenAS){
    	String[] element = s.split("\",\"");
    	String lesDatum = element[0].replace("[","").replace("\"", "");
    	String lesBeginDatumTijdString = lesDatum+" "+element[2];
			String lesEindDatumTijdString = lesDatum+" "+element[3];
			Calendar lesBeginDatumTijdCal = Calendar.getInstance();
			Calendar lesEindDatumTijdCal = Calendar.getInstance();
			
			System.out.println(lesBeginDatumTijdString);
			
			
			Date lesBeginDatum = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(lesBeginDatumTijdString);
			Date lesEindDatum = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(lesEindDatumTijdString);

			
			lesBeginDatumTijdCal.setTime(lesBeginDatum);
			lesEindDatumTijdCal.setTime(lesEindDatum);
    	Afwezigheid afwezigheidStudent = new Afwezigheid(useCase, lesBeginDatumTijdCal, lesEindDatumTijdCal);
    	student.voegAfwezigheidToe(afwezigheidStudent);
    	
    	
    	JsonObjectBuilder lJsonObjectTerug = Json.createObjectBuilder();
    	lJsonObjectTerug
    	.add("Student afgemeld", username);
    	
    	lJsonArrayBuilder.add(lJsonObjectTerug);
    	
    	
    	
    }
    

    String terug = lJsonArrayBuilder.build().toString();	
    System.out.println(terug);
    conversation.sendJSONMessage(terug);			
	}


	private void ophalen(Conversation conversation) {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String lGebruikersnaam = lJsonObjectIn.getString("username");
		Student student = informatieSysteem.getStudent(lGebruikersnaam);
		
		ArrayList<Afwezigheid> afwezighedenStudent = student.getAfwezigheden();
		JsonArrayBuilder lJsonArrayBuilder = Json.createArrayBuilder();	
		
		for(Afwezigheid a : afwezighedenStudent){
			JsonObjectBuilder lJob =	Json.createObjectBuilder(); 
			lJob
			.add("vollegdige afwezigheid", a.getVolledigeAfwezigheid());
			
			lJsonArrayBuilder.add(lJob);
			
		}
  	
  	
  	
  	
   	//nothing to return use only errorcode to signal: ready!
  	String lJsonOutStr = lJsonArrayBuilder.build().toString();
 		conversation.sendJSONMessage(lJsonOutStr);					// terug naar de Polymer-GUI!
	}

	@Override
	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/student/useCase/ophalen")) {
			try {
				opslaan(conversation);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			ophalen(conversation);
		}
	}
}
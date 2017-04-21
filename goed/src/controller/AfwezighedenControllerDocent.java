package controller;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.PrIS;
import model.persoon.Docent;
import model.presentie.Afwezigheid;
import model.presentie.Presentie;
import server.Conversation;
import server.Handler;

public class AfwezighedenControllerDocent implements Handler {
	private PrIS informatieSysteem;

/**
	 * Deze methode haalt eerst de opgestuurde JSON-data op. Daarna worden
	 * de benodigde gegevens uit het domeinmodel gehaald. Deze gegevens worden
	 * dan weer omgezet naar JSON en teruggestuurd naar de Polymer-GUI!
	 * 
	 * @param conversation - alle informatie over het request
	 */
	
	public AfwezighedenControllerDocent(PrIS infoSys) {
		informatieSysteem = infoSys;
	}


	private void ophalenAfwezigheden(Conversation conversation) throws ParseException, IOException {

		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String lGebruikersnaam = lJsonObjectIn.getString("username");
		
		ArrayList<Afwezigheid> afwezigheden = informatieSysteem.getAfwezigheden();
		
		Collections.sort(afwezigheden, new Comparator<Afwezigheid>() {
			public int compare(Afwezigheid m1, Afwezigheid m2) {
				return m1.getBeginTijd().compareTo(m2.getEindTijd());
			}
		});
    
		JsonArrayBuilder lJsonArrayZiektes = Json.createArrayBuilder();
		JsonArrayBuilder lJsonArrayAfwezigheden = Json.createArrayBuilder();
		JsonObjectBuilder lTotaal =	Json.createObjectBuilder();
		
		Docent docentVanStudent = informatieSysteem.getDocent(lGebruikersnaam);
		for(Afwezigheid a : afwezigheden){
			if(a.getDocent().equals(docentVanStudent.getGebruikersnaam())){
				String datum = new SimpleDateFormat("dd-MM-yyyy").format(a.getBeginTijd());
				String startTijd = new SimpleDateFormat("HH:mm").format(a.getBeginTijd());
				String eindTijd = new SimpleDateFormat("HH:mm").format(a.getEindTijd());	
  			if(a.getUseCase().equals("ziekMelden")){
  			JsonObjectBuilder lJob =	Json.createObjectBuilder(); 
    			lJob
    			.add("datum", datum)
    			.add("startTijd", startTijd)
    			.add("eindTijd", eindTijd)
    			.add("vak", a.getVak())
    			.add("klas", a.getKlas())
    			.add("lokaal", a.getLokaal())
    			.add("student", a.getUsername());
    			lJsonArrayZiektes.add(lJob);
  			}
  			if(a.getUseCase().equals("afwezigMelden")){
  			JsonObjectBuilder lJob =	Json.createObjectBuilder(); 
    			lJob
    			.add("datum", datum)
    			.add("startTijd", startTijd)
    			.add("eindTijd", eindTijd)
    			.add("vak", a.getVak())
    			.add("klas", a.getKlas())
    			.add("lokaal", a.getLokaal())
    			.add("student", a.getUsername());
    			lJsonArrayAfwezigheden.add(lJob);
  			}
			}
		}
		lTotaal.add("ziektes", lJsonArrayZiektes);
		lTotaal.add("afwezigheden", lJsonArrayAfwezigheden);
		
   	//nothing to return use only errorcode to signal: ready!
  	String lJsonOutStr = lTotaal.build().toString();
 		conversation.sendJSONMessage(lJsonOutStr);					// terug naar de Polymer-GUI!
  	
		}
	@Override
	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/docent/afwezigStudenten/ophalen")) {
			try {
				ophalenAfwezigheden(conversation);
			} catch (ParseException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
				System.out.println("nope");
		}
	}
	
}
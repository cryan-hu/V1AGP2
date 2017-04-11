package model.presentie;

import model.persoon.Student;

import model.vakGegevens.Les;


public class Presentie {
	private boolean isAanwezig;
	private Student student; 
	private Les les;
	private boolean opnameDoorDocent;
	
	public Presentie(boolean isAanwezig, Student student, Les les, boolean opnameDoorDocent){
		this.isAanwezig = isAanwezig;
		this.student = student; 
		this.les = les;
		this.opnameDoorDocent = opnameDoorDocent;
	}
	
	public boolean isOpnameDoorDocent(){
		return opnameDoorDocent;
	}
	
	public void setOpnameDoorDocent(boolean opnameDoorDocent){
		this.opnameDoorDocent = opnameDoorDocent;
	}
	
	public Les getLes(){
		return les;
	}
	
	public void setLes(Les les){
		this.les = les;
		
	}
	
	public Student getStudent(){
		return student;
	}
	
	public void setStudent(Student student){
		this.student = student;
	}
	
	public boolean isAanwezig(){
		return isAanwezig;
	}
	
	public void setAanwezig(boolean isAanwezig){
		this.isAanwezig = isAanwezig;
	}

	public boolean equals(Object obj){
		boolean gelijk = false;
		if (obj instanceof Presentie){
			Presentie anderePresentie = (Presentie) obj;
			if (anderePresentie.getStudent().equals(this.student)&&
					anderePresentie.getLes().equals(this.les)){
				return gelijk;
			}
		}
		return gelijk;
	}


	public String getVolledigePresentie(){
		String s = isAanwezig +""+ student + les + opnameDoorDocent;
		return s;
	}




}

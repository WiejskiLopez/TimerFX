package application;

public class Event {

	String villageName;
	String villageID;
	String time;
	String targetName;
	String targetID;
	String formation;
	String direction;
	String order;
	String speed;
	String hide;
	
	public Event()
	{
		this.villageName="";
		this.villageID="";
		this.time="";
		this.targetName="";
		this.targetID="";
		this.formation="";
		this.direction="";
		this.order="";
		this.speed="";
		this.hide="";
	}
	
	
	
	public Event(String villageName,String villageID,String time,String targetName,String targetID,String formation,String direction,String order,String speed,String hide)
	{
		this.villageName=villageName;
		this.villageID=villageID;
		this.time=time;
		this.targetName=targetName;
		this.targetID=targetID;	
		this.formation=formation;
		this.direction =direction;
		this.order =order;
		this.speed = speed;
		this.hide=hide;
	}
	public String getVillageName()
	{
		return this.villageName;
	}
	public String getVillageID()
	{
		return this.villageID;
	}
	public String getTime()
	{
		return this.time;
	}
	public String getTargetName()
	{
		return this.targetName;
	}
	public String getTargetID()
	{
		return this.targetID;
	}
	
	public void setVillageName(String villageName)
	{
	  this.villageName = villageName;
	}
	public void setVillageID(String villageID)
	{
		this.villageID=villageID;
	}
	public void setTime(String time)
	{
		this.time=time;
	}
	public void setTargetName(String targetName)
	{
		this.targetName=targetName;
	}
	public void setTargetID(String targetID)
	{
		this.targetID=targetID;
	}
	
	public String getFormation() {
		return formation;
	}



	public void setFormation(String formation) {
		this.formation = formation;
	}



	public String getDirection() {
		return direction;
	}



	public void setDirection(String direction) {
		this.direction = direction;
	}



	public String getOrder() {
		return order;
	}



	public void setOrder(String order) {
		this.order = order;
	}



	public String getHide() {
		return hide;
	}



	public void setHide(String hide) {
		this.hide = hide;
	}
	
	public String getTimeOrder()
	{		
		int timeOrder = Integer.parseInt(this.time.substring(6, 8))
				+ 60 * Integer.parseInt(this.time.substring(3, 5))
				+ 3600 * Integer.parseInt(this.time.substring(0, 2));
		int iorder =Integer.parseInt(this.order);  
		timeOrder =  timeOrder + iorder;
		
		int hoursOrder = timeOrder / 3600;
		
		timeOrder =  timeOrder - 3600*hoursOrder;
		
		int minutsOrder = timeOrder / 60;
		
		int secondsOrder =  timeOrder - 60*minutsOrder;
		
		String sHoursOrder = Integer.toString(hoursOrder);
		if (sHoursOrder.length()==1)
		{
			sHoursOrder = "0"+sHoursOrder;
		}
		
		String sMinutsOrder = Integer.toString(minutsOrder);
		if (sMinutsOrder.length()==1)
		{
			sMinutsOrder = "0"+sMinutsOrder;
		}
		
		String sSecondsOrder = Integer.toString(secondsOrder);
		if (sSecondsOrder.length()==1)
		{
			sSecondsOrder = "0"+sSecondsOrder;
		}
		System.out.println(sHoursOrder+":"+sMinutsOrder+":"+sSecondsOrder);
		return sHoursOrder+":"+sMinutsOrder+":"+sSecondsOrder;
	}
	
	public boolean equal(Event event)
	{
		if(event.getVillageName().compareTo(this.villageName)==0 && 
				event.getVillageID().compareTo(this.villageID)==0 && 
				event.getTime().compareTo(this.time)==0  &&
				event.getTargetName().compareTo(this.targetName)==0  && 
				event.getTargetID().compareTo(this.targetID)==0 &&
				event.getFormation().compareTo(this.formation)==0 &&
				event.getDirection().compareTo(this.direction)==0 &&
				event.getOrder().compareTo(this.order)==0 &&
				event.getSpeed().compareTo(this.speed)==0 &&
				event.getHide().compareTo(this.hide)==0 
				)
		{
			return true;
		}
		return false;
	}



	public String getSpeed() {
		return speed;
	}



	public void setSpeed(String speed) {
		this.speed = speed;
	}
	
}

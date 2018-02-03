package application;

public class TimeEx {
	int hours;
	int minutes;
	int seconds;

	public TimeEx() {
		// TODO Auto-generated constructor stub
		this.hours = 0;
		this.minutes = 0;
		this.seconds = 0;
	}
	
	public TimeEx(String hours,String minutes,String seconds) {
		// TODO Auto-generated constructor stub
		this.hours = Integer.parseInt(hours);
		this.minutes = Integer.parseInt(minutes);
		this.seconds = Integer.parseInt(seconds);
	}
	
	public TimeEx(int hours,int minutes,int seconds) {
		// TODO Auto-generated constructor stub
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}
	//"00:00:67"
	public TimeEx(String time) {
		// TODO Auto-generated constructor stub
		this.hours = Integer.parseInt(time.substring(0, 2));
		this.minutes = Integer.parseInt(time.substring(3, 5));
		this.seconds = Integer.parseInt(time.substring(6, 8));
	}

  // 1
	public int getHours() {
		return hours;
	}
	// "00"  , "01"
	public String getStringHours() {
		String hours =String.valueOf(this.hours);
		if (hours.length() == 1) {
			hours = "0" + hours;
		}
		return hours;
	}


	public void setHours(int hours) {
		this.hours = hours;
	}


	public int getMinutes() {
		return minutes;
	}
	
	public String getStringMinutes() {
		String minutes =String.valueOf(this.minutes);
		if (minutes.length() == 1) {
			minutes = "0" + minutes;
		}
		return minutes;
	}


	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}


	public int getSeconds() {
		return seconds;
	}

	public String getStringSeconds() {
		String seconds =String.valueOf(this.seconds);
		if (seconds.length() == 1) {
			seconds = "0" + seconds;
		}
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public int getTime() {
		return (seconds + 60*minutes + 3600*hours) ;
		
	}
	
	public void setTimeEx(int totalSeconds) {
		this.hours = totalSeconds/ 3600;
		this.minutes = (totalSeconds-this.hours*3600)/60;
		this.seconds = (totalSeconds-(this.hours*3600 + this.minutes*60));
		
	}
	
	 //00:00:00
	public String getStringTime() {
		return getStringHours()+":"+getStringMinutes()+";"+getStringSeconds() ;
		
	}
	
	public void inSecond() {
	  setTimeEx(getTime()+1);	
	}
	
	public void outSecond() {
		int totalSeconds = getTime();
		if (totalSeconds == 0) return;
		setTimeEx(getTime()-1);
	}
	public void setTimeEx(String hours,String minutes,String seconds) {
		// TODO Auto-generated constructor stub
		this.hours = Integer.parseInt(hours);
		this.minutes = Integer.parseInt(minutes);
		this.seconds = Integer.parseInt(seconds);
	}
	public void setTimeEx(int hours,int minutes,int seconds) {
		// TODO Auto-generated constructor stub
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}
}

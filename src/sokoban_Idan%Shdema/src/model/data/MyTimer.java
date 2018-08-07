package model.data;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
/**
 * A clock that count time from the loading of a level.
 * @author Eon
 *
 */
public class MyTimer implements Serializable {
	
	private int sec,min,hour;
	private DateFormat df = new SimpleDateFormat("HH:mm:ss");
	
	
	public MyTimer()
	{
		sec = 0;
		min = 0;
		hour = 0;
	}
	
	public MyTimer(MyTimer t)
	{
		this.sec = t.sec;
		this.min = t.min;
		this.hour = t.hour;
	}
	
	@Override
	public String toString()
	{
		String timeStr = "";
		
		if (hour<10)
			timeStr += ("0" + hour);
		else
			timeStr += ("" + hour);
		if (min<10)
			timeStr += (":0" + min);
		else
			timeStr += (":" + min);
		if (sec<10)
			timeStr += (":0" + sec);
		else
			timeStr += (":" + sec);
		
		return timeStr;
	}
	
	public void printTime(PrintWriter out)
	{
		out.println(toString());
	}
	
	public Time convert()
	{
		long ms;
		try {
			ms = df.parse(this.toString()).getTime();
			return new Time(ms);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//gets and sets
	public int getSec() {
		return sec;
	}

	public void setSec(int sec) {
		this.sec = sec;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}
}

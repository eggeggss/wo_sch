package com.roger.schedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;
@SuppressWarnings("serial")
public class TimStep implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2631139553547721971L;

	public class Break{
		private Date m_start,m_stop;
		private int second;
	}
		
	/**************************************************/
	//宣告
	private String msg_catalog="Time Err=>";
	/**************************************************/
	
	/**************************************************/
	//欄位
	private Date m_start,m_stop;
	private float m_work_time;	//一天有多少工時
	private int m_change_time,m_break_time;
	private Date m_last_stop;
	private ArrayList<Break> break_list;
	/**************************************************/
		
	public TimStep()
	{
		break_list=new ArrayList<Break>();
	}
		
	/**************************************************/
	//計算結束
	//span 秒
	public Date cal_stoptime(Date dt_start,int span)
	{
		long valid = 0, remain;
		Date dt_keep_start,dt_stop = null;
		String fun_cal="cal_stoptime=>";
		
		try {
			remain = span;
			do {
				
				dt_stop = dateadd(dt_start, remain);
		
				if (dt_stop.equals(dt_start))
				break;
				
				//結束日大於下班日,dt_start跳下一天
				if (dt_stop.after(this.m_stop))
				{		
					long diff1=(dt_stop.getTime()-this.m_stop.getTime())/1000;
								
					this.m_start=dt_start=dateadd(this.m_start,86400);
					
					this.m_stop=dateadd(this.m_stop,86400);
					
					remain=diff1;
					
					dt_stop = dateadd(dt_start, remain);
					
					if (dt_stop.equals(dt_start))
					break;
				}
						
				valid = cal_validtime(dt_start, dt_stop);
							
				remain = remain - valid;
				
				dt_start=dt_stop;
				
			} while (remain > 0);
			
			

		} catch (Exception ex) {
             Log.d(msg_catalog,fun_cal+ex.getMessage());
		}

		return dt_stop;
	}
	
	//計算此區間有多少可用工時
	public long cal_validtime(Date dt_start,Date dt_stop)
	{
		for(int i=0;i<=break_list.size();i++)
		{	
			
		}
		
		long diff = dt_stop.getTime() - dt_start.getTime();
				
		return diff;
	}
	/**************************************************/
	
	//印出屬性
	public String printobject(){
		
		return "";
	}
	
	
	/**************************************************/
	//屬性
	public void setworktime(float m_work_time){
		this.m_work_time=m_work_time;
	}
	
	public void setbreaktime(int m_break_time){
		this.m_break_time=m_break_time;
	}
	
	
	
	public Date getstart(){
		return this.m_start;		
	}
	
	public Date getstop(){
		return this.m_stop;
	}
	
	public void setstart(Date m_start){
		this.m_start=m_start;
		//結束時間+休息時間=今天下班時間
		this.m_stop=dateadd(m_start,(long)this.m_work_time * 3600);	
		this.m_last_stop=this.m_start;
	}
	
	public void setbreaklist(Break breakobj)
	{
		break_list.add(breakobj);	
	}
	
	public void setlaststop(Date last_stop)
	{
	   this.m_last_stop=last_stop;
	}
	
	public float getworktime()
	{
		return this.m_work_time;
		
	}
	
	/**************************************************/
	
	
	/**************************************************/
	//util
	//區得當錢日期
	private void getnow(){
		Date myDate = new Date();
		int thisYear = myDate.getYear() + 1900;//thisYear = 2003
	    int thisMonth = myDate.getMonth() + 1;//thisMonth = 5
	    int thisDate = myDate.getDate();//thisDate = 30	
	}
	
	
	private long caltimspan(Date start,Date stop)
	{
		//String input = "2003-05-01";
		//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = start;
		Date d2 = stop;
		long diff = d2.getTime() - d1.getTime();
		
		//diff=(diff/(1000*60*60*24));//day
		diff=(diff/(1000));//second
				
		return diff;
	}
	
	private Date dateadd(Date dt_start,long time)
	{
		Calendar now = Calendar.getInstance();
		now.setTime(dt_start);
		//SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		//out.println("It is now " + formatter.format(now.getTime()));
		now.add(Calendar.SECOND,(int)time);
		
		return now.getTime();
	}
	
	public Date getlaststop()
	{
	   return this.m_last_stop;
	}
	
	/**************************************************/
	

}

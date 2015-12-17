package com.roger.schedule;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import android.util.Log;

@SuppressWarnings("serial")
public class WoClass implements Observer,Comparable<WoClass>, Serializable {
	
	/**宣告**/
	private SimpleDateFormat formatter;
	private static final long serialVersionUID = -8416797530343544327L;
	private String wo_type;
	
	/**欄位**/
	private String m_process_no;
	private float m_seq,m_qty_wip;
	private float m_work_time;//1 pcs
	private float m_total_work_time; //m_qty_wip x m_work_time
	private String m_manufactured;
	private Date m_start,m_stop;
    private Map<Long,History> m_history;
    private long hist_count=0;
    private WoCollect m_observable;
    private String m_product_no;
    private long m_change_time=0;
    
    public static enum WoTypeEnum {
	    G, R, T, M, K 
	} 
    
    public class History implements Serializable
    {
    	public Date dt_start;
    	public Date dt_stop;
    }
     
    /****************************/
    public WoClass(WoTypeEnum wo_type,String process_no,float seq,float qty_wip,float work_time,String manufactured,String product_no)
    {
    	this.setwotype(wo_type);
    	this.setprocessno(process_no);
    	this.setseq(seq);
    	this.setqtywip(qty_wip);
    	this.setworktime(work_time);
    	this.setmanufactured(manufactured);
    	this.m_history=new HashMap<Long,History>();
    	this.m_product_no=product_no;
    	formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    /****************************/
    //取值
    public void setwotype(WoTypeEnum type_enum)
    {
    	String wo_type = null;
    	
    	switch(type_enum)
    	{
    	  case G:
    		  wo_type="G";
    		  break;
    	  case R:
    		  wo_type="R";
    		  break;
    	  case T:
    		  wo_type="T";
    		  break;
    	  case K:
    		  wo_type="K";
    	  default:	
    	}
    	  	
    	this.wo_type=wo_type;
    }
    public void setprocessno(String process_no)
    {
    	this.m_process_no=process_no;
    }  
    public void setseq(float m_seq)
    {
        this.m_seq=m_seq;	
    } 
    public void setqtywip(float qty_wip)
    {
    	this.m_qty_wip=qty_wip;
    }   
    public void setworktime(float work_time)
    {
    	this.m_work_time=work_time;
    	this.m_total_work_time=this.m_work_time * this.m_work_time;
    }
    public void setmanufactured(String manufactured)
    {
    	this.m_manufactured=manufactured;
    }   
    public void sethistory(Date dt_start,Date dt_stop)
    {
    	History hist=new History();
    	hist.dt_start=dt_start;
    	hist.dt_stop=dt_stop;
    	hist_count++;
    	m_history.put(hist_count, hist);  	
    }
    
    public void setobservable(WoCollect observable)
    {
    	this.m_observable=observable;
    	this.m_observable.addObserver(this);  	
    }
    public void setproductno(String product_no)
    {
    	this.m_product_no=product_no;
    }
    
    /****************************/
    //設值
    public String getwotype(){
    	
    	return this.wo_type;
    }
    public String getprocessno()
    {
    	return this.m_process_no;
    }
    public float getseq()
    {
    	return this.m_seq;
    }   
    public float getqtywip()
    {
    	return this.m_qty_wip;
    }  
    public float getworktime()
    {
    	return this.m_work_time;
    }  
    public float gettotalworktime()
    {
    	return this.m_total_work_time;
    }    
    public String getmanufactured()
    {
    	return this.m_manufactured;
    }   
    public Date getstart()
    {
    	return this.m_start;
    } 
    public Date getstop()
    {
    	return this.m_stop;
    }  
    public Map<Long,History> gethistory()
    {
    	return this.m_history;
    }
    public String getproductno()
    {
    	return this.m_product_no;
    }
    
    /****************************/
	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
		TimStep t1=(TimStep) data;
		
		Date dt_start=t1.getlaststop();
		
		Date dt_stop= t1.cal_stoptime(dt_start, (int)this.m_total_work_time);
	    
	    //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   
	    t1.setlaststop(dt_stop);
		
	    hist_count++;
	    
	    History hist=new History();
	    hist.dt_start=this.m_start=dt_start;
	    hist.dt_stop= this.m_stop=dt_stop;
	    this.m_history.put(hist_count, hist);
	    
		Log.d("Debug=>",this.m_process_no+"/"+t1.getworktime()+"/"+this.m_seq+"/"+formatter.format(dt_stop));	    
	}
	
	public String returnwoinfo()
	{
		//SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
		
		this.m_start=(this.m_start ==null)? new Date():this.m_start;
		this.m_stop=(this.m_stop ==null)? new Date():this.m_stop;
		
		String result = null;
		
		try{
			result=String.format("seq:%d \r\n product_no:%s \r\n 數量:%d \r\n 工時:%d \r\n "+
		        "製程:%s \r\n 換線:%d \r\n 起始:%s \r\n 結束:%s", 
				(int)this.m_seq,
				this.m_product_no,
				(int)this.m_qty_wip,
				(int)this.m_work_time,
				this.m_manufactured,
				this.m_change_time,
				formatter.format(this.m_start),
				formatter.format(this.m_stop)
				);	
		}catch(Exception ex)
		{
			Log.d("Debug",ex.getMessage());
		}
		return result;
		//"seq:1.00 \r\n product_no:CIZAS9117091M \r\n 數量:1500 \r\n 工時:19500.00 \r\n 製程:DIP \r\n 換線:15 \r\n 起始:2015-11-05 08:00:00 \r\n 結束: 2015-11-05 14:15:00"		
	}
	
	@Override
	public int compareTo(WoClass another) {
		// TODO Auto-generated method stub
		return (this.m_seq>another.m_seq)? 1:(this.m_seq<another.m_seq)? -1:0;
	}
}





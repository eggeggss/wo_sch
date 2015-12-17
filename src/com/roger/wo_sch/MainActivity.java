package com.roger.wo_sch;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Text;

import com.roger.schedule.DownloadWo;
import com.roger.schedule.DownloadWo.IDownload;
import com.roger.schedule.DownloadWo.Wo_struct;
import com.roger.schedule.NormalWo;
import com.roger.schedule.TimStep;
import com.roger.schedule.WoClass;
import com.roger.schedule.WoCollect;
import com.roger.schedule.WoClass.WoTypeEnum;
import com.roger.schedule.WoCollect.Iwocollect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements Iwocollect,IDownload,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6236264773900226385L;
	Date date = null;
	String[] wo_arr;   
	private WoCollect wo_collect;
	private int wo_count=0,wo_all_count=0;
	private static Button b_execute;
	private static TextView t_status;
	private static ProgressBar myProgressBar;
	private static MyHandle myHandle;
    int myProgress = 0;
    private Wo_struct[] wo_struct;
	  
    @SuppressWarnings("unused")
	public class MyHandle extends Handler  implements Serializable {
		private static final long serialVersionUID = 8911259443275564491L;

		@Override
        public void handleMessage(Message msg) {
            String progress="";
            
			if (msg.arg1==0)
			{ myProgress++;
              myProgressBar.setProgress(myProgress);
              int max=myProgressBar.getMax();
              //progress=(myProgress<=max)? String.valueOf(myProgress)+"/"+String.valueOf(max):"完成"; 
              t_status.setText(String.valueOf(myProgress)+"/"+String.valueOf(max));
			}
            
		    if (msg.arg1==1)
		    {
		    	progress="完成";
		    	b_execute.setEnabled(true);
		        t_status.setText(progress);
		    }
		 
        }
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);           
     
        myProgressBar = (ProgressBar)findViewById(R.id.progressbar);
        
        t_status=(TextView)findViewById(R.id.t_status);
       
        Button b_download=(Button)findViewById(R.id.b_download);
        
        b_download.setEnabled(true);
        
        b_download.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				wo_collect=null;
				DownloadWo dow=new DownloadWo();
			    dow.onCreate(MainActivity.this);
			    
				//Toast.makeText(MainActivity.this, "hi", Toast.LENGTH_LONG).show();
			}});        
        
        Button b_explain=(Button)findViewById(R.id.b_expand);
        
        b_explain.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent();
				i.setClass(MainActivity.this, explain_layout.class);
				startActivity(i);
			}});
            
        b_execute=(Button)findViewById(R.id.b_execute);
        
        b_execute.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
							
				// TODO Auto-generated method stub			
				if (wo_collect==null)
			       wo_collect=initWoCollect();
			    
				TimStep t1=initTimStep();
				wo_collect.notifywo(t1);
							
				wo_all_count=0;
				wo_count=myProgress=0;
				
				myProgressBar.setProgress(myProgress);
			        
			    myHandle=new MyHandle();
			    
				myProgressBar.setMax(wo_collect.obj_list.size());
				
                b_execute.setEnabled(false);
				               
				  new Thread(new Runnable(){

			            Message msg;//myHandle.obtainMessage();
			            
			            @Override
			            public void run() {
			                while(myProgress<wo_collect.obj_list.size()){
			                try{
			                	    msg=myHandle.obtainMessage();  
			                	    msg.arg1=0;
			                        myHandle.sendMessage(msg);
			                        Thread.sleep(10);
			                        			                        
			                    }catch(Throwable t){
			                    }          
			                }
			                msg=myHandle.obtainMessage();  
	                	    msg.arg1=1;
	                        myHandle.sendMessage(msg);		            
			            }
			            
			        }).start();
				
				
			}});
        
        
        Button b_sch=(Button)findViewById(R.id.button1);
        b_sch.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent();
				
				Bundle bundle=new Bundle();
				
				if (wo_collect==null)
					wo_collect=initWoCollect();
				
				WoCollect wo_collect2=wo_collect;//(wo_collect==null)? initWoCollect():RegisterWoClass(wo_collect.obj_list);//wo_collect;//initWoCollect();
							
				bundle.putSerializable("WoCollect", wo_collect2);
				 
				i.putExtras(bundle);
				 
				//i.putExtra("WoCollect", wo_collect);
				i.setClass(MainActivity.this, layout2.class);
				//startActivity(i);
				startActivityForResult(i,1);
			}});       
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public TimStep initTimStep()
    {
    	SimpleDateFormat simple = new java.text.SimpleDateFormat();
 		simple.applyPattern("yyyy-MM-dd HH:mm:ss");
 		
 		Date now=new Date();
 		
 		Calendar cal = Calendar.getInstance();
         cal.setTime(new Date());
         cal.set(Calendar.HOUR_OF_DAY, 0);
         cal.set(Calendar.MINUTE, 0);
         cal.set(Calendar.SECOND, 0);
         cal.set(Calendar.MILLISECOND, 0);
 		  
 		String ls_now=simple.format(cal.getTime());
 			
 		try {
 			date=simple.parse(ls_now);
 			//date = simple.parse("2015-11-6 08:00:00");
 		} catch (ParseException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}			
 		
 		final TimStep t1=new TimStep();
 		
 		t1.setworktime(8);// 8hours
 		t1.setstart(date);
 	    t1.setbreaktime(0); // 0 breaktime
    	
 	    return t1;
    }
    
    public WoCollect RegisterWoClass(List<WoClass> obj_list)
    {
    	WoCollect wo_collect =new WoCollect(MainActivity.this);
    	
    	Iterator<WoClass> iter=obj_list.iterator();
    	
    	while(iter.hasNext())
    	{
    		WoClass woclass=iter.next();
    		
    		woclass.setobservable(wo_collect);   		
    	}
    	
    	return wo_collect;
    }
    
    
    public WoCollect initWoCollect()
    {
    	Wo_struct[] wo=this.wo_struct;
    	WoCollect wo_collect=new WoCollect(MainActivity.this);
    	if( (wo!=null) && wo.length>0)
    	{
    		
    		 for(int i=0;i<wo.length;i++)
    		 {
    			 WoClass normalwo=new WoClass(WoTypeEnum.G, wo[i].process_no,wo[i].seq, wo[i].qty_wip, wo[i].work_time, wo[i].manufactured,wo[i].product_no);	
    	    		
    	         normalwo.setobservable(wo_collect);	  			 
    			 
    		 }
  	 	    
    	 	        
    	}//Toast.makeText(MainActivity.this,this.wo_struct.length,Toast.LENGTH_LONG).show();
    	else{
 	         
 	    
 	         WoClass normalwo=new WoClass(WoTypeEnum.G, "C-G5040391-1-1-1",
 	    		2, 553, 13, "DIP","P1");	   
 	    
 	         normalwo.setobservable(wo_collect);
 	    
 	         normalwo=new NormalWo(WoTypeEnum.G, "C-G5040205-1-1-1",
 	    		1, 600, 13, "DIP","P2");
 	    
 	         normalwo.setobservable(wo_collect);
 	    
 	         normalwo=new NormalWo(WoTypeEnum.G, "C-G5040205-1-2-1",
	    		3, 600, 13, "DIP","P3");
	    
	         normalwo.setobservable(wo_collect);
 	    
	         normalwo=new NormalWo(WoTypeEnum.G, "C-G5040205-1-3-1",
	    		4, 600, 13, "DIP","P4");
	    
	         normalwo.setobservable(wo_collect);
	    
	         normalwo=new NormalWo(WoTypeEnum.G, "C-G5040205-1-4-1",
	    		5, 600, 13, "DIP","P5");
	    
	         normalwo.setobservable(wo_collect);
    	}
 	    return wo_collect;
    	
    }

	@Override
	public void complete_count(int count,int all) {
		// TODO Auto-generated method stub
		String progress="";
		wo_count=count;
		wo_all_count=all;
				
		progress=(count<=all)? String.valueOf(count)+"/"+String.valueOf(all):"完成";
		
		t_status.setText(progress);
		//Toast.makeText(MainActivity.this,progress, Toast.LENGTH_SHORT).show();
	}

	

	
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent intent){
	        super.onActivityResult(requestCode,resultCode,intent);
	         //判断结果码是否与回传的结果码相同
	        if(resultCode == 1){
	           //获取回传数据
	        	//String ls_result=intent.getStringExtra("hello");
	        	this.wo_collect = (WoCollect) intent.getSerializableExtra("WoCollect");
	        	//Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
	            //String name = intent.getStringExtra("name");
	           
	           
	         }
	        
	 }

	@Override
	public void returnWo(Wo_struct[] wo_struct) {
		// TODO Auto-generated method stub
		this.wo_struct=wo_struct;
		
	}

	

	
}

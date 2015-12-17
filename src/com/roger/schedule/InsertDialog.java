package com.roger.schedule;

import com.roger.schedule.WoClass.WoTypeEnum;
import com.roger.wo_sch.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InsertDialog extends WoInfoDialog {

	public InsertDialog(Context context, WoClass wo, int arg) {
		super(context, wo, arg);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void createDialog(){
				
	}

	@Override
	public void setBuilderView()
	{
		 final AlertDialog.Builder builder2=new AlertDialog.Builder(context);
			
			builder.setView(v)		   
	        
	        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                //LoginDialogFragment.this.getDialog().cancel();
	            }
	        }).setNeutralButton("Insert", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	              
	             String ls_process_no,ls_manufactured,ls_product_no;
	             int seq;
	             int qty_wip;
	             int work_time;
	             
	             EditText t=(EditText) v.findViewById(R.id.process_no);
	         	   
	     	     ls_process_no=t.getText().toString();
	     	        	         	    
	       	     EditText t_seq=(EditText) v.findViewById(R.id.seq);
	       	     
	       	     seq=Integer.parseInt(t_seq.getText().toString());
	        
	       	     EditText t_product_no=(EditText) v.findViewById(R.id.product_no);
	       	    	    
	       	     ls_product_no=t_product_no.getText().toString();
	       	        	  
	     	     EditText t_manufacture=(EditText) v.findViewById(R.id.manufacture);
	         	    
	     	     ls_manufactured=t_manufacture.getText().toString();
	     	         	 
	     	     EditText t_qty=(EditText) v.findViewById(R.id.qty_wip);
	     	    
	     	     qty_wip=Integer.parseInt(t_qty.getText().toString());
	     	    
	     	     EditText t_work=(EditText) v.findViewById(R.id.work_time);
	     	    
	     	     work_time=Integer.parseInt(t_work.getText().toString());
	     	         	    
	          	 WoClass normalwo=new WoClass(WoTypeEnum.G, ls_process_no,
	          	    		seq, qty_wip, work_time, ls_manufactured,ls_product_no);
	            	   	            	
	             ia.insertitem(normalwo);
	               
	            }
	            
	        });  		
	}
	
}

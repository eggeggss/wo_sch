package com.roger.schedule;

import com.roger.schedule.WoClass.WoTypeEnum;
import com.roger.wo_sch.R;

import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateDialog extends WoInfoDialog {

	private WoClass wo_new;	
	 String ls_process_no;
	public UpdateDialog(Context context, WoClass wo, int arg) {
		super(context, wo, arg);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void createDialog(){
	   
		EditText t=(EditText) v.findViewById(R.id.process_no);
	    	
	    t.setText(String.format("%s", wo.getprocessno()));
	    
  	    EditText t_seq=(EditText) v.findViewById(R.id.seq);
  	    
  	    t_seq.setText(String.format("%d", (int)wo.getseq()));
  	    
  	    EditText t_product_no=(EditText) v.findViewById(R.id.product_no);
  	    	    
	    t_product_no.setText(String.format("%s", wo.getproductno()));
	    
	    EditText t_manufacture=(EditText) v.findViewById(R.id.manufacture);
    	    
	    t_manufacture.setText(String.format("%s", wo.getmanufactured()));
	    
	    EditText t_qty=(EditText) v.findViewById(R.id.qty_wip);
	    
	    t_qty.setText(String.format("%d", (int)wo.getqtywip()));     
	    	    
	    EditText t_work=(EditText) v.findViewById(R.id.work_time);
	    
	    t_work.setText(String.format("%d", (int)wo.getworktime()));    
	    //wo_new=super.wo;
	    /*
	     wo_new=new WoClass(WoTypeEnum.G,
	    		 ls_process_no,
	    		 seq,qty_wip,work_time,
	    		 ls_manufactured,
	    		 ls_product_no);
	     */
	}
	
	@Override
	public void setBuilderView()
	{
		 final AlertDialog.Builder builder2=new AlertDialog.Builder(context);
		 		 
		 builder2.setCancelable(false);
		 
			builder.setView(v)		   
	        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int id) {
	         	          	  
	         	   builder2.setTitle("Information")
	                .setMessage("確定刪除?")
	                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                 	    ia.deleteitem(arg);	                   	 
	                    }
	                })
	                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                   
	                       //adapter.notifyDataSetChanged();
	                    }
	                }).show();	  
	            }
	        })
	        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                //LoginDialogFragment.this.getDialog().cancel();
	            }
	        }).setNeutralButton("Update", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                //LoginDialogFragment.this.getDialog().cancel();
	            	
	            	EditText t=(EditText) v.findViewById(R.id.process_no);
	            	String ls_process_no=t.getText().toString(); 
	     	
	            	EditText t_manufacture=(EditText) v.findViewById(R.id.manufacture);
	           	 
	         	    String ls_manufactured=t_manufacture.getText().toString();
	         	          	    
	         	    EditText t_product_no=(EditText) v.findViewById(R.id.product_no);
	         	    
	         	    String ls_product_no=t_product_no.getText().toString();
	         	    
	         	    EditText t_seq=(EditText) v.findViewById(R.id.seq);
	         	    
	         	    int seq=Integer.parseInt(t_seq.getText().toString());
	         	    
	         	    EditText t_qty=(EditText) v.findViewById(R.id.qty_wip);
	       	       	    
	         	    float qty_wip=Float.parseFloat((t_qty.getText().toString()));
	         	    
	         	    EditText t_work=(EditText) v.findViewById(R.id.work_time);       	    
	         	    
	         	    float work_time=Float.parseFloat((t_work.getText().toString()));
	         	    
	         	    ls_process_no=(ls_process_no.isEmpty())? "":ls_process_no;
	         	    ls_product_no= (ls_product_no.isEmpty())? "":ls_product_no; 
	         	    ls_manufactured=(ls_manufactured.isEmpty())? "":ls_manufactured;
	         	    
	         	    wo.setprocessno(ls_process_no);
	         	    wo.setproductno(ls_product_no);
	         	    wo.setmanufactured(ls_manufactured);
	         	    wo.setseq(seq);
	         	    wo.setqtywip(qty_wip);
	         	    wo.setworktime(work_time);	
	            	ia.updateitem(arg, wo);            	 
	            }
	        });  		
	}
	
}

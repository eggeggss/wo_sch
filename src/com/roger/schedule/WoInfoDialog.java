package com.roger.schedule;



import java.lang.reflect.Field;

import com.roger.wo_sch.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.roger.wo_sch.layout2;

public class WoInfoDialog {
    
	public interface IAdapter{
		void deleteitem(int arg);	
		void insertitem(WoClass wo);
		void updateitem(int arg,WoClass wo);
	}
	
	protected AlertDialog.Builder builder;
	protected AlertDialog dialog;
	protected Context context;
	protected WoClass wo;
	protected int arg;
	protected View v;
	protected  IAdapter ia;
	 
	public WoInfoDialog(Context context,WoClass wo, int arg)
	{
		this.context=context;
		this.wo=wo;
		this.arg=arg;	
		ia= (layout2)context;
	    builder = new AlertDialog.Builder(context);
	    LayoutInflater inflater = (LayoutInflater)context.getSystemService
	    	      (Context.LAYOUT_INFLATER_SERVICE);
	    v=inflater.inflate(R.layout.wo_info, null);
	    builder.setCancelable(false);
	}
	
	public void show()
	{
		dialog= builder.create();
		dialog.show();	
	}
	
	public void setBuilderView()
	{	
        
		  
	}
	
	public void createDialog()  
	{	

	        	           
	    //dialog= builder.create();
	}  	
}

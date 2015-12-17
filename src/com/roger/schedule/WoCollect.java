package com.roger.schedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;

import com.roger.schedule.DownloadWo.Wo_struct;
import com.roger.wo_sch.MainActivity;

import android.content.Context;

@SuppressWarnings("serial")
public class WoCollect extends Observable implements Serializable{
	
	
	private static final long serialVersionUID = -7043295297546189441L;
	private Wo_struct wo_struct;
	
	public List<WoClass> obj_list=null;
	public MainActivity main;
	
	public interface Iwocollect{
		void complete_count(int count,int all);
	}
	
	
	public WoCollect(Context context)
	{
		obj_list=new ArrayList<WoClass>();	
		main=(MainActivity) context;
		//Iwocollect iwocollect=(Iwocollect) context.getApplicationContext();
	}
	
    @Override
    public void addObserver(Observer observer)
	{
    	obj_list.add((WoClass)observer);
    	//super.addObserver(observer);
	}
	
    @Override
    public void deleteObserver(Observer observer)
    {
    	obj_list.remove((WoClass)observer);
    	//super.deleteObserver(observer);
    }
    
  
    public void deleteObserverAll()
    {
    	obj_list.removeAll(obj_list);
    }
    
    
    public void notifywo(TimStep t1)
    {
    	super.setChanged();
    	notifyObservers(t1);  	
    }
    
    public String[] returnprocessno()
    {
    	Collections.sort(obj_list);
    	
    	ListIterator<WoClass> iter=obj_list.listIterator();
    	
    	ArrayList<String> list=new ArrayList<String>();
    	
    	WoClass wo_class;
    	
    	while(iter.hasNext())
    	{
    		wo_class=iter.next();
    		list.add(wo_class.getprocessno());
    	}  		
    	return list.toArray(new String[list.size()]);
    }
    
    public String[] returnwoinfo()
    {
        Collections.sort(obj_list);
    	
    	ListIterator<WoClass> iter=obj_list.listIterator();
    	
    	ArrayList<String> list=new ArrayList<String>();
    	
    	WoClass wo_class;
    	
    	while(iter.hasNext())
    	{
    		wo_class=iter.next();
    		list.add(wo_class.returnwoinfo());
    	}  	
    	
    	return list.toArray(new String[list.size()]);
    }
    
    @Override
    public void notifyObservers(Object obj)
    {
    	Collections.sort(obj_list);
    	
    	for(int i=0;i<obj_list.size();i++)
    	{
    		obj_list.get(i).update(this, (TimStep)obj);
    		main.complete_count(i,obj_list.size());
    	}
    }
    
}

package com.roger.schedule;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;
import com.roger.wo_sch.MainActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.readystatesoftware.viewbadger.BadgeView;

public class DownloadWo {


	public interface IDownload{
		 void returnWo(Wo_struct[] wo_struct);	
	}
	
	private ProgressDialog progressDialog;
	private ProgressDialog progressDialogerror;	
	private Handler handler;
	

	public class FireMissilesDialog {  
		
	    public Dialog onCreateDialog(Context context) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        builder.setMessage("下載完成")
	               .setPositiveButton("確定", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // FIRE ZE MISSILES!
	                   }
	               })
	               ;
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}
	
	
	public class MyAsyncTask extends AsyncTask<String, Integer, String> {
		private Exception exceptionTo;
        
		private Context context;
		
		public MyAsyncTask(Context context)
		{
			this.context=context;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// 在onPreExecute()中我們讓ProgressDialog顯示出來
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			String Result = null;
			try {
				Result = getData(params[0]);
			} catch (Exception e) {
				exceptionTo = e;
			}
			return Result;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			// super.onPostExecute(result);

			if (exceptionTo != null) {
				progressDialog.dismiss();
				progressDialogerror.setMessage(exceptionTo.getMessage());
				progressDialogerror.show();

			} else {

				//textView2.setText("pk");
				progressDialog.dismiss();
				FireMissilesDialog f1=new FireMissilesDialog();			
				f1.onCreateDialog(context).show();
			}
			// 使ProgressDialog框消失
		}
	}

	private class AsynTask implements Runnable {
		String mText;

		public AsynTask(String text) {
			mText = text;
		}

		public void run() {
			//textView2.setText(mText);
			// Toast.makeText(GcmIntentService.this, mText,
			// Toast.LENGTH_SHORT).show();
		}
	}

	
	@SuppressWarnings("unused")
	public class Wo_struct implements Serializable{
		private static final long serialVersionUID = 8911259443275564491L;
		public String wo_type;
		public String process_no;
		public int seq;
		public int qty_wip;
		public int work_time;
		public String manufactured;
		public String product_no;	
	}
	
	
	public class Video{
		private String type;
		private String name;
		private String link;
		private String link_type;
	}


	public class Info 
	{
		public Info() {
		}
        private Wo_struct wo[];
		private Video video[];
	}

	public String getData(String url) throws Exception {

		String str = " ";
		HttpPost httppost = new HttpPost(url); // 宣告要使用的頁面
		List<NameValuePair> params = new ArrayList<NameValuePair>(); // 宣告參數清單

		params.add(new BasicNameValuePair("id", "wo_sch")); // 加入參數定義
		params.add(new BasicNameValuePair("catelog", "download"));
		params.add(new BasicNameValuePair("line_name", "ASSYA"));
		//http://eggeggss.ddns.net/appservice2/Default.aspx?id=bubbles&catelog=Video
		//http://eggeggss.ddns.net/appservice2/Default.aspx?id=wo_sch&catelog=download&line_name=ASSYA
		Wo_struct[] data=null;
		//Video[] data = null;
		
		try 
		{
			int timeoutConnection = 3000;
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); // 設定參數和編碼
			HttpParams h1 = httppost.getParams();
			
			HttpConnectionParams.setConnectionTimeout(h1, timeoutConnection);
			httppost.setParams(h1);
			HttpResponse res = new DefaultHttpClient().execute(httppost); // 執行並接收Server回傳的Page值

			if (res.getStatusLine().getStatusCode() == 200) // 判斷回傳的狀態是不是200
				str = EntityUtils.toString(res.getEntity()); // 取出Server回傳的Code：「Frank您好！」

			Gson gson = new Gson();
			data = gson.fromJson(str, Wo_struct[].class);
			
			id.returnWo(data);
			// Info info = gson.fromJson(str, Info.class);

		}
		catch (Exception e) {
			throw new Exception(e);	
		}
		return String.valueOf(data[0].process_no);// str;

	}

	IDownload id;
	
	public void onCreate(Context context) {
		
		id=(MainActivity)context;
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("提示信息");
		progressDialog.setMessage("正在下載中，請稍後......");
		// 設置setCancelable(false); 表示我們不能取消這個彈出框，等下載完成之後再讓彈出框消失
		progressDialog.setCancelable(false);
		// 設置ProgressDialog樣式為圓圈的形式
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		progressDialogerror = new ProgressDialog(context);
		progressDialogerror.setTitle("資料下載失敗,請洽roger #14389");
		progressDialogerror.setMessage("正在下載中，請稍後......");
		// 設置setCancelable(false); 表示我們不能取消這個彈出框，等下載完成之後再讓彈出框消失
		progressDialogerror.setCancelable(true);
		// 設置ProgressDialog樣式為圓圈的形式
		progressDialogerror.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		handler = new Handler();

		MyAsyncTask tes = new MyAsyncTask(context);
		tes.execute("http://eggeggss.ddns.net/appservice2/Default.aspx");			
	}
}

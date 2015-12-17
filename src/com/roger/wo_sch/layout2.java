package com.roger.wo_sch;

import java.util.ArrayList;
import java.util.List;

import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortListView.DragListener;
import com.roger.schedule.InsertDialog;
import com.roger.schedule.UpdateDialog;
import com.roger.schedule.WoClass;
import com.roger.schedule.WoCollect;
import com.roger.schedule.WoInfoDialog;
import com.roger.schedule.WoInfoDialog.IAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class layout2 extends ListActivity implements IAdapter {
	private AlertDialog.Builder builder;
	private JazzAdapter adapter;

	private ArrayList<JazzArtist> mArtists;

	private String[] mArtistNames;
	private String[] mArtistAlbums;

	private WoCollect wo_collect = null;

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			JazzArtist item = adapter.getItem(from);
			item.woclass.setseq(to);
			adapter.remove(item);
			adapter.insert(item, to);
		}
	};

	private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
		@Override
		public void remove(final int which1) {

		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			wo_collect.deleteObserverAll();

			for (int i = 0; i < adapter.getCount(); i++) {
				JazzArtist ja = adapter.getItem(i);
				WoClass wo_class = ja.woclass;
				wo_class.setseq(i + 1);
				wo_collect.addObserver(wo_class);
			}

			Intent result_intent = new Intent();
			Bundle bundle = new Bundle();

			bundle.putSerializable("WoCollect", wo_collect);

			result_intent.putExtras(bundle);

			setResult(1, result_intent);
			// return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		wo_collect = (WoCollect) this.getIntent().getSerializableExtra(
				"WoCollect");

		String[] wo_array = wo_collect.returnprocessno();
		String[] woinfo_array = wo_collect.returnwoinfo();

		setContentView(R.layout.hetero_main);

		Button b_new = (Button) findViewById(R.id.b_new);

		b_new.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				WoInfoDialog wo_dia = new InsertDialog(layout2.this, null, 0);
				wo_dia.createDialog();
				wo_dia.setBuilderView();
				wo_dia.show();

			}
		});

		DragSortListView lv = (DragSortListView) getListView();

		lv.setDropListener(onDrop);
		lv.setRemoveListener(onRemove);

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {

				WoInfoDialog wo_dia = new UpdateDialog(layout2.this, adapter
						.getItem(arg2).woclass, arg2);
				wo_dia.createDialog();
				wo_dia.setBuilderView();
				wo_dia.show();
				/*
				 * builder.setTitle("Information") .setMessage("確定刪除?")
				 * .setPositiveButton("OK", new
				 * DialogInterface.OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface dialog, int
				 * which) {
				 * 
				 * adapter.remove(adapter.getItem(arg2)); } })
				 * .setNegativeButton("Cancel", new
				 * DialogInterface.OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface dialog, int
				 * which) {
				 * 
				 * adapter.notifyDataSetChanged(); } }).show();
				 */
				// TODO Auto-generated method stub
				return false;
			}
		});

		mArtistNames = wo_array;
		mArtistAlbums = woinfo_array;

		mArtists = new ArrayList<JazzArtist>();
		JazzArtist ja;
		for (int i = 0; i < mArtistNames.length; ++i) {
			ja = new JazzArtist();
			ja.woclass = wo_collect.obj_list.get(i);
			ja.name = mArtistNames[i];
			if (i < mArtistAlbums.length) {
				ja.albums = mArtistAlbums[i];
			} else {
				ja.albums = "No albums listed";
			}
			mArtists.add(ja);
		}

		adapter = new JazzAdapter(mArtists);
		setListAdapter(adapter);
	}

	private class JazzArtist {
		public String name;
		public String albums;
		public WoClass woclass;

		@Override
		public String toString() {
			return name;
		}
	}

	private class ViewHolder {
		public TextView albumsView;
	}

	private class JazzAdapter extends ArrayAdapter<JazzArtist> {

		public JazzAdapter(List<JazzArtist> artists) {
			super(layout2.this, R.layout.jazz_artist_list_item,
					R.id.artist_name_textview, artists);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);

			if (v != convertView && v != null) {
				ViewHolder holder = new ViewHolder();

				TextView tv = (TextView) v
						.findViewById(R.id.artist_albums_textview);
				holder.albumsView = tv;

				v.setTag(holder);
			}

			ViewHolder holder = (ViewHolder) v.getTag();
			String albums = getItem(position).albums;

			holder.albumsView.setText(albums);

			return v;
		}
	}

	@Override
	public void deleteitem(int arg) {
		// TODO Auto-generated method stub
		adapter.remove(adapter.getItem(arg));
	}

	@Override
	public void insertitem(WoClass wo) {
		// TODO Auto-generated method stub
		JazzArtist ai=new JazzArtist();
		ai.woclass=wo;
		ai.name=wo.getprocessno();
		ai.albums=wo.returnwoinfo();
		adapter.add(ai);
		adapter.notifyDataSetChanged(); 
       
	}

	@Override
	public void updateitem(int arg, WoClass wo) {
		// TODO Auto-generated method stub
		JazzArtist ja=adapter.getItem(arg);
		ja.woclass=wo;
		ja.name=wo.getprocessno();
		ja.albums=wo.returnwoinfo();
		
		adapter.notifyDataSetChanged(); 

	}

}

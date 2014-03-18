package org.tsunamistudios.computerwatch;

import java.io.IOException;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.Collection;

import org.tsunamistudios.computerwatch.net.Net;
import org.tsunamistudios.computerwatch.processes.Program;

import android.app.ListActivity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tsunamistudios.computerwatch.R;

public class Client extends ListActivity {
	
	private static String message;
	private static ArrayList<Program> programs = new ArrayList<Program>();
	private static Net net = new Net();
	private static TextView txtView;
	private static ProgramAdapter prAdapter;
	private static Context context;
	private static ArrayList<TextView> textViews = new ArrayList<TextView>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		context = getApplicationContext();			
	}
	
	protected void onDestroy() {
		net.getOut().println("Exit");
	}
	 
	private class GetObject extends AsyncTask<Void, Void, String> {

		@SuppressWarnings("unchecked")
		@Override
		protected String doInBackground(Void... arg0) {
			net.setSocket();
	        try {
				getPrograms().addAll((Collection<? extends Program>) net.getObjectInputStream().readObject());
			} catch (OptionalDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	       	
			return null;
		}		
		
		protected void onPostExecute(String result) {
        	prAdapter = new ProgramAdapter(context, R.layout.row, programs);
        	setListAdapter(prAdapter);
        }
	}
	
	public void onKillClick(View v) {		
		LinearLayout row = (LinearLayout) v.getParent();
		TextView s = (TextView) row.getChildAt(0);
		net.killProcess(s.getText().toString().split(" ")[1]);
	}
	
	private class ProgramAdapter extends ArrayAdapter<Program> {

	    private ArrayList<Program> programs;

	    public ProgramAdapter(Context context, int textViewResourceId, ArrayList<Program> programs) {
            super(context, textViewResourceId, programs);
            this.programs = programs;
	    }
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row, null);
            }
            Program p = programs.get(position);
            if (p != null) {
            	Bitmap bmp = BitmapFactory.decodeByteArray(p.getImage(), 0, p.getImage().length);
                TextView tt = (TextView) v.findViewById(R.id.toptext);
                TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                ImageView im = (ImageView) v.findViewById(R.id.icon);
                im.setImageBitmap(bmp);
                
                if (tt != null) {
                      tt.setText("Name: "+ p.getName().substring(0, p.getName().length() - 4));  
                      textViews.add(tt);
                }
                if(bt != null) {
                      bt.setText("Desc: "+ p.getDescription());
                }
            }
            return v;
	    }
	}
	
	public void onIpClick(View v){
		EditText et = (EditText) findViewById(android.R.id.empty);
		net.setHostName(et.getText().toString());
		new GetObject().execute((Void) null);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.client, menu);
		return true;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		Client.message = message;
	}
	
	public static ArrayList<Program> getPrograms() {
		return programs;
	}
	
	public static void setTextView() {
		txtView.setText(getPrograms().get(0).getName() + getPrograms().get(0).getDescription());
	}

	public void setPrograms(ArrayList<Program> programs) {
		Client.programs = programs;
	}

	public Net getNet() {
		return net;
	}

	public void setNet(Net net) {
		Client.net = net;
	}
	

	public static TextView getTxtView() {
		return txtView;
	}
}

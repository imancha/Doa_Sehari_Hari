package me.imancha.doa;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DoaDB mydb = new DoaDB(this);

		try {
			mydb.CopyDatabaseFromAssets();
		} catch (IOException e) {
			Log.e("copy", "Copy Failed");
		}

		mydb.OpenDatabase();

		final ListView list = (ListView) findViewById(R.id.listView1);

		list.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mydb.GetAllDoa()) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final View view = super.getView(position, convertView, parent);
				final TextView text = (TextView) view
						.findViewById(android.R.id.text1);

				Typeface type = Typeface.createFromAsset(getAssets(),
						"KacstBook.ttf");

				text.setTypeface(type);

				return view;
			}
		});

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getApplicationContext(), DoaView.class);
				Bundle bundle = new Bundle();

				bundle.putString("nama", arg0.getItemAtPosition(arg2).toString());

				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Toast.makeText(getApplicationContext(),
						arg0.getItemAtPosition(arg2).toString(), Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		});

	}
}
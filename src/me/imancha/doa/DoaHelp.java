package me.imancha.doa;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class DoaHelp extends Activity {

	private static ImageView IV1, IV2;
	private static TextView TV1, TV2, TV3, TV4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_doa);
		setTitle(R.string.action_help);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		IV1 = (ImageView) findViewById(R.id.imageView1);
		IV2 = (ImageView) findViewById(R.id.imageView2);
		TV1 = (TextView) findViewById(R.id.textView1);
		TV2 = (TextView) findViewById(R.id.textView2);
		TV3 = (TextView) findViewById(R.id.textView3);
		TV4 = (TextView) findViewById(R.id.textView4);

		IV1.setImageResource(R.drawable.ic_bookmark_on);
		IV1.setScaleX(2);
		IV1.setScaleY(2);

		TV1.setText(R.string.action_bookmark);
		TV2.setText(R.string.help_bookmark);

		IV2.setImageResource(R.drawable.ic_action_search);
		IV2.setScaleX(2);
		IV2.setScaleY(2);

		TV3.setText(R.string.action_search);
		TV4.setText(R.string.help_search);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

package me.imancha.doa;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class DoaAbout extends Activity {

	private static ImageView IV1, IV2, IV3, IV4;
	private static TextView TV1, TV2, TV3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_doa);
		setTitle(R.string.action_about);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		IV1 = (ImageView) findViewById(R.id.imageView1);
		IV2 = (ImageView) findViewById(R.id.imageView2);
		IV3 = (ImageView) findViewById(R.id.imageView3);
		IV4 = (ImageView) findViewById(R.id.imageView4);
		TV1 = (TextView) findViewById(R.id.textView1);
		TV2 = (TextView) findViewById(R.id.textView2);
		TV3 = (TextView) findViewById(R.id.textView3);

		IV1.setImageResource(R.drawable.ic_launcher);
		IV1.setScaleX(4);
		IV1.setScaleY(4);
		IV1.setTranslationY(111);

		TV1.setText(R.string.app_name);
		TV1.setTranslationY(246);

		String vName = "1.0";
		int vCode = 1;

		try {
			vName = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;

			vCode = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e("vName", "NameNotFoundException");
		}

		TV2.setText("V " + vName + "." + vCode);
		TV2.setTextColor(Color.GRAY);
		TV2.setTranslationY(246);

		IV2.setImageResource(R.drawable.ic_facebook);
		IV2.setScaleX(2);
		IV2.setScaleY(2);
		IV2.setTranslationY(-100);
		IV2.setTranslationX(-120);
		IV2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("https://facebook.com/imancha.os")));
			}
		});

		IV3.setImageResource(R.drawable.ic_twitter);
		IV3.setScaleX(2);
		IV3.setScaleY(2);
		IV3.setTranslationY(-100);
		IV3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("https://twiter.com/ImanchaOS")));

			}
		});

		IV4.setImageResource(R.drawable.ic_email);
		IV4.setScaleX(2);
		IV4.setScaleY(2);
		IV4.setTranslationY(-100);
		IV4.setTranslationX(120);
		IV4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("mailto:imancha_266@ymail.com")));

			}
		});

		TV3.setText(R.string.about);
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

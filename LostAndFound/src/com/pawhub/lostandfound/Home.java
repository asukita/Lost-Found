package com.pawhub.lostandfound;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.facebook.Session;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.Toast;

public class Home extends ActionBarActivity {

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private Fragment fragment;
	private Bundle arguments;

	private static int TAKE_PICTURE = 1;

	// Change the numbers to the actions name like btn_alerts
	private TableRow btn_0;
	private TableRow btn_1;
	private TableRow btn_2;
	private TableRow btn_3;
	private TableRow btn_4;
	private TableRow btn_5;
	private TableRow btn_6;
	private TableRow btn_7;
	private TableRow btn_8;
	private TableRow btn_9;
	private TableRow btn_10;

	private final int SCREEN_HOME = 0;
	private final int SCREEN_ALERTS = 1;
	private final int SCREEN_REPORTS = 2;
	private final int SCREEN_LOST = 3;
	private final int SCREEN_FOUND = 4;
	private final int SCREEN_ABUSE = 5;
	private final int SCREEN_HOMELESS = 6;
	private final int SCREEN_ACCIDENT = 8;
	private final int SCREEN_MAP = 7;
	private final int SCREEN_RANK = 9;
	private final int SCREEN_DONATE = 10;

	private int CURRENT_SCREEN = 0;
	private String currentTitle, prefName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		initSlidingMenu();
		initViews();
		showScreen(0);

		SharedPreferences preferences = this.getSharedPreferences("userPrefs",
				MODE_PRIVATE);
		prefName = preferences.getString("username", "nothing");

		Log.i("username", "" + prefName);
	}

	private void initSlidingMenu() {

		final ActionBar actionBar = getSupportActionBar();

		currentTitle = "";

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setIcon(R.drawable.app_name2);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#B71C4E")));
		actionBar.setTitle(currentTitle);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View view) {
				actionBar.setTitle(currentTitle);
				supportInvalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				actionBar.setTitle(currentTitle);
				supportInvalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void initViews() {
		btn_0 = (TableRow) findViewById(R.id.entry_Home);
		btn_1 = (TableRow) findViewById(R.id.entry_1);
		btn_2 = (TableRow) findViewById(R.id.entry_2);
		btn_3 = (TableRow) findViewById(R.id.entry_3);
		btn_4 = (TableRow) findViewById(R.id.entry_4);
		btn_5 = (TableRow) findViewById(R.id.entry_5);
		btn_6 = (TableRow) findViewById(R.id.entry_6);
		btn_7 = (TableRow) findViewById(R.id.entry_7);
		btn_8 = (TableRow) findViewById(R.id.entry_8);
		btn_9 = (TableRow) findViewById(R.id.entry_9);
		btn_10 = (TableRow) findViewById(R.id.entry_10);

		MenuListener listener = new MenuListener();
		btn_0.setOnClickListener(listener);
		btn_1.setOnClickListener(listener);
		btn_2.setOnClickListener(listener);
		btn_3.setOnClickListener(listener);
		btn_4.setOnClickListener(listener);
		btn_5.setOnClickListener(listener);
		btn_6.setOnClickListener(listener);
		btn_7.setOnClickListener(listener);
		btn_8.setOnClickListener(listener);
		btn_9.setOnClickListener(listener);
		btn_10.setOnClickListener(listener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		} else {
			switch (item.getItemId()) {
			case R.id.action_camera:
				openCamera();
				return true;
			case R.id.action_add_report:
				openReport();
				return true;
			case R.id.action_alerts:
				openAlerts();
				return true;
			case R.id.action_settings:
				openSettings();
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}

	}

	private void openSettings() {
		Intent openSet = new Intent(this, SettingsActivity.class);
		startActivity(openSet);
	}

	private void openAlerts() {
		Toast.makeText(this, "Esta opción aún no está disponible en el demo",
				Toast.LENGTH_LONG).show();
	}

	private void openCamera() {
		if (!prefName.equals("guest")) {
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cameraIntent, TAKE_PICTURE);
		} else {
			Toast.makeText(this, "Como invitado no puedes usar esta opción",
					Toast.LENGTH_LONG).show();
		}

	}

	private void openReport() {
		if (!prefName.equals("guest")) {
			Intent openRepo = new Intent(this, ReportActivity.class);
			startActivity(openRepo);
		} else {
			Toast.makeText(this, "Como invitado no puedes usar esta opción",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void showScreen(int id) {

		fragment = null;
		arguments = new Bundle();

		switch (id) {

		case R.id.entry_Home:
			fragment = new CasesListFragment();
			arguments.putInt("TYPE", SCREEN_HOME);
			currentTitle = "";
			CURRENT_SCREEN = SCREEN_HOME;
			break;
		case R.id.entry_1:
			fragment = new CasesListFragment();
			arguments.putInt("TYPE", SCREEN_ALERTS);
			currentTitle = "";
			CURRENT_SCREEN = SCREEN_ALERTS;
			break;
		case R.id.entry_2:
			fragment = new CasesListFragment();
			arguments.putInt("TYPE", SCREEN_REPORTS);
			CURRENT_SCREEN = SCREEN_REPORTS;
			break;
		case R.id.entry_3:
			fragment = new CasesListFragment();
			arguments.putInt("TYPE", SCREEN_LOST);
			CURRENT_SCREEN = SCREEN_LOST;
			break;
		case R.id.entry_4:
			fragment = new CasesListFragment();
			arguments.putInt("TYPE", SCREEN_FOUND);
			CURRENT_SCREEN = SCREEN_FOUND;
			break;
		case R.id.entry_5:
			fragment = new CasesListFragment();
			arguments.putInt("TYPE", SCREEN_ABUSE);
			CURRENT_SCREEN = SCREEN_ABUSE;
			break;
		case R.id.entry_6:
			fragment = new CasesListFragment();
			arguments.putInt("TYPE", SCREEN_HOMELESS);
			CURRENT_SCREEN = SCREEN_HOMELESS;
			break;
		case R.id.entry_7:
			initMap();
			break;
		case R.id.entry_8:
			fragment = new CasesListFragment();
			arguments.putInt("TYPE", SCREEN_ACCIDENT);
			CURRENT_SCREEN = SCREEN_ACCIDENT;
			break;
		case R.id.entry_9:
			fragment = new CasesListFragment();
			arguments.putInt("TYPE", SCREEN_RANK);
			CURRENT_SCREEN = SCREEN_HOME;
			break;
		case R.id.entry_10:
			fragment = new CasesListFragment();
			arguments.putInt("TYPE", SCREEN_DONATE);
			CURRENT_SCREEN = SCREEN_HOME;
			break;
		default:
			fragment = new CasesListFragment();
			arguments.putInt("TYPE", SCREEN_HOME);
			currentTitle = "";
			CURRENT_SCREEN = SCREEN_HOME;
			break;
		}

		fragment.setArguments(arguments);

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		mDrawerLayout.closeDrawers();
	}

	private void initMap() {
		fragment = new FragmentCasesMap();
		arguments.putInt("TYPE", SCREEN_MAP);
		CURRENT_SCREEN = SCREEN_MAP;
	}

	public void removeMap() {
		Fragment mFragment = getSupportFragmentManager().findFragmentById(
				R.id.content_frame);
		FragmentCasesMap mapFragment = (FragmentCasesMap) mFragment;

		mapFragment.removeMap();
	}

	@Override
	public void onBackPressed() {
		if (CURRENT_SCREEN == SCREEN_HOME)
			finish();
		else if (CURRENT_SCREEN == SCREEN_MAP) {
			removeMap();
			showScreen(R.id.entry_Home);
		} else
			showScreen(R.id.entry_Home);
	}

	private class MenuListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			showScreen(id);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// Review if the image come from the camera or the gallery
		Toast toast2 = Toast.makeText(getApplicationContext(),
				"Ocurrió un error", Toast.LENGTH_SHORT);

		if (resultCode == RESULT_OK) {

			// If comes from camera
			if (requestCode == TAKE_PICTURE) {
				if (data != null) {
					if (data.hasExtra("data")) {
						String pic = System.currentTimeMillis() + ".jpg";
						Bitmap photo = (Bitmap) data.getExtras().get("data");

						try {
							File temp = new File(
									Environment.getExternalStorageDirectory(),
									File.separator + "Pawhub");
							if (!temp.exists())
								temp.mkdirs();
							OutputStream stream = new FileOutputStream(
									Environment.getExternalStorageDirectory()
											+ File.separator + "Pawhub"
											+ File.separator + pic);
							photo.prepareToDraw();
							photo.compress(CompressFormat.JPEG, 100, stream);
							stream.flush();
							stream.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							toast2.show();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							toast2.show();
						}

						Intent openRepo = new Intent(this, ReportActivity.class);
						openRepo.putExtra("BitmapImage", photo);
						startActivity(openRepo);

					} else {
						toast2.show();
					}
				}

			}

		}
	}

}

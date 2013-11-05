package com.pawhub.lostandfound;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private Spinner spinnerUserSex;
	private TextView terms;
	private Button birthBtn;
	private Calendar myCalendar;
	private ImageButton takePicBtn;
	private ImageButton choosePicBtn;
	private ImageView imgViewUserPic;

	private static int TAKE_PICTURE = 1;
	private static int SELECT_PICTURE = 0;
	private Bitmap setphoto;

	// data for spinner for sex
	String[] userSex = { "Mujer", "Hombre" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		// set adapter for spinner sex
		spinnerUserSex = (Spinner) findViewById(R.id.spinnerUserSex);
		spinnerUserSex.setAdapter(new TypesAdapter(RegisterActivity.this,
				R.layout.spinner_register, userSex));

		// properties for date picker
		myCalendar = Calendar.getInstance();
		birthBtn = (Button) findViewById(R.id.btnUserBirthday);

		final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateLabel();

			}

		};

		birthBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(RegisterActivity.this, date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		// set onClick listener for terms of use
		terms = (TextView) findViewById(R.id.textViewTerms);
		terms.setMovementMethod(LinkMovementMethod.getInstance());

		// take pic intent

		imgViewUserPic = (ImageView) findViewById(R.id.imgViewUserPic);

		takePicBtn = (ImageButton) findViewById(R.id.btnTakePhotoRegister);
		takePicBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, TAKE_PICTURE);
			}
		});

		// choose pic intent

		choosePicBtn = (ImageButton) findViewById(R.id.btnSelectPhotoRegister);
		choosePicBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent galeryIntent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				startActivityForResult(galeryIntent, SELECT_PICTURE);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Toast toast2 = Toast.makeText(getApplicationContext(),
				"Ocurrió un error", Toast.LENGTH_SHORT);

		// Review if the image come from the camera or the gallery

		if (resultCode == RESULT_OK) {
			// If comes from camera
			if (requestCode == TAKE_PICTURE) {
				if (data != null) {
					if (data.hasExtra("data")) {
						String pic = System.currentTimeMillis() + ".jpg";
						Bitmap photo = (Bitmap) data.getExtras().get("data");

						try {
							OutputStream stream = new FileOutputStream(
									Environment.getExternalStorageDirectory()
											+ File.separator + "Pawhub"
											+ File.separator + pic);
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

						photo = Bitmap.createBitmap(photo);
						imgViewUserPic.setImageBitmap(photo);

					} else {
						toast2.show();
					}
				}

				// If comes from gallery
			} else if (requestCode == SELECT_PICTURE) {

				try {
					this.imageFromGallery(resultCode, data, 200, 200);
					imgViewUserPic.setImageBitmap(null);
					imgViewUserPic.setImageBitmap(setphoto);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					toast2.show();
				}

			}
		}
	}

	// Takes the image chosen and resizes to show it in image view
	private void imageFromGallery(int resultCode, Intent data, int reqWidth,
			int reqHeight) throws IOException {
		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		Cursor cursor = getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

		String profile_Path = cursor.getString(columnIndex);
		cursor.close();

		// First decode with inJustDecodeBounds=true to check dimensions

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		setphoto = BitmapFactory.decodeFile(profile_Path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		ExifInterface ei = new ExifInterface(profile_Path);
		int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
				ExifInterface.ORIENTATION_NORMAL);
		Toast toast1 = Toast.makeText(getApplicationContext(),
				"" + orientation, Toast.LENGTH_SHORT);
		toast1.show();

		setphoto = BitmapFactory.decodeFile(profile_Path, options);

	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.register_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// Method for update the text in button for birthday
	private void updateLabel() {

		String myFormat = "dd/MM/yy"; // In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		birthBtn.setText("Cumpleaños: " + sdf.format(myCalendar.getTime()));
	}

	public class TypesAdapter extends ArrayAdapter<String> {

		public TypesAdapter(Context context, int textViewResourceId,
				String[] objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.spinner_register, parent,
					false);
			TextView label = (TextView) row
					.findViewById(R.id.textViewSpinnerRegisterAdptr);
			label.setText(userSex[position]);

			return row;
		}
	}

}

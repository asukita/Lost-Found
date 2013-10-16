package com.pawhub.lostandfound.views;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pawhub.lostandfound.R;
import com.pawhub.lostandfound.transferobjects.Report;

public class ReportView {

	private Report reportObject;
	private LayoutInflater inflater;

	private View reportChart;

	public ReportView(Report reportObject, LayoutInflater inflater) {
		this.reportObject = reportObject;
		this.inflater = inflater;
	}

	private void generateChart_1() {
		View v = inflater.inflate(R.layout.detail_chart_1, null);
		generateChartGeneral(v);

		LinearLayout colorBackGound = (LinearLayout) v
				.findViewById(R.id.linearmain2);
		colorBackGound
				.setBackgroundResource(getTransparentColorReport(reportObject
						.getTypeReport()));

		reportChart = v;
	}

	private void generateChart_2() {
		View v = inflater.inflate(R.layout.detail_chart_2, null);
		generateChartGeneral(v);

		LinearLayout colorBackGound = (LinearLayout) v
				.findViewById(R.id.linearmain2);
		colorBackGound.setBackgroundResource(getColorReport(reportObject
				.getTypeReport()));

		reportChart = v;
	}

	private void generateChartGeneral(View v) {

		// ////// Alert
		final ImageView imageViewAlert = (ImageView) v
				.findViewById(R.id.ivChart_Alert);
		if (reportObject.isAlert())
			imageViewAlert.setImageResource(R.drawable.alert_active);
		else
			imageViewAlert.setImageResource(R.drawable.menu_alert_icon);

		LinearLayout layoutAlert = (LinearLayout) v
				.findViewById(R.id.layoutAlert);

		layoutAlert.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				reportObject.changeAlert();
				if (reportObject.isAlert())
					imageViewAlert.setImageResource(R.drawable.alert_active);
				else
					imageViewAlert.setImageResource(R.drawable.menu_alert_icon);
			}
		});

		// /////////////////////////////////////////////////////

		// ///Share
		LinearLayout layoutShare = (LinearLayout) v
				.findViewById(R.id.layoutShare);

		layoutShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (reportObject.getHasPicture()) {
					shareIntentImage(v);
				} else {
					shareIntentText(v);
				}
			}
		});

		// /////////////////////////////////////////////////////

		// ///// Num of comments
		TextView tvNumComents = (TextView) v.findViewById(R.id.tvNumComments);
		tvNumComents.setText("" + reportObject.getNumComments());

		// /////////////////////////////////////////////////////

		// /// User Name
		TextView tvUserName = (TextView) v.findViewById(R.id.txtUserNameMain);
		tvUserName.setText(reportObject.getUserName());
		// //////////////////////////////////////////////////////

		// /// Type report text
		TextView tvTypeReport = (TextView) v.findViewById(R.id.reportTypeLbl);
		tvTypeReport.setText(getTitleReport(reportObject.getTypeReport()));
		// /////////////////////////////////////////////////////

		// ////// Image Icon
		ImageView imageViewIcon = (ImageView) v.findViewById(R.id.titleIcon);
		imageViewIcon.setImageResource(getIcon(reportObject.getTypeReport()));
		// /////////////////////////////////////////////////////

		// //////Resolve
		TextView tvResolve = (TextView) v.findViewById(R.id.tvResolve);
		if (reportObject.getTypeReport() == Report.CAUSE_ABUSE)
			tvResolve.setTextColor(Color.parseColor("#63C2D0"));
		else if (reportObject.getTypeReport() == Report.CAUSE_FOUND)
			tvResolve.setTextColor(Color.parseColor("#B71C4E"));
		else if (reportObject.getTypeReport() == Report.CAUSE_ACCIDENT)
			tvResolve.setTextColor(Color.parseColor("#B71C4E"));
		if (reportObject.isResolve())
			tvResolve.setText(" - ¡Resuelto!");
		else
			tvResolve.setText("");

		// /////////////////////////////////////////////////////

		// ///User message
		TextView tvUserShortMessage = (TextView) v
				.findViewById(R.id.userShortMsg);
		tvUserShortMessage.setText(reportObject.getComments());
		// /////////////////////////////////////////////////////

	}

	private int getTransparentColorReport(int type) {
		switch (type) {
		case Report.CAUSE_ABUSE:
			return R.drawable.detail_chart_transparent_bottom_yellow;
		case Report.CAUSE_ACCIDENT:
			return R.drawable.detail_chart_transparent_bottom_pink;
		case Report.CAUSE_FOUND:
			return R.drawable.detail_chart_transparent_bottom_green;
		case Report.CAUSE_LOST:
			return R.drawable.detail_chart_transparent_bottom_magenta;
		case Report.CAUSE_HOMELESS:
			return R.drawable.detail_chart_transparent_bottom_blue;
		default:
			return 0;
		}
	}

	private int getColorReport(int type) {
		switch (type) {
		case Report.CAUSE_ABUSE:
			return R.drawable.detail_chart_yellow;
		case Report.CAUSE_ACCIDENT:
			return R.drawable.detail_chart_pink;
		case Report.CAUSE_FOUND:
			return R.drawable.detail_chart_green;
		case Report.CAUSE_LOST:
			return R.drawable.detail_chart_magenta;
		case Report.CAUSE_HOMELESS:
			return R.drawable.detail_chart_blue;
		default:
			return 0;
		}
	}

	private String getTitleReport(int type) {
		switch (type) {
		case Report.CAUSE_ABUSE:
			return "Maltrato";
		case Report.CAUSE_ACCIDENT:
			return "Accidente";
		case Report.CAUSE_FOUND:
			return "Encontrado";
		case Report.CAUSE_LOST:
			return "Perdido";
		case Report.CAUSE_HOMELESS:
			return "Busca hogar";
		default:
			return "";
		}
	}

	private int getIcon(int type) {
		switch (type) {
		case Report.CAUSE_ABUSE:
			return R.drawable.abuse_icon;
		case Report.CAUSE_ACCIDENT:
			return R.drawable.abuse_icon;
		case Report.CAUSE_FOUND:
			return R.drawable.found_icon;
		case Report.CAUSE_LOST:
			return R.drawable.missing_ico;
		case Report.CAUSE_HOMELESS:
			return R.drawable.home_ico;
		default:
			return 0;
		}
	}

	private void shareIntentText(View view) {

		// sharing implementation
		List<Intent> targetedShareIntents = new ArrayList<Intent>();
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);

		sharingIntent.setType("text/plain");

		String shareBody = reportObject.getComments()
				+ " - Pawhub Lost&Found Get the app at http://pawhub.me";

		PackageManager pm = view.getContext().getPackageManager();
		List<ResolveInfo> activityList = pm.queryIntentActivities(
				sharingIntent, 0);

		for (final ResolveInfo app : activityList) {

			String packageName = app.activityInfo.packageName;
			Intent targetedShareIntent = new Intent(
					android.content.Intent.ACTION_SEND);
			targetedShareIntent.setType("text/plain");
			targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"share");
			if (TextUtils.equals(packageName, "com.facebook.katana")
					|| TextUtils.equals(packageName, "com.twitter.android")) {
				targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						"prueba");
			} else {
				targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						shareBody);
			}

			targetedShareIntent.setPackage(packageName);
			targetedShareIntents.add(targetedShareIntent);

		}

		Intent chooserIntent = Intent.createChooser(
				targetedShareIntents.remove(0), "Compartir idea");

		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				targetedShareIntents.toArray(new Parcelable[] {}));
		inflater.getContext().startActivity(chooserIntent);
	}

	private void shareIntentImage(View view) {

		String shareBody = reportObject.getComments()
				+ " - Pawhub Lost&Found Get the app at http://pawhub.me";

		Bitmap icon = BitmapFactory.decodeResource(inflater.getContext()
				.getResources(), R.drawable.finding_home);
		FileOutputStream fo;
		
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("image/png");
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		icon.compress(Bitmap.CompressFormat.PNG, 100, bytes);
		File f = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "temporary_file.png");
		try {
			f.createNewFile();
			fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		share.putExtra(Intent.EXTRA_TEXT,
				shareBody);
		share.putExtra(Intent.EXTRA_STREAM,
				Uri.parse("file:///sdcard/temporary_file.png"));
		inflater.getContext().startActivity(
				Intent.createChooser(share, "Compartir Imagen"));
	}

	public View getReportChart() {

		if (reportObject.getHasPicture())
			generateChart_1();
		else
			generateChart_2();

		return reportChart;
	}

}

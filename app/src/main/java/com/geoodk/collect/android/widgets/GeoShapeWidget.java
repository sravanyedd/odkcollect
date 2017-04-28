/*
 * Copyright (C) 2014 GeoODK
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 * Widget for geoshape data type
 *
 * @author Jon Nordling (jonnordling@gmail.com)
 */
package com.geoodk.collect.android.widgets;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.javarosa.core.model.data.GeoPointData;
import org.javarosa.core.model.data.GeoShapeData;
import org.javarosa.core.model.data.GeoShapeData.GeoShape;
import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.StringData;
import org.javarosa.form.api.FormEntryPrompt;

import com.geoodk.collect.android.R;
import com.geoodk.collect.android.activities.FormEntryActivity;
import com.geoodk.collect.android.activities.GeoPointActivity;
import com.geoodk.collect.android.activities.GeoPointMapActivity;
import com.geoodk.collect.android.activities.GeoPointMapActivitySdk7;
import com.geoodk.collect.android.activities.GeoShapeActivity;
import com.geoodk.collect.android.application.Collect;
import com.geoodk.collect.android.utilities.CompatibilityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * GeoShapeWidget is the widget that allows the user to get Collect multiple GPS points.
 *
 * @author Jon Nordling (jonnordling@gmail.com)
 */

public class GeoShapeWidget extends QuestionWidget implements IBinaryWidget {
	public static final String ACCURACY_THRESHOLD = "accuracyThreshold";
	public static final String READ_ONLY = "readOnly";
	private final boolean mReadOnly;
	public static final String SHAPE_LOCATION = "gp";
	private Button createShapeButton;
	private Button viewShapeButton;

	private TextView mStringAnswer;
	private TextView mAnswerDisplay;

	public GeoShapeWidget(Context context, FormEntryPrompt prompt) {
		super(context, prompt);
		// assemble the widget...
		setOrientation(LinearLayout.VERTICAL);
		TableLayout.LayoutParams params = new TableLayout.LayoutParams();
		params.setMargins(7, 5, 7, 5);
		mReadOnly = prompt.isReadOnly();
		
		mStringAnswer = new TextView(getContext());
		mStringAnswer.setId(QuestionWidget.newUniqueId());

		mAnswerDisplay = new TextView(getContext());
		mAnswerDisplay.setId(QuestionWidget.newUniqueId());
		mAnswerDisplay.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mAnswerFontsize);
		mAnswerDisplay.setGravity(Gravity.CENTER);
		
		// setup view-edit shape button
		/*viewShapeButton = new Button(getContext());
		viewShapeButton.setId(QuestionWidget.newUniqueId());
		viewShapeButton.setText(getContext().getString(R.string.view_shape));
		viewShapeButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mAnswerFontsize);
		viewShapeButton.setPadding(20, 20, 20, 20);
		viewShapeButton.setLayoutParams(params);
		
		viewShapeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});*/
		
		
		// setup play button
		createShapeButton = new Button(getContext());
		createShapeButton.setId(QuestionWidget.newUniqueId());
		createShapeButton.setText(getContext().getString(R.string.record_geoshape));
		createShapeButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mAnswerFontsize);
		createShapeButton.setPadding(20, 20, 20, 20);
		createShapeButton.setLayoutParams(params);
				// finish complex layout
				// control what gets shown with setVisibility(View.GONE)
				//addView(mGetLocationButton);
		//mReadOnly = prompt.isReadOnly();

		
		//Toast.makeText(getContext(), temp+" ", Toast.LENGTH_LONG).show();
		
		createShapeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Collect.getInstance().getFormController().setIndexWaitingForData(mPrompt.getIndex());
				Intent i = null;
				
				i = new Intent(getContext(), GeoShapeActivity.class);
				String s = mStringAnswer.getText().toString();
				if ( s.length() != 0 ) {
					i.putExtra(SHAPE_LOCATION, s);
				}
				//((Activity) getContext()).startActivity(i);
				((Activity) getContext()).startActivityForResult(i,FormEntryActivity.GEOSHAPE_CAPTURE);
			}
		});
		addView(createShapeButton);
		addView(mAnswerDisplay);
		
		boolean dataAvailable = false;
		String s = prompt.getAnswerText();
		if (s != null && !s.equals("")) {
			//Toast.makeText(getContext(), prompt.getAnswerText()+" ", Toast.LENGTH_LONG).show();
			dataAvailable = true;
			setBinaryData(s);
		}else{
			//Toast.makeText(getContext(), "Nothing", Toast.LENGTH_LONG).show();
		}
		//addView(mStringAnswer);
		
		
		updateButtonLabelsAndVisibility(dataAvailable);
	}
	
	
	private void updateButtonLabelsAndVisibility(boolean dataAvailable) {
		if (dataAvailable == true){
			// There is already a shape recorded
			createShapeButton.setText(getContext().getString(R.string.view_shape));
		}else{
			createShapeButton.setText(getContext().getString(R.string.record_geoshape));
		}
	}

	@Override
	public void setBinaryData(Object answer) {
		// TODO Auto-generated method stub
		//Toast.makeText(getContext(), answer.toString(), Toast.LENGTH_LONG).show();
		String s = (String) answer.toString();
		mStringAnswer.setText(s);
		//mStringAnswer.setText(s);
		mAnswerDisplay.setText(s);
		Collect.getInstance().getFormController().setIndexWaitingForData(null);
		//updateButtonLabelsAndVisibility(true);
	}

	@Override
	public void cancelWaitingForBinaryData() {
		// TODO Auto-generated method stub
		Collect.getInstance().getFormController().setIndexWaitingForData(null);
	}

	@Override
	public boolean isWaitingForBinaryData() {
		Boolean test = mPrompt.getIndex().equals(
				Collect.getInstance().getFormController()
				.getIndexWaitingForData());
		//Toast.makeText(getContext(), test+" ", Toast.LENGTH_LONG).show();
		return mPrompt.getIndex().equals(
				Collect.getInstance().getFormController()
				.getIndexWaitingForData());
	}

	@Override
	public IAnswerData getAnswer() {
		// TODO Auto-generated method stub
		
		GeoShapeData data = new GeoShapeData();
		ArrayList<double[]> list = new ArrayList<double[]>();  
		String s = mStringAnswer.getText().toString();
		if (s == null || s.equals("")) {
			return null;
		} else {
			try {
				String[] sa = s.split(";");
				for (int i=0;i<sa.length;i++){
					String[] sp = sa[i].split(" ");
					double gp[] = new double[4];
					gp[0] = Double.valueOf(sp[0]).doubleValue();
					gp[1] = Double.valueOf(sp[1]).doubleValue();
					gp[2] = Double.valueOf(sp[2]).doubleValue();
					gp[3] = Double.valueOf(sp[3]).doubleValue();
					list.add(gp);
				}
				GeoShape shape = new GeoShape(list);
				//return new GeoShapeData(shape);
				return new StringData(s);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}

	@Override
	public void clearAnswer() {
		// TODO Auto-generated method stub
		mStringAnswer.setText(null);
		mAnswerDisplay.setText(null);
		
	}

	@Override
	public void setFocus(Context context) {
		// TODO Auto-generated method stub
		InputMethodManager inputManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(this.getWindowToken(), 0);
		
	}

	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		// TODO Auto-generated method stub
		
	}

}

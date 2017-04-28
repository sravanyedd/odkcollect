/*
 * Copyright (C) 2014 GeoODK
 *
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
 *
 * @author Jon Nordling (jonnordling@gmail.com)
 */
package com.geoodk.collect.android.spatial;

import java.io.File;

import android.app.ListActivity;
import android.database.Cursor;

import com.geoodk.collect.android.provider.FormsProviderAPI.FormsColumns;

public class XmlGeopointHelper extends ListActivity {
	
	//The string passed with be the ID name for the form that
	//The purpose of this function is to find the field associated with the geopoint for mapping
    //public static void main(String[] args) {
    	
    //}
	public String getGeopointDBField(String form_name){
		String field = null;
		
		String sortOrder = FormsColumns.DISPLAY_NAME + " ASC, " + FormsColumns.JR_VERSION + " DESC";
        Cursor c = managedQuery(FormsColumns.CONTENT_URI, null, null, null, sortOrder);
        String x = "sdgkd";
        //String[] data = new String[] {
                //FormsColumns.DISPLAY_NAME, FormsColumns.DISPLAY_SUBTEXT, FormsColumns.JR_VERSION
        //};
		
		//Get the cursor fo the forms
		//Identify the correct form.
		//Find the geopoint field
		//Get db value and return it
		
		return form_name;
		
	}
	

}
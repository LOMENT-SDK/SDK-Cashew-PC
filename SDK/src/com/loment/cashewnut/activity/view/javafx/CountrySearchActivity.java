package com.loment.cashewnut.activity.view.javafx;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.loment.cashewnut.search.CSVReader;
import com.loment.cashewnut.search.CountryModal;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import javafx.scene.control.ListView;

public class CountrySearchActivity {

    // Declare Variables
    ListView list;
    String[] country_code;
    String[] country_Name;
    String[] country_abbr;
    ArrayList<CountryModal> arraylist = new ArrayList<CountryModal>();

    public CountrySearchActivity() {

        String[] next = {};
        final List<String[]> countriesInfo = new ArrayList<String[]>();
        try {
            InputStream input = CountrySearchActivity
                    .class.getResource("country_codes.csv").openStream();
            CSVReader reader = new CSVReader(new InputStreamReader(input));
            for (;;) {
                next = reader.readNext();
                if (next != null) {
                    countriesInfo.add(next);
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        try {
            ArrayList<String> countryNames = new ArrayList<String>();
            ArrayList<String> countryAbber = new ArrayList<String>();
            ArrayList<String> countryCodes = new ArrayList<String>();

            for (int i = 0; i < countriesInfo.size(); i++) {
                countryNames.add(countriesInfo.get(i)[0]);
                countryAbber.add(countriesInfo.get(i)[1]);
                countryCodes.add(countriesInfo.get(i)[2]);
            }
            // convert data
            country_code = countryCodes
                    .toArray(new String[countryCodes.size()]);
            country_Name = countryNames
                    .toArray(new String[countryNames.size()]);
            country_abbr = countryAbber
                    .toArray(new String[countryAbber.size()]);
        } catch (Exception e) {

            e.printStackTrace();
        }

        for (int i = 0; i < country_code.length; i++) {
            CountryModal wp = new CountryModal(country_code[i],
                    country_Name[i], country_abbr[i]);
            // Binds all strings into an array
            arraylist.add(wp);
        }
    }

    public ArrayList<CountryModal> getCountryList() {
        Collections.sort(arraylist, new Comparator<CountryModal>() {
            @Override
            public int compare(CountryModal o1, CountryModal o2) {
	      String name1 = o1.getCountryName().toUpperCase();
	      String name2 = o2.getCountryName().toUpperCase();
	      //ascending order
	      return name1.compareTo(name2);
            }
        }); 
        return arraylist;
    }
}

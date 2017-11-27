package com.loment.cashewnut.activity.view.javafx;

import com.loment.cashewnut.search.CSVReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


/**
 * @author Madhur
 * Class for loading, searching country codes based on mobile number
 */
public class CountryCodes {

        private static CountryCodes instance;
        private Hashtable countries = new Hashtable();
        int[] headerMap = null;
       
        /**
         * @author Madhur Class to store country data
         */
        class CountryCode {
                String countryName;
                String isoCode;
                String telephoneCode;

                public CountryCode(String countryName, String isoCode,
                                String telephoneCode) {
                        this.countryName = countryName;
                        this.isoCode = isoCode;
                        this.telephoneCode = telephoneCode;
                }

                public String getCountryName() {
                        return countryName;
                }

                public void setCountryName(String country) {
                        this.countryName = country;
                }

                public String getIsoCode() {
                        return isoCode;
                }

                public void setIsoCode(String isoCode) {
                        this.isoCode = isoCode;
                }

                public String getTelephoneCode() {
                        return telephoneCode;
                }

                public void setTelephoneCode(String telephoneCode) {
                        this.telephoneCode = telephoneCode;
                }
        }

        /**
         * Load countries data from csv file
         * 
         * @param is
         */
        public void fillCountryCodes(InputStream is) {
                countries.clear();
                try {
                	
                        // load from csv file
                        CSVReader csvReader = new CSVReader(new InputStreamReader(is));
                        // String[] headers = csvReader.readNext();
                        // String line = "";
                        // headerMap = new int[headers.length];
                        // fillHeaderMap(headers);
                        String[] contactInfo;
                        while ((contactInfo = csvReader.readNext()) != null) {
                                String country_name = "";
                                String phoneCode = "";
                                String isoCode = "";
                                for (int i = 0; i < contactInfo.length; i++) {
                                        String field = contactInfo[i];
                                        field = field.replace('"', ' ').trim();
                                        if (field.length() > 0) {
                                                switch (i) {
                                                case 0:
                                                        country_name = field;
                                                        break;
                                                case 2:
                                                        phoneCode = field;
                                                        break;
                                                case 1:
                                                        isoCode = field;
                                                default:
                                                        break;
                                                }
                                        }

                                }
                                CountryCode cc = new CountryCode(country_name, isoCode,
                                                phoneCode);

                                countries.put(isoCode, cc);
                        }
                        is.close();
                } catch (Exception e) {
                        e.printStackTrace();
                }

        }

        private int getFieldType(int i) {
                return headerMap[i];
        }

        public static CountryCodes getInstance() {
                // TODO Auto-generated method stub
                if (instance == null) {
                        instance = new CountryCodes();
                }
                return instance;
        }

        /**
         * returns matching country iso codes based on number.
         * 
         * @param number
         * @return array of all matching iso codes.
         */
        public String[] getIsoCodeByNumber(String number) {
                Vector result = new Vector();
                if (number.startsWith("+")) {
                        number = number.substring(1);
                        Enumeration countryCodes = countries.elements();
                        while (countryCodes.hasMoreElements()) {
                                CountryCode countryCode = (CountryCode) countryCodes
                                                .nextElement();
                                if (number.startsWith(countryCode.getTelephoneCode())) {
                                        result.addElement(countryCode.getIsoCode());
                                }
                        }
                        String[] codes = new String[result.size()];
                        result.copyInto(codes);
                        return codes;
                }

                return new String[0];
        }

        /**
         * returns country name based on iso code
         * 
         * @param isoCode
         * @return
         */
        public String[] getCountryNames() {
                Vector result = new Vector();
                String[] majorCodes = new String[6];
                int i = 0;

                Enumeration countryCodes = countries.elements();
                while (countryCodes.hasMoreElements()) {
                        CountryCode countryCode = (CountryCode) countryCodes.nextElement();
                        CountryCode cc = (CountryCode) countries.get(countryCode
                                        .getIsoCode());
                        if (cc != null) {
                                String code = countryCode.getIsoCode();
                                if (code.equalsIgnoreCase("US") || code.equalsIgnoreCase("IN")
                                                || code.equalsIgnoreCase("CN")
                                                || code.equalsIgnoreCase("RS")
                                                || code.equalsIgnoreCase("TH")
                                                || code.equalsIgnoreCase("MY")) {
                                        majorCodes[i] = cc.getCountryName() + " - "
                                                        + countryCode.getIsoCode();
                                        i++;
                                } else {
                                        result.addElement(cc.getCountryName() + " - "
                                                        + countryCode.getIsoCode());
                                }

                        }
                }

                String[] codes = new String[result.size()];
                result.copyInto(codes);
                return codes;
        }
        public String getTelephoneCodeByCountry(String country){
                Enumeration countryCodes = countries.elements();
                while(countryCodes.hasMoreElements()){
                        CountryCode countryCode = (CountryCode) countryCodes.nextElement();
                        if(country.equalsIgnoreCase(countryCode.getCountryName())){
                                String telephoneCode = countryCode.telephoneCode;
                                if(!telephoneCode.startsWith("+")){ 
                                        return "+" + countryCode.telephoneCode;
                                }else{
                                        return countryCode.telephoneCode;
                                }
                        }
                }
        return "";
}
        public String getIsoCodeByCountry(String country){
            Enumeration countryCodes = countries.elements();
            while(countryCodes.hasMoreElements()){
                    CountryCode countryCode = (CountryCode) countryCodes.nextElement();
                    if(country.equalsIgnoreCase(countryCode.getCountryName())){
                            //String isoCode = countryCode.isoCode;
                          
                                    return countryCode.isoCode;
                           
                    }
            }
    return "";
}
        
        /**
         * returns country name based on iso code
         * 
         * @param isoCode
         * @return
         */
        public String[] getCountryNamesByCode(String isoCodes[]) {
                String countryNames[] = new String[isoCodes.length];
                int size = 0;
                for (int i = 0; i < isoCodes.length; i++) {
                        CountryCode cc = (CountryCode) countries.get(isoCodes[i]);
                        if (cc != null) {
                                countryNames[i] = cc.getCountryName() + " - " + isoCodes[i];
                                size++;
                        }
                }

                return countryNames;
        }

        public String getTeliphoneCodeByIsoCode(String string) {
                CountryCode cc = (CountryCode) countries.get(string);
                return "+" + cc.getTelephoneCode().trim();
        }

}

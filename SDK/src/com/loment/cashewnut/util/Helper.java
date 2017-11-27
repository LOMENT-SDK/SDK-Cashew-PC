package com.loment.cashewnut.util;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;

import com.loment.cashewnut.AppConfiguration;
import com.loment.cashewnut.database.mappers.DBGroupsMapper;
import com.loment.cashewnut.model.GroupModel;

public class Helper {

    public static final String[] MONTHS1 = {"Jan", "Feb", "Mar", "Apr", "May",
        "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static final String[] MONTHS = {"01", "02", "03", "04", "05", "06",
        "07", "08", "09", "10", "11", "12"};

    public static String dateToStringFull(Date date) {
        try {
            Calendar cal = Calendar.getInstance();

            if (date != null) {
                cal.setTime(date);
            } else {
                cal.setTime(new Date()); // now
            }
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);

            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);

            String[] AMPM = {"am", "pm"};
            String am_pm = (AMPM[cal.get(Calendar.AM_PM)]);
            if (hour > 12) {
                hour = hour - 12;
            }
            return ((day < 10) ? "0" + day : "" + day) + "-"
                    + MONTHS1[month - 1] + "-" + year + " "
                    + ((hour < 10) ? "0" + hour : "" + hour) + ":"
                    + ((minute < 10) ? "0" + minute : "" + minute) + " "
                    + am_pm;

        } catch (Exception e) {
            return "" + date.getTime();
        }
    }

    public static String dateToStringFull2(Date date) {
        try {
            Calendar cal = Calendar.getInstance();

            if (date != null) {
                cal.setTime(date);
            } else {
                cal.setTime(new Date()); // now
            }
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);

            int hour = cal.get(Calendar.HOUR);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);

            String[] AMPM = {"am", "pm"};
            String am_pm = (AMPM[cal.get(Calendar.AM_PM)]);

            return ((day < 10) ? "0" + day : "" + day) + " "
                    + MONTHS[month - 1] + " " + year + " "
                    + ((hour < 10) ? "0" + hour : "" + hour) + ":"
                    + ((minute < 10) ? "0" + minute : "" + minute) + " "
                    + am_pm;
        } catch (Exception e) {
            return "" + date.getTime();
        }
    }

    public static String dateToShortString(Date date) {

        try {
            Calendar cal = Calendar.getInstance();

            if (date != null) {
                cal.setTime(date);
            } else {
                cal.setTime(new Date()); // now
            }
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);

            int hour = cal.get(Calendar.HOUR);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);

			// the preverifier doesnt like ( a ? b : c) statements returning
            // different
            // types - therefore this stupid ""+int construct making sure it's
            // always String
            String[] AMPM = {"AM", "PM"};
            String am_pm = (AMPM[cal.get(Calendar.AM_PM)]);

            return +year + "-" + MONTHS1[month - 1] + "-"
                    + ((day < 10) ? "0" + day : "" + day) + "-"
                    + ((hour < 10) ? "0" + hour : "" + hour) + ":"
                    + ((minute < 10) ? "0" + minute : "" + minute) + " "
                    + am_pm;
        } catch (Exception e) {
            return "" + date.getTime();
        }
    }

    public static String replace(String in, String txtToReplace,
            String replaceWith) {
        String[] split = splitUsingStringDelim(in, txtToReplace);
        if (split.length >= 1) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < split.length; i++) {
                sb.append(split[i] + replaceWith);
            }
            String val = sb.delete(sb.length() - replaceWith.length(),
                    sb.length()).toString();
            return val;
        }
        return in;
    }

    public final static String[] splitUsingStringDelim(String original,
            String separator) {
        try {
            Vector nodes = new Vector();
            // Parse nodes into vector
            int index = original.indexOf(separator);
            while (index >= 0) {
                nodes.addElement(original.substring(0, index));
                original = original.substring(index + separator.length());
                index = original.indexOf(separator);
            }
            // Get the last node
            if (original.trim().length() > 0) {
                nodes.addElement(original);
            }

            // Create splitted string array
            String[] result = new String[nodes.size()];
            if (nodes.size() > 0) {
                for (int loop = 0; loop < nodes.size(); loop++) {
                    result[loop] = (String) nodes.elementAt(loop);
                }

            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    public static Vector<String> SplittoVector(String original, String separator) {
        Vector<String> nodes = new Vector<String>();
        // Parse nodes into vector
        int index = original.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(original.substring(0, index));
            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }

        // Get the last node
        nodes.addElement(original);
        return nodes;
    }

	// public static String getCashewnutFolderPath(String body) {
    // final String bodyName = getAbsolutePath(body);
    // File bodyDir = new File(bodyName);
    // if (!bodyDir.exists())
    // bodyDir.mkdirs();
    //
    // String bodyPath = bodyDir.getAbsolutePath();
    // if (!bodyPath.endsWith("//")) {
    // bodyPath = bodyPath + "//";
    // }
    // return bodyPath;
    // }
    public static String getCashewnutFolderPath(String outFile) {
        // File[] roots = File.listRoots();
        File dir = new File(new File(System.getProperty("user.home")),
        		AppConfiguration.getFolderName() + "/" + outFile);
        dir.mkdir();
        String CashewnutDir = dir.getAbsolutePath();
        if (!CashewnutDir.endsWith("//")) {
            CashewnutDir = CashewnutDir + "//";
        }
        return CashewnutDir;
    }

    public static String getCashewnutDatabasePath(String outFile) {
        File dir = new File(new File(System.getProperty("user.home")),
        		AppConfiguration.getFolderName() + "/" + "config");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String CashewnutDir = dir.getAbsolutePath();
        if (!CashewnutDir.endsWith("//")) {
            CashewnutDir = CashewnutDir + "//";
        }
        return CashewnutDir + outFile;
    }

//	public static String getCashewnutFolderPath(String outFile) {
//		try {
//
//			String path = getCashewnutFolderPath() + outFile;
//			File dir = new File(path);
//
//			if (!dir.exists()) {
//				dir.mkdir();
//			}
//
//			if (!path.endsWith("//")) {
//				path = path + "//";
//			}
//
//			return path;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
    public static String getAbsolutePath(String body) {
        File dir = new File(new File(System.getProperty("user.home")),
        		AppConfiguration.getFolderName() + "/" + body);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String bodyName = dir.getAbsolutePath();
        if (!bodyName.endsWith("//")) {
            bodyName = bodyName + "//";
        }
        return bodyName;
    }

	public static boolean compareTwoVectors(List<String> v1, List<String> v2) {
		if (v1.size() != v2.size()) {
			return false;
		}
		for (int i = 0; i < v1.size(); i++) {
			boolean isEquals = false;
			for (int j = 0; j < v2.size(); j++) {
				if (v1.get(i).equals(v2.get(j))) {
					isEquals = true;
				}
			}
			if (!isEquals) {
				return false;
			}
		}
		for (int i = 0; i < v2.size(); i++) {
			boolean isEquals = false;
			for (int j = 0; j < v1.size(); j++) {
				if (v2.get(i).equals(v1.get(j))) {
					isEquals = true;
				}
			}
			if (!isEquals) {
				return false;
			}
		}
		return true;
	}

	public static GroupModel getGroupDetails(final String group_id) {
		try {
			if (group_id != null && !group_id.trim().equalsIgnoreCase("")) {
				final GroupModel gModel = DBGroupsMapper.getInstance()
						.getGroup(group_id, true);
				if (!gModel.getGroupName().equals("")
						&& gModel.getMembers() != null
						&& gModel.getMembers().length() != 0) {
					return gModel;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
     public static String getOs(){
        String platform = "";
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win")) {
            //System.out.println("This is Windows");
            platform = "windows";
        } else if (OS.contains("mac")) {
            System.out.println("This is Mac");
            platform = "macintosh";
        } else if (OS.contains("nix") || OS.contains("nux")
                || OS.indexOf("aix") > 0) {
            System.out.println("This is Unix or Linux");
            platform = "linux";
        } else if (OS.contains("sunos")) {
            System.out.println("This is Solaris");
            platform = "solaris";
        } else {
            System.out.println("Your OS is not support!!");
        }
        return platform;
    }
     
     public static JSONArray removeJSONArrayIndex(JSONArray jSONArray,
 			Vector<Integer> removedIndexes) {
 		JSONArray newjSONArray = new JSONArray();
 		if (jSONArray != null) {
 			for (int i = 0; jSONArray.length() > i; i++) {
 				if (!removedIndexes.contains(i)) {
 					try {
 						newjSONArray.put(jSONArray.get(i));
 					} catch (JSONException e) {
 						e.printStackTrace();
 						return jSONArray;
 					}
 				}
 			}
 			return newjSONArray;
 		}
 		return jSONArray;
 	}
}

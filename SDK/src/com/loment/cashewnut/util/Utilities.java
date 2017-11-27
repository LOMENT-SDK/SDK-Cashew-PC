//package com.loment.cashewnut.util;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.channels.FileChannel;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Hashtable;
//import java.util.zip.GZIPOutputStream;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.ContentUris;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Point;
//import android.graphics.Typeface;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Environment;
//import android.provider.DocumentsContract;
//import android.provider.MediaStore;
//import android.text.Html;
//import android.text.SpannableStringBuilder;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//
//import com.loment.cashew.R;
//import com.loment.cashewnut.CashewnutApplication;
//
//public class Utilities {
//
//	public static float density = 1;
//	public static Point displaySize = new Point();
//
//	public static byte[] compress(byte[] data) {
//		if (data == null) {
//			return null;
//		}
//
//		byte[] packedData = null;
//		ByteArrayOutputStream bytesStream = new ByteArrayOutputStream();
//		try {
//			GZIPOutputStream zip = new GZIPOutputStream(bytesStream);
//			zip.write(data);
//			zip.close();
//			packedData = bytesStream.toByteArray();
//		} catch (IOException e) {
//			// FileLog.e("tmessages", e);
//		}
//		return packedData;
//	}
//
//	public static int dp(int value) {
//		return (int) (density * value);
//	}
//
//	private static final String TAG = "Typefaces";
//	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();
//
//	public static Typeface getTypeface(String assetPath) {
//		synchronized (cache) {
//			if (!cache.containsKey(assetPath)) {
//				try {
//					Typeface t = Typeface
//							.createFromAsset(
//									CashewnutApplication.context.getAssets(),
//									assetPath);
//					cache.put(assetPath, t);
//				} catch (Exception e) {
//					// FileLog.e(TAG, "Could not get typeface '" + assetPath +
//					// "' because " + e.getMessage());
//					return null;
//				}
//			}
//			return cache.get(assetPath);
//		}
//	}
//
//	public static void showKeyboard(View view) {
//		if (view == null) {
//			return;
//		}
//		InputMethodManager inputManager = (InputMethodManager) view
//				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//		inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
//
//		((InputMethodManager) view.getContext().getSystemService(
//				Context.INPUT_METHOD_SERVICE)).showSoftInput(view, 0);
//	}
//
//	public static boolean isKeyboardShowed(View view) {
//		if (view == null) {
//			return false;
//		}
//		InputMethodManager inputManager = (InputMethodManager) view
//				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//		return inputManager.isActive(view);
//	}
//
//	public static void hideKeyboard(View view) {
//		if (view == null) {
//			return;
//		}
//		InputMethodManager imm = (InputMethodManager) view.getContext()
//				.getSystemService(Context.INPUT_METHOD_SERVICE);
//		if (!imm.isActive()) {
//			return;
//		}
//		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//	}
//
//	public static ProgressDialog progressDialog;
//
//	public static void ShowProgressDialog(final Activity activity,
//			final String message) {
//		activity.runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				if (!activity.isFinishing()) {
//					progressDialog = new ProgressDialog(activity);
//					if (message != null) {
//						progressDialog.setMessage(message);
//					}
//					progressDialog.setCanceledOnTouchOutside(false);
//					progressDialog.setCancelable(false);
//					progressDialog.show();
//				}
//			}
//		});
//	}
//
//	public static void HideProgressDialog(Activity activity) {
//		activity.runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				if (progressDialog != null) {
//					progressDialog.dismiss();
//				}
//			}
//		});
//	}
//
//	public static boolean copyFile(File sourceFile, File destFile)
//			throws IOException {
//		if (!destFile.exists()) {
//			destFile.createNewFile();
//		}
//		FileChannel source = null;
//		FileChannel destination = null;
//		boolean result = true;
//		try {
//			source = new FileInputStream(sourceFile).getChannel();
//			destination = new FileOutputStream(destFile).getChannel();
//			destination.transferFrom(source, 0, source.size());
//		} catch (Exception e) {
//			// FileLog.e("tmessages", e);
//			result = false;
//		} finally {
//			if (source != null) {
//				source.close();
//			}
//			if (destination != null) {
//				destination.close();
//			}
//		}
//		return result;
//	}
//
//	public static void addMediaToGallery(String fromPath) {
//		if (fromPath == null) {
//			return;
//		}
//		Intent mediaScanIntent = new Intent(
//				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//		File f = new File(fromPath);
//		Uri contentUri = Uri.fromFile(f);
//		mediaScanIntent.setData(contentUri);
//		CashewnutApplication.context.sendBroadcast(mediaScanIntent);
//	}
//
//	public static void addMediaToGallery(Uri uri) {
//		if (uri == null) {
//			return;
//		}
//		Intent mediaScanIntent = new Intent(
//				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//		mediaScanIntent.setData(uri);
//		CashewnutApplication.context.sendBroadcast(mediaScanIntent);
//	}
//
//	private static File getAlbumDir() {
//		File storageDir = null;
//		if (Environment.MEDIA_MOUNTED.equals(Environment
//				.getExternalStorageState())) {
//			storageDir = new File(
//					Environment
//							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//					CashewnutApplication.context.getResources().getString(
//							R.string.app_name));
//			if (storageDir != null) {
//				if (!storageDir.mkdirs()) {
//					if (!storageDir.exists()) {
//						// FileLog.d("tmessages", "failed to create directory");
//						return null;
//					}
//				}
//			}
//		} else {
//			// FileLog.d("tmessages",
//			// "External storage is not mounted READ/WRITE.");
//		}
//
//		return storageDir;
//	}
//
//	private static File getMediaDir() {
//		File storageDir = null;
//		if (Environment.MEDIA_MOUNTED.equals(Environment
//				.getExternalStorageState())) {
//			storageDir = new File(
//					Environment
//							.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
//					CashewnutApplication.context.getResources().getString(
//							R.string.app_name));
//			if (storageDir != null) {
//				if (!storageDir.mkdirs()) {
//					if (!storageDir.exists()) {
//						// FileLog.d("tmessages", "failed to create directory");
//						return null;
//					}
//				}
//			}
//		} else {
//			// FileLog.d("tmessages",
//			// "External storage is not mounted READ/WRITE.");
//		}
//
//		return storageDir;
//	}
//
//	@SuppressLint("NewApi")
//	public static String getPath(final Uri uri) {
//		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//		if (isKitKat
//				&& DocumentsContract.isDocumentUri(
//						CashewnutApplication.context, uri)) {
//			if (isExternalStorageDocument(uri)) {
//				final String docId = DocumentsContract.getDocumentId(uri);
//				final String[] split = docId.split(":");
//				final String type = split[0];
//				if ("primary".equalsIgnoreCase(type)) {
//					return Environment.getExternalStorageDirectory() + "/"
//							+ split[1];
//				}
//			} else if (isDownloadsDocument(uri)) {
//				final String id = DocumentsContract.getDocumentId(uri);
//				final Uri contentUri = ContentUris.withAppendedId(
//						Uri.parse("content://downloads/public_downloads"),
//						Long.valueOf(id));
//				return getDataColumn(CashewnutApplication.context, contentUri,
//						null, null);
//			} else if (isMediaDocument(uri)) {
//				final String docId = DocumentsContract.getDocumentId(uri);
//				final String[] split = docId.split(":");
//				final String type = split[0];
//
//				Uri contentUri = null;
//				if ("image".equals(type)) {
//					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//				} else if ("video".equals(type)) {
//					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//				} else if ("audio".equals(type)) {
//					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//				}
//
//				final String selection = "_id=?";
//				final String[] selectionArgs = new String[] { split[1] };
//
//				return getDataColumn(CashewnutApplication.context, contentUri,
//						selection, selectionArgs);
//			}
//		} else if ("content".equalsIgnoreCase(uri.getScheme())) {
//			return getDataColumn(CashewnutApplication.context, uri, null, null);
//		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
//			return uri.getPath();
//		}
//
//		return null;
//	}
//
//	public static String getDataColumn(Context context, Uri uri,
//			String selection, String[] selectionArgs) {
//
//		Cursor cursor = null;
//		final String column = "_data";
//		final String[] projection = { column };
//
//		try {
//			cursor = context.getContentResolver().query(uri, projection,
//					selection, selectionArgs, null);
//			if (cursor != null && cursor.moveToFirst()) {
//				final int column_index = cursor.getColumnIndexOrThrow(column);
//				return cursor.getString(column_index);
//			}
//		} catch (Exception e) {
//			// FileLog.e("tmessages", e);
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//		}
//		return null;
//	}
//
//	public static boolean isExternalStorageDocument(Uri uri) {
//		return "com.android.externalstorage.documents".equals(uri
//				.getAuthority());
//	}
//
//	public static boolean isDownloadsDocument(Uri uri) {
//		return "com.android.providers.downloads.documents".equals(uri
//				.getAuthority());
//	}
//
//	public static boolean isMediaDocument(Uri uri) {
//		return "com.android.providers.media.documents".equals(uri
//				.getAuthority());
//	}
//
//	public static File generatePicturePath() {
//		try {
//			File storageDir = getAlbumDir();
//			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
//					.format(new Date());
//			String imageFileName = "IMG_" + timeStamp + "_";
//			return File.createTempFile(imageFileName, ".jpg", storageDir);
//		} catch (Exception e) {
//			// FileLog.e("tmessages", e);
//		}
//		return null;
//	}
//
//	public static CharSequence generateSearchName(String name, String name2,
//			String q) {
//		if (name == null && name2 == null) {
//			return "";
//		}
//		int index;
//		SpannableStringBuilder builder = new SpannableStringBuilder();
//		String wholeString = name;
//		if (wholeString == null || wholeString.length() == 0) {
//			wholeString = name2;
//		} else if (name2 != null && name2.length() != 0) {
//			wholeString += " " + name2;
//		}
//		wholeString = wholeString.trim();
//		String[] args = wholeString.split(" ");
//
//		for (String arg : args) {
//			String str = arg;
//			if (str != null) {
//				String lower = str.toLowerCase();
//				if (lower.startsWith(q)) {
//					if (builder.length() != 0) {
//						builder.append(" ");
//					}
//					String query = str.substring(0, q.length());
//					builder.append(Html.fromHtml("<font color=\"#357aa8\">"
//							+ query + "</font>"));
//					str = str.substring(q.length());
//					builder.append(str);
//				} else {
//					if (builder.length() != 0) {
//						builder.append(" ");
//					}
//					builder.append(str);
//				}
//			}
//		}
//		return builder;
//	}
//
//	public static File generateVideoPath() {
//		try {
//			File storageDir = getAlbumDir();
//			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
//					.format(new Date());
//			String imageFileName = "VID_" + timeStamp + "_";
//			return File.createTempFile(imageFileName, ".mp4", storageDir);
//			/*
//			 * 
//			 * String fileName = "VID" + id + ".mp4"; return new
//			 * File(CashewnutApplication.context.getCacheDir(), fileName);
//			 */
//		} catch (Exception e) {
//			// FileLog.e("tmessages", e);
//		}
//		return null;
//	}
//
//	public static File generateAudioPath() {
//		try {
//			File storageDir = getMediaDir();
//			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
//					.format(new Date());
//			String imageFileName = "AID_" + timeStamp + "_";
//			return File.createTempFile(imageFileName, ".mp3", storageDir);
//			/*
//			 * 
//			 * String fileName = "VID" + id + ".mp4"; return new
//			 * File(CashewnutApplication.context.getCacheDir(), fileName);
//			 */
//		} catch (Exception e) {
//			// FileLog.e("tmessages", e);
//		}
//		return null;
//	}
//
//	public static String formatName(String firstName, String lastName) {
//		String result = firstName;
//		if (result == null || result.length() == 0) {
//			result = lastName;
//		} else if (result.length() != 0 && lastName.length() != 0) {
//			result += " " + lastName;
//		}
//		return result;
//	}
//
//	public static String formatFileSize(long size) {
//		if (size < 1024) {
//			return String.format("%d B", size);
//		} else if (size < 1024 * 1024) {
//			return String.format("%.1f KB", size / 1024.0f);
//		} else if (size < 1024 * 1024 * 1024) {
//			return String.format("%.1f MB", size / 1024.0f / 1024.0f);
//		} else {
//			return String.format("%.1f GB", size / 1024.0f / 1024.0f / 1024.0f);
//		}
//	}
//
//}

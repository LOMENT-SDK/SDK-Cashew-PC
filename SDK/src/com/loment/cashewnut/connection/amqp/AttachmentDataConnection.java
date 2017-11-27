package com.loment.cashewnut.connection.amqp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;

import com.loment.cashewnut.AppConfiguration;

//import android.net.http.AndroidHttpClient;
public class AttachmentDataConnection {

	private static AttachmentDataConnection instance = null;
	private static final String attUploadedUrl = AppConfiguration.getAttachmentApi()
			+ "/uploadAtt.php";
	private static final String attDownloadUrl = AppConfiguration.getAttachmentApi()
			+ "/downloadAtt.php";

	// private static String attUploadedUrl = "http://10.1.1.6/uploadAtt.php";
	// private static String attDownloadUrl = "http://10.1.1.6/downloadAtt.php";
	private static final PasswordAuthentication _uplaodCreds = new PasswordAuthentication(
			"cashew_upload", "efgmn245tmnewfmn245g".toCharArray());

	private static final PasswordAuthentication _downloadCreds = new PasswordAuthentication(
			"cashew_download", "efgmn245tmnewfmn245g".toCharArray());

	public static AttachmentDataConnection getInstance() throws IOException {
		if (instance == null) {
			instance = new AttachmentDataConnection();
		}
		return instance;
	}

	public String uploadAttachment(String filePath, String token) {
		String response = "";
		try {
			InputStream inputStream = new FileInputStream(filePath);
			String CrLf = "\r\n";
			URL url = new URL(attUploadedUrl);

			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			Authenticator.setDefault(new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return _uplaodCreds;
				}
			});

			conn.setDoOutput(true);

			byte[] imgData = new byte[inputStream.available()];
			inputStream.read(imgData);

			String message1 = "";
			message1 += "-----------------------------4664151417711" + CrLf;
			message1 += "Content-Disposition: form-data; name=\"file\";"
					+ " filename=\"test.txt\"" + CrLf;
			message1 += "Content-Type: application/octet-stream" + CrLf;
			message1 += CrLf;

			// the image is sent between the messages in the multipart message.
			String message2 = "";
			message2 += CrLf + "-----------------------------4664151417711--"
					+ CrLf;

			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=---------------------------"
							+ "4664151417711");
			// might not need to specify the content-length when sending chunked
			// data.
			conn.setRequestProperty("Content-Length", String.valueOf((message1
					.length() + message2.length() + imgData.length)));

			// System.out.println("open os" + String.valueOf((message1
			// .length() + message2.length() + imgData.length)));
			// System.out.println(message1);
			// System.out.println(message2);
			OutputStream os = conn.getOutputStream();
			os.write(message1.getBytes());

			// Send the file
			int index = 0;
			int size = 1024;
			do {
				// System.out.println("write:" + index);
				if ((index + size) > imgData.length) {
					size = imgData.length - index;
				}
				os.write(imgData, index, size);
				index += size;
			} while (index < imgData.length);
			// System.out.println("written:" + index);

			// System.out.println(message2);
			os.write(message2.getBytes());
			os.flush();

			InputStream is = conn.getInputStream();
			final byte[] buffer = new byte[8196];
			int readCount;
			final StringBuilder builder = new StringBuilder();
			while ((readCount = is.read(buffer)) > -1) {
				builder.append(new String(buffer, 0, readCount));
			}
			response = builder.toString();
			// System.out.println(response);
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public boolean downloadAttachment(String fileName, String localFileName) {
		try {
			// System.out.println(fileName);
			String post = "file_name=" + fileName + "&auth_key="
					+ RPCClientSender.getInstance().getAuthKey();
			URL url = new URL(attDownloadUrl);
			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			Authenticator.setDefault(new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return _downloadCreds;
				}
			});

			conn.setDoOutput(true);

			// might not need to specify the content-length when sending chunked
			// data.
			conn.setRequestProperty("Content-Length",
					String.valueOf((post.length())));
			OutputStream os = conn.getOutputStream();
			os.write(post.getBytes());
			os.flush();

			InputStream bis = conn.getInputStream();
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(new File(localFileName)));
			int inByte;
			while ((inByte = bis.read()) != -1) {
				if (inByte != '\r' && inByte != '\n') {
					bos.write(inByte);
				}
			}
			conn.disconnect();
			bis.close();
			bos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}

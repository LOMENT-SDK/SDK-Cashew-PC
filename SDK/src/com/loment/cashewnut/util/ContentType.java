/*
 * Copyright (C) 2007-2008 Esmertec AG.
 * Copyright (C) 2007-2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.loment.cashewnut.util;

import java.util.ArrayList;

public class ContentType {

	public static final String TEXT_PLAIN = "text/plain";
	public static final String TEXT_HTML = "text/html";
	public static final String TEXT_VCALENDAR = "text/x-vCalendar";
	public static final String TEXT_VCARD = "text/x-vCard";
	
	public static final String APP_PDF = "application/pdf";

	public static final String IMAGE_UNSPECIFIED = "image/*";
	public static final String IMAGE_JPEG = "image/jpeg";
	public static final String IMAGE_JPG = "image/jpg";
	public static final String IMAGE_GIF = "image/gif";
	public static final String IMAGE_WBMP = "image/vnd.wap.wbmp";
	public static final String IMAGE_PNG = "image/png";
	public static final String IMAGE_BMP = "image/bmp";
	public static final String IMAGE_WEBP = "image/webp";

	public static final String AUDIO_UNSPECIFIED = "audio/*";
	public static final String AUDIO_AAC = "audio/aac";
	public static final String AUDIO_AMR = "audio/amr";
	public static final String AUDIO_IMELODY = "audio/imelody";
	public static final String AUDIO_MID = "audio/mid";
	public static final String AUDIO_MIDI = "audio/midi";
	public static final String AUDIO_MPEG3 = "audio/mpeg3";
	public static final String AUDIO_MPEG = "audio/mpeg";
	public static final String AUDIO_MP3 = "audio/mp3";
	public static final String AUDIO_MPG = "audio/mpg";
	public static final String AUDIO_MP4 = "audio/mp4";
	public static final String AUDIO_X_MID = "audio/x-mid";
	public static final String AUDIO_X_MIDI = "audio/x-midi";
	public static final String AUDIO_X_MP3 = "audio/x-mp3";
	public static final String AUDIO_X_MPEG3 = "audio/x-mpeg3";
	public static final String AUDIO_X_MPEG = "audio/x-mpeg";
	public static final String AUDIO_X_MPG = "audio/x-mpg";
	public static final String AUDIO_3GPP = "audio/3gpp";
	public static final String AUDIO_OGG = "audio/ogg";
	public static final String AUDIO_M4A = "audio/m4a";
	public static final String AUDIO_3GP = "audio/3gp";
        public static final String VIDEO_FLV = "video/flv";
	
	public static final String AUDIO_WAVE = "audio/wav";
	public static final String AUDIO_VORBIS = "audio/mkv";
	public static final String AUDIO_FLAC = "audio/flac";
	
	

	public static final String VIDEO_UNSPECIFIED = "video/*";
	public static final String VIDEO_3GPP = "video/3gp";
	public static final String VIDEO_3G2 = "video/3gpp2";
	public static final String VIDEO_H263 = "video/h263";
	public static final String VIDEO_MP4 = "video/mp4";
	
	
	public static final String VIDEO_AAC = "video/aac";
	public static final String VIDEO_WEBM = "video/webm";
	public static final String VIDEO_MKV = "video/mkv";

	public static final String APP_SMIL = "application/smil";
	public static final String APP_WAP_XHTML = "application/vnd.wap.xhtml+xml";
	public static final String APP_XHTML = "application/xhtml+xml";

	public static final String APP_DRM_CONTENT = "application/vnd.oma.drm.content";
	public static final String APP_DRM_MESSAGE = "application/vnd.oma.drm.message";

	private static final ArrayList<String> sSupportedContentTypes = new ArrayList<String>();
	private static final ArrayList<String> sSupportedImageTypes = new ArrayList<String>();
	private static final ArrayList<String> sSupportedAudioTypes = new ArrayList<String>();
	private static final ArrayList<String> sSupportedVideoTypes = new ArrayList<String>();
	
//	private static final ArrayList<String> sSupportedDocumentTypes = new ArrayList<String>();

	static {
		sSupportedContentTypes.add(TEXT_PLAIN);
		sSupportedContentTypes.add(TEXT_HTML);
		sSupportedContentTypes.add(TEXT_VCALENDAR);
		sSupportedContentTypes.add(TEXT_VCARD);

		sSupportedContentTypes.add(IMAGE_JPEG);
		sSupportedContentTypes.add(IMAGE_GIF);
		sSupportedContentTypes.add(IMAGE_WBMP);
		sSupportedContentTypes.add(IMAGE_PNG);
		sSupportedContentTypes.add(IMAGE_JPG);
		sSupportedContentTypes.add(IMAGE_BMP);
		sSupportedContentTypes.add(IMAGE_WEBP);
		// supportedContentTypes.add(IMAGE_SVG); not yet supported.

		sSupportedContentTypes.add(AUDIO_AAC);
		sSupportedContentTypes.add(AUDIO_AMR);
		sSupportedContentTypes.add(AUDIO_IMELODY);
		sSupportedContentTypes.add(AUDIO_MID);
		sSupportedContentTypes.add(AUDIO_MIDI);
		sSupportedContentTypes.add(AUDIO_MP3);
		sSupportedContentTypes.add(AUDIO_MPEG3);
		sSupportedContentTypes.add(AUDIO_MPEG);
		sSupportedContentTypes.add(AUDIO_MPG);
		sSupportedContentTypes.add(AUDIO_X_MID);
		sSupportedContentTypes.add(AUDIO_X_MIDI);
		sSupportedContentTypes.add(AUDIO_X_MP3);
		sSupportedContentTypes.add(AUDIO_X_MPEG3);
		sSupportedContentTypes.add(AUDIO_X_MPEG);
		sSupportedContentTypes.add(AUDIO_X_MPG);
		sSupportedContentTypes.add(AUDIO_3GPP);
		sSupportedContentTypes.add(AUDIO_OGG);
		sSupportedContentTypes.add(AUDIO_M4A);
		sSupportedContentTypes.add(AUDIO_WAVE);
		sSupportedContentTypes.add(AUDIO_OGG);
		sSupportedContentTypes.add(AUDIO_VORBIS);
		sSupportedContentTypes.add(AUDIO_FLAC);
                sSupportedContentTypes.add(VIDEO_FLV);

		sSupportedContentTypes.add(VIDEO_3GPP);
		sSupportedContentTypes.add(VIDEO_3G2);
		sSupportedContentTypes.add(VIDEO_H263);
		sSupportedContentTypes.add(VIDEO_MP4);
		sSupportedContentTypes.add(VIDEO_AAC);
		sSupportedContentTypes.add(VIDEO_WEBM);
		sSupportedContentTypes.add(VIDEO_MKV);
		
		sSupportedContentTypes.add(APP_SMIL);
		sSupportedContentTypes.add(APP_WAP_XHTML);
		sSupportedContentTypes.add(APP_XHTML);

		sSupportedContentTypes.add(APP_DRM_CONTENT);
		sSupportedContentTypes.add(APP_DRM_MESSAGE);

		// add supported image types
		sSupportedImageTypes.add(IMAGE_JPEG);
		sSupportedImageTypes.add(IMAGE_GIF);
		sSupportedImageTypes.add(IMAGE_WBMP);
		sSupportedImageTypes.add(IMAGE_PNG);
		sSupportedImageTypes.add(IMAGE_JPG);
		sSupportedImageTypes.add(IMAGE_BMP);
		sSupportedImageTypes.add(IMAGE_WEBP);
		// add supported audio types
		sSupportedAudioTypes.add(AUDIO_AAC);
		sSupportedAudioTypes.add(AUDIO_AMR);
		sSupportedAudioTypes.add(AUDIO_IMELODY);
		sSupportedAudioTypes.add(AUDIO_MID);
		sSupportedAudioTypes.add(AUDIO_MIDI);
		sSupportedAudioTypes.add(AUDIO_MP3);
		sSupportedAudioTypes.add(AUDIO_MPEG3);
		sSupportedAudioTypes.add(AUDIO_MPEG);
		sSupportedAudioTypes.add(AUDIO_MPG);
		sSupportedAudioTypes.add(AUDIO_MP4);
		sSupportedAudioTypes.add(AUDIO_X_MID);
		sSupportedAudioTypes.add(AUDIO_X_MIDI);
		sSupportedAudioTypes.add(AUDIO_X_MP3);
		sSupportedAudioTypes.add(AUDIO_X_MPEG3);
		sSupportedAudioTypes.add(AUDIO_X_MPEG);
		sSupportedAudioTypes.add(AUDIO_X_MPG);
		sSupportedAudioTypes.add(AUDIO_3GPP);
		sSupportedAudioTypes.add(AUDIO_OGG);
		sSupportedAudioTypes.add(AUDIO_M4A);
		sSupportedAudioTypes.add(AUDIO_3GP);
		sSupportedAudioTypes.add(AUDIO_WAVE);
		sSupportedAudioTypes.add(AUDIO_VORBIS);
		sSupportedAudioTypes.add(AUDIO_FLAC);

		// add supported video types
		sSupportedVideoTypes.add(VIDEO_3GPP);
		sSupportedVideoTypes.add(VIDEO_3G2);
		sSupportedVideoTypes.add(VIDEO_H263);
		sSupportedVideoTypes.add(VIDEO_MP4);
		sSupportedVideoTypes.add(VIDEO_AAC);
		sSupportedVideoTypes.add(VIDEO_WEBM);
		sSupportedVideoTypes.add(VIDEO_MKV);
                sSupportedVideoTypes.add(VIDEO_FLV);
	}

	// This class should never be instantiated.
	private ContentType() {
	}

	public static boolean isSupportedType(String contentType) {
		return (null != contentType)
				&& sSupportedContentTypes.contains(contentType);
	}

	public static boolean isSupportedImageType(String contentType) {
		return (null != contentType)&& sSupportedImageTypes.contains(contentType);
	}

	public static boolean isSupportedAudioType(String contentType) {
		return (null != contentType)&& sSupportedAudioTypes.contains(contentType);
	}

	public static boolean isSupportedVideoType(String contentType) {
		return (null != contentType)&& sSupportedVideoTypes.contains(contentType);
	}

	public static boolean isTextType(String contentType) {
		return (null != contentType) && contentType.startsWith("text/");
	}

	public static boolean isImageType(String contentType) {
		return (null != contentType) && contentType.startsWith("image/");
	}

	public static boolean isAudioType(String contentType) {
		return (null != contentType) && contentType.startsWith("audio/");
	}

	public static boolean isVideoType(String contentType) {
		return (null != contentType) && contentType.startsWith("video/");
	}

	public static boolean isDrmType(String contentType) {
		return (null != contentType)
				&& (contentType.equals(APP_DRM_CONTENT) || contentType
						.equals(APP_DRM_MESSAGE));
	}

	public static boolean isUnspecified(String contentType) {
		return (null != contentType) && contentType.endsWith("*");
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String> getImageTypes() {
		return (ArrayList<String>) sSupportedImageTypes.clone();
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String> getAudioTypes() {
		return (ArrayList<String>) sSupportedAudioTypes.clone();
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String> getVideoTypes() {
		return (ArrayList<String>) sSupportedVideoTypes.clone();
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String> getSupportedTypes() {
		return (ArrayList<String>) sSupportedContentTypes.clone();
	}

	public static String getContentType(String attachmentPath) {
		String extension = "";
		int i = attachmentPath.lastIndexOf('.');
		int p = Math.max(attachmentPath.lastIndexOf('/'),
				attachmentPath.lastIndexOf('\\'));
		if (i != -1 && i > p) {
			extension = attachmentPath.substring(i + 1);
		}

		if (extension != null && extension.trim().length() > 0) {
			if (extension.toLowerCase().equalsIgnoreCase("pdf")) {
				return APP_PDF;
			}
			if (extension.toLowerCase().equalsIgnoreCase("txt")) {
				return TEXT_PLAIN;
			}

			if (extension.toLowerCase().equalsIgnoreCase("jpeg")) {
				return IMAGE_JPEG;
			}
			if (extension.toLowerCase().equalsIgnoreCase("jpg")) {
				return IMAGE_JPG;
			}
			if (extension.toLowerCase().equalsIgnoreCase("gif")) {
				return IMAGE_GIF;
			}
			if (extension.toLowerCase().equalsIgnoreCase("png")) {
				return IMAGE_PNG;
			}
			if (extension.toLowerCase().equalsIgnoreCase("webp")) {
				return IMAGE_WEBP;
			}
			if (extension.toLowerCase().equalsIgnoreCase("bmp")) {
				return IMAGE_BMP;
			}
		
			if (extension.toLowerCase().equalsIgnoreCase("mp4")) {
				return VIDEO_MP4;
			}
			
			if (extension.toLowerCase().equalsIgnoreCase("aac")) {
				return VIDEO_AAC;
			}
			if (extension.toLowerCase().equalsIgnoreCase("webm")) {
				return VIDEO_WEBM;
			}
			if (extension.toLowerCase().equalsIgnoreCase("mkv")) {
				return VIDEO_MKV;
			}
			
			
			
			if (extension.toLowerCase().equalsIgnoreCase("h263")) {
				return VIDEO_H263;
			}
			if (extension.toLowerCase().equalsIgnoreCase("3gpp2")) {
				return VIDEO_3G2;
			}
			if (extension.toLowerCase().equalsIgnoreCase("3gp")) {
				return VIDEO_3GPP;
			}
			
			if (extension.toLowerCase().equalsIgnoreCase("aac")) {
				return AUDIO_AAC;
			}
			if (extension.toLowerCase().equalsIgnoreCase("amr")) {
				return AUDIO_AMR;
			}
			if (extension.toLowerCase().equalsIgnoreCase("imelody")) {
				return AUDIO_IMELODY;
			}
			if (extension.toLowerCase().equalsIgnoreCase("mid")) {
				return AUDIO_MID;
			}
			if (extension.toLowerCase().equalsIgnoreCase("midi")) {
				return AUDIO_MIDI;
			}
			if (extension.toLowerCase().equalsIgnoreCase("mp3")) {
				return AUDIO_MP3;
			}
			if (extension.toLowerCase().equalsIgnoreCase("mpeg3")) {
				return AUDIO_MPEG3;
			}
			if (extension.toLowerCase().equalsIgnoreCase("mpeg")) {
				return AUDIO_MPEG;
			}
			if (extension.toLowerCase().equalsIgnoreCase("mpg")) {
				return AUDIO_MPG;
			}
			if (extension.toLowerCase().equalsIgnoreCase("mp4")) {
				return AUDIO_MP4;
			}
			if (extension.toLowerCase().equalsIgnoreCase("x-mid")) {
				return AUDIO_X_MID;
			}
			if (extension.toLowerCase().equalsIgnoreCase("x-midi")) {
				return AUDIO_X_MIDI;
			}
			if (extension.toLowerCase().equalsIgnoreCase("x-mp3")) {
				return AUDIO_X_MP3;
			}
			if (extension.toLowerCase().equalsIgnoreCase("x-mpeg3")) {
				return AUDIO_X_MPEG3;
			}
			if (extension.toLowerCase().equalsIgnoreCase("x-mpeg")) {
				return AUDIO_X_MPEG;
			}
			if (extension.toLowerCase().equalsIgnoreCase("x-mpg")) {
				return AUDIO_X_MPG;
			}
			if (extension.toLowerCase().equalsIgnoreCase("3gpp")) {
				return AUDIO_3GPP;
			}
			if (extension.toLowerCase().equalsIgnoreCase("ogg")) {
				return AUDIO_OGG;
			}
			if (extension.toLowerCase().equalsIgnoreCase("3gp")) {
				return AUDIO_3GP;
			}
			if (extension.toLowerCase().equalsIgnoreCase("m4a")) {
				return AUDIO_M4A;
			}
			if (extension.toLowerCase().equalsIgnoreCase("wav")) {
				return AUDIO_WAVE;
			}
			if (extension.toLowerCase().equalsIgnoreCase("mkv")) {
				return AUDIO_VORBIS;
			}
			if (extension.toLowerCase().equalsIgnoreCase("flac")) {
				return AUDIO_FLAC;
			}
                        if (extension.toLowerCase().equalsIgnoreCase("flv")) {
				return VIDEO_FLV;
			}

		}
		return extension;
	}
}

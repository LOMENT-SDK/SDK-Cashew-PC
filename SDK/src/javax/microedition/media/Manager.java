package javax.microedition.media;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


//import com.sun.lwuit.impl.android.AndroidImplementationFactory;
//import com.sun.lwuit.impl.android.LWUITActivity;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.media.AudioFormat;
//import android.media.AudioManager;
//import android.media.AudioTrack;
//import android.media.MediaPlayer;
//import android.media.RingtoneManager;
//import android.net.Uri;

public class Manager {
	private final static int duration = 3; // seconds
    private final static int sampleRate = 8000;
    private final static int numSamples = duration * sampleRate;
    private final static double sample[] = new double[numSamples];
    private final static double freqOfTone = 440; // hz

    private static byte generatedSnd[] = new byte[2 * numSamples];
    
//    static{
//    	genTone();
//    }

	public static void playTone(int i, int j, int k) {
		// TODO Auto-generated method stub
//		playSound();
//		NotificationManager mNotificationManager = (NotificationManager)LWUITActivity.currentActivity.getSystemService(Context.NOTIFICATION_SERVICE);
//	   	long when = System.currentTimeMillis();
//	
//	   	Notification notification = new Notification(-1,null, when);
//	   	notification.defaults |= Notification.DEFAULT_SOUND;
//	   	mNotificationManager.notify(0, notification);
//		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        if(alert == null){  // I can't see this ever being null (as always have a default notification) but just incase
//            // alert backup is null, using 2nd backup
//            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);               
//        }
//        try {
//        	MediaPlayer mediaPlayer = new MediaPlayer();
//        	mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
//        	mediaPlayer.setDataSource(LWUITActivity.currentActivity, alert);
//        	mediaPlayer.prepare();
//        	mediaPlayer.start();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			sound(440,500,1.0);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void sound(int hz, int msecs, double vol)
	throws IllegalArgumentException, LineUnavailableException {

		if (vol > 1.0 || vol < 0.0)
			throw new IllegalArgumentException("Volume out of range 0.0	- 1.0");

		byte[] buf = new byte[msecs * 8];

		for (int i=0; i<buf.length; i++) {
			double angle = i / (8000.0 / hz) * 2.0 * Math.PI;
			buf[i] = (byte)(Math.sin(angle) * 127.0 * vol);
		}

		// shape the front and back ends of the wave form
		for (int i=0; i<20 && i < buf.length / 2; i++) {
			buf[i] = (byte)(buf[i] * i / 20);
			buf[buf.length - 1 - i] = (byte)(buf[buf.length - 1 - i] *
					i / 20);
		}

		AudioFormat af = new AudioFormat(8000f,8,1,true,false);
		SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
		sdl.open(af);
		sdl.start();
		sdl.write(buf,0,buf.length);
		sdl.drain();
		sdl.close();
	}

	static void genTone(){
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

    static void playSound(){
//        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
//                sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
//                AudioFormat.ENCODING_PCM_16BIT, numSamples,
//                AudioTrack.MODE_STATIC);
//        audioTrack.write(generatedSnd, 0, numSamples);
//        audioTrack.play();
    }

}

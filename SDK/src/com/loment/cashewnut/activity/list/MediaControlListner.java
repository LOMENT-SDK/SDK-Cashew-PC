/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loment.cashewnut.activity.list;

import javafx.scene.media.MediaPlayer;

/**
 *
 * @author sekhar
 */
public class MediaControlListner {

    private static MediaControlListner instance;
    MediaPlayer mp;
    MediaControl mediaControl;

    private MediaControlListner() {
    }

    public static MediaControlListner getInstance() {
        if (instance == null) {
            try {
                instance = new MediaControlListner();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return instance;
    }

    public void listenForResponse(MediaPlayer mp, MediaControl mediaControl) {
        if (this.mp != null) {
            if (this.mp == mp) {
                return;
            }
            this.mp.seek(this.mp.getStartTime());
            this.mp.pause();
        }
        this.mp = mp;
        this.mediaControl = mediaControl;
    }
}

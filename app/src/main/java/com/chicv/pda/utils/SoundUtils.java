package com.chicv.pda.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.chicv.pda.R;

public class SoundUtils {

    private static SoundPool soundPool;
    private static SparseIntArray soundArray;

    public static void init(Context context) {
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        soundArray = new SparseIntArray();
        soundArray.append(0, soundPool.load(context, R.raw.scan, 1));
        soundArray.append(1, soundPool.load(context, R.raw.error, 1));
    }

    public static void playSuccess() {
        soundPool.play(soundArray.get(0), 1, 1, 0, 0, 1);
    }

    public static void playError() {
        soundPool.play(soundArray.get(1), 1, 1, 0, 0, 1);
    }
}

package main;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.util.Objects;

public class Sound {

    private final Clip clip = AudioSystem.getClip();

    public static final Sound musicBackground;

    static {
        try {
            musicBackground = new Sound("/music.wav");
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Sound hurtEffect;

    static {
        try {
            hurtEffect = new Sound("/hurt.wav");
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private Sound(String name) throws LineUnavailableException {
        try {
            clip.open(AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(name))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        try {
            new Thread(() -> {
                synchronized (clip) {
                    clip.stop();
                    clip.setFramePosition(0);
                    clip.start();
                }
            }).start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void loop() {
        try {
            new Thread(() -> {
                synchronized (clip) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
            }).start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

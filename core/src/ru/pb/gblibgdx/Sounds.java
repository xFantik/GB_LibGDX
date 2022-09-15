package ru.pb.gblibgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class Sounds {
    private static Sounds instance;
    private Map<SoundTag, Sound> soundsMap;

    private final Music music;

    public enum SoundTag {GAME_OVER, WIN, JUMP, GET_KEY, PORTAL}


    private Sounds() {
        soundsMap = new HashMap<>();

        soundsMap.put(SoundTag.GAME_OVER, Gdx.audio.newSound(Gdx.files.internal("sounds/mario/super-mario-game-over.mp3")));
        soundsMap.put(SoundTag.WIN, Gdx.audio.newSound(Gdx.files.internal("sounds/mario/win.mp3")));
        soundsMap.put(SoundTag.GET_KEY, Gdx.audio.newSound(Gdx.files.internal("sounds/mario/get_key.mp3")));
        soundsMap.put(SoundTag.JUMP, Gdx.audio.newSound(Gdx.files.internal("sounds/mario/jump.mp3")));
        soundsMap.put(SoundTag.PORTAL, Gdx.audio.newSound(Gdx.files.internal("sounds/mario/portal.mp3")));

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/mario/super-mario-saundtrek.mp3"));
        music.setLooping(true);
        music.setVolume(0.2f);


    }

    public static Sounds getInstance() {
        if (instance == null) {
            instance = new Sounds();
        }

        return instance;
    }

    public void play(SoundTag s) {
        if (s != null) {
            soundsMap.get(s).play();
        }
    }

    public void dispose() {
        for (Sound sound : soundsMap.values()) {
            sound.dispose();
        }
        soundsMap = null;
    }

    public void playMusic(boolean play) {
        if (play)
            music.play();
        else music.stop();

    }


}

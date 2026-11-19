
package robotassembly;

import java.io.File;
import javax.sound.sampled.*;

public class SoundManager
{

    private Clip backgroundMusic;

    public void playMusic()
    {
        try
        {
            if (backgroundMusic != null)
            {
                return;
            }

            File file = new File("sounds/background.wav");

            try (AudioInputStream audio = AudioSystem.getAudioInputStream(file))
            {

                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(audio);
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
        catch (Exception e)
        {
            System.out.println("Music error: " + e.getMessage());
        }
    }


    public void stopMusic()
    {
        if (backgroundMusic != null)
        {
            backgroundMusic.stop();
            backgroundMusic.close();
            backgroundMusic = null;
        }
    }


    public void playSound(String filename)
    {
        try
        {
            File file = new File("sounds/" + filename);

            try (AudioInputStream audio = AudioSystem.getAudioInputStream(file))
            {
                Clip sound = AudioSystem.getClip();
                sound.open(audio);

                sound.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP)
                    {
                        sound.close();
                    }
                });

                sound.start();
            }
        }
        catch (Exception e)
        {
            System.out.println("Sound error: " + e.getMessage());
        }
    }


    public boolean isMusicPlaying()
    {
        return backgroundMusic != null && backgroundMusic.isRunning();
    }
}

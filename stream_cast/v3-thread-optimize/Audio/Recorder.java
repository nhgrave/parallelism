package Audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Recorder {

    public static final int BUFFER_SIZE = 4096;
    private ByteArrayOutputStream recordBytes;
    private TargetDataLine audioLine;
    private AudioFormat format;
    private boolean isRunning;

    public Recorder() {
        format = getAudioFormat();
    }

    public AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public void start() throws LineUnavailableException {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Not supported");
        }

        audioLine = AudioSystem.getTargetDataLine(format);

        audioLine.open(format);
        audioLine.start();

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;

        clean();
        setRunning(true);

        while (isRunning) {
            bytesRead = audioLine.read(buffer, 0, buffer.length);
            recordBytes.write(buffer, 0, bytesRead);
        }
    }

    public void stop() {
        setRunning(false);

        if (audioLine != null) {
            audioLine.drain();
            audioLine.close();
        }
    }

    public byte[] getRecordBytes() {
        if (recordBytes == null) {
            return new byte[0];
        } else {
            return recordBytes.toByteArray();
        }
    }

    public void play(byte[] audioData) throws LineUnavailableException, InterruptedException {
        Clip clip = AudioSystem.getClip();
        clip.open(format, audioData, 0, audioData.length);
        clip.start();
        clip.drain();

        long sleepTime = clip.getMicrosecondLength() / 1000;

        Thread.sleep(sleepTime);
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    public void addBuffer(byte[] audioBytes) {
        recordBytes.write(audioBytes, 0, audioBytes.length);
    }

    public void keepPlaing() {
        while (isRunning) {
            byte[] audioData = getRecordBytes();

            clean();

            try {
                if (audioData.length > 0) play(audioData);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void save(File wavFile) throws IOException {
        byte[] audioData = getRecordBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
        AudioInputStream audioInputStream = new AudioInputStream(bais, format, audioData.length / format.getFrameSize());

        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, wavFile);

        audioInputStream.close();
        recordBytes.close();
    }

    public void clean() {
        recordBytes = new ByteArrayOutputStream();
    }
}

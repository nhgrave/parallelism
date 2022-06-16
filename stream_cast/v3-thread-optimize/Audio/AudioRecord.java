package Audio;

public class AudioRecord {

    Recorder recorder;

    public AudioRecord() {
        recorder = new Recorder();
    }

    public void lissen() {
        Thread recordThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    recorder.start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        recordThread.start();
    }

    public byte[] getAudioBytes() {
        return recorder.getRecordBytes();
    }

    public void cleanAudioRecorded() {
        recorder.clean();
    }

    public void keepPlaing() {
        Thread playThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    recorder.setRunning(true);
                    recorder.keepPlaing();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        playThread.start();

    }

    public void playBuffer(byte[] audioBytes) {
        try {
            recorder.addBuffer(audioBytes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

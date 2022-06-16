package main;

import Audio.AudioRecord;
import Audio.Recorder;
import Network.AudioSendPack;
import Network.ImageSendPack;
import Resources.Util;
import Screen.ScreenShot;
import Screen.ScreenShotBlock;

public class Server {

    private AudioRecord audioRecord;
    private ScreenShot screenShot;

    private Server() {
        audioRecord = new AudioRecord();
        screenShot = new ScreenShot();
    }

    private void start() {
        audioRecord.lissen();
        screenShot.start();

        while (true) {
            sendAudio();
            sendImageBlocks();
        }
    }

    private void sendAudio() {
        byte[] audioBytes = audioRecord.getAudioBytes();
        audioRecord.cleanAudioRecorded();

        int packSize = Recorder.BUFFER_SIZE;
        int packs = audioBytes.length / packSize;

        for (int i = 0; i < packs; i++) {
            byte[] pack = new byte[packSize];

            System.arraycopy(audioBytes, i * packSize, pack, 0, packSize);

            new AudioSendPack().sendBuffer(audioBytes);
        }
    }

    private void sendImageBlocks() {
        for (ScreenShotBlock imageBlock : screenShot.imageBlocks) {
            if (imageBlock != null) {
                byte[] bufferPack = getImageBytes(imageBlock);

                new ImageSendPack().sendBuffer(bufferPack);
            }
        }
    }

    private byte[] getImageBytes(ScreenShotBlock imageBlock) {
        int[] imagePixels = imageBlock.getPixels();

        byte[] buffer = new byte[(imagePixels.length * 4) + 4 + 4];
        int aux = 0;

        for (int color : imagePixels) {
            byte[] colorBytes = Util.integerToBytes(color);
            for (byte colorByte : colorBytes) {
                buffer[aux++] = colorByte;
            }
        }

        // bytes do posX
        byte[] auxBufferPosX = Util.integerToBytes(imageBlock.srcCornerX);
        for (byte b : auxBufferPosX) {
            buffer[aux++] = b;
        }

        // bytes do posY
        byte auxBufferPosY[] = Util.integerToBytes(imageBlock.srcCornerY);
        for (byte b : auxBufferPosY) {
            buffer[aux++] = b;
        }

        return buffer;
    }

    public static void main(String[] args) {
        new Server().start();
    }
}

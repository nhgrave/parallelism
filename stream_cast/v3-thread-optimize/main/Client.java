package main;

import Audio.AudioRecord;
import Network.AudioRecivePack;
import Network.ImageRecivePack;
import Screen.ScreenDisplay;

public class Client {

    public static AudioRecord audioRecord;
    private static ScreenDisplay screenDisplay;

    public static void setPixelColor(int posX, int posY, int cor) {
        if (screenDisplay != null) {
            screenDisplay.setPixelColor(posX, posY, cor);
        }
    }

    public static void main(String[] args) {
        new AudioRecivePack().start();
        new ImageRecivePack().start();

        audioRecord = new AudioRecord();
        audioRecord.keepPlaing();

        screenDisplay = new ScreenDisplay();
        screenDisplay.start();
    }
}

package com.stevensadler.android.blocspot.ui;

import java.util.Random;

/**
 * Created by Steven on 3/2/2016.
 */
public class UIUtils {

    public static int generateRandomColor(int baseColor) {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        int baseRed = (baseColor & 0xFF0000) >> 16;
        int baseGreen = (baseColor & 0xFF00) >> 8;
        int baseBlue = baseColor & 0xFF;

        red = (red + baseRed) / 2;
        green = (green + baseGreen) / 2;
        blue = (blue + baseBlue) / 2;
        return 0xFF000000 | (red << 16) | (green << 8) | blue;
    }

    public static int generateRandomBrightColor() {
        Random random = new Random();
        int index = random.nextInt(6);
        int[] baseColors = { 0xFF0000, 0x00FF00, 0x0000FF, 0xFFFF00, 0x00FFFF, 0xFF00FF };
        return generateRandomColor(baseColors[index]);

        // TODO consider switching from using int color to float[] hsl
        // hopefully it will make poi list item color and map marker color look the same

//        Random random = new Random();
//        int index = random.nextInt(10);
//        float[] hues = {
//                BitmapDescriptorFactory.HUE_AZURE,
//                BitmapDescriptorFactory.HUE_BLUE,
//                BitmapDescriptorFactory.HUE_CYAN,
//                BitmapDescriptorFactory.HUE_GREEN,
//                BitmapDescriptorFactory.HUE_MAGENTA,
//                BitmapDescriptorFactory.HUE_ORANGE,
//                BitmapDescriptorFactory.HUE_RED,
//                BitmapDescriptorFactory.HUE_ROSE,
//                BitmapDescriptorFactory.HUE_VIOLET,
//                BitmapDescriptorFactory.HUE_YELLOW
//        };
//        float[] hsl = { hues[index], 0.5f, 0.5f};
//        int color = ColorUtils.HSLToColor(hsl);
//        return color;
    }
}

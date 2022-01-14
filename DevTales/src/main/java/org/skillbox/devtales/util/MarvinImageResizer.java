package org.skillbox.devtales.util;

import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;

import java.awt.image.BufferedImage;

public class MarvinImageResizer {
    static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        MarvinImage image = new MarvinImage(originalImage);
        Scale scale = new Scale();
        scale.load();
        scale.setAttribute("newWidth", targetWidth);
        scale.setAttribute("newHeight", targetHeight);
        scale.process(image.clone(), image, null, null, false);
        return image.getBufferedImageNoAlpha();
    }

}

package org.skillbox.devtales.util;

import org.imgscalr.Scalr;
import java.awt.image.BufferedImage;

    public class ImgScalrResizer {

        public static BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) {
            return Scalr.resize(originalImage, targetWidth);
        }

        public static BufferedImage resizeImage(BufferedImage image, int targetImgWidth) {
            int imageHeight = image.getHeight();
            int imageWidth = image.getWidth();
            int targetImgHeight = (int) Math.round(imageHeight / (imageWidth / (double) targetImgWidth));
            return Scalr.resize(image, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, targetImgWidth, targetImgHeight, Scalr.OP_ANTIALIAS);
        }

    }


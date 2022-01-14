package org.skillbox.devtales.util;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

    public class ImgScalrResizer {
        public static BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) {
            return Scalr.resize(originalImage, targetWidth);
        }

        public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
            return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
        }

    }


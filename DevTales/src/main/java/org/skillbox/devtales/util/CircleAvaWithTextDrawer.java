package org.skillbox.devtales.util;

import ij.ImagePlus;
import org.apache.commons.lang3.RandomStringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.AttributedString;

public class CircleAvaWithTextDrawer {

    public static Image getCircleAvaWithText() throws IOException {
        String imagePath = "https://robohash.org/User.png?size=360x360&set=set3";

        ImagePlus resultGraphicsCentered = new ImagePlus("", signImageCenter(RandomStringUtils.randomAlphabetic(2).toUpperCase(), imagePath));
        return resultGraphicsCentered.getImage();
    }

    /**
     * Draw a String centered in the middle of an image.
     *
     * @param 'g' The Graphics instance.
     * @param 'text' The String to draw.
     * @param 'rect' The Rectangle to center the text in.
     * @throws IOException
     */
    public static BufferedImage signImageCenter(String text, String path) throws IOException {

        BufferedImage image = ImageIO.read(new URL(path));
        Font font = new Font("Arial", Font.BOLD, 210);

        AttributedString attributedText = new AttributedString(text);
        attributedText.addAttribute(TextAttribute.FONT, font);
        attributedText.addAttribute(TextAttribute.FOREGROUND, Color.orange);

        Graphics g = image.getGraphics();

        FontMetrics metrics = g.getFontMetrics(font);
        int positionX = (image.getWidth() - metrics.stringWidth(text)) / 2;
        int positionY = (image.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();

        g.drawString(attributedText.getIterator(), positionX, positionY);

        return image;
    }

}

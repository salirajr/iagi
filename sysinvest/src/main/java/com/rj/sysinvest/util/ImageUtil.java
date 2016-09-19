package com.rj.sysinvest.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public class ImageUtil {

    public static byte[] scale(String formatName, byte[] bytes, int scaleToWidth, int scaleToHeight) throws IOException {
        BufferedImage img = scale(ImageIO.read(new ByteArrayInputStream(bytes)), scaleToWidth, scaleToHeight);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, formatName, baos);
        return baos.toByteArray();
    }

    public static BufferedImage scale(BufferedImage img, int scaleToWidth, int scaleToHeight) {
        Dimension d = getScaledDimension(img.getWidth(), img.getHeight(), scaleToWidth, scaleToHeight);
        Image scaledImage = img.getScaledInstance(d.width, d.height, Image.SCALE_SMOOTH);
        img = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        g.drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);
        g.dispose();
        return img;
    }

//    /**
//     * Gets image dimensions for given file http://stackoverflow.com/a/12164026
//     *
//     * @param imgPath image file
//     * @return dimensions of image
//     * @throws IOException if the file is not a known image
//     */
//    public static Dimension getImageDimension(Path imgPath) throws IOException {
//        String fileName = imgPath.getFileName().toString();
//        int pos = fileName.lastIndexOf(".");
//        if (pos == -1) {
//            throw new IOException("No extension for file: " + imgPath);
//        }
//        String suffix = fileName.substring(pos + 1);
//        Iterator<ImageReader> i = ImageIO.getImageReadersBySuffix(suffix);
//        if (i.hasNext()) {
//            ImageReader reader = i.next();
//            reader.setInput(new FileImageInputStream(imgPath.toFile()));
//            int width = reader.getWidth(reader.getMinIndex());
//            int height = reader.getHeight(reader.getMinIndex());
//            reader.dispose();
//            return new Dimension(width, height);
//        } else {
//            throw new IOException("Unknown image file: " + imgPath);
//        }
//    }
    public static Dimension getImageDimension(InputStream imageInputStream) throws IOException {
        SimpleImageInfo imageInfo = new SimpleImageInfo(imageInputStream);
        return new Dimension(imageInfo.getWidth(), imageInfo.getHeight());
    }

    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
        return getScaledDimension(imgSize.width, imgSize.height, boundary.width, boundary.height);
    }

    public static Dimension getScaledDimension(int original_width, int original_height, int bound_width, int bound_height) {
        int new_width = original_width;
        int new_height = original_height;
        // first check if we need to scale width
        if (original_width > bound_width && bound_width > 0) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }
        // then check if we need to scale even with the new height
        if (new_height > bound_height && bound_height > 0) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }
        return new Dimension(new_width, new_height);
    }
}

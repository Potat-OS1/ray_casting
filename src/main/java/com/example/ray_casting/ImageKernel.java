package com.example.ray_casting;

import com.aparapi.Kernel;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageKernel extends Kernel {
    int width;
    int height;
    WritableImage base;
    Image overlay;
    PixelReader basePixelReader, overlayPixelReader;
    PixelWriter basePixelWriter;

    ImageKernel (int width, int height, WritableImage base, PixelReader basePixelReader, PixelWriter basePixelWriter, Image overlay, PixelReader overlayPixelReader) {
        this.width = width;
        this.height = height;
        this.base = base;
        this.overlay = overlay;
        this.basePixelReader = basePixelReader;
        this.overlayPixelReader = overlayPixelReader;
        this.basePixelWriter = basePixelWriter;
    }

    @Override
    public void run() {
        int gid = getGlobalId();
        final int x = gid % width;
        final int y = gid / height;
        Color base = basePixelReader.getColor(x, y);
        Color overlay = overlayPixelReader.getColor(x, y);
        basePixelWriter.setColor(x, y, new Color(
                Math.max(base.getRed(), overlay.getRed()),
                Math.max(base.getBlue(), overlay.getBlue()),
                Math.max(base.getGreen(), overlay.getGreen()),
                Math.max(Math.max(base.getOpacity(), overlay.getOpacity()), .7)
        ));
    }
}

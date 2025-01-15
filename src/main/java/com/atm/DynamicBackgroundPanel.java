package com.atm;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class DynamicBackgroundPanel extends JPanel {
    private ImageIcon backgroundImage;

    public DynamicBackgroundPanel() {
        setLayout(new BorderLayout());
    }

    /**
     * Sets the background image for the panel.
     *
     * @param imagePath The path to the image file to be used as the background.
     *                  The image file should be in a format supported by Java's
     *                  ImageIcon class.
     *
     * @return This method does not return a value.
     *
     * @throws IllegalArgumentException If the provided imagePath is null or an
     *                                  empty string.
     *                                  If the image file at the specified path
     *                                  cannot be loaded.
     *
     * @see ImageIcon
     */
    public void setBackgroundImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            throw new IllegalArgumentException("Image path must not be null or empty");
        }
        backgroundImage = new ImageIcon(imagePath);
        repaint();
    }

    /**
     * Overrides the paintComponent method of the JPanel class to draw the
     * background image.
     *
     * @param g The Graphics object used for drawing.
     *
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            int width = getWidth();
            int height = getHeight();
            Image img = backgroundImage.getImage();
            g.drawImage(img, 0, 0, width, height, this);
        }
    }
}

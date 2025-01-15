package com.atm;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class RoundedPanel extends JPanel {
    private final int cornerRadius;

    public RoundedPanel(int radius) {
        this.cornerRadius = radius;
        setOpaque(false);
    }

    /**
     * Overrides the paintComponent method to draw a rounded rectangle with
     * anti-aliasing.
     *
     * @param g The Graphics object used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        // Enable anti-aliasing for smoother rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Calculate the dimensions of the rounded rectangle
        int x = getInsets().left;
        int y = getInsets().top;
        int width = getWidth() - getInsets().left - getInsets().right;
        int height = getHeight() - getInsets().top - getInsets().bottom;

        // Set the background color and fill the rounded rectangle
        g2.setColor(getBackground());
        g2.fillRoundRect(x, y, width, height, cornerRadius, cornerRadius);

        // Dispose of the Graphics2D object to free up resources
        g2.dispose();
    }
}

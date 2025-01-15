package com.atm;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class RoundedButton extends JButton {

    private Color hoverBackgroundColor;
    private Color pressedBackgroundColor;
    private Color defaultBackgroundColor;

    public RoundedButton(String text, int r, int g, int b) {
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFont(new Font("Arial", Font.BOLD, 16));

        defaultBackgroundColor = new Color(r, g, b);
        hoverBackgroundColor = new Color((r + 20), (g + 20), (b));
        pressedBackgroundColor = new Color((r - 40), (g - 40), (b));
        setBackground(defaultBackgroundColor);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBackgroundColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(defaultBackgroundColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedBackgroundColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverBackgroundColor);
            }
        });
    }

    /**
     * Overrides the paintComponent method to draw a rounded rectangle with the
     * button's background color.
     * This method is called by Swing when the component needs to be repainted.
     *
     * @param g The Graphics object used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground()); // Set the color to the button's background
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Draw a rounded rectangle with the specified
                                                                // dimensions and corner radius
        super.paintComponent(g); // Call the superclass's paintComponent method toensure any other necessary
        // drawing is performed
    }

    /**
     * Overrides the paintBorder method to draw a rounded border around the button.
     * This method is called by Swing when the component needs to be repainted.
     *
     * @param g The Graphics object used for drawing.
     *
     * @see javax.swing.JComponent#paintBorder(java.awt.Graphics)
     */
    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground()); // Set the color to the button's foreground color
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30); // Draw a rounded rectangle with the specified
                                                                        // dimensions and corner radii
    }
}

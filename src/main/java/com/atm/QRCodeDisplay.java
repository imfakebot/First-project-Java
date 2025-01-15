package com.atm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeDisplay extends JFrame {

    public QRCodeDisplay(String accountID, String accountName, String accountNumber) {
        super("QR Code Display");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/com/atm/icon-removebg-preview.png")).getImage());

        BufferedImage qrCodeImage = generateQRCodeImage(accountID);

        JLabel qrLabel = new JLabel(new ImageIcon(qrCodeImage));
        qrLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel infoPanel = new JPanel();
        JLabel accountInfoLabel = new JLabel("<html><center>Account Name: " + accountName + "<br>Account Number: "
                + accountNumber + "</center></html>");
        infoPanel.add(accountInfoLabel);

        setLayout(new BorderLayout());
        add(infoPanel, BorderLayout.NORTH);
        add(qrLabel, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Generates a QR code image from the given text.
     * 
     * This method creates a QR code as a BufferedImage using the ZXing library.
     * The QR code is generated with a fixed size of 300x300 pixels.
     * 
     * @param text The text to be encoded in the QR code. This typically represents
     *             the data you want to store in the QR code, such as a URL or any
     *             other string of characters.
     * @return A BufferedImage containing the generated QR code. The image is in
     *         RGB format with black modules on a white background. Returns null if
     *         there was an error during QR code generation.
     */
    private BufferedImage generateQRCodeImage(String text) {
        int width = 300;
        int height = 300;
        BufferedImage bufferedImage = null;

        try {
            Writer qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width,
                    height);

            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }
        } catch (WriterException e) {
            JOptionPane.showMessageDialog(null, "Error generating QR Code: " +
                    e.getMessage());
        }
        return bufferedImage;
    }

}

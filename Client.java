package Client;

import java.awt.Component;
import java.awt.Font;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;

public class Client {
    public static void main(String[] args) {
        File[] filetoSend = new File[1];

        JFrame jFrame = new JFrame();
        jFrame.setSize(500,450);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);

        JLabel jtitle = new JLabel("Expediteur de Fichier");
        jtitle.setFont(new Font("Arial",Font.BOLD, 25));
        jtitle.setBorder(new EmptyBorder(20,0,10,0));
        jtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel jlFileName = new JLabel("Choisissez un fichier a envoyer.");
        jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
        jlFileName.setBorder(new EmptyBorder(70, 0, 0, 0));
        jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jpButton = new JPanel();
        jpButton.setBorder(new EmptyBorder(75, 0, 10, 0));

        JButton jbSendFile = new JButton("Envoyer le fichier ");
        jbSendFile.setPreferredSize(new Dimension(250, 75));
        jbSendFile.setFont(new Font("Arial", Font.BOLD, 20));
       

        JButton jbChooseFile = new JButton("Choisir le fichier");
        jbChooseFile.setPreferredSize(new Dimension(250, 75));
        jbChooseFile.setFont(new Font("Arial", Font.BOLD, 20));
        jpButton.add(jbSendFile);
        jpButton.add(jbChooseFile);

        jbChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFile = new JFileChooser();
                jFile.setDialogTitle("Choisissez un fichier a envoyer");

                if (jFile.showOpenDialog(null) == jFile.APPROVE_OPTION) {
                    filetoSend[0] = jFile.getSelectedFile();
                    jlFileName.setText("Le fichier que vous souhaitez envoyer : "+filetoSend[0].getName());
                }
            }
        });

        jbSendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (filetoSend[0] == null) {
                    jlFileName.setText("Veuillez d'abord choisir un fichier");
                } else {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(filetoSend[0].getAbsolutePath());
                        Socket socket = new Socket("localhost", 1234);

                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                        String filename = filetoSend[0].getName();
                        byte[] filenameBytes = filename.getBytes();

                        byte[] fileContentBytes = new byte[(int)filetoSend[0].length()];
                        fileInputStream.read(fileContentBytes);

                        dataOutputStream.writeInt(filenameBytes.length);
                        dataOutputStream.write(filenameBytes);

                        dataOutputStream.writeInt(fileContentBytes.length);
                        dataOutputStream.write(fileContentBytes);
                    } catch (Exception error) {
                        error.printStackTrace();
                    }
                }
            }
        });

        jFrame.add(jtitle);
        jFrame.add(jlFileName);
        jFrame.add(jpButton);
        jFrame.setVisible(true);
    }
}
package ca.ulaval.glo2004;


import ca.ulaval.glo2004.gui.MainFrame;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;





public class App {
    //Exemple de creation d'une fenetre et d'un bouton avec swing. Lorsque vous allez creer votre propre GUI
    // Vous n'aurez pas besoin d'ecrire tout ce code, il sera genere automatiquement par intellij ou netbeans
    // Par contre vous aurez a creer les actions listener pour vos boutons et etc.
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        // Creation du main window
        UIManager.setLookAndFeel(new NimbusLookAndFeel());
        MainFrame frame = new MainFrame();
        frame.setTitle("Microsoftears");
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}


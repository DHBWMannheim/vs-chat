package vs.chat.client.UI.Listener;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EmojiMouseListener implements MouseListener {

    String unicode;
    JTextArea textArea;
    public EmojiMouseListener(String unicode, JTextArea textArea){
        this.unicode = unicode;
        this.textArea = textArea;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        textArea.append(unicode);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}

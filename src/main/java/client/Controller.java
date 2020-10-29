package client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.io.*;
import java.net.Socket;

public class Controller {
    private Socket socket;
    private DataOutputStream dos;
    private final String IP_ADRESS = "localhost";
    private final int PORT = 8189;
    private DataInputStream dis;
    @FXML
    private TextField textField;

    @FXML
    private TextArea textArea;

    @FXML
    public void mouseClickedTextArea(MouseEvent mouseEvent) {
        textArea.editableProperty().setValue(false);
    }

    @FXML
    void disableTextInput(KeyEvent event) {
        textArea.editableProperty().setValue(false);
    }


    public void start() throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new Socket(IP_ADRESS, PORT);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        }
        String[] cmd = textField.getText().split(" ");
        switch (cmd[0]) {
            case "upload":
                getFile(cmd[1], "upload");
                break;
            case "exit":
                Platform.exit();
                break;
            case "download":
                getFile(cmd[1], "download");
                break;
            default:
                textArea.appendText("Use command upload or download." + "\n");
                break;
        }
        textField.clear();
    }

    private void getFile(String s, String action) {
        try {
            String dir = null;
            if (action.equals("download")) {
                dir = "server/";
            } else if (action.equals("upload")) {
                dir = "client/";
            }
            dos.writeUTF(action);
            dos.writeUTF(s);
            File file = new File(dir + s);
            long length = file.length();
            dos.writeLong(length);
            FileInputStream fileBytes = new FileInputStream(file);
            int read = 0;
            byte[] buffer = new byte[256];
            while ((read = fileBytes.read(buffer)) != -1) {
                dos.write(buffer, 0, read);
            }
            dos.flush();
            textArea.appendText(action + " " + s + "\n");
            System.out.println(action + " " + s + " completed");
        } catch (IOException e) {
            textArea.appendText(action + " " + s + ". ");
            System.out.println("File not found!");
            textArea.appendText("File not found \n");
        }
    }
}
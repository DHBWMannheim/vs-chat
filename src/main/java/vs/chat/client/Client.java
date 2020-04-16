package vs.chat.client;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import vs.chat.client.CMD.Cmd;
import vs.chat.client.UI.ClientGUI;

public class Client {

    public static void main(String[] args) {
		List<String> nodes = new ArrayList<>();

        for (String arg: args) {
        	nodes.add(arg);
		}

        if (nodes.size() == 0) {
        	nodes.add("localhost");
		}

        try {
            BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;

            ClientApiImpl api = new ClientApiImpl(nodes, userIn);

            do {
                System.out.print("Start with GUI [Y/n]? ");
                userInput = userIn.readLine();
            } while (!(userInput.toLowerCase().equals("y") || userInput.toLowerCase().equals("n")));

            if (userInput.toLowerCase().equals("y")) {
                new ClientGUI(api);
            } else if (userInput.toLowerCase().equals("n")) {
                new Cmd(api);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
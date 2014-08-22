/*
 * The MIT License
 *
 * Copyright 2014 Christian Bikle.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package vtcpt;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christian Bikle
 */
public final class Player implements Runnable {

    private Socket socket;
    private String name;
    private int score = 0;
    private BufferedReader fromPlayer;
    private DataOutputStream toPlayer;
    private boolean run = true;
    private boolean isRegistered = false;
    private ConcurrentLinkedQueue<Message> msgQueue;

    public Player(Socket socket, ConcurrentLinkedQueue<Message> m) throws IOException {
        this.socket = socket;
        this.msgQueue = m;
        fromPlayer = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        toPlayer = new DataOutputStream(this.socket.getOutputStream());
        
        sendMessageToPlayer(ACK_CONNECTION + " " + PROTOTCOL_VERSION + CRLF);
    }

    public Player() {
    }

    public Socket getSocket() {
        return socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int updateScore(int addlPoints) {
        return (score += addlPoints);
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean status) {
        this.isRegistered = status;
    }

    public String listenToPlayer() throws IOException {
        if (!socket.isClosed()) {
            return fromPlayer.readLine();
        }
        return null;
    }

    public void sendMessageToPlayer(String msg) throws IOException {
        //System.out.println("msg:"+msg);
        if (!msg.endsWith(CRLF)) {
            msg += CRLF;
        }
        if (!socket.isClosed()) {
            toPlayer.writeBytes(msg);
        }

        if (msg.startsWith(ACK_BYE)) {
            System.out.println("bye ack'd");
            close();
        } else if (msg.startsWith(ACK_ELO_ACCEPT)) {
            System.out.println("is now registered");
            setRegistered(true);
        }
    }

    @Override
    public void run() {
        String input = "";

        while (this.run) {
            try {
                input = listenToPlayer();

                System.out.println(input);

                if (input == null) {
                    input = BYE;
                }
            } catch (SocketException e) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, e);
                input = BYE;
            } catch (IOException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }

            Message m = parseInput(input);
            if (m != null) {
                if (isRegistered || m.getCommand().equals(ELO) || m.getCommand().equals(BYE)) {
                    addMessageToQueue(m);
                }
                if (BYE.equals(m.getCommand())) {
                    System.out.println("shutdown");
                    this.run = false;
                }
            }
        }

        System.out.println("shutdown 2");
    }

    private void close() {
        try {
            fromPlayer.close();
            toPlayer.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Message parseInput(String input) {
        System.out.println("input:" + input);

        Message m;
        String cmd;
        String arguments;
        int posOfSpace = input.indexOf(" ");
        System.out.println("pos: " + posOfSpace);
        if (posOfSpace == lengthOfCmds) {//it may be a command other than BYE
            cmd = input.substring(0, posOfSpace).toUpperCase();
            arguments = input.substring(posOfSpace).trim();
        } else if (posOfSpace == -1) {//it may be BYE
            cmd = input.toUpperCase();
            arguments = "";
        } else {//it doesn't fit any recognized command--period
            cmd = null;
            arguments = "";
        }

        System.out.println("cmd: " + cmd);
        String[] args = splitArguments(cmd, arguments);

        if (isValidCommand(cmd) && validateArguments(cmd, args)) {
            m = new Message(this, cmd, args);
        } else {
            Logger.getLogger(Player.class.getName()).log(Level.WARNING, "Unrecognized command or arguments: {0}", input);
            m = null;
        }

        return m;
    }

    private boolean isValidCommand(String cmd) {
        return (ELO.equals(cmd) || MSG.equals(cmd) || FIR.equals(cmd) || BYE.equals(cmd));
    }

    private String[] splitArguments(String cmd, String args) {
        String[] argsA;
        if (ELO.equals(cmd)) {
            args = args.replace("\\" + SEPARATOR_TOKEN, "\\" + TEMP_TOKEN);

            argsA = args.split(SEPARATOR_TOKEN);

            for (int i = 0; i < argsA.length; i++) {
                argsA[i] = argsA[i].replace("\\" + TEMP_TOKEN, SEPARATOR_TOKEN);
            }
        } else if (FIR.equals(cmd)) {
            argsA = args.toUpperCase().split(SEPARATOR_TOKEN);
        } else if (MSG.equals(cmd)) {
            args = args.replace("\\", "");
            argsA = new String[1];
            argsA[0] = args;
        } else {//BYE
            argsA = new String[1];
            argsA[0] = "";
        }

        return argsA;
    }

    private boolean validateArguments(String cmd, String[] args) {
        if (ELO.equals(cmd)) {
            boolean rightLength = args.length == 2;
            boolean secondArgIsNumber = true;
            try {
                Integer.parseInt(args[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, "Wrong number of args", e);
                secondArgIsNumber = false;
            } catch (NumberFormatException e) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, "Second arg not a number", e);
                secondArgIsNumber = false;
            }

            //TODO: make sure username doesn't contain malicious code
            return (rightLength && secondArgIsNumber);
        } else if (MSG.equals(cmd)) {

            //TODO: make sure msg doesn't contain malicious code
            return (args.length == 1);
        } else if (FIR.equals(cmd)) {
            boolean rightLength = args.length == 2;
            boolean firstArgIsLetter = false;
            try {
                firstArgIsLetter = Character.isLetter(args[0].charAt(0));
            } catch (StringIndexOutOfBoundsException e) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, e);
            }
            boolean firstArgIsRightLength = (args[0].length() == 1);
            boolean secondArgIsNumber = false;
            try {
                Integer.parseInt(args[1]);
                secondArgIsNumber = true;
            } catch (ArrayIndexOutOfBoundsException e) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, e);
            } catch (NumberFormatException e) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, e);
            }

            return (rightLength && firstArgIsLetter && firstArgIsRightLength && secondArgIsNumber && inBounds(args[0], args[1]));
        } else {//BYE
            return (args.length == 1);
        }
    }

    private boolean inBounds(String row, String col) {
        int rowInt;
        int colInt;

        try {
            rowInt = alphabet.indexOf(row);
            colInt = Integer.parseInt(col);
        } catch (Exception e) {
            return false;
        }

        return (rowInt >= 0 && rowInt <= MAP_HEIGHT && colInt >= 0 && colInt <= MAP_WIDTH);
    }

    private void addMessageToQueue(Message m) {
        msgQueue.add(m);
        synchronized (msgQueue) {
            msgQueue.notify();
        }
    }
}

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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christian Bikle
 */
public class Vtcpt_server {
    /* members */
    private static int port = 8081;
    private static int timeout = 180000;
    private ServerSocket listenSocket = null;
    private int[] playerLevel = {0, 1, 2}; //0 for DMs, 1 for players,
                                            //2 for spectators
    private boolean run = true;
    private ConnectionAccepter receptionist = null;
    private Thread receptionistThread = null;
    private final ConcurrentLinkedQueue<Message> msgQueue;

//---MAIN---//    
    public static void main(String args[]) {
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
                System.out.println("Using port " + port);
            } catch (NumberFormatException e) {
                String s = "Warning: expected integer for port number and got "
                        + args[0] + "; using default port number " + port;
                Logger.getLogger(Vtcpt_server.class.getName()).
                        log(Level.WARNING, s, e);
                System.out.println("Using default port " + port);
            }
        } else {
            System.out.println("Using default port " + port);
        }

        Vtcpt_server v = null;
        try {
            v = new Vtcpt_server();
        } catch (IOException ex) {
            String s = "Failed to create listen socket.";
            Logger.getLogger(Vtcpt_server.class.getName()).
                    log(Level.SEVERE, s, ex);
            System.exit(1);
        }
    }
    
//---CONSTRUCTOR---//
    public Vtcpt_server() throws IOException {
        listenSocket = new ServerSocket(port);
        runAcceptor();
    }
    
//----RUN ACCEPTOR---//
    private void runAcceptor() {
        receptionist = new ConnectionAccepter(listenSocket);
        receptionistThread = new Thread(receptionist);
        receptionistThread.start();
    }
    
//-----CONNECTION ACCEPTER class-----//
    private class ConnectionAccepter implements Runnable {
        /* members */
        private final ServerSocket listen;
        
//---CONSTRUCTOR---//
        public ConnectionAccepter(ServerSocket listen) {
            this.listen = listen;
        }
        
//---RUN---//
        @Override
        public void run() {
            while(run) {
                try {
                    Socket s = listen.accept();
                    //TODO: new thread for player
                } catch (IOException ex) {
                    String s = "Failed to accept client connection.";
                    Logger.getLogger(Vtcpt_server.class.getName()).
                            log(Level.SEVERE, s, ex);
                }
            }
        }
    }
}

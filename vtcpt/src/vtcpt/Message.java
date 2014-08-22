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

import java.util.Arrays;

/**
 *
 * @author Christian Bikle
 */
public class Message {

    private Player sender;
    private String command;
    private String[] args;

    public Message(Player sender, String command, String[] args) {
        this.sender = sender;
        this.command = command;
        this.args = args;
    }

    public Player getSender() {
        return sender;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return sender.getName() + ": " + command + " " + Arrays.deepToString(args);
    }
}

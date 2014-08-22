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

/**
 *
 * @author Christian Bikle
 */
public enum Codes {
    /* client to server */
   ELO ("ELO");
//   public static final String MSG = "MSG";
//   public static final String FIR = "FIR";
//   public static final String BYE = "BYE";
//   /* server to client */
//   public static final String PLAYER_MESSAGE = "001";
//   public static final String SYSTEM_MESSAGE = "002";
//   public static final String ACK_FIR_MISS = "100";
//   public static final String ACK_FIR_HIT = "150";
//   public static final String ACK_FIR_SELFHIT = "151";
//   public static final String ACK_FIR_SUNK = "190";
//   public static final String ACK_CONNECTION = "220";
//   public static final String ACK_ELO_ACCEPT = "310";
//   public static final String ACK_ELO_REJECT_NAMETAKEN = "351";
//   public static final String ACK_ELO_REJECT_NOROOM = "390";
//   public static final String SHIP_HIT = "500";
//   public static final String SHIP_SUNK = "505";
//   public static final String SHIPS_SUNK_ALL = "555";
//   public static final String START_GAME = "800";
//   public static final String ACK_BYE = "900";
//   public static final String GAMEOVER_PLAYERSLEFT = "990";
//   public static final String GAMEOVER_WON = "999";
//   public static final String CRLF = "\r\n";
//   public static final String PROTOTCOL_VERSION = "dndP/2013";
//   public static final int lengthOfCmds = 3;
   
   private String c;
   private Codes(String v) {
       c = v;
   }
}

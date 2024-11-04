/*
 * Copyright (C) 2010 José Luis Risco Martín <jlrisco@ucm.es>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *  - José Luis Risco Martín
 */
package jeco.core.util.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logger class to setup the logger.
 */
public class JecoLogger {

    /**
     * Setup the logger with the specified file path and level.
     *
     * @param filePath the file path
     * @param level the level
     */
    public static void setup(String filePath, Level level) {
        Logger logger = Logger.getLogger("");
        logger.setLevel(level);
        try {
            FileHandler fileHandler = new FileHandler(filePath, true);
            logger.addHandler(fileHandler);
            fileHandler.setFormatter(LoggerFormatter.formatter);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        Handler[] handlers = logger.getHandlers();
        for (Handler handler : handlers) {
            handler.setFormatter(LoggerFormatter.formatter);
            handler.setLevel(level);
        }
    }

    /**
     * Setup the logger with the specified level.
     * @param level the level
     */
    public static void setup(Level level) {
        setup("logger.log", level);
    }

    /**
     * Setup the logger with the default file path and level.
     */
    public static void setup() {
        setup("logger.log", Level.INFO);
    }

    /*    public void setLevel(Level level) {
     logger.setLevel(level);
     }*/
}

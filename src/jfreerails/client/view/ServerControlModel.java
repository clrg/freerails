package jfreerails.client.view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import jfreerails.client.common.ActionAdapter;
import jfreerails.controller.ServerCommand;
import jfreerails.controller.ServerControlInterface;


/**
 * Exposes the ServerControlInterface to client UI implementations.
 * @author rob
 */
public class ServerControlModel {
    private ServerControlInterface serverInterface;

    private class NewGameAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (serverInterface != null) {
                String mapName = e.getActionCommand();

                if (mapName != null) {
                    serverInterface.newGame(mapName);
                }
            }
        }

        public NewGameAction(String s) {
            if (s == null) {
                putValue(NAME, "New Game...");
            } else {
                putValue(NAME, s);
                putValue(ACTION_COMMAND_KEY, s);
            }
        }
    }

    private ActionAdapter selectMapActions;
    private final Action newGameAction = new NewGameAction(null);

    private class LoadGameAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (serverInterface != null) {
                serverInterface.loadGame();
            }
        }

        public LoadGameAction() {
            putValue(NAME, "Load Game");
            putValue(MNEMONIC_KEY, new Integer(76));
        }
    }

    private final Action loadGameAction = new LoadGameAction();

    private class SaveGameAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (serverInterface != null) {
                serverInterface.saveGame();
                loadGameAction.setEnabled(true);
            }
        }

        public SaveGameAction() {
            putValue(NAME, "Save Game");
            putValue(MNEMONIC_KEY, new Integer(83));
        }
    }

    private final Action saveGameAction = new SaveGameAction();

    private class SetTargetTicksPerSecondAction extends AbstractAction {
        final int speed;

        public void actionPerformed(ActionEvent e) {
            if (serverInterface != null) {
                if (speed == 0) { // pausing/unpausing

                    int newSpeed = -1 * serverInterface.getTargetTicksPerSecond();
                    serverInterface.setTargetTicksPerSecond(newSpeed);
                } else {
                    serverInterface.setTargetTicksPerSecond(speed);
                }
            }
        }

        public SetTargetTicksPerSecondAction(String name, int speed) {
            this(name, speed, KeyEvent.VK_UNDEFINED);
        }

        /**
         * Same as the constructor above but it enables also to associate a <code>keyEvent</code>
         * with the action.
         *
         * @param name action name
         * @param speed speed
         * @param keyEvent associated key event. Use values from <code>KeyEvent</class>.
         *
         * by MystiqueAgent
         */
        public SetTargetTicksPerSecondAction(String name, int speed,
            int keyEvent) {
            putValue(NAME, name);
            this.speed = speed;
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(keyEvent, 0));
        }
    }

    private final SetTargetTicksPerSecondAction[] speedActions = new SetTargetTicksPerSecondAction[] {
            new SetTargetTicksPerSecondAction("Pause", 0, KeyEvent.VK_P), // by MystiqueAgent: added keyEvent parameter
            new SetTargetTicksPerSecondAction("Slow", 10, KeyEvent.VK_1), // by MystiqueAgent: added keyEvent parameter
            new SetTargetTicksPerSecondAction("Moderate", 30, KeyEvent.VK_2), // by MystiqueAgent: added keyEvent parameter
            new SetTargetTicksPerSecondAction("Fast", 50, KeyEvent.VK_3), // by MystiqueAgent: added keyEvent parameter

            /* TODO one day we will make turbo faster :) */
            new SetTargetTicksPerSecondAction("Turbo", 50)
        };
    private final ActionAdapter targetTicksPerSecondActions = new ActionAdapter(speedActions,
            0);

    public void setServerControlInterface(ServerControlInterface i) {
        serverInterface = i;

        boolean enabled = (serverInterface != null);

        //Check that there is a file to load..
        boolean canLoadGame = ServerCommand.isSaveGameAvailable();

        loadGameAction.setEnabled(enabled && canLoadGame);
        saveGameAction.setEnabled(enabled);

        Enumeration e = targetTicksPerSecondActions.getActions();
        targetTicksPerSecondActions.setPerformActionOnSetSelectedItem(false);

        while (e.hasMoreElements()) {
            ((Action)e.nextElement()).setEnabled(enabled);
        }

        if (i == null) {
            selectMapActions = new ActionAdapter(new Action[0]);
        } else {
            String[] mapNames = i.getMapNames();
            Action[] actions = new Action[mapNames.length];

            for (int j = 0; j < actions.length; j++) {
                actions[j] = new NewGameAction(mapNames[j]);
                actions[j].setEnabled(enabled);
            }

            selectMapActions = new ActionAdapter(actions);
        }

        newGameAction.setEnabled(enabled);

        //        serverInterface.setTargetTicksPerSecond(((GameSpeed)world.get(ITEM.GAME_SPEED)).getSpeed());
    }

    public ServerControlModel(ServerControlInterface i) {
        setServerControlInterface(i);
    }

    /**
     * @return an action to load a game.
     * TODO The action produces a file selector dialog to load the game
     */
    public Action getLoadGameAction() {
        return loadGameAction;
    }

    /**
     * @return an action to save a game
     * TODO The action produces a file selector dialog to save the game
     */
    public Action getSaveGameAction() {
        return saveGameAction;
    }

    /**
     * @return an action adapter to set the target ticks per second
     */
    public ActionAdapter getSetTargetTickPerSecondActions() {
        return targetTicksPerSecondActions;
    }

    /**
     * Returns human readable string description of <code>tickPerSecond</code> number.
     * Looks for <code>tickPerSecond</code> in <code>targetTicksPerSecondActions</code>.
     * If appropriate action is not found returns first greater value or the greatest value.
     *
     * @param tickPerSecond int
     * @return String human readable description
     */
    public String getGameSpeedDesc(int tickPerSecond) {
        SetTargetTicksPerSecondAction action = null;

        for (int i = 0; i < speedActions.length; i++) {
            action = speedActions[i];

            if (action.speed >= tickPerSecond)
                break;
        }

        return (String)action.getValue(Action.NAME);
    }

    /**
     * When calling this action, set the action command string to the desired
     * map name, or call the appropriate selectMapAction.
     * @return an action to start a new game
     */
    public Action getNewGameAction() {
        return newGameAction;
    }

    /**
     * @return an ActionAdapter representing a list of actions representing
     * valid map names.
     */
    public ActionAdapter getMapNames() {
        return selectMapActions;
    }
}
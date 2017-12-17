package freerails.network;

import freerails.move.*;
import freerails.world.common.ImList;
import freerails.world.player.FreerailsPrincipal;
import freerails.world.top.KEY;
import freerails.world.top.WorldListListener;
import freerails.world.top.WorldMapListener;

import java.awt.*;
import java.util.ArrayList;

/**
 * A central point at which a client may register to receive moves which have
 * been committed.
 *
 * @author Luke
 * @author rob
 */
final public class MoveChainFork implements MoveReceiver {
    private final ArrayList<MoveReceiver> moveReceivers = new ArrayList<>();

    private final ArrayList<MoveReceiver> splitMoveReceivers = new ArrayList<>();

    private final ArrayList<WorldListListener> listListeners = new ArrayList<>();

    private final ArrayList<WorldMapListener> mapListeners = new ArrayList<>();

    private long lastTickTime = System.currentTimeMillis();

    public long getLastTickTime() {
        return lastTickTime;
    }

    public MoveChainFork() {
        // do nothing
    }

    public void addMapListener(WorldMapListener l) {
        mapListeners.add(l);
    }

    public void removeMapListener(WorldMapListener l) {
        mapListeners.remove(l);
    }

    public void removeCompleteMoveReceiver(MoveReceiver moveReceiver) {
        if (null == moveReceiver) {
            throw new NullPointerException();
        }

        moveReceivers.remove(moveReceiver);
    }

    public void addCompleteMoveReceiver(MoveReceiver moveReceiver) {
        if (null == moveReceiver) {
            throw new NullPointerException();
        }

        moveReceivers.add(moveReceiver);
    }

    public void addSplitMoveReceiver(MoveReceiver moveReceiver) {
        if (null == moveReceiver) {
            throw new NullPointerException();
        }

        splitMoveReceivers.add(moveReceiver);
    }

    public void addListListener(WorldListListener listener) {
        if (null == listener) {
            throw new NullPointerException();
        }

        listListeners.add(listener);
    }

    public void processMove(Move move) {
        for (MoveReceiver m : moveReceivers) {
            m.processMove(move);
        }

        splitMove(move);
    }

    private void splitMove(Move move) {
        if (move instanceof UndoMove) {
            UndoMove undoneMove = (UndoMove) move;
            move = undoneMove.getUndoneMove();
        }

        if (move instanceof CompositeMove) {
            ImList<Move> moves = ((CompositeMove) move).getMoves();

            for (int i = 0; i < moves.size(); i++) {
                splitMove(moves.get(i));
            }
        } else {
            for (MoveReceiver m : splitMoveReceivers) {
                m.processMove(move);
            }

            if (move instanceof AddItemToListMove) {
                AddItemToListMove mm = (AddItemToListMove) move;
                sendItemAdded(mm.getKey(), mm.getIndex(), mm.getPrincipal());
            } else if (move instanceof ChangeItemInListMove) {
                ChangeItemInListMove mm = (ChangeItemInListMove) move;
                sendListUpdated(mm.getKey(), mm.getIndex(), mm.getPrincipal());
            } else if (move instanceof RemoveItemFromListMove) {
                RemoveItemFromListMove mm = (RemoveItemFromListMove) move;
                sendItemRemoved(mm.getKey(), mm.getIndex(), mm.getPrincipal());
            } else if (move instanceof MapUpdateMove) {
                Rectangle r = ((MapUpdateMove) move).getUpdatedTiles();
                if (r.x != 0 && r.y != 0 && r.width != 0 && r.height != 0) {
                    // System.out.println("TilesChanged = " + r + " "
                    // + move.getClass().getCanonicalName());
                    // if (move instanceof WorldDiffMove) {
                    // WorldDiffMove wm = (WorldDiffMove) move;
                    // ImList<MapDiff> diffs = wm.getDiffs();
                    // for (int i = 0; i < diffs.size(); i++) {
                    // System.out.println(" " + diffs.get(i).x + "/"
                    // + diffs.get(i).y);
                    // }
                    // }
                    sendMapUpdated(r);
                }
            } else if (move instanceof TimeTickMove) {
                lastTickTime = System.currentTimeMillis();
            }
        }
    }

    private void sendMapUpdated(Rectangle r) {
        for (WorldMapListener l : mapListeners) {
            l.tilesChanged(r);
        }
    }

    private void sendItemAdded(KEY key, int index, FreerailsPrincipal p) {
        for (WorldListListener l : listListeners) {
            l.itemAdded(key, index, p);
        }
    }

    private void sendItemRemoved(KEY key, int index, FreerailsPrincipal p) {
        for (WorldListListener l : listListeners) {
            l.itemRemoved(key, index, p);
        }
    }

    private void sendListUpdated(KEY key, int index, FreerailsPrincipal p) {
        for (WorldListListener l : listListeners) {
            l.listUpdated(key, index, p);
        }
    }
}
/* Generated by Together */

package jfreerails.client.view.map;

import java.awt.Dimension;
public interface NewMapViewMode {
    Dimension getTileSize();

    float getScale();

    void setScale(float scale);

    boolean scaleCanChange();

    String getViewModeName();
}

/*
 * SelectWagonsJPanel.java
 *
 * Created on 29 December 2002, 16:54
 */

package jfreerails.client.view;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import jfreerails.client.renderer.TrainImages;
import jfreerails.client.renderer.ViewLists;
import jfreerails.controller.ModelRoot;
import jfreerails.world.cargo.CargoType;
import jfreerails.world.top.SKEY;
import jfreerails.world.train.TrainModel;

/**
 * This JPanel lets the user add wagons to a train.
 * 
 * @author lindsal8
 * 
 */
public class SelectWagonsJPanel extends javax.swing.JPanel implements View {

	private static final long serialVersionUID = 3905239009449095220L;

	private final GraphicsConfiguration defaultConfiguration = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDefaultConfiguration();

	private final Image stationView;

	private final ArrayList<Integer> wagons = new ArrayList<Integer>();

	private int engineType = 0;

	private ViewLists vl;

	public SelectWagonsJPanel() {
		initComponents();
		updateMaxWagonsText();

		URL url = SelectWagonsJPanel.class
				.getResource("/jfreerails/data/station.gif");
		Image tempImage = (new javax.swing.ImageIcon(url)).getImage();

		stationView = defaultConfiguration.createCompatibleImage(tempImage
				.getWidth(null), tempImage.getHeight(null),
				Transparency.BITMASK);

		Graphics g = stationView.getGraphics();

		g.drawImage(tempImage, 0, 0, null);
	}

	public void resetSelectedWagons() {
		this.wagons.clear();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the FormEditor.
	 */
	private void initComponents() {// GEN-BEGIN:initComponents
		java.awt.GridBagConstraints gridBagConstraints;

		jPanel1 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		wagonTypesJList = new javax.swing.JList();
		okjButton = new javax.swing.JButton();
		clearjButton = new javax.swing.JButton();
		jLabel1 = new javax.swing.JLabel();

		setLayout(new java.awt.GridBagLayout());

		setBackground(new java.awt.Color(0, 255, 51));
		setMinimumSize(new java.awt.Dimension(640, 400));
		setPreferredSize(new java.awt.Dimension(620, 380));
		jPanel1.setLayout(new java.awt.GridBagLayout());

		jPanel1.setMinimumSize(new java.awt.Dimension(170, 300));
		jPanel1.setPreferredSize(new java.awt.Dimension(170, 300));
		wagonTypesJList.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent evt) {
				wagonTypesJListKeyTyped(evt);
			}
		});
		wagonTypesJList.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				wagonTypesJListMouseClicked(evt);
			}
		});

		jScrollPane1.setViewportView(wagonTypesJList);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		jPanel1.add(jScrollPane1, gridBagConstraints);

		okjButton.setText("OK");
		okjButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okButtonAction(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		jPanel1.add(okjButton, gridBagConstraints);

		clearjButton.setText("Clear");
		clearjButton.setActionCommand("clear");
		clearjButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanel1.add(clearjButton, gridBagConstraints);

		jLabel1.setFont(new java.awt.Font("Dialog", 0, 10));
		jLabel1.setText("The maximum train length is 6 wagons");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
		jPanel1.add(jLabel1, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(20, 400, 70, 10);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
		add(jPanel1, gridBagConstraints);

	}// GEN-END:initComponents

	private void okButtonAction(java.awt.event.ActionEvent evt) { // GEN-FIRST:event_okButtonAction
		// Add your handling code here:

	} // GEN-LAST:event_okButtonAction

	private void wagonTypesJListMouseClicked(java.awt.event.MouseEvent evt) { // GEN-FIRST:event_wagonTypesJListMouseClicked
		// Add your handling code here:
		addwagon();
	} // GEN-LAST:event_wagonTypesJListMouseClicked

	private void wagonTypesJListKeyTyped(java.awt.event.KeyEvent evt) { // GEN-FIRST:event_wagonTypesJListKeyTyped
		// Add your handling code here:
		if (KeyEvent.VK_ENTER == evt.getKeyCode()) {
			addwagon();
		} else {

		}

	} // GEN-LAST:event_wagonTypesJListKeyTyped

	// Adds the wagon selected in the list to the train consist.
	private void addwagon() {
		if (wagons.size() < TrainModel.MAX_NUMBER_OF_WAGONS) {
			int type = wagonTypesJList.getSelectedIndex();
			wagons.add(new Integer(type));

			updateMaxWagonsText();
			this.repaint();
		}

	}

	private void updateMaxWagonsText() {
		if (wagons.size() >= TrainModel.MAX_NUMBER_OF_WAGONS) {
			jLabel1.setText("Max train length is "
					+ TrainModel.MAX_NUMBER_OF_WAGONS + " wagons");
		} else {
			jLabel1.setText("");
		}
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) { // GEN-FIRST:event_jButton1ActionPerformed
		// Add your handling code here:
		wagons.clear();
		jLabel1.setText("");
		this.repaint();
	} // GEN-LAST:event_jButton1ActionPerformed

	public void paint(Graphics g) {
		// paint the background
		g.drawImage(this.stationView, 0, 0, null);

		int x = this.getWidth();

		int y = 330;

		final int SCALED_IMAGE_HEIGHT = 50;
		// paint the wagons
		for (int i = this.wagons.size() - 1; i >= 0; i--) { // Count down so we
			// paint the wagon
			// at the end of the
			// train first.

			Integer type = wagons.get(i);
			Image image = vl.getTrainImages().getSideOnWagonImage(
					type.intValue());
			int scaledWidth = image.getWidth(null) * SCALED_IMAGE_HEIGHT
					/ image.getHeight(null);
			x -= scaledWidth;
			g.drawImage(image, x, y, scaledWidth, SCALED_IMAGE_HEIGHT, null);

		}

		// paint the engine
		if (-1 != this.engineType) { // If an engine is selected.
			Image image = vl.getTrainImages().getSideOnEngineImage(
					this.engineType);
			int scaledWidth = (image.getWidth(null) * SCALED_IMAGE_HEIGHT)
					/ image.getHeight(null);
			x -= scaledWidth;
			g.drawImage(image, x, y, scaledWidth, SCALED_IMAGE_HEIGHT, null);
		}

		this.paintChildren(g);
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton clearjButton;

	private javax.swing.JLabel jLabel1;

	private javax.swing.JPanel jPanel1;

	private javax.swing.JScrollPane jScrollPane1;

	private javax.swing.JButton okjButton;

	private javax.swing.JList wagonTypesJList;

	// End of variables declaration//GEN-END:variables
	final private class WagonCellRenderer implements ListCellRenderer {
		private final Component[] labels;

		final TrainImages trainImages;

		public WagonCellRenderer(World2ListModelAdapter w2lma, TrainImages s) {
			trainImages = s;

			labels = new Component[w2lma.getSize()];
			for (int i = 0; i < w2lma.getSize(); i++) {
				JLabel label = new JLabel();
				label.setFont(new java.awt.Font("Dialog", 0, 12));
				Image image = trainImages.getSideOnWagonImage(i);
				int height = image.getHeight(null);
				int width = image.getWidth(null);
				int scale = height / 10;

				ImageIcon icon = new ImageIcon(image.getScaledInstance(width
						/ scale, height / scale, Image.SCALE_FAST));
				label.setIcon(icon);
				labels[i] = label;
			}
		}

		public Component getListCellRendererComponent(JList list, Object value, /*
																				 * value
																				 * to
																				 * display
																				 */
		int index, /* cell index */
		boolean isSelected, /* is the cell selected */
		boolean cellHasFocus) /* the list and the cell have the focus */{
			if (index >= 0 && index < labels.length) {
				CargoType cargoType = (CargoType) value;
				String text = "<html><body>"
						+ (isSelected ? "<strong>" : "")
						+ cargoType.getDisplayName()
						+ (isSelected ? "</strong>"
								: "&nbsp;&nbsp;&nbsp;&nbsp;"/*
															 * padding to stop
															 * word wrap due to
															 * greater wodth of
															 * strong font
															 */) + "</body></html>";
				((JLabel) labels[index]).setText(text);
				return labels[index];
			}
			return null;
		}
	}

	public void setup(ModelRoot mr, ViewLists vl,
			Action closeAction) {
		World2ListModelAdapter w2lma = new World2ListModelAdapter(
				mr.getWorld(), SKEY.CARGO_TYPES);
		this.wagonTypesJList.setModel(w2lma);
		this.vl = vl;
		TrainImages trainImages = vl.getTrainImages();
		WagonCellRenderer wagonCellRenderer = new WagonCellRenderer(w2lma,
				trainImages);
		this.wagonTypesJList.setCellRenderer(wagonCellRenderer);
		this.okjButton.addActionListener(closeAction);
	}

	public int[] getWagons() {
		int[] wagonsArray = new int[wagons.size()];
		for (int i = 0; i < wagons.size(); i++) {
			Integer type = wagons.get(i);
			wagonsArray[i] = type.intValue();
		}
		return wagonsArray;
	}

	public void setEngineType(int engineType) {
		this.engineType = engineType;
	}

}

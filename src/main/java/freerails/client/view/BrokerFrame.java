/*
 * FreeRails
 * Copyright (C) 2000-2018 The FreeRails Team
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package freerails.client.view;

import freerails.client.renderer.RendererRoot;
import freerails.controller.ModelRoot;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 *
 */
public class BrokerFrame extends JInternalFrame {

    private static final long serialVersionUID = 4121409622587815475L;
    private static final Logger logger = Logger.getLogger(BrokerFrame.class.getName());
    JMenu bonds;
    JMenuBar brokerMenu;
    JButton done;
    JLabel htmlJLabel;
    JMenuItem issueBond;
    JPanel jPanel1;
    JScrollPane jScrollPane1;
    JMenuItem repayBond;
    JMenu stocks;

    /**
     * Creates new form BrokerFrame
     */
    BrokerFrame() {
        initComponents();
    }

    /**
     * @param url
     */
    public BrokerFrame(URL url) {
        initComponents();
        setHtml(loadText(url));
    }

    /**
     * @param url
     * @param context
     */
    public BrokerFrame(URL url, HashMap context) {
        initComponents();
        String template = loadText(url);
        String populatedTemplate = populateTokens(template, context);
        setHtml(populatedTemplate);
    }

    /**
     * @param html
     */
    public BrokerFrame(String html) {
        initComponents();
        setHtml(html);
    }

    /**
     * Load the help text from file.
     */
    static String loadText(final URL htmlUrl) {
        try {
            InputStream in = htmlUrl.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(in)));
            String line;
            StringBuilder text = new StringBuilder();
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            return text.toString();
        } catch (Exception e) {
            logger.warn(htmlUrl.toString());
            return "Couldn't read: " + htmlUrl;
        }
    }

    /**
     * @param template
     * @param context
     * @return
     */
    public static String populateTokens(String template, Object context) {
        StringTokenizer tokenizer = new StringTokenizer(template, "$");
        StringBuilder output = new StringBuilder();

        while (tokenizer.hasMoreTokens()) {
            output.append(tokenizer.nextToken());
            if (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                String value;
                if (context instanceof HashMap) {
                    value = (String) ((HashMap) context).get(token);
                } else {
                    try {
                        Field field = context.getClass().getField(token);
                        value = field.get(context).toString();
                    } catch (Exception e) {
                        throw new NoSuchElementException(token);
                    }
                }
                output.append(value);
            }
        }

        return output.toString();
    }

    /**
     * @param m
     * @param vl
     * @param closeAction
     */
    public void setup(ModelRoot m, RendererRoot vl, Action closeAction) {
        done.setAction(closeAction);
    }

    void setHtml(String s) {
        htmlJLabel.setText(s);
    }


    // ">
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new JScrollPane();
        jPanel1 = new JPanel();
        htmlJLabel = new JLabel();
        done = new JButton();
        brokerMenu = new JMenuBar();
        bonds = new JMenu();
        issueBond = new JMenuItem();
        repayBond = new JMenuItem();
        stocks = new JMenu();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jPanel1.setLayout(new java.awt.BorderLayout());

        htmlJLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        htmlJLabel.setText("sdfa");
        htmlJLabel.setVerticalAlignment(SwingConstants.TOP);
        htmlJLabel.setVerticalTextPosition(SwingConstants.TOP);
        jPanel1.add(htmlJLabel, java.awt.BorderLayout.CENTER);

        jScrollPane1.setViewportView(jPanel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jScrollPane1, gridBagConstraints);

        done.setText("Close");
        done.setVerifyInputWhenFocusTarget(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        getContentPane().add(done, gridBagConstraints);

        bonds.setText("Bonds");
        issueBond.setText("Issue Bond");
        bonds.add(issueBond);

        repayBond.setText("Repay Bond");
        bonds.add(repayBond);

        brokerMenu.add(bonds);

        stocks.setText("Stocks");
        brokerMenu.add(stocks);

        setJMenuBar(brokerMenu);
        pack();
    }
}
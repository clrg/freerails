/*
 * Created on Mar 18, 2004
 */
package freerails.client.view;

import freerails.client.renderer.RenderersRoot;
import freerails.config.ClientConfig;
import freerails.controller.ModelRoot;
import freerails.world.player.FreerailsPrincipal;
import freerails.world.top.ReadOnlyWorld;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * A HtmlJPanel that displays the income statement.
 *
 * @author Luke
 */
public class IncomeStatementHtmlJPanel extends HtmlJPanel implements View {
    private static final long serialVersionUID = 3257846588885120057L;

    private String template;

    private ModelRoot modelRoot;

    public IncomeStatementHtmlJPanel() {
        super();

        URL url = IncomeStatementHtmlJPanel.class
                .getResource(ClientConfig.VIEW_INCOME_STATEMENT);
        template = loadText(url);
    }

    @Override
    public void setup(ModelRoot modelRoot, RenderersRoot vl, Action closeAction) {
        super.setup(modelRoot, vl, closeAction);
        this.modelRoot = modelRoot;
        updateHtml();
    }

    private void updateHtml() {
        ReadOnlyWorld world = modelRoot.getWorld();
        FreerailsPrincipal playerPrincipal = modelRoot.getPrincipal();
        IncomeStatementGenerator balanceSheetGenerator = new IncomeStatementGenerator(
                world, playerPrincipal);
        balanceSheetGenerator.calculateAll();
        String populatedTemplate = populateTokens(template,
                balanceSheetGenerator);
        setHtml(populatedTemplate);
    }

    @Override
    protected void paintComponent(Graphics g) {
        /* Check to see if the text needs updating before painting. */
        ReadOnlyWorld world = modelRoot.getWorld();
        FreerailsPrincipal playerPrincipal = modelRoot.getPrincipal();
        int currentNumberOfTransactions = world
                .getNumberOfTransactions(playerPrincipal);

        int lastNumTransactions = 0;
        if (currentNumberOfTransactions != lastNumTransactions) {
            updateHtml();
        }

        super.paintComponent(g);
    }
}
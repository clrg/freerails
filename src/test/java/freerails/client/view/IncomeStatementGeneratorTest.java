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

import freerails.model.finances.IncomeStatementGenerator;
import freerails.util.Vector2D;
import freerails.model.world.FullWorld;
import freerails.model.world.SharedKey;
import freerails.model.world.World;
import freerails.model.cargo.CargoBatch;
import freerails.model.cargo.CargoCategory;
import freerails.model.cargo.CargoType;
import freerails.model.finances.CargoDeliveryMoneyTransaction;
import freerails.model.finances.Money;
import freerails.model.MapFixtureFactory;
import junit.framework.TestCase;

/**
 * Test for IncomeStatementGenerator.
 */
public class IncomeStatementGeneratorTest extends TestCase {

    private World world;
    private IncomeStatementGenerator balanceSheetGenerator;

    /**
     *
     */
    public void testCalExpense() {
        balanceSheetGenerator.calculateAll();
        Money m = balanceSheetGenerator.mailTotal;
        assertEquals(0, m.amount);

        CargoType ct = (CargoType) world.get(SharedKey.CargoTypes, 0);
        assertEquals(CargoCategory.Mail, ct.getCategory());

        Money amount = new Money(100);
        addTrans(CargoCategory.Mail, amount);
        addTrans(CargoCategory.Passengers, amount);
        balanceSheetGenerator.calculateAll();
        m = balanceSheetGenerator.mailTotal;
        assertEquals(amount, m);
    }

    private void addTrans(CargoCategory category, Money amount) {
        for (int i = 0; i < world.size(SharedKey.CargoTypes); i++) {
            CargoType ct = (CargoType) world.get(SharedKey.CargoTypes, i);

            if (ct.getCategory() == category) {
                CargoBatch cb = new CargoBatch(i, Vector2D.ZERO, 0, 0);
                world.addTransaction(MapFixtureFactory.TEST_PRINCIPAL,
                        new CargoDeliveryMoneyTransaction(amount, 10, 0, cb, 1));
                return;
            }
        }

        throw new IllegalArgumentException(category.toString());
    }

    /**
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        world = new FullWorld();
        world.addPlayer(MapFixtureFactory.TEST_PLAYER);
        MapFixtureFactory.generateCargoTypesList(world);
        balanceSheetGenerator = new IncomeStatementGenerator(world, MapFixtureFactory.TEST_PRINCIPAL);
    }
}
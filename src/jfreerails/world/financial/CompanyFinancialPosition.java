/* Generated by Together */

package jfreerails.world.financial;


public class CompanyFinancialPosition {
    /**
     * Does ...
     * 
     * @param inThisCompany ...
     * @return A int with ...
     */
    public int getNumberOfSharesOwned(ListedCompany inThisCompany) {
        return 0;
    }

    /**
     * Does ...
     * 
     * @param stock ...
     */
    public void addStock(Stock stock) {
    }

    /**
     * Does ...
     * 
     * 
     */
    public void getNumberOfOutstandingBonds() {
    }

    /**
     * Does ...
     * 
     * 
     */
    public void getStockPrice() {
    }

    public int getNumberOfBankruptcies() {
        return numberOfBankruptcies;
    }

    public void setNumberOfBankruptcies(int numberOfBankruptcies) {
        this.numberOfBankruptcies = numberOfBankruptcies;
    }

    public Money getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(Money netWorth) {
        this.netWorth = netWorth;
    }

    public CurrentAccount getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(CurrentAccount currentAccount) { this.currentAccount = currentAccount; }

    /**
     * Represents ...
     * 
     */
    private int numberOfBankruptcies;

    /**
     * Represents ...
     * 
     */
    private Money netWorth;
    public CurrentAccount currentAccount;
}
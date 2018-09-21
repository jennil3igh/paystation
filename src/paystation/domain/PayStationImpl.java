package paystation.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the pay station.
 *
 * Responsibilities:
 *
 * 1) Accept payment; 2) Calculate parking time based on payment; 3) Know
 * earning, parking time bought; 4) Issue receipts; 5) Handle buy and cancel
 * events.
 *
 * This source code is from the book "Flexible, Reliable Software: Using
 * Patterns and Agile Development" published 2010 by CRC Press. Author: Henrik B
 * Christensen Computer Science Department Aarhus University
 *
 * This source code is provided WITHOUT ANY WARRANTY either expressed or
 * implied. You may study, use, modify, and distribute it for non-commercial
 * purposes. For any commercial use, see http://www.baerbak.com/
 */
public class PayStationImpl implements PayStation {

    private int insertedSoFar;
    private int timeBought;
    //all coins inserted bought
    private int totalBought;
    //the total amount of money collected since last call
    //only add if you buy
    private int lastCall;
    //coin mapping
    private Map<Integer, Integer> coinMap = new HashMap<Integer, Integer>();
    //coins
    private int nickleCount;
    private int dimeCount;
    private int quarterCount;

    @Override
    public void addPayment(int coinValue)
            throws IllegalCoinException {
        switch (coinValue) {
            case 5:
                nickleCount++;
                coinMap.put(5, nickleCount);
                break;
            case 10:
                dimeCount++;
                coinMap.put(10, dimeCount);
                break;
            case 25:
                quarterCount++;
                coinMap.put(25, quarterCount);
                break;
            default:
                throw new IllegalCoinException("Invalid coin: " + coinValue);
        }
         
        insertedSoFar += coinValue;
        timeBought = insertedSoFar / 5 * 2;
    }

    @Override
    public int readDisplay() {
        return timeBought;
    }

    @Override
    public Receipt buy() {
        Receipt r = new ReceiptImpl(timeBought);
        totalBought += insertedSoFar;
        reset();
        coinMap.clear();
        return r;
    }

    @Override
    public Map<Integer, Integer> cancel() {
        Map<Integer, Integer> coinMapReturn = new HashMap<Integer, Integer>();
       coinMapReturn.putAll(coinMap);
        
        reset();
        coinMap.clear();
        return coinMapReturn;
    }

    private void reset() {
        timeBought = insertedSoFar = nickleCount = dimeCount = quarterCount = 0;
    }

    /*returns the total amount of money collected 
    by the paystation since the last call and empties 
    it, setting the total to zero. Note that money 
    is only collected after a call to buy*/
    @Override
    public int empty() {
        int cents = totalBought;
        
        totalBought = 0;
        //resests the total amount to zero
        reset();

        return cents;
    }
}

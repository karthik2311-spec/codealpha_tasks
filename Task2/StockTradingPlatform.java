import java.util.*;

class Stock {
    String symbol;
    String name;
    double price;

    Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }
}

class Transaction {
    String type;
    String symbol;
    int quantity;
    double price;

    Transaction(String type, String symbol, int quantity, double price) {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
    }
}

class User {
    String name;
    double balance;
    HashMap<String, Integer> portfolio;
    ArrayList<Transaction> transactions;

    User(String name, double balance) {
        this.name = name;
        this.balance = balance;
        portfolio = new HashMap<>();
        transactions = new ArrayList<>();
    }
}

public class StockTradingPlatform {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Stock> stocks = new ArrayList<>();
    static ArrayList<User> users = new ArrayList<>();

    public static void main(String[] args) {

        // Sample market data
        stocks.add(new Stock("AAPL", "Apple", 220.00));
        stocks.add(new Stock("GOOGL", "Google", 180.00));
        stocks.add(new Stock("MSFT", "Microsoft", 420.00));
        stocks.add(new Stock("AMZN", "Amazon", 200.00));
        stocks.add(new Stock("TSLA", "Tesla", 250.00));

        System.out.println("================================");
        System.out.println("     STOCK TRADING PLATFORM");
        System.out.println("================================");

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        User user = new User(name, 10000);
        users.add(user);

        int choice;

        do {
            System.out.println("\n========== MENU ==========");
            System.out.println("1. View Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transaction History");
            System.out.println("6. Update Stock Prices");
            System.out.println("7. Exit");
            System.out.println("==========================");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    showMarketData();
                    break;

                case 2:
                    buyStock(user);
                    break;

                case 3:
                    sellStock(user);
                    break;

                case 4:
                    showPortfolio(user);
                    break;

                case 5:
                    showTransactions(user);
                    break;

                case 6:
                    updatePrices();
                    break;

                case 7:
                    System.out.println("Thank you for using the Stock Trading Platform!");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 7);

        sc.close();
    }

    // Display available stocks
    static void showMarketData() {

        System.out.println("\n========== MARKET DATA ==========");

        System.out.printf("%-10s %-15s %-10s%n",
                "Symbol", "Company", "Price");

        System.out.println("----------------------------------------");

        for (Stock stock : stocks) {
            System.out.printf("%-10s %-15s %.2f%n",
                    stock.symbol,
                    stock.name,
                    stock.price);
        }
    }

    // Buy stock
    static void buyStock(User user) {

        showMarketData();

        System.out.print("\nEnter stock symbol to buy: ");
        String symbol = sc.next().toUpperCase();

        Stock stock = findStock(symbol);

        if (stock == null) {
            System.out.println("Stock not found.");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = sc.nextInt();

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return;
        }

        double cost = stock.price * quantity;

        if (cost > user.balance) {
            System.out.println("Insufficient balance.");
            return;
        }

        user.balance -= cost;

        int currentQuantity = user.portfolio.getOrDefault(symbol, 0);
        user.portfolio.put(symbol, currentQuantity + quantity);

        user.transactions.add(
                new Transaction("BUY", symbol, quantity, stock.price)
        );

        System.out.printf(
                "Successfully bought %d shares of %s for %.2f%n",
                quantity, symbol, cost
        );

        System.out.printf("Remaining balance: %.2f%n", user.balance);
    }

    // Sell stock
    static void sellStock(User user) {

        if (user.portfolio.isEmpty()) {
            System.out.println("You do not own any stocks.");
            return;
        }

        showPortfolio(user);

        System.out.print("\nEnter stock symbol to sell: ");
        String symbol = sc.next().toUpperCase();

        if (!user.portfolio.containsKey(symbol)) {
            System.out.println("You do not own this stock.");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = sc.nextInt();

        int ownedQuantity = user.portfolio.get(symbol);

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return;
        }

        if (quantity > ownedQuantity) {
            System.out.println("You do not own enough shares.");
            return;
        }

        Stock stock = findStock(symbol);

        double revenue = stock.price * quantity;

        user.balance += revenue;

        int remaining = ownedQuantity - quantity;

        if (remaining == 0) {
            user.portfolio.remove(symbol);
        } else {
            user.portfolio.put(symbol, remaining);
        }

        user.transactions.add(
                new Transaction("SELL", symbol, quantity, stock.price)
        );

        System.out.printf(
                "Successfully sold %d shares of %s for %.2f%n",
                quantity, symbol, revenue
        );

        System.out.printf("Current balance: %.2f%n", user.balance);
    }

    // Display user's portfolio
    static void showPortfolio(User user) {

        System.out.println("\n========== YOUR PORTFOLIO ==========");

        System.out.printf("Cash Balance: %.2f%n", user.balance);

        if (user.portfolio.isEmpty()) {
            System.out.println("You don't own any stocks.");
            return;
        }

        double portfolioValue = 0;

        System.out.printf(
                "%-10s %-10s %-15s%n",
                "Symbol", "Quantity", "Current Value"
        );

        System.out.println("-------------------------------------");

        for (String symbol : user.portfolio.keySet()) {

            int quantity = user.portfolio.get(symbol);

            Stock stock = findStock(symbol);

            double value = stock.price * quantity;

            portfolioValue += value;

            System.out.printf(
                    "%-10s %-10d %.2f%n",
                    symbol,
                    quantity,
                    value
            );
        }

        double totalValue = user.balance + portfolioValue;

        System.out.println("-------------------------------------");

        System.out.printf(
                "Stock Value: %.2f%n",
                portfolioValue
        );

        System.out.printf(
                "Total Portfolio Value: %.2f%n",
                totalValue
        );
    }

    // Display transaction history
    static void showTransactions(User user) {

        System.out.println("\n========== TRANSACTION HISTORY ==========");

        if (user.transactions.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }

        for (Transaction t : user.transactions) {

            double total = t.quantity * t.price;

            System.out.printf(
                    "%s | %s | Quantity: %d | Price: %.2f | Total: %.2f%n",
                    t.type,
                    t.symbol,
                    t.quantity,
                    t.price,
                    total
            );
        }
    }

    // Simulate changing market prices
    static void updatePrices() {

        Random random = new Random();

        System.out.println("\n========== MARKET UPDATE ==========");

        for (Stock stock : stocks) {

            double change = (random.nextDouble() * 20) - 10;

            stock.price += change;

            if (stock.price < 1) {
                stock.price = 1;
            }

            System.out.printf(
                    "%s new price: %.2f%n",
                    stock.symbol,
                    stock.price
            );
        }

        System.out.println("Market prices have been updated.");
    }

    // Find a stock using its symbol
    static Stock findStock(String symbol) {

        for (Stock stock : stocks) {

            if (stock.symbol.equalsIgnoreCase(symbol)) {
                return stock;
            }
        }

        return null;
    }
}

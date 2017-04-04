package com.example.android.justjava; /**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 1;
    double price = 5;
    double priceChocolate = 1;
    double priceWhippedCream = .5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when + button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100) {
            //show error message
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity++;
        displayQuantity(quantity);
    }

    /**
     * This method is called when - button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 1) {
            //show error message
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity--;
        displayQuantity(quantity);
    }

    /**
     * Calculates the price of the order based on the current quantity.
     *
     * @param price           - base price
     * @param quantity        - number of coffees being orderred
     * @param hasWhippedCream - returns True if whipped cream checkbox is checked
     * @param hasChocolate    - returns True if chocolate checkbox is checked
     * @return string that is formatted to the correct currency
     */
    private String calculatePrice(double price, int quantity, boolean hasWhippedCream, boolean hasChocolate) {
        double totalPrice;

        if (hasChocolate)
            price = price + priceChocolate;
        if (hasWhippedCream)
            price = price + priceWhippedCream;

        totalPrice = price * quantity;

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String totalPriceString = formatter.format(totalPrice);

        return totalPriceString;
    }

    /**
     * Generates a message to display when order is submited
     *
     * @param name           - Name on order
     * @param numberOfCoffee - quantity ordered
     * @param price          - price of one unit
     * @return message to show on receipt
     */

    private String createOrderSummary(String name, int numberOfCoffee, double price, boolean hasWhippedCream, boolean hasChocolate) {
        String priceMessage = getString(R.string.order_summary_name, name);
        priceMessage = priceMessage + "\nAdded Whipped Cream: " + hasWhippedCream;
        priceMessage = priceMessage + "\nAdded Chocolate: " + hasChocolate;
        priceMessage = priceMessage + "\nQuantity: " + numberOfCoffee;
        priceMessage = priceMessage + "\nTotal: " + calculatePrice(price, numberOfCoffee, hasWhippedCream, hasChocolate);
        // grabs the thank you string from the strings.xml resource - this allows for translation
        priceMessage = priceMessage + "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        String priceMessage;

        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        CheckBox chocolateCreamCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        EditText nameInput = (EditText) findViewById(R.id.editName);

        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        boolean hasChocolate = chocolateCreamCheckBox.isChecked();
        String name = nameInput.getText().toString();

        // add log messages to see the status of some variables within the android monitor
       // Log.v("MainActivity", "The checkbox status is " + hasWhippedCream);

        priceMessage = createOrderSummary(name, quantity, price, hasWhippedCream, hasChocolate);

        //displays the message on the app
        displayMessage(priceMessage);

        //Sends a request to email app that will display the messages
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java order for " + name);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);  //the findViewById method returns a view - in order to get TextView we need to cast by doing (TextView)
        quantityTextView.setText("" + numberOfCoffees);
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }
}
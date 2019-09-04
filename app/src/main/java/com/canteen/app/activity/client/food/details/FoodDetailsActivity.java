package com.canteen.app.activity.client.food.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.canteen.app.R;
import com.canteen.app.activity.client.cart.OrderCartActivity;
import com.canteen.app.models.Food;
import com.canteen.app.service.ToastService;
import com.canteen.app.service.login.LoginService;
import com.canteen.app.service.login.LoginServiceImpl;
import com.canteen.app.service.order.OrderCartService;
import com.canteen.app.service.order.OrderItem;
import com.canteen.app.service.price.PriceFormatter;
import com.canteen.app.service.price.PriceFormatterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodDetailsActivity extends AppCompatActivity implements PriceContainer {

    private Food food;

    private FoodAdditionAdapter adapter;

    private LoginService loginService = LoginServiceImpl.of();

    @BindView(R.id.food_description_text_view)
    TextView foodDescriptionTextView;

    @BindView(R.id.food_price_text_view)
    TextView foodPriceTextView;

    @BindView(R.id.food_additions_recycler_view)
    RecyclerView foodAdditionsRecyclerView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        ButterKnife.bind(this);

        retrieveFood();
        initView();
        updatePrice(0);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                startActivity(new Intent(this, OrderCartActivity.class));
                return true;
            case R.id.action_logout:
                loginService.logout(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.add_to_cart_button)
    void addToCartButtonHandler() {
        OrderCartService.getInstance().addItem(OrderItem.builder()
                .food(food)
                .additions(adapter.getSelected())
                .build());
        ToastService.make(getString(R.string.food_added_to_cart));
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(food.getName());
        }

        foodAdditionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FoodAdditionAdapter(food.getAdditions(), this);
        foodAdditionsRecyclerView.setAdapter(adapter);

        foodDescriptionTextView.setText(food.getDescription());
    }

    private void retrieveFood() {
        food = (Food) getIntent().getSerializableExtra("food");
    }

    @Override
    public void updatePrice(final double priceIncrease) {
        PriceFormatter formatter = PriceFormatterImpl.of();
        foodPriceTextView.setText(formatter.format(food.getPrice() + priceIncrease));
    }
}

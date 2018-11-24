package com.example.microtemp.microblog.Activities.menu.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.microtemp.microblog.AdapterContainer;
import com.example.microtemp.microblog.R;
import com.example.microtemp.microblog.api.HttpRequestData;
import com.example.microtemp.microblog.api.HttpRequestMethods;
import com.example.microtemp.microblog.api.handlers.AllMenusRequestHandler;
import com.example.microtemp.microblog.api.models.requests.AllMenusRequestBody;
import com.example.microtemp.microblog.api.models.responses.AllMenusResponse;


public class MenuListsActivity extends AppCompatActivity implements AdapterContainer {

    private RecyclerView menusRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        this.menusRecyclerView = findViewById(R.id.menusRecyclerView);
        this.menusRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        this.menusRecyclerView.setLayoutManager(layoutManager);

        AllMenusRequestBody requestBody = new AllMenusRequestBody();
        HttpRequestData<AllMenusRequestBody> requestData = HttpRequestData
                .<AllMenusRequestBody>builder()
                .method(HttpRequestMethods.GET)
                .requestBody(requestBody)
                .build();
        new AllMenusRequestHandlerImpl(this).execute(requestData);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        this.menusRecyclerView.setAdapter(adapter);
    }


    private static class AllMenusRequestHandlerImpl extends AllMenusRequestHandler {

        private final AdapterContainer adapterContainer;

        AllMenusRequestHandlerImpl(AdapterContainer adapterContainer) {
            this.adapterContainer = adapterContainer;
        }

        @Override
        protected void onPostExecute(AllMenusResponse result) {
            MenuListsAdapter adapter = new MenuListsAdapter(result.getData().getMenus());
            this.adapterContainer.setAdapter(adapter);
        }
    }
}


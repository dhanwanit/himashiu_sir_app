package com.arcticfoxappz.walmartcoupon.activity.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.arcticfoxappz.walmartcoupon.R;
import com.arcticfoxappz.walmartcoupon.activity.CouponCodeActivity;
import com.arcticfoxappz.walmartcoupon.activity.ClickListenerItem;
import com.arcticfoxappz.walmartcoupon.adapters.ProductsAdapter;
import com.arcticfoxappz.walmartcoupon.bean.Coupons;
import com.arcticfoxappz.walmartcoupon.utils.AppConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    List<Coupons> productList;
    SharedPreferences mPrefs;
    Coupons coupons;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
       // onTokenRefresh();

        SetupUI();

       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        return view;
    }

    private void SetupUI(){
        recyclerView = view.findViewById(R.id.recylcerView);
        productList = new ArrayList<>();
      /*  progressBar = (ProgressBar)view.findViewById(R.id.spin_kit);
        Sprite doubleBounce = new FadingCircle();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.VISIBLE);*/

     /*  Handler handler = new Handler();
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
             //  CheckVersion();
           }
       },3000);*/
        loadCoupons();
    }

    private  void CheckVersion(){
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.check_version,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("values");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONArray inner_array=jsonArray.getJSONArray(i);
                                String version = String.valueOf(inner_array.get(1));

                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
                                String chk_version = sharedPreferences.getString("version", "");
                                if (version.equals(chk_version)){
                                    Gson gson = new Gson();
                                    String json = sharedPreferences.getString("task list", null);
                                    Type type = new TypeToken<ArrayList<Coupons>>() {}.getType();
                                    productList = gson.fromJson(json, type);
                                    if (productList == null) {
                                        productList = new ArrayList<>();
                                    }
                                    ProductsAdapter adapter = new ProductsAdapter(getContext(),productList,clickListener);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(adapter);
                                }else {

                                    loadCoupons();

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("version", version);
                                    editor.apply();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
                        Gson gson = new Gson();
                        String json = sharedPreferences.getString("task list", null);
                        Type type = new TypeToken<ArrayList<Coupons>>() {}.getType();
                        productList = gson.fromJson(json, type);
                        if (productList == null) {
                            productList = new ArrayList<>();
                        }
                        ProductsAdapter adapter = new ProductsAdapter(getContext(),productList,clickListener);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }
                });
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private  void loadCoupons(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConstants.fullURL_coupons,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("values");

                            for(int i=0;i<jsonArray.length();i++){
                                JSONArray inner_array=jsonArray.getJSONArray(i);
                                coupons = new Coupons(""+inner_array.get(0),""+inner_array.get(1),""+inner_array.get(2));
                                coupons.setDescription(""+inner_array.get(0));
                                coupons.setCode(""+inner_array.get(1));
                                coupons.setUrl(""+inner_array.get(2));
                                productList.add(coupons);

                               sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(productList);
                                editor.putString("task list", json);
                                editor.apply();
                            }

                            SharedPreferences.Editor editor2 = sharedPreferences.edit();
                            editor2.clear();
                            editor2.putString("web_url", ""+jsonArray.getJSONArray(1).get(3));
                            editor2.apply();

                            ProductsAdapter adapter = new ProductsAdapter(getContext(),productList,clickListener);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
                        Gson gson = new Gson();
                        String json = sharedPreferences.getString("task list", null);
                        Type type = new TypeToken<ArrayList<Coupons>>() {}.getType();
                        productList = gson.fromJson(json, type);
                        if (productList == null) {
                            productList = new ArrayList<>();
                        }
                        ProductsAdapter adapter = new ProductsAdapter(getContext(),productList,clickListener);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);
                    }
                });
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private ClickListenerItem clickListener = new ClickListenerItem() {
        @Override
        public void onItemCLickListener(int position, Coupons coupons) {
            Intent intent = new Intent(getContext(), CouponCodeActivity.class);
            intent.putExtra("code",coupons.getCode());
            intent.putExtra("desc",coupons.getDescription());
            intent.putExtra("url",coupons.getUrl());
            startActivity(intent);
            //Toast.makeText(getContext(), ""+position, Toast.LENGTH_SHORT).show();
        }
    };
}
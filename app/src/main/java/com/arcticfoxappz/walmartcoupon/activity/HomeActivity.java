package com.arcticfoxappz.walmartcoupon.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.facebook.ads.AdView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.arcticfoxappz.walmartcoupon.R;
import com.arcticfoxappz.walmartcoupon.activity.ui.gallery.GalleryFragment;
import com.arcticfoxappz.walmartcoupon.activity.ui.home.HomeFragment;
import com.arcticfoxappz.walmartcoupon.utils.AppConstants;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AlertDialog.Builder builder;
    private AdView adView;
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        builder = new AlertDialog.Builder(this);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("show_rating");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value = dataSnapshot.getValue(String.class);
                Log.d("firebase", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("firebase", "Failed to read value.", error.toException());
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_rateus,
//              R.id.nav_more,
                R.id.nav_share,
                R.id.nav_exit
        ).setDrawerLayout(drawer).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;

                    case R.id.nav_rateus:
                        Uri uri = Uri.parse(AppConstants.app_id_link+"&hl=en");
                        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(myAppLinkToMarket);
                        break;
//                    case R.id.nav_more:
//                        Intent i = new Intent(Intent.ACTION_VIEW);
//                        i.setData(Uri.parse(AppConstants.developer_id_link));
//                        startActivity(i);
//                        break;
                    case R.id.nav_share:
                        try {
                            Intent i1 = new Intent(Intent.ACTION_SEND);
                            i1.setType("text/plain");
                            i1.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                            String sAux = "Download app now.\n\n";
                            sAux = sAux + AppConstants.app_id_link+"\n\n";
                            i1.putExtra(Intent.EXTRA_TEXT, sAux);
                            startActivity(Intent.createChooser(i1, "Choose one"));
                        } catch(Exception e) {
                            //e.toString();
                        }
                        break;
                    case R.id.nav_exit:
                        AlertMessage();
                        break;
                }
                NavigationUI.onNavDestinationSelected(menuItem,navController);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
             //   Log.d("position",""+position);
            }

            @Override
            public void onPageSelected(int position) {

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                String alert_box = pref.getString("alert_box", null);

                    if (position == 1) {
                        if (value.equalsIgnoreCase("yes")) {
                        if (alert_box == null) {
                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.NORMAL_TYPE);
                            sweetAlertDialog.setCancelable(false);
                            sweetAlertDialog.setTitleText("Rate this app");
                            sweetAlertDialog.setContentText("Please r@te us g00d to see premium c0up0ns!");
                            sweetAlertDialog.setConfirmText("R@te us");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("alert_box", "click");
                                    editor.commit();

                                    sweetAlertDialog.dismissWithAnimation();

                                    try {
                                        Intent viewIntent =
                                                new Intent("android.intent.action.VIEW",
                                                        Uri.parse("https://play.google.com/store/apps/details?id=com.arcticfoxappz.walmartcoupon"));
                                        startActivity(viewIntent);
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Unable to Connect Try Again...",
                                                Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }
                            });
                            sweetAlertDialog.setCancelText("Cancel")
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();
                                            Intent i = new Intent(HomeActivity.this, HomeActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    });
                            sweetAlertDialog.show();
                        }else {
                        }
                    }else {
                        }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
              //  Log.d("position",""+state);
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "New Coupons");
        adapter.addFragment(new GalleryFragment(), "Premium Coupons");
       //adapter.addFragment(new SlideshowFragment(), "Three");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    private void AlertMessage(){
        builder.setMessage("Are you sure, you want to exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        HomeActivity.this.finish();
                        Toast.makeText(getApplicationContext(),"You are exit",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      //Action for 'NO' Button
                        dialog.cancel();

                           /* Toast.makeText(getApplicationContext(),"Toast.LENGTH_SHORT).show();*/
                    }
                });

        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Exit...!!!");
        alert.show();
    }

    private void callHomeActivity(){
        Intent intent = new Intent(this,HomeActivity.class);
    }
}

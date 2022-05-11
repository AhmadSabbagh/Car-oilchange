package ebtkar.app.oilex2.activities;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import ebtkar.app.oilex2.FinishedOrdersActivity;
import ebtkar.app.oilex2.MyOrdersActivity;
import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.adapters.DrawerMenuItemsAdapter;
import ebtkar.app.oilex2.helper.APIUrl;
import ebtkar.app.oilex2.helper.BackgroundServices;
import ebtkar.app.oilex2.helper.ConnectionUtils;
import ebtkar.app.oilex2.helper.PostAction;
import ebtkar.app.oilex2.helper.SharedPrefManager;
import ebtkar.app.oilex2.models.CostumeMenuItem;
import ebtkar.app.oilex2.models.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RootActivity extends AppCompatActivity {


  //  private final static int PLACE_PICKER_REQUEST = 999, PICK_FROM_GALLERY = 888;

    //double latitude = -1, longitude = -1;


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.e("RESULTED ", requestCode + " and " + resultCode);
//
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//
//
//                case PLACE_PICKER_REQUEST:
//                    Place place = PlacePicker.getPlace(RootActivity.this, data);
//                    // String placeName = String.format("Place: %s", place.getName());
//                    latitude = place.getLatLng().latitude;
//                    longitude = place.getLatLng().longitude;
//                    Toast.makeText(this, getString(R.string.you_selected) +
//                                    place.getAddress()
//                                    +
//                                    getString(R.string.on_the_map)
//
//                            , Toast.LENGTH_SHORT).show();
//                    Intent ii = new Intent(RootActivity.this, SelectCarActivity.class);
//                    ii.putExtra("lng", longitude + "");
//                    ii.putExtra("lat", latitude + "");
//                    startActivity(ii);
//                    req_place = false;
//                    break;
//
//
//            }
//        }
//
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
//                                           @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case PICK_FROM_GALLERY:
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    pickPlace();
//                } else {
//                    Toast.makeText(getApplicationContext(), R.string.no_permissions_granted, Toast.LENGTH_SHORT).show();
//                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
//                }
//                break;
//        }
//    }

//    void pickPlace() {
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//        try {
//
//            startActivityForResult(builder.build(RootActivity.this), PLACE_PICKER_REQUEST);
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
//    }

    public Toolbar toolbar;
    public NavigationView navigationView;
    public DrawerLayout drawer;
    public View navHeader;
    public Handler mHandler;
    boolean first_time = true;
    private ProgressDialog progressDialog;

    protected void init() {
        mHandler = new Handler();
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayShowHomeEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navHeader = navigationView.getHeaderView(0);


        loadNavHeader();
        setUpNavigationView();
    }


    public void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent ii = null;
                //Check to see which item was being clicked and perform appropriate action
//                switch (menuItem.getItemId()) {
//                    //Replacing the main content with ContentFragment Which is our Inbox View;
//
//                    case R.id.nav_home:
//
//                        break;
//
//                    case R.id.nav_chat:
////                        ii = new Intent(MainActivity.this , ChatActivity.class);
////                        startActivity(ii);
//                        drawer.closeDrawers();
//                        return true;
////                    case R.id.nav_account:
////                        if(SharedPrefManager.getInstance(MainActivity.this).IsUserLoggedIn())
////                        {
////                            ii = new Intent(MainActivity.this , AccountActivity.class);
////                            startActivity(ii);
////                        }
////                        else{
////                        ii = new Intent(MainActivity.this , LoginActivty.class);
////                        startActivity(ii);}
////                        drawer.closeDrawers();
////                        return true;
//
//
//                    default:
//
//                }
                //Checking if the item is in checked state or not, if not make it in checked state
                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                        super.onDrawerClosed(drawerView);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                        super.onDrawerOpened(drawerView);
                    }
                };
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        findViewById(R.id.go_toggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
//        findViewById(R.id.open_notifications).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openFragment(new NotificationsFragment(),false);
//            }
//        });


    }


    public void loadNavHeader() {
        ArrayList menuItems = new ArrayList();
        menuItems.add(new CostumeMenuItem("0", getString(R.string.home), R.drawable.ic_menu_home_white) {
            @Override
            public void doAction(View v) {
                Intent mainIntent = new Intent(RootActivity.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                finish();
            }
        });

        if (SharedPrefManager.getInstance(this).getUser().getUser_type().equals(User.SERVICE_PROVIDER)) {
            menuItems.add(new CostumeMenuItem("0", getString(R.string.waiting_requetss), R.drawable.ic_my_requests_white) {
                @Override
                public void doAction(View v) {
                    if (!ConnectionUtils.isNetworkAvailable(getApplicationContext())) {
                        Toast.makeText(getApplicationContext(), R.string.connect_please, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    startActivity(new Intent(RootActivity.this, MyOrdersActivity.class));

                }
            });
            menuItems.add(new CostumeMenuItem("0", getString(R.string.my_service), R.drawable.ic_my_requests_white) {
                @Override
                public void doAction(View v) {
                    if (!ConnectionUtils.isNetworkAvailable(getApplicationContext())) {
                        Toast.makeText(getApplicationContext(), R.string.connect_please, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    startActivity(new Intent(RootActivity.this, FinishedOrdersActivity.class));

                }
            });

        }
        if (SharedPrefManager.getInstance(this).getUser().getUser_type().equals(User.CUSTOMER)) {
            menuItems.add(new CostumeMenuItem("0", getString(R.string.promo_code), R.drawable.ic_promo_code_white) {
                @Override
                public void doAction(View v) {
                    if (!ConnectionUtils.isNetworkAvailable(getApplicationContext())) {
                        Toast.makeText(getApplicationContext(), R.string.connect_please, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(RootActivity.this);
                    builder.setTitle(R.string.enter_promo_code);

                    // Set up the input
                    final EditText input = new EditText(RootActivity.this);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton(getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String m_Text = input.getText().toString();
                                    if (m_Text.equals("")) {
                                        Toast.makeText(RootActivity.this,
                                                R.string.enter_valid_code, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    // approve(m_Text,0);
                                    promo(0, m_Text);
                                }
                            });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });
            menuItems.add(new CostumeMenuItem("0",
                    getString(R.string.my_orders), R.drawable.ic_user_icon) {
                @Override
                public void doAction(View v) {

                    startActivity(new Intent(RootActivity.this, GetCustomerOrdersActivity.class));
                }
            });

        }
        menuItems.add(new CostumeMenuItem("0", getString(R.string.invite_a_friend), R.drawable.ic_invite_friend_white) {
            @Override
            public void doAction(View v) {

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = getString(R.string.share_str);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.dowmlaod));
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));

            }
        });

        menuItems.add(new CostumeMenuItem("0", getString(R.string.change_password), R.drawable.ic_password_change) {
            @Override
            public void doAction(View v) {

                startActivity(new Intent(RootActivity.this, ChangPassActivity.class));
            }
        });


        menuItems.add(new CostumeMenuItem("0", getString(R.string.support), R.drawable.ic_support_white) {
            @Override
            public void doAction(View v) {
                if (!ConnectionUtils.isNetworkAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), R.string.connect_please, Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(RootActivity.this, SupportActivity.class));
            }
        });
        menuItems.add(new CostumeMenuItem("0",
                getString(R.string.about), R.drawable.ic_about_icon_white) {
            @Override
            public void doAction(View v) {
                startActivity(new Intent(RootActivity.this, AboutActivity.class));

            }
        });
        menuItems.add(new CostumeMenuItem("0",
                getString(R.string.language), R.drawable.ic_language_icon) {
            @Override
            public void doAction(View v) {
                Intent ii = new Intent(RootActivity.this, SelectLangActivity.class);
                startActivity(ii);
                finish();

            }
        });
        menuItems.add(new CostumeMenuItem("0",
                getString(R.string.logout), R.drawable.ic_logout_icon) {
            @Override
            public void doAction(View v) {
                SharedPrefManager.getInstance(RootActivity.this).Logout();
                Intent ii = new Intent(RootActivity.this, SplashActivity.class);
                ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(ii);
                finish();

            }
        });
        RecyclerView menuList = (RecyclerView) findViewById(R.id.menu_items);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        DrawerMenuItemsAdapter adapter = new DrawerMenuItemsAdapter(menuItems, this);
        menuList.setLayoutManager(lm);
        menuList.setAdapter(adapter);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dashboard, menu);

        return super.onCreateOptionsMenu(menu);
    }

    boolean doubleBackToExitPressedOnce = false;


    void promo(final int t, final String code) {
        Toast.makeText(this, R.string.sending_code, Toast.LENGTH_SHORT).show();
        HashMap args = new HashMap<String, String>();
        args.put("customer_id", SharedPrefManager.getInstance(this).getUser().getId());
        args.put("promo_code", code);
        BackgroundServices.getInstance(this)
                .CallPost(APIUrl.SERVER + "promo_code",
                 args
                , new PostAction() {
                    @Override
                    public void doTask(String s) {
                        if (s.equals("")) {
                            if (t < 20) promo(t + 1, code);
                            else {
                                Toast.makeText(RootActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            try {
                                JSONObject array = new JSONObject(s);
                                Toast.makeText(RootActivity.this,
                                        array.getString("message"), Toast.LENGTH_SHORT).show();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


    }

    void SUPPORT(final int t, final String code) {
        // HashMap<String, String> params =  new HashMap<String, String>();
        //  params.put("car_id",);
        Toast.makeText(this, R.string.sending_code, Toast.LENGTH_SHORT).show();
        HashMap args = new HashMap<String, String>();
        args.put("agent_id", SharedPrefManager.getInstance(this).getUser().getId());
        args.put("help", code);

        BackgroundServices.getInstance(this).CallPost(APIUrl.SERVER + "Complaints_and_suggestions_agent", args

                , new PostAction() {
                    @Override
                    public void doTask(String s) {
                        if (s.equals("")) {
                            if (t < 20) SUPPORT(t + 1, code);
                            else {
                                Toast.makeText(RootActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            try {
                                JSONObject array = new JSONObject(s);
                                Toast.makeText(RootActivity.this, array.getString("message"), Toast.LENGTH_SHORT).show();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadin, R.anim.fadout);
        Locale locale = new Locale(SharedPrefManager.getInstance(this).getSavedLanguage());
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
    }
}

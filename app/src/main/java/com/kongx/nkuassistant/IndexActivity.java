package com.kongx.nkuassistant;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;

public class IndexActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, Connectable{
    private Toast mPressBackToast;
    private long mLastBackPress;
    private static final long mBackPressThreshold = 3500;
    private HomeFragment homeFragment;
    private NavigationView navigationView;
    private static class RequestType{
        static final int CHECK_FOR_UPDATE = 0;
        static final int CHECK_FOR_NOTICE = 1;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        homeFragment = new HomeFragment();
        fragmentTransaction.add(R.id.fragment_container,homeFragment,"HomeFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        mPressBackToast = Toast.makeText(getApplicationContext(), R.string.press_back_again_to_exit, Toast.LENGTH_SHORT);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo == null) Toast.makeText(getApplicationContext(),R.string.connection_error, Toast.LENGTH_SHORT).show();
        new Connect(this, RequestType.CHECK_FOR_UPDATE,null).execute(Information.UPDATE_URL);
        new Connect(this,RequestType.CHECK_FOR_NOTICE,null).execute(Information.NOTICE_URL);
    }
    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    public void onTaskComplete(Object o, int type) {
        InputStream is = (InputStream) o;
        String returnstring;
        switch (type){
            case RequestType.CHECK_FOR_UPDATE:{
                returnstring = new Scanner(is).useDelimiter("\\n").next();
                String version = null;
                try{
                    PackageManager manager = this.getPackageManager();
                    PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
                    version = info.versionName;
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (version.equals(returnstring)){
                    return;
                }else {
                    Toast.makeText(this,"有版本更新", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case RequestType.CHECK_FOR_NOTICE:{
                SharedPreferences settings = getSharedPreferences(Information.PREFS_NAME,0);
                Information.newestNotice = settings.getString("newestNotice",null) == null ? -1 : Integer.parseInt(settings.getString("newestNotice",null));
                returnstring = new Scanner(is).useDelimiter("\\A").next();
                Pattern pattern = Pattern.compile("<id>([0-9])(</id>)");
                Matcher matcher = pattern.matcher(returnstring);
                matcher.find();
                int tmpId = Integer.parseInt(matcher.group(1));
                if(Information.newestNotice == tmpId){
                    return;
                }else {
                    Information.newestNotice = tmpId;
                    pattern = Pattern.compile("<headline>(.+)(</headline>)");
                    matcher = pattern.matcher(returnstring);
                    matcher.find();
                    String tmpHeadline = matcher.group(1);
                    pattern = Pattern.compile("<content>(.+)(</content>)");
                    matcher = pattern.matcher(returnstring);
                    matcher.find();
                    String tmpContent = matcher.group(1);
                    new AlertDialog.Builder(this).setTitle(tmpHeadline)
                            .setMessage(tmpContent)
                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("newestNotice",String.valueOf(Information.newestNotice));
                    editor.apply();
                }
                break;
            }
            default:
                break;
        }
    }

    public void onClick(View view){
        if(view.getId() == R.id.home_schedule_details) {
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_schedule));
        }else if(view.getId() == R.id.home_exam_details){
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_exam));
        }else if(view.getId() == R.id.home_score_details){
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_score));
        }else if(view.getId() == R.id.home_select_details){
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_select));
        }else if(view.getId() == R.id.home_bus_details){
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_612bus));
        }
    }

    public void headerClicked(View view){
        startActivity(new Intent(getApplicationContext(),PersonalPage.class));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(getFragmentManager().getBackStackEntryCount() > 1){
            getFragmentManager().popBackStack();
        }
        else {
            long currentTime = System.currentTimeMillis();
            if (Math.abs(currentTime - mLastBackPress) > mBackPressThreshold) {
                mPressBackToast.show();
                mLastBackPress = currentTime;
            } else {
                mPressBackToast.cancel();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.index, menu);

        TextView nameTextView = (TextView) findViewById(R.id.drawer_Name);
        nameTextView.setText(Information.name);

        TextView facultyTextView = (TextView) findViewById(R.id.drawer_Faculty);
        facultyTextView.setText(Information.facultyName);

        TextView idTextView = (TextView) findViewById(R.id.drawer_ID);
        idTextView.setText(Information.id);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        }
        navigationView.setCheckedItem(id);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.hide(homeFragment);
        if (id == R.id.nav_home) {
            fragmentTransaction.show(homeFragment);
        } else {
            if (id == R.id.nav_curriculum) {
                fragmentTransaction.replace(R.id.fragment_container, new CurriculumFragment());
            } else if (id == R.id.nav_exam) {
                fragmentTransaction.replace(R.id.fragment_container, new ExamFragment());
            } else if (id == R.id.nav_schedule) {
                fragmentTransaction.replace(R.id.fragment_container, new ScheduleFragment());
            } else if (id == R.id.nav_score) {
                fragmentTransaction.replace(R.id.fragment_container, new ScoreFragment());
            } else if (id == R.id.nav_select) {
                fragmentTransaction.replace(R.id.fragment_container, new SelectFragment());
            } else if (id == R.id.nav_about) {
                fragmentTransaction.replace(R.id.fragment_container, new AboutFragment());
            } else if (id == R.id.nav_612bus) {
                fragmentTransaction.replace(R.id.fragment_container, new ShuttleBusFragment());
            }
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

package tech.eduardosolano.movietime.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import tech.eduardosolano.movietime.Api.Response.Result;
import tech.eduardosolano.movietime.Fragment.DetailMovieFragment;
import tech.eduardosolano.movietime.Fragment.MovieListFragment;
import tech.eduardosolano.movietime.R;
import tech.eduardosolano.movietime.Utils.Constants;
import tech.eduardosolano.movietime.Utils.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MovieListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utils.getInstance().initSharedPrefs(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setFragment();
        setActionBarTitle(getString(R.string.app_name));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_list) {
            setFragment();
        } else if (id == R.id.nav_portfolio) {
            Intent intent = new Intent();

            intent.setDataAndType(Uri.parse(Constants.PORTFOLIO_URL), "application/pdf");
            startActivity(intent);
        } else if (id == R.id.nav_cv) {
            Intent intent = new Intent();
            intent.setDataAndType(Uri.parse(Constants.CV_URL), "application/pdf");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new MovieListFragment());
        fragmentTransaction.commit();
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof MovieListFragment) {
            MovieListFragment headlinesFragment = (MovieListFragment) fragment;
            headlinesFragment.setOnListFragmentInteractionListener(this);
        }
    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onListFragmentInteraction(Result item) {
        DetailMovieFragment newFragment = new DetailMovieFragment();
        Bundle args = new Bundle();
        args.putSerializable("item", item);
        newFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

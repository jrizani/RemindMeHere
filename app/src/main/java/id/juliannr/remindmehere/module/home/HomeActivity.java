package id.juliannr.remindmehere.module.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import id.juliannr.remindmehere.R;
import id.juliannr.remindmehere.data.model.Reminder;
import id.juliannr.remindmehere.module.addreminder.AddReminderActivity;
import id.juliannr.remindmehere.module.base.BaseActivity;
import id.juliannr.remindmehere.module.reminderdetail.ReminderDetailActivity;
import id.juliannr.remindmehere.module.savedlocation.SavedLocationActivity;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeView.View {

    private RecyclerView recyclerView;
    private SlimAdapter adapter;
    private HomePresenter presenter;
    private RelativeLayout noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Permissions.check(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest
                .permission.ACCESS_COARSE_LOCATION}, "Location are required in this application" +
                ".", new Permissions.Options().setSettingsDialogTitle("Permission Required!")
                .setRationaleDialogTitle("Info"), new PermissionHandler() {
            @Override
            public void onGranted() {
                presenter = HomePresenter.getInstance();
                presenter.setView(HomeActivity.this);
                initView();
                initSlimAdapter();
                setSwipe();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                finish();
            }
        });
    }

    private void setSwipe() {
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback
            (0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            Reminder data = ((Reminder) adapter.getItem(viewHolder.getAdapterPosition()));
            presenter.delete(data);
        }
    });

    itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void showData(List<Reminder> model) {
        adapter.updateData(model);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void initSlimAdapter() {
        final Gson gson = new Gson();
        adapter = SlimAdapter.create()
                .register(R.layout.item_reminder, new SlimInjector<Reminder>() {
                    @Override
                    public void onInject(final Reminder data, IViewInjector injector) {
                        injector.text(R.id.location, data.getLocationName())
                                .text(R.id.description, data.getDescription())
                                .text(R.id.dueDate, data.getDueDate())
                                .clicked(R.id.item, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(HomeActivity.this,
                                                ReminderDetailActivity.class).putExtra("data",
                                                data));
                                    }
                                });
                    }
                }).attachTo(recyclerView);
    }

    private List<Reminder> makeDummyData() {
        Reminder satu = new Reminder(UUID.randomUUID().toString(),
                "Alfamart",
                "912084",
                "-1.9028",
                "Beli Sikat Gigi",
                "25 Apr 2018");
        Reminder dua = new Reminder(UUID.randomUUID().toString(),
                "JNE",
                "912084",
                "-1.9028",
                "Ambil paket",
                "25 Apr 2018");
        List<Reminder> list = new ArrayList<>();
        list.add(satu);
        list.add(dua);
        return list;
    }

    private void initView() {
        noData = findViewById(R.id.noData);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setToolbar("Home", false);
        Toolbar toolbar = getToolbar();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(this, AddReminderActivity.class));
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(this, SavedLocationActivity.class));
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getData();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    @Override
    public void onDataFound(List<Reminder> datas) {
        noData.setVisibility(View.GONE);
        showData(datas);
    }

    @Override
    public void onDataNotFound() {
        noData.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDeleteData() {
        presenter.getData();
    }
}

package id.juliannr.remindmehere.module.savedlocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;

import java.util.List;

import id.juliannr.remindmehere.R;
import id.juliannr.remindmehere.data.model.SavedLocation;
import id.juliannr.remindmehere.module.base.BaseActivity;

/**
 * Created by juliannr on 24/04/18.
 */

public class SavedLocationActivity extends BaseActivity implements SavedLocationView.View {

    private RecyclerView           recyclerView;
    private RelativeLayout         noData;
    private FloatingActionButton   addButton;
    private SlimAdapter            adapter;
    private SavedLocationPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_location);
        initView();
        setToolbar("Saved Location", true);
        initSlimAdapter();
        presenter = SavedLocationPresenter.getInstance();
        presenter.setView(this);
        presenter.loadData();
    }

    private void initSlimAdapter() {
        adapter = SlimAdapter.create().register(R.layout.item_saved_location, new SlimInjector<SavedLocation>() {
                    @Override
                    public void onInject(SavedLocation data, IViewInjector injector) {
                        injector.text(R.id.location, data.getLocationName())
                                .text(R.id.latitude, String.valueOf(data.getLatitude()))
                                .text(R.id.longitude, String.valueOf(data.getLongitude()));
                    }
                }).attachTo(recyclerView);
    }

    private void initView() {
        this.addButton = (FloatingActionButton) findViewById(R.id.addButton);
        this.noData = (RelativeLayout) findViewById(R.id.noData);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(SavedLocationActivity.this,
                        AddSavedLocationActivity.class), 125);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 125){
            if(resultCode == RESULT_OK){
                presenter.loadData();
            }else if(resultCode == RESULT_CANCELED){
                if(data != null){
                    Toast.makeText(this, data.getStringExtra("message"), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    @Override
    public void onDataFound(List<SavedLocation> data) {
        noData.setVisibility(View.GONE);
        adapter.updateData(data);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDataNotFound() {
        noData.setVisibility(View.VISIBLE);
    }
}

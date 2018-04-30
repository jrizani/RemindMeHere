package id.juliannr.remindmehere.module.base;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import id.juliannr.remindmehere.R;

/**
 * Created by juliannr on 20/04/18.
 */

public class BaseActivity extends AppCompatActivity {
    private Toolbar  toolbar;
    private TextView title;

    public void setToolbar(String title, boolean useNavButton){
        toolbar = findViewById(R.id.toolbar);
        this.title = findViewById(R.id.title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        this.title.setText(title);
        if(useNavButton)
            setNavigation();
    }


    private void setNavigation(){
        toolbar.setNavigationIcon(R.drawable.ic_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public Toolbar getToolbar(){
        return toolbar;
    }
}

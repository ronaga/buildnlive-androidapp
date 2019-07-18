package buildnlive.com.buildlive.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import buildnlive.com.buildlive.R;

public class BillImageView extends AppCompatActivity {
    TextView no_content;
    String item = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_view);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView textView = findViewById(R.id.toolbar_title);
        textView.setText("View Details");


        no_content = findViewById(R.id.no_content);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            item = bundle.getString("Link");
        }

        if (item != null) {
            if (item.equals("")) {
                no_content.setVisibility(View.VISIBLE);
            }
        } else no_content.setVisibility(View.GONE);

        TextView bill = findViewById(R.id.data);


        bill.setMovementMethod(new ScrollingMovementMethod());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            bill.setText(Html.fromHtml(item,Html.FROM_HTML_MODE_LEGACY));
        }
        else {
            bill.setText(Html.fromHtml(item));
        }

//        RecyclerView images = findViewById(R.id.list);
//        String new_item = item.replace("[", "");
//        new_item = new_item.replace("\"", "");
//        new_item = new_item.replace("]", "");
//        String[] array = new_item.split(",");
//        ArrayList<String> arrayList = new ArrayList<>();
//        for (String anArray : array) {
//            String arraynew = anArray.replaceAll("\\\\", "");
//            arrayList.add(arraynew);
//        }
//
//        if (arrayList.isEmpty()) {
//            no_content.setVisibility(View.VISIBLE);
//        } else no_content.setVisibility(View.GONE);

//        ImageAdapter imageAdapter = new ImageAdapter(getApplicationContext(), arrayList);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        SnapHelper snapHelper = new LinearSnapHelper();
//        snapHelper.attachToRecyclerView(images);
//        DividerItemDecoration decoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
//        images.addItemDecoration(decoration);
//        images.setLayoutManager(linearLayoutManager);
//        images.setAdapter(imageAdapter);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}

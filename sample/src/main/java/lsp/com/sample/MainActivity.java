package lsp.com.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Arrays;

import lsp.com.library.FlowLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        FlowLayout flowLayout = new FlowLayout(this);

        flowLayout.initData(Arrays.asList("Welcome", "IT工程师", "学习ing", "恋爱ing"
                , "挣钱ing", "努力ing", "I thick i can"), 5, R.drawable.flag_02);

        setContentView(flowLayout);
    }
}

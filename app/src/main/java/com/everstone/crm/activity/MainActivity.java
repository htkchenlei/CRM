package com.everstone.crm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.everstone.crm.R;
import com.everstone.crm.adapter.MyFragmentAdapter;
import com.everstone.crm.fragment.AddUserFragment;
import com.everstone.crm.fragment.UserFragment;
import com.everstone.crm.fragment.UserSettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private RadioButton radioButtonUser, radioButtonAddUser, radioButtonUserSettings;
    private MyFragmentAdapter myFragmentAdapter;
    private Fragment userFragmemnt, addUserFragment, userSettingFragment;
    private List<Fragment> fragmentList;
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("from System.out.println.....");
        Log.d("from", "log system");
        initViews();
        bindViews();
        radioButtonUser.setChecked(true);
    }

    private void initViews(){
        userFragmemnt = new UserFragment();
        addUserFragment = new AddUserFragment();
        userSettingFragment = new UserSettingsFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(userFragmemnt);
        fragmentList.add(addUserFragment);
        fragmentList.add(userSettingFragment);
        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragmentList);
    }

    private void bindViews(){
        radioButtonUser = (RadioButton)findViewById(R.id.user);
        radioButtonAddUser = (RadioButton)findViewById(R.id.add_user);
        radioButtonUserSettings= (RadioButton)findViewById(R.id.user_settings);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setAdapter(myFragmentAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.user:
                viewPager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.add_user:
                viewPager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.user_settings:
                viewPager.setCurrentItem(PAGE_THREE);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(state == 2){
            switch (viewPager.getCurrentItem()){
                case PAGE_ONE:
                    radioButtonUser.setChecked(true);
                    break;
                case PAGE_TWO:
                    radioButtonAddUser.setChecked(true);
                    break;
                case PAGE_THREE:
                    radioButtonUserSettings.setChecked(true);
                    break;
            }
        }
    }
}

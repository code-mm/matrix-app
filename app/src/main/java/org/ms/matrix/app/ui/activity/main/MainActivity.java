package org.ms.matrix.app.ui.activity.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.matrix.androidsdk.rest.client.RoomsRestClient;
import org.ms.matrix.app.MatrixClient;
import org.ms.matrix.app.R;
import org.ms.matrix.app.ui.fragment.message.MessageFragment;
import org.ms.matrix.app.ui.fragment.my.MyFragment;
import org.ms.matrix.app.ui.fragment.room.RoomFragment;
import org.ms.module.base.view.BaseAppCompatActivity;
import org.ms.module.base.view.StatusBarUtil;
import org.ms.view.navigation.NavigationView;
import org.ms.view.navigation.NavigationViewAdapter;
import org.ms.view.navigation.NavigationViewItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseAppCompatActivity<MainActivityPresenter> implements IMainActivity {


    private NavigationView navigationView;

    private List<NavigationViewItem> navigationViewItems = new ArrayList<>();

    private NavigationViewAdapter navigationViewAdapter;

    private HashMap<String, Fragment> hashMapFragments = new HashMap<>();


    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();

        navigationView = findViewById(R.id.navigationView);
        navigationView.setOnItemClickListener((view, navigationChildItemView) -> {
            String name = navigationChildItemView.getItem().name;
            Fragment fragment = hashMapFragments.get(name);
            showFragment(fragment);
        });
    }

    @Override
    protected MainActivityPresenter initPresenter() {
        return new MainActivityPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        navigationViewItems.clear();
        navigationViewAdapter = new NavigationViewAdapter(navigationViewItems);
        navigationView.setAdapter(navigationViewAdapter);

        NavigationViewItem navigationViewMessage = new NavigationViewItem();
        navigationViewMessage.name = "消息";
        navigationViewMessage.drawable = getDrawable(R.drawable.tab_bar_message);
        navigationViewItems.add(navigationViewMessage);

        NavigationViewItem navigationViewRoom = new NavigationViewItem();
        navigationViewRoom.name = "房间";
        navigationViewRoom.drawable = getDrawable(R.drawable.image_room);
        navigationViewItems.add(navigationViewRoom);


        NavigationViewItem navigationViewMy = new NavigationViewItem();
        navigationViewMy.name = "我的";
        navigationViewMy.drawable = getDrawable(R.drawable.tab_bar_my);
        navigationViewItems.add(navigationViewMy);


        navigationViewAdapter.notificationDataChange();


        hashMapFragments.clear();

        hashMapFragments.put("消息", MessageFragment.newInstance());
        hashMapFragments.put("房间", RoomFragment.newInstance());
        hashMapFragments.put("我的", MyFragment.newInstance());

        for (String key : hashMapFragments.keySet()) {
            fragmentManager.beginTransaction().add(R.id.frameLayoutFragment, hashMapFragments.get(key)).commit();
        }
        showFragment(hashMapFragments.get("消息"));
        navigationView.setSelected(0);


    }

    private void showFragment(Fragment fragment) {
        for (String key : hashMapFragments.keySet()) {
            fragmentManager.beginTransaction().hide(hashMapFragments.get(key)).commit();
        }
        fragmentManager.beginTransaction().show(fragment).commit();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);
    }
}

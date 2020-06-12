package org.ms.matrix.app.ui.fragment.room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ms.matrix.app.R;
import org.ms.matrix.app.db.MatrixDbInjection;
import org.ms.matrix.app.db.matrix.MatrixRoom;
import org.ms.module.base.inter.IPresenter;
import org.ms.module.base.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class RoomFragment extends BaseFragment<RoomFragmentPresenter> implements View.OnClickListener, IRoomFragment {


    private static final String TAG = "RoomFragment";


    private RecyclerView recyclerViewRooms;


    private RecyclerViewRoomsAdapter recyclerViewRoomsAdapter;


    private RoomFragmentViewModel roomFragmentViewModel;


    private List<MatrixRoom> matrixRoomList = new ArrayList<>();


    @Override
    protected int getLayout() {
        return R.layout.fragment_room;
    }


    @Override
    public void initView() {
        super.initView();

        recyclerViewRooms = findView(R.id.recyclerViewRooms);
    }


    @Override
    protected RoomFragmentPresenter initPresenter() {
        return new RoomFragmentPresenter(this);
    }

    public static RoomFragment newInstance() {

        Bundle args = new Bundle();

        RoomFragment fragment = new RoomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        roomFragmentViewModel = new ViewModelProvider(this).get(RoomFragmentViewModel.class);


        roomFragmentViewModel.getMatrixRoomMutableLiveData().observe(this, new Observer<List<MatrixRoom>>() {
            @Override
            public void onChanged(List<MatrixRoom> matrixRooms) {

                if (matrixRooms != null) {

                    matrixRoomList.clear();
                    matrixRoomList.addAll(matrixRooms);

                    if (recyclerViewRoomsAdapter == null) {

                        recyclerViewRoomsAdapter = new RecyclerViewRoomsAdapter(getContext(), matrixRooms);
                        recyclerViewRooms.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerViewRooms.setAdapter(recyclerViewRoomsAdapter);
                    } else {
                        recyclerViewRoomsAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


        MatrixDbInjection.providerMatrixRoomLocalDataSource().liveDataMatrixRooms().observe(this, new Observer<List<MatrixRoom>>() {
            @Override
            public void onChanged(List<MatrixRoom> matrixRooms) {
                roomFragmentViewModel.getMatrixRoomMutableLiveData().postValue(matrixRooms);
            }
        });


    }
}

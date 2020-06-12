package org.ms.matrix.app.ui.fragment.message;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MessageFragmentViewModel extends ViewModel {



    private MutableLiveData<RecyclerViewMessageListBean> messageListBeanMutableLiveData=new MutableLiveData<>();

    public MutableLiveData<RecyclerViewMessageListBean> getMessageListBeanMutableLiveData() {
        return messageListBeanMutableLiveData;
    }
}

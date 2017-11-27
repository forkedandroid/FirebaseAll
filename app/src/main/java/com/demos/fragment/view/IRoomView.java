package com.demos.fragment.view;


import com.demos.fragment.BaseView;
import com.demos.fragment.bean.Room;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * @since 2017/3/7
 */

public interface IRoomView extends BaseView {



    void enterRoom(Room room);

    void playUrl(String url);

}

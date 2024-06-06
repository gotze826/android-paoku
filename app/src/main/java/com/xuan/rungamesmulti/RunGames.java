package com.xuan.rungamesmulti;
import android.os.Bundle;
import android.view.KeyEvent;

import com.unity3d.player.UnityPlayerActivity;

public class RunGames extends UnityPlayerActivity {
    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}

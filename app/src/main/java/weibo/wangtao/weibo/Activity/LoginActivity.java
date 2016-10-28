package weibo.wangtao.weibo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.nio.BufferUnderflowException;
import java.nio.channels.InterruptibleChannel;

import weibo.wangtao.weibo.R;
import weibo.wangtao.weibo.Tools.Tools;

public class LoginActivity extends AppCompatActivity {
    private Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

    }
    private void init()
    {
        login_btn=(Button)findViewById(R.id.login_button);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.isNetworkAvaliable(LoginActivity.this))//network avaliable
                {
                    Intent intent=new Intent(LoginActivity.this,OAuthActivity.class);
                    startActivity(intent);

                }else
                {
                    Tools.checkNetwork(LoginActivity.this);
                }
            }
        });

    }
}

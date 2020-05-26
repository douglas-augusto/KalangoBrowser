package com.codedevs.kalangobrowser;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    WebView pagina;
    ProgressBar Pbar;
    EditText urlr;
    int cont = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //PROPRIEDADADES DA ACTIVITY
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        //getSupportActionBar().hide();

        //IMPORTANDO ELEMENTOS DE LAYOUT
        ImageButton voltar = (ImageButton)findViewById(R.id.voltar);

        ImageButton avancar = (ImageButton)findViewById(R.id.avancar);

        ImageButton atualizar = (ImageButton)findViewById(R.id.atualizar);

        urlr = (EditText)findViewById(R.id.url);
        urlr.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        pagina = (WebView)findViewById(R.id.pagina);

        Pbar = (ProgressBar) findViewById(R.id.progressBar);

        //PROPRIEDADES DO NAVEGADOR
        pagina.setWebViewClient(new MyWebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(cont == 1){
                    urlr.setText(url);
                }
                cont=1;
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        //PROPRIEDADES DO WEBVIEW
        pagina.getSettings().setBuiltInZoomControls(true);
        pagina.getSettings().setDisplayZoomControls(false);
        pagina.getSettings().setJavaScriptEnabled(true);
        pagina.setWebChromeClient(new MyChrome());

        //CARREGAMENTO INICIAL DO NAVEGADOR
        pagina.loadUrl("http://www.youtube.com");

        //BARRA INFERIOR DO NAVEGADOR
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pagina.canGoBack()) {
                    pagina.goBack();
                }
            }
        });

        avancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pagina.canGoForward()) {
                    pagina.goForward();
                }
            }
        });

        atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagina.reload();
            }
        });

        //CARREGAMENTO DE URL/ENDERECO URL
        urlr.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                        String endereco = urlr.getText().toString();
                        if(endereco.toLowerCase().contains("http://") || endereco.toLowerCase().contains("https://")
                                || endereco.toLowerCase().contains("https:/")
                                || endereco.toLowerCase().contains("https:")
                                || endereco.toLowerCase().contains("https")
                                || endereco.toLowerCase().contains("http")){
                            pagina.loadUrl(urlr.getText().toString());
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(urlr.getWindowToken(), 0);
                        }else if(endereco.toLowerCase().contains(".")){
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(urlr.getWindowToken(), 0);
                            String endereco_comp = "http://"+urlr.getText().toString();
                            pagina.loadUrl(endereco_comp);
                            urlr.setText(endereco_comp);
                        }else{
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(urlr.getWindowToken(), 0);
                            String endereco_comp = "https://www.google.com/search?q="+urlr.getText().toString();
                            pagina.loadUrl(endereco_comp);
                            urlr.setText(endereco_comp);
                        }
                        return true;
                    }

                }
                return false;
            }
        });

    }//fim do onCreate

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("http://")) {
                return false;

            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.item_sobre :
                Intent intent = new Intent(MainActivity.this, SobreActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class MyChrome extends WebChromeClient {

        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout frame;

        // Initially mOriginalOrientation is set to Landscape
        private int mOriginalOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        private int mOriginalSystemUiVisibility;

        // Constructor for CustomWebClient
        public MyChrome() {}

        public Bitmap getDefaultVideoPoster() {
            if (MainActivity.this == null) {
                return null;
            }
            return BitmapFactory.decodeResource(MainActivity.this.getApplicationContext().getResources(), 2130837573); }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback viewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = MainActivity.this.getWindow().getDecorView().getSystemUiVisibility();

            MainActivity.this.setRequestedOrientation(this.mOriginalOrientation);

            this.mOriginalOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            this.mCustomViewCallback = viewCallback; ((FrameLayout)MainActivity.this.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1)); MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(3846);
        }

        public void onHideCustomView() {
            ((FrameLayout)MainActivity.this.getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);

            MainActivity.this.setRequestedOrientation(this.mOriginalOrientation);

            this.mOriginalOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE; this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onProgressChanged(WebView view, int progress) {
            if (progress < 100 && Pbar.getVisibility() == ProgressBar.GONE) {
                Pbar.setVisibility(ProgressBar.VISIBLE);
            }
            Pbar.setProgress(progress);
            if (progress == 100) {
                Pbar.setVisibility(ProgressBar.GONE);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        pagina.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pagina.restoreState(savedInstanceState);
    }

}

package com.example.mobeomsearchapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends Activity {

    EditText edit;
    TextView text;

    String serviceURL = "http://apis.data.go.kr/6260000/BusanTblFnrstrnStusService/getTblFnrstrnStusInfo";
    String serviceKey = "?serviceKey=" + "UvbltoXzj6ZR42XbDzaLoI%2BIzE5hQz5KTvkq%2F%2B4v9oLAq2nIbf50kFgS9yj9PjBaG9C23HDJ4TYfTonXrAZWzA%3D%3D";
    String numOfRows = "&numOfRows=" + "100";
    String pageNO = "&pageNO=" + "1";
    String bsnsNm = "&bsnsNm=";
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit = (EditText) findViewById(R.id.edit);
        text = (TextView) findViewById(R.id.text);
    }

    public void mOnClick(View v) {
        switch ( v.getId() ) {
            case R.id.button:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        data = getXmlData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText(data);
                            }
                        });
                    }
                }).start();
                break;
        }
    }

    String getXmlData() {
        StringBuffer buffer = new StringBuffer();

        String str = edit.getText().toString();
        String search = null;
        try {
            search = URLEncoder.encode(str,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

//      String SumUrl = serviceURL + serviceKey + numOfRows + pageNO + bsnsNm + search;     /* SearchVer */
        String SumUrl = serviceURL + serviceKey + numOfRows + pageNO;                   /* ListVer */

        try {
            URL url=new URL(SumUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();

                        if(tag.equals("item"));
                        else if(tag.equals("bsnsSector")){
                            buffer.append("업종: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("bsnsCond")){
                            buffer.append("업태: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("bsnsNm")){
                            buffer.append("업소명: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("addrRoad")){
                            buffer.append("소재지(도로명): ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("tel")){
                            buffer.append("TEL: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();

                        if(tag.equals("item"))
                            buffer.append("\n");
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {

        }
        buffer.append("파싱 끝\n");

        return buffer.toString();
    }
}

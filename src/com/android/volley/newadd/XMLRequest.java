package com.android.volley.newadd;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by hoyouly on 15/4/25.
 */
public class XMLRequest extends Request<XmlPullParser> {

    private final Listener<XmlPullParser> mListener;

    public XMLRequest(int method, String url, Listener<XmlPullParser> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    public XMLRequest(String url, Listener<XmlPullParser> listener, ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    /**
     * 对服务器响应的数据进行解析
     * 根据XmlPullParser 解析器进行解析数据
     *
     * @param response Response from the network
     * @return
     */
    @Override
    protected Response<XmlPullParser> parseNetworkResponse(NetworkResponse response) {
        try {
            //得到的字节数据，根据响应头标签里面的字符编码转换成字符串
            String xmlString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlString));//把得到的字符串放到一个StringReader 中然后设置到解析器里面
            return Response.success(xmlPullParser, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (XmlPullParserException e) {
            return Response.error(new ParseError(e));
        }
    }

    /**
     * 服务器响应的数据进行回调
     *
     * @param response The parsed response returned by
     */
    @Override
    protected void deliverResponse(XmlPullParser response) {
        mListener.onResponse(response);
    }

}

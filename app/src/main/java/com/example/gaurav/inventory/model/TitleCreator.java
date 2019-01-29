package com.example.gaurav.inventory.model;

import android.content.Context;
import android.service.quicksettings.Tile;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.example.gaurav.inventory.Adapters.MaterialissueAdapter;
import com.example.gaurav.inventory.Backend.Backendserver;
import com.example.gaurav.inventory.material;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TitleCreator {
    static TitleCreator _titleCreator;
    List<TitleParent> _titleParents;
    AsyncHttpClient client;
    Context context;
    String title;

    public TitleCreator(final Context context) {

        Backendserver backend = new Backendserver(context);

        client = backend.getHTTPClient();
        _titleParents = new ArrayList<>();



       for(int i=1;i<=100;i++)

        {
            TitleParent title = new TitleParent(String.format("Caller #%d",i));
            _titleParents.add(title);
        }
    }

    public static TitleCreator get(Context context)

    {
        if(_titleCreator == null)
            _titleCreator = new TitleCreator(context);
        return _titleCreator;
    }

    public List<TitleParent> getAll() {
        return _titleParents;
    }
}
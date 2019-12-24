package com.papel.ui.recommendations;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.papel.Constants;
import com.papel.R;
import com.papel.data.Article;
import com.papel.data.User;
import com.papel.ui.articles.ReadArticleActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RecommendedArticleFragment extends Fragment {

    private ListView listView;
    private ArrayList<Article> articles = new ArrayList<>();
    private RecommendedArticleAdapter adapter;

    public RecommendedArticleFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommended_article, container, false);
        listView = view.findViewById(R.id.listView);
        fetchRecommendedArticles(getContext());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Article clickedArticle = articles.get(i);
                Intent intent = new Intent(getContext(), ReadArticleActivity.class);
                intent.putExtra("articleId",clickedArticle.getId());
                startActivity(intent);
            }
        });
        return view;
    }

    private void fetchRecommendedArticles(final Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = Constants.LOCALHOST + Constants.RECOMMENDATION + "articles";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    JSONObject becauseJSON = responseJSON.getJSONObject("because");
                    JSONArray articlesJSON = responseJSON.getJSONArray("articles");
                    // TODO use them
                    String becauseId = becauseJSON.getString("_id");
                    String becauseTitle = becauseJSON.getString("title");
                    articles.clear();
                    for(int  i = 0; i<articlesJSON.length(); i++) {
                        JSONObject object = articlesJSON.getJSONObject(i);
                        String articleId = object.getString("_id");
                        String articleTitle = object.getString("title");
                        String articleBody = object.getString("body");
                        if (object.has("imgUri")) {
                            String articleImageUrl = object.getString("imgUri");
                            articles.add(new Article(articleId,articleTitle,articleBody,articleImageUrl));
                        } else {
                            articles.add(new Article(articleId,articleTitle,articleBody));
                        }
                    }
                    adapter = new RecommendedArticleAdapter(context,articles);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + User.getInstance().getToken());
                return headers;
            }
        };

        requestQueue.add(request);

    }

}

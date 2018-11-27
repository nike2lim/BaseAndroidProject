package com.example.shlim.baseandroidproject.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.services.urlshortener.Urlshortener;
import com.google.api.services.urlshortener.model.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class URLShortenerUtil {
	private static final String TAG = URLShortenerUtil.class.getSimpleName();	// 디버그 태그

	public static final String GOOGLE_API_KEY = "AIzaSyDj07NaSWKyL6a0cobC3oN9sGs5p_KWGmU";                      // Google Console API Key

	/**
	 * Google Short URL 생성
	 * @param url
	 * @return
	 */
	public static String googleShortUrl(String url) {
		AsyncTask<String, Void, String> shortUrlTask = new AsyncTask<String, Void, String>() {
			@Override
			protected void onPreExecute() {
				Log.i(TAG, "onPreExecute() -> Start !!!");
			}

			@Override
			protected String doInBackground(String... params) {
				LogUtil.i(TAG, "doInBackground() -> Start !!!");
                LogUtil.d(TAG, "doInBackground() -> params[0] : " + params[0]);
				Urlshortener.Builder builder = new Urlshortener.Builder(AndroidHttp.newCompatibleTransport(), AndroidJsonFactory.getDefaultInstance(), null);
				Urlshortener urlshortener = builder.build();

				Url url = new Url();
				url.setLongUrl(params[0]);
				try {
					url = urlshortener.url().insert(url).setKey(GOOGLE_API_KEY).execute();
                    LogUtil.e(TAG, String.valueOf(url.getId()));
					return url.getId();
				} catch (IOException e) {
                    LogUtil.e(TAG, String.valueOf(e));
					return "Error (1)";
				}
			}

			@Override
			protected void onPostExecute(String s) {
				LogUtil.i(TAG, "onPostExecute() -> Start !!!");
			}
		};

		String urlOut = "";
		try {
			urlOut = shortUrlTask.execute(url).get();
		} catch (InterruptedException e) {
			LogUtil.e(TAG, e.getMessage());
		} catch (ExecutionException e) {
            LogUtil.e(TAG, e.getMessage());
		}
		return urlOut;
	}

	/**
	 * Naver Short URL 생성
	 * @param urlParam
	 * @return
	 */
	public static String naverShortUrl(String urlParam) {

		AsyncTask<String, Void, String> shortUrlTask = new AsyncTask<String, Void, String>() {
			@Override
			protected void onPreExecute() {
				Log.i(TAG, "onPreExecute() -> Start !!!");
			}

			@Override
			protected String doInBackground(String... params) {
				String clientId = "TKgANs4_W0093fOg4o8D";//애플리케이션 클라이언트 아이디값";
				String clientSecret = "2_akmdWViF";//애플리케이션 클라이언트 시크릿값";
				StringBuffer response = new StringBuffer();
				String returnUrl = "";
				HttpURLConnection con = null;
				try {
					String text = params[0];
//					String text = "https://developers.naver.com/notice";
					String apiURL = "https://openapi.naver.com/v1/util/shorturl";
					URL url = new URL(apiURL);
					con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("POST");
					con.setRequestProperty("X-Naver-Client-Id", clientId);
					con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
					// post request
					String postParams = "url=" + text;
					con.setDoOutput(true);
					DataOutputStream wr = new DataOutputStream(con.getOutputStream());
					wr.writeBytes(postParams);
					wr.flush();
					wr.close();
					int responseCode = con.getResponseCode();
					BufferedReader br;
					if (responseCode == 200) { // 정상 호출
						br = new BufferedReader(new InputStreamReader(con.getInputStream()));
					} else {  // 에러 발생
						br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
					}
					String inputLine;
					while ((inputLine = br.readLine()) != null) {
						response.append(inputLine);
					}
					br.close();
					LogUtil.d(TAG, "naverShortUrl ==>" + response.toString());
				} catch (Exception e) {
					System.out.println(e);
				}
				try {
					JSONObject obj = new JSONObject(response.toString());
					JSONObject urlObject = (JSONObject)obj.get("result");
					returnUrl = (String)urlObject.get("url");
					return returnUrl;
				} catch (JSONException e) {
					e.printStackTrace();
				}

				try {
					con.disconnect();
				}catch (Exception e) {
					e.printStackTrace();
				}

				return returnUrl;
			}

			@Override
			protected void onPostExecute(String s) {
				LogUtil.i(TAG, "onPostExecute() -> Start !!!");
			}
		};

		String urlOut = "";
		try {
			urlOut = shortUrlTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, urlParam).get();
		} catch (InterruptedException e) {
			LogUtil.e(TAG, e.getMessage());
		} catch (ExecutionException e) {
			LogUtil.e(TAG, e.getMessage());
		}
		return urlOut;
	}
}
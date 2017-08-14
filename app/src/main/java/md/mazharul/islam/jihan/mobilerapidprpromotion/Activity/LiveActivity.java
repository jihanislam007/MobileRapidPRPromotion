
package md.mazharul.islam.jihan.mobilerapidprpromotion.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import md.mazharul.islam.jihan.mobilerapidprpromotion.AdvertiseModel;
import md.mazharul.islam.jihan.mobilerapidprpromotion.Model.Live;
import md.mazharul.islam.jihan.mobilerapidprpromotion.Model.LivePlayList;
import md.mazharul.islam.jihan.mobilerapidprpromotion.R;
import md.mazharul.islam.jihan.mobilerapidprpromotion.ServerInfo.ServerInfo;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class LiveActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {


    ArrayList<String> listAddress=new ArrayList<String>();
    public int currentIdx=0;



    ImageView L1 , L2 , L3 , M1 , M2 , M3 , R1 , R2 ,R3;
    TextView tvOne;


    ArrayList<AdvertiseModel> listL1=new ArrayList<AdvertiseModel>();
    ArrayList<AdvertiseModel> listL2=new ArrayList<AdvertiseModel>();
    ArrayList<AdvertiseModel> listL3=new ArrayList<AdvertiseModel>();
    ArrayList<AdvertiseModel> listM1=new ArrayList<AdvertiseModel>();
    ArrayList<AdvertiseModel> listM2=new ArrayList<AdvertiseModel>();
    ArrayList<AdvertiseModel> listM3=new ArrayList<AdvertiseModel>();
    ArrayList<AdvertiseModel> listR1=new ArrayList<AdvertiseModel>();
    ArrayList<AdvertiseModel> listR2=new ArrayList<AdvertiseModel>();
    ArrayList<AdvertiseModel> listR3=new ArrayList<AdvertiseModel>();

    boolean b=true;
    Live live=new Live();
    Handler handler = new Handler();
    Live l;

    public static final String API_KEY = "AIzaSyAS_aJZL9lTtNUybEtCrfM7hpzMGD4Onfc";

    //https://www.youtube.com/watch?v=<VIDEO_ID>
    public static final String VIDEO_ID = "hBkrWEvF5FY";

    YouTubePlayerView youTubePlayerView;
    YouTubePlayer youTubePlayer;
    boolean isBuffer=false;
    private HubProxy hub;
    Boolean playFirst=false;

    String currentlyPlayingAddress="";

    int onLoading=0;


    // HorizontalScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_activity);


        L1 = (ImageView) findViewById(R.id.Lone);
        L2 = (ImageView) findViewById(R.id.LTwo);
        L3 = (ImageView) findViewById(R.id.LThree);
        M1 = (ImageView) findViewById(R.id.MOne);
        M2 = (ImageView) findViewById(R.id.MTwo);
        M3 = (ImageView) findViewById(R.id.MThree);
        R1 = (ImageView) findViewById(R.id.ROne);
        R2 = (ImageView) findViewById(R.id.RTwo);
        R3 = (ImageView) findViewById(R.id.RThree);



        tvOne = (TextView) findViewById(R.id.MarqueeTextOne);
        tvOne.setSelected(true);
        tvOne.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);


        /*listAddress.add("4mQmEsXTJb4");
        listAddress.add("FmW_lGoin2I");
        listAddress.add("B-g_YytMgMY");
        listAddress.add("Deluh9iAnaE");
        listAddress.add("Nu0P15yj6Ps");
        listAddress.add("9TThDeVhZ-U");
        listAddress.add("Ukux2QjJ3zQ");
        listAddress.add("-cQtd6iXUPA");
        listAddress.add("9Az7RDQDptM");
        listAddress.add("Gnq-QJkXYic");
        listAddress.add("wBF1r0Y7zi4");
        listAddress.add("2SRo1RJmVKY");*/


        //scrollView= (HorizontalScrollView) findViewById(R.id.scrollView);


        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tvOne.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    System.out.println(scrollX+"  "+scrollY+"  "+oldScrollX+"  "+oldScrollY);
                }
            });
        }*/
        tvOne.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });
        live.setAddress("");
        live.setSeekPosition(0);



        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize(API_KEY, this);






        LoadServer();
    }

    public void LoadServer(){
        tvOne.setText("");

        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        HubConnection connection = new HubConnection(ServerInfo.HOST_ADDRESS);
        hub = connection.createHubProxy("TvHub");
        SignalRFuture<Void> awaitConnection = connection.start();
        try {
            awaitConnection.get();
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Invoke List");
            hub.invoke("LoadText");
        } catch (Exception e) {
            e.printStackTrace();
        }
        hub.on("playlist",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("playlist "+o);

                handler.post(new Runnable() {
                    public void run() {
                        Gson gson=new Gson();
                        LivePlayList livePlayList=gson.fromJson(o,LivePlayList.class);
                        listAddress.clear();

                        for (int i = 0; i < livePlayList.address.size(); i++) {
                            listAddress.add(livePlayList.address.get(i));
                        }

                        /*for (int i = livePlayList.play.idx+1; i < livePlayList.address.size(); i++) {
                            listAddress.add(livePlayList.address.get(i));
                        }
                        for (int i = 0; i <=livePlayList.play.idx ; i++) {
                            listAddress.add(livePlayList.address.get(i));
                        }*/
                        //youTubePlayer.loadVideos(listAddress,0,livePlayList.play.skiptime);
                        //playVideoLocation(livePlayList.address.get(livePlayList.play.idx),livePlayList.play.skiptime);

                        System.out.println("Skip time "+livePlayList.play.skiptime);
                        youTubePlayer.loadVideos(listAddress,livePlayList.play.idx,livePlayList.play.skiptime);
                    }
                });

            }
        },String.class);

        hub.on("RealTime",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("RealTime playlist "+o);

                handler.post(new Runnable() {
                    public void run() {
                        if(youTubePlayer==null)
                            return;
                        Gson gson=new Gson();
                        LivePlayList livePlayList=gson.fromJson(o,LivePlayList.class);

                        if((int)(Math.abs(livePlayList.play.skiptime-youTubePlayer.getCurrentTimeMillis()))>5000 && livePlayList.play.skiptime>5000){
                            System.out.println("RealTime playlist Load");
                            listAddress.clear();

                            for (int i = 0; i < livePlayList.address.size(); i++) {
                                listAddress.add(livePlayList.address.get(i));
                            }
                           /* for (int i = livePlayList.play.idx+1; i < livePlayList.address.size(); i++) {
                                listAddress.add(livePlayList.address.get(i));
                            }
                            for (int i = 0; i <=livePlayList.play.idx ; i++) {
                                listAddress.add(livePlayList.address.get(i));
                            }*/
                            youTubePlayer.loadVideos(listAddress,livePlayList.play.idx,livePlayList.play.skiptime);
                            //playVideoLocation(livePlayList.address.get(livePlayList.play.idx),livePlayList.play.skiptime);
                            System.out.println("Skip time "+livePlayList.play.skiptime);
                            System.out.println("differnce time "+(int)(Math.abs(livePlayList.play.skiptime-youTubePlayer.getCurrentTimeMillis())));
                        }

                    }
                });

            }
        },String.class);

        /*try {
            hub.invoke("hello");
        } catch (Exception e) {
            e.printStackTrace();
        }
        hub.on("hello",new SubscriptionHandler1<String>() {
            @Override
            public void run(String o) {
                System.out.println(o);
            }
        },String.class);*/

        hub.on("Text", new SubscriptionHandler1<String>() {
            @Override
            public void run(final String s) {
                handler.post(new Runnable() {
                    public void run() {
                        tvOne.setText(s);
                        //scrollView.fullScroll(View.FOCUS_RIGHT);
                        /*ObjectAnimator animator= ObjectAnimator.ofInt(scrollView, "scrollX",scrollView.getScrollBarSize() );
                        animator.setDuration(10000);
                        animator.start();*/

                    }
                });
                System.out.println(s);
                //tvOne.setText(s);
            }
        },String.class);

        hub.on("AdvertiseView",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("View Advertise "+o);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson=new Gson();
                        final AdvertiseModel advertiseModel=gson.fromJson(o,AdvertiseModel.class);
                        viewAdvertise(advertiseModel);
                    }
                });
            }
        },String.class);

        /*hub.on("VideoLive",new SubscriptionHandler1<String>() {
            @Override
            public void run(final String o) {
                System.out.println("Video Live "+o);
                handler.post(new Runnable() {
                    public void run() {
                        Gson gson=new Gson();
                        l=gson.fromJson(o,Live.class);

                        if(youTubePlayer==null){
                            youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
                            youTubePlayerView.initialize(API_KEY, LiveActivity.this);
                            return;
                        }

                        System.out.println("Mobile "+listAddress.get(currentIdx)+"  "+youTubePlayer.getCurrentTimeMillis());
                        System.out.println("server "+l.getAddress()+"  "+l.getSeekPosition());
                        System.out.println("Sekking diff "+(l.getSeekPosition()-youTubePlayer.getCurrentTimeMillis()));

                        try {
                            if((Math.abs(l.getSeekPosition()-youTubePlayer.getCurrentTimeMillis())>8000) || youTubePlayer.getCurrentTimeMillis()<=0 || l.getSeekPosition()==0){
                                try {
                                    System.out.println("Invoke List");
                                    hub.invoke("LoadPlayList");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {


                        }


                        *//*try {
                            if((Math.abs(l.getSeekPosition()-youTubePlayer.getCurrentTimeMillis())>8000) || youTubePlayer.getCurrentTimeMillis()<=0 || l.getSeekPosition()==0){
                                if(!isBuffer){
                                    System.out.println("Video LOADING started..");
                                    youTubePlayer.loadVideo(l.getAddress(),l.getSeekPosition());
                                }
                                else
                                    System.out.println("SKIP LOAD...");

                            }
                        } catch (Exception e) {
                            youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
                            youTubePlayerView.initialize(API_KEY, LiveActivity.this);
                            e.printStackTrace();
                            return;

                        }*//*

                    }
                });

            }
        },String.class);*/


        //RefreshData();
    }
    public void RefreshData(){
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        // call service
                        try {
                            System.out.println("Invoke List");
                            hub.invoke("LoadText");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(youTubePlayer!=null) {
                            try {
                                System.out.println("Invoke List");
                                hub.invoke("LoadPlayList");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, 0, 10, TimeUnit.MINUTES);
    }

    public void viewAdvertise(AdvertiseModel advertiseModel){
        if(advertiseModel.getBoxName().equalsIgnoreCase("L-1")){
            Picasso.with(LiveActivity.this).load(ServerInfo.HOST_ADDRESS+"/"+advertiseModel.getAddress()).into(L1);
        }else if(advertiseModel.getBoxName().equalsIgnoreCase("L-2")){
            Picasso.with(LiveActivity.this).load(ServerInfo.HOST_ADDRESS+"/"+advertiseModel.getAddress()).into(L2);
        }else if(advertiseModel.getBoxName().equalsIgnoreCase("L-3")){
            Picasso.with(LiveActivity.this).load(ServerInfo.HOST_ADDRESS+"/"+advertiseModel.getAddress()).into(L3);
        }else if(advertiseModel.getBoxName().equalsIgnoreCase("M-1")){
            Picasso.with(LiveActivity.this).load(ServerInfo.HOST_ADDRESS+"/"+advertiseModel.getAddress()).into(M1);
        }else if(advertiseModel.getBoxName().equalsIgnoreCase("M-2")){
            Picasso.with(LiveActivity.this).load(ServerInfo.HOST_ADDRESS+"/"+advertiseModel.getAddress()).into(M2);
        }else if(advertiseModel.getBoxName().equalsIgnoreCase("M-3")){
            Picasso.with(LiveActivity.this).load(ServerInfo.HOST_ADDRESS+"/"+advertiseModel.getAddress()).into(M3);
        }else if(advertiseModel.getBoxName().equalsIgnoreCase("R-1")){
            Picasso.with(LiveActivity.this).load(ServerInfo.HOST_ADDRESS+"/"+advertiseModel.getAddress()).into(R1);
        }else if(advertiseModel.getBoxName().equalsIgnoreCase("R-2")){
            Picasso.with(LiveActivity.this).load(ServerInfo.HOST_ADDRESS+"/"+advertiseModel.getAddress()).into(R2);
        }else if(advertiseModel.getBoxName().equalsIgnoreCase("R-3")){
            Picasso.with(LiveActivity.this).load(ServerInfo.HOST_ADDRESS+"/"+advertiseModel.getAddress()).into(R3);
        }
    }
    @Override
    public void onDestroy() {
        if (youTubePlayer != null) {
            youTubePlayer.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Resume Call");
        youTubePlayerView.initialize(API_KEY, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        youTubePlayerView.initialize(API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
        if(null== youTubePlayer) return;
        this.youTubePlayer=youTubePlayer;
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);

        /*youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {
                System.out.println("onLoading");
                isBuffer=true;
                onLoading=0;
            }

            @Override
            public void onLoaded(String s) {
                System.out.println("onLoaded");
                isBuffer=false;
                youTubePlayer.play();
                currentlyPlayingAddress=s;
                onLoading=0;
            }

            @Override
            public void onAdStarted() {
                System.out.println("onAdStarted");
                isBuffer=false;
            }

            @Override
            public void onVideoStarted() {
                System.out.println("onVideoStarted");
                isBuffer=false;
            }

            @Override
            public void onVideoEnded() {
                System.out.println("onVideoEnded");
                isBuffer=false;
                *//*if(playFirst){
                    youTubePlayer.loadVideos(listAddress);
                }*//*
                playFirst=false;
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {
                System.out.println("onError");
               *//* youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
                youTubePlayerView.initialize(API_KEY, LiveActivity.this);*//*
                isBuffer=false;
            }
        });
        youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {
                System.out.println("Playing");
                youTubePlayerView.setVisibility(View.VISIBLE);
                // isBuffer=false;

            }

            @Override
            public void onPaused() {
                System.out.println("Pause");
                youTubePlayerView.setVisibility(View.GONE);
                isBuffer=false;
                youTubePlayer.play();
                onLoading++;

            }

            @Override
            public void onStopped() {
                System.out.println("Stoped");
                youTubePlayerView.setVisibility(View.GONE);
                isBuffer=false;
                onLoading++;

            }

            @Override
            public void onBuffering(boolean b) {
                System.out.println("Buffering");
                isBuffer=true;
                youTubePlayerView.setVisibility(View.GONE);

            }

            @Override
            public void onSeekTo(int i) {
                System.out.println("Seek to");
                youTubePlayerView.setVisibility(View.GONE);
                //isBuffer=false;
            }
        });


        youTubePlayer.setPlaylistEventListener(new YouTubePlayer.PlaylistEventListener() {
            @Override
            public void onPrevious() {

            }

            @Override
            public void onNext() {

            }

            @Override
            public void onPlaylistEnded() {
                System.out.println("Play List End...");
               if(onLoading>5) {
                   youTubePlayer.loadVideos(listAddress, 0, 0);
                   return;
               }
                onLoading++;
            }
        });*/

        youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {
                youTubePlayerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPaused() {
                youTubePlayerView.setVisibility(View.GONE);
                youTubePlayer.play();
            }

            @Override
            public void onStopped() {
                youTubePlayerView.setVisibility(View.GONE);
            }

            @Override
            public void onBuffering(boolean b) {
                youTubePlayerView.setVisibility(View.GONE);
            }

            @Override
            public void onSeekTo(int i) {
                youTubePlayerView.setVisibility(View.GONE);
            }
        });

        youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(String s) {
                youTubePlayer.play();
                currentlyPlayingAddress=s;
            }

            @Override
            public void onAdStarted() {

            }

            @Override
            public void onVideoStarted() {

            }

            @Override
            public void onVideoEnded() {
                System.out.println("Load ended");
                if(listAddress.get(listAddress.size()-1).equalsIgnoreCase(currentlyPlayingAddress)){
                    youTubePlayer.loadVideos(listAddress);
                }
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {

            }
        });
        /*youTubePlayer.setPlaylistEventListener(new YouTubePlayer.PlaylistEventListener() {
            @Override
            public void onPrevious() {

            }

            @Override
            public void onNext() {

            }

            @Override
            public void onPlaylistEnded() {
                System.out.println("Playlist end..");
                *//*if(youTubePlayer!=null){
                    youTubePlayer.loadVideos(listAddress);
                }*//*
            }
        });*/

        if(listAddress.size()>0){
            youTubePlayer.loadVideos(listAddress);
        }else{
            try {
                System.out.println("Invoke List 01");
                hub.invoke("LoadPlayList");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                System.out.println("Invoke List 02");
                hub.invoke("LoadPlayList");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /*public void playVideo(int idx){
        if(idx>=listAddress.size()){
            idx=0;
            currentIdx=0;
        }


        if(youTubePlayer!=null)
            youTubePlayer.loadVideo(listAddress.get(idx));
    }*/

    public void playVideoLocation(String s,int millisecond){

        if(youTubePlayer!=null){
            youTubePlayer.loadVideo(s,millisecond);
            playFirst=true;
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Failed to initialize. "+youTubeInitializationResult, Toast.LENGTH_LONG).show();
        System.out.println(youTubeInitializationResult);
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize(API_KEY, LiveActivity.this);

    }

    /*private ProgressBar findProgressBar(View view) {
        if (view instanceof ProgressBar) {
            return (ProgressBar)view;
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                System.out.println(viewGroup.getChildAt(i).toString());
                ProgressBar res = findProgressBar(viewGroup.getChildAt(i));
                if (res != null) return res;
            }
        }
        return null;
    }*/
}

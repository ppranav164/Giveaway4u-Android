package com.shopping.gway_4u;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class products_fragment extends Fragment implements RecyclerViewClickListener , product_slider_listener {

    private static final int RESULT_OK = 1 ;


    language_text languageText = new language_text();

    View view;

    View entireView;

    ImageButton wishlist;

    TextView textView,priceView,stockView,cid,offer,dfilename,brandTv;

    String brandName = "";

    RecyclerView productsrecyclerviw;

    ViewPager viewPager;


    LinearLayout linearLayout;
    LinearLayout Textboxlayout;
    LinearLayout datebox;
    LinearLayout timebox;
    LinearLayout datetimebox;

    Button UploadBtn;

    ImageButton fullscreenBtn;

    TextView descp;

    ImageView previewImg;

    RecyclerView recyclerView;

    PhotoView photoView;

    Context context;

    specFragment specFragment;

    JSONArray reviewsJson;

    int reviewsCount = 0;

    TextView reviewTotalTv;
    RatingBar ratingbar;

    private ImageView imageView,upimageshow;

    private Bitmap bitmap;

    private Uri filePath;

    EditText editText;

    TextView codd;

    String uploadCodes;

    List<String> sendImage;

     int product_option_id;

    RelativeLayout frameLayout;

    TabLayout tabLayout;

    Button cartBtn;

    public static   String descriptions,reviews,tabTexts;


    String fulldata;

    boolean isFileRequired;
    boolean isCheckRequired;
    boolean isTextRequired;

    boolean isChecked;

    boolean error;

    TextView notify;

    Cart_details cart_details;

    private String cartCount;


    View menutabs;

    LinearLayout radioGroup;

    RadioButton[] radioButtons = new RadioButton[5];

    RadioGroup[] radioGroups = new RadioGroup[5];

    RadioButton rbutton;

    TextView radiotxt;

    boolean success;

    TextView[] radioHeader = new TextView[5];

    EditText qtext;


    ImageButton plus,minus;

    int num=0;

    int limit;

    String link;

    config_hosts hosts  = new config_hosts();

    ArrayList<String> products_id = new ArrayList<>();


    int product_options_id = 0; //227 for file options[227] = code // gf44gr44e434ww435df345234523452345
    int textArea_product_id = 0;
    int dateOption_id = 0;
    int timeOption_id = 0;
    int datetimeOption_id = 0;

    recycleradapter_cart cart;


    Map<Integer,String> keyValue = new HashMap<>();

    String param = "";


    HashMap<Integer,String> uploadKeys = new HashMap<>();
    HashMap<Integer,String> textKeys = new HashMap<>();
    HashMap<Integer,String> dateKeys = new HashMap<>();
    HashMap<Integer,String> timeKeys = new HashMap<>();
    HashMap<Integer,String> datetimeKeys = new HashMap<>();

    HashMap<String,Boolean> rules = new HashMap<>();
    HashMap<Integer,String> text_error = new HashMap<>();

    ArrayList<String> fileErrors = new ArrayList<>();


    RadioGroup customRadio;

    LinearLayout uploadlayout;

    Button[] uploadButton  = new Button[5];

    LinearLayout[] linearLayouts = new LinearLayout[5];

    TextView[] codeView = new TextView[5];

    TextView textViewcode;

    String options ="";

    ImageView imageViews;

    ImageButton share;

    LinearLayout previewLayout;


    boolean hasPermission;

    String shareProductID = "";

    String productShareTitle = "";


    product_slider_listener slider_listener;

    private Handler handler;


    BackStacks backStacks;


    public products_fragment(Context context) {

        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.layout_products,container,false);
        viewPager = view.findViewById(R.id.productslider);
        Textboxlayout = view.findViewById(R.id.textbox);
        datebox = view.findViewById(R.id.datebox);
        timebox = view.findViewById(R.id.timebox);
        datetimebox =  view.findViewById(R.id.datetimebox);

        return view;
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        //super.onCreateOptionsMenu(menu, inflater);

        //getActivity().getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem menuItem = menu.findItem(R.id.cart);
        menutabs = MenuItemCompat.getActionView(menuItem);


    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        backStacks = new BackStacks();
        backStacks.getInstance(getContext());
        backStacks.setCurrentActivity("PRODUCT");

        final View view = getView();


        qtext = view.findViewById(R.id.quantity);

        qtext.setText(String.valueOf(limit));

        reviewTotalTv = view.findViewById(R.id.reviewsCount);
        ratingbar = view.findViewById(R.id.ratingbutton);


        quanityButton(view);


        cartBtn = view.findViewById(R.id.addToCart);

        share = view.findViewById(R.id.btnshare);

        shareProduct();

        uploadlayout = view.findViewById(R.id.uploadbox);


        previewLayout = view.findViewById(R.id.preview);


        frameLayout = view.findViewById(R.id.framwla);

        tabLayout = view.findViewById(R.id.mytabs);

        TabLayout.Tab firstTab = tabLayout.newTab();

        TabLayout.Tab secTab = tabLayout.newTab();

        TabLayout.Tab ThTab = tabLayout.newTab();

        firstTab.setText("Description");

        ThTab.setText("Reviews");

        tabLayout.addTab(firstTab);
        tabLayout.addTab(ThTab);

        tabLayout.getTabAt(0).select();

        FragmentManager manager = getFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.framwla,new specFragment(descriptions));

        transaction.commit();

        final Boolean[] clicked = {false};
         setWishlist(view);
         checkPermissionForReadExtertalStorage();

         hasPermission = checkPermissionForReadExtertalStorage();

         handler = new Handler();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction stransaction = fragmentManager.beginTransaction();
        stransaction.replace(R.id.framwla,new loadingSmall());
        stransaction.commit();

         handler.postDelayed(new Runnable() {
             @Override
             public void run() {
                 FragmentManager fragmentManager = getFragmentManager();
                 FragmentTransaction transaction = fragmentManager.beginTransaction();
                 transaction.replace(R.id.framwla,new specFragment(descriptions));
                 transaction.commit();
             }
         },1000);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                Fragment fragment = null;

                int id = tab.getPosition();

                if (id == 0)
                {
                    fragment = new specFragment(descriptions);

                }else if (id == 1)
                {
                    fragment = new reviewsFragment(reviewsJson,getContext());

                    if (reviewsJson.isNull(0)){
                        Snackbar.make(view,"No reviews found for this item !",Snackbar.LENGTH_SHORT).show();
                    }

                }


                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.framwla,fragment);
                transaction.commit();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        Bundle bundle = getArguments();

        if (bundle !=null)
        {
            String res = bundle.getString("_id");

            //Toast.makeText(getContext(),"Id is "+res,Toast.LENGTH_LONG).show();

            new SyncProduct(getContext(), new products() {
                @Override
                public void loadProductInfo(String data) {

                    fulldata = data;


                    setProductInfo(data);

                    try {

                        JSONObject object = new JSONObject(data);

                        setUpCart(object ,cartBtn);

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }



                }
            },res).execute();

        }




        isWishlistExists();


        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        //Toast.makeText(getContext(),"Products Fragment",Toast.LENGTH_LONG).show();
                        getFragmentManager().popBackStackImmediate();
                        return true;
                    }
                }
                return false;
            }
        });





        wishlist = view.findViewById(R.id.wishlistB);


        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Hii",Toast.LENGTH_LONG).show();
            }
        });

    }




    public void setWishlist(View view)
    {
        wishlist = view.findViewById(R.id.wishlistB);
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tag = wishlist.getTag().toString();
                switch (tag)
                {
                    case "yellow" :  wishlist.setBackgroundResource(R.drawable.wishlistp);
                                     wishlist.setTag("red");
                                     addWishlist();
                                     break;

                    case "red"    :  wishlist.setBackgroundResource(R.drawable.wishlist);
                                     wishlist.setTag("yellow");
                                     removeWishlist();
                                     break;
                }
            }
        });


    }




    public void isWishlistExists()
    {


        wishlist.setBackgroundResource(R.drawable.wishlist);

        wishlist.setTag("yellow");


        new sync_get_wishlist(getContext(), new wishlist() {
            @Override
            public void loadWishlist(String data) {
                try {

                    JSONObject object = new JSONObject(data);

                    JSONArray products = object.getJSONArray("products");

                    for (int i=0; i < products.length(); i++)
                    {
                       JSONObject id = products.getJSONObject(i);

                       String product_id = id.getString("product_id");

                       listWishlists(product_id);
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).execute();



    }



    public void listWishlists(String products)
    {
        products_id.add(products);

        cid = view.findViewById(R.id.cid);

        String id = cid.getText().toString();


        for (int i=0; i < products_id.size(); i++)
        {

            String data = products_id.get(i);

            if (data.contains(id))
            {
                wishlist.setBackgroundResource(R.drawable.wishlistp);
                wishlist.setTag("red");
            }
        }

    }




    public void addWishlist()
    {

        cid = view.findViewById(R.id.cid);

        int id = Integer.parseInt(cid.getText().toString());

        link = hosts.wishlist_remove+id;

        new syncWishlist(getContext(),id).execute();

        Snackbar snackbar = Snackbar.make(view,"Added to Wishlist",Snackbar.LENGTH_LONG);

        View snackView = snackbar.getView();

        snackView.setBackgroundColor(Color.parseColor("#008c48"));

        snackbar.setActionTextColor(Color.parseColor("#FFFFFF"));

        snackbar.setAction("View Wishlist", new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentManager manager = getFragmentManager();

                FragmentTransaction transaction = manager.beginTransaction();

                transaction.replace(R.id.mainframeL,new WishlistFragment(),"products");
                transaction.addToBackStack("products");
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();

            }
        });

        snackbar.show();

    }



    public void removeWishlist()
    {

        cid = view.findViewById(R.id.cid);

        String id = cid.getText().toString();

        link = hosts.wishlist_remove+id;

        new syncRemoveWishlist(getContext(),link,"GET").execute();

        Snackbar snackbar = Snackbar.make(view,"Removed successfully",Snackbar.LENGTH_SHORT);

        snackbar.show();



    }




    public void fileChooser(int requestCode)
    {

       if (hasPermission !=false)
       {
           Intent intent = new Intent();

           intent.setType("image/*");

           intent.setAction(Intent.ACTION_GET_CONTENT);

           startActivityForResult(Intent.createChooser(intent,"Select a Image"),requestCode);

       }else {


           Toast.makeText(getContext(),"You don not have granted permission access storage",Toast.LENGTH_SHORT).show();

          try {

              requestPermissionForReadExtertalStorage();

              hasPermission = true;

          }catch (Exception e)
          {
              e.printStackTrace();
          }


       }



    }




    @Override
    public void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
            //dfilename = view.findViewById(R.id.filename);
            String selectedFilePath = FilePath.getPath(getContext(),data.getData());
            String[] parts = selectedFilePath.split("/");
            final String fileName = parts[parts.length - 1];
            final int plus = 1;
            final LinearLayout.LayoutParams previewparam = new LinearLayout.LayoutParams(300,300);
            previewparam.setMargins(10,10,10,10);
            //dfilename.setText(fileName);
            codd = view.findViewById(R.id.codes);
            new syncUpload_image(getContext(),filePath, new upload() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void status(String status) {

                    Log.e("error",status);
                    try {
                        JSONObject object = new JSONObject(status);
                        if (object.has("code"))
                        {
                            String code = object.getString("code");
                            codd.setText(code);
                            uploadCodes = code;
                            keyValue.put(requestCode,code);
                            String cod = String.valueOf(requestCode);
                            rules.replace(cod,false);
                            text_error.remove(requestCode);

                            Log.e("requestCode","Upload for >> "+requestCode);

                            Log.e("final",keyValue.toString());
                            ImageView imageView = new ImageView(getContext());
                            imageView.setId(plus+requestCode);
                            imageView.setLayoutParams(previewparam);
                            previewLayout.addView(imageView);
                            ImageView screen = (ImageView) previewLayout.findViewById(plus+requestCode);
                            screen.setImageURI(filePath);


                            String error_tag = "error_"+requestCode;
                            TextView uploadtext = uploadlayout.findViewWithTag(error_tag);

                            if (uploadtext != null)
                            {
                                uploadtext.setVisibility(View.INVISIBLE);
                            }


                        }else
                        {
                            Log.e("isUploaded",object.getString("uploaded"));
                            Toast.makeText(getContext(),object.getString("error"),Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).execute();

            cid = view.findViewById(R.id.cid);
        }


    }



    public void setUploadCode(int requestCode,String code)
    {
        StringBuilder builder = new StringBuilder();
        String data = "option["+requestCode+"]="+code+"&";
        builder.append(data);
    }


    public  void setDescriptions(String data)
    {
        descriptions = data;
    }

    public void setTabTexts(String data)
    {
        tabTexts = data;
    }

    public void setReviewData(JSONArray array)
    {
        this.reviewsJson = array;
        String count =String.valueOf(array.length());
        reviewTotalTv.setText("("+count+")");
        ratingbar.setRating(reviewsCount);
    }


    
   public void setProductInfo(String data)

   {


       try {

           JSONObject object = new JSONObject(data);

           parseData(object);



       }catch (Exception e)
       {
           e.printStackTrace();
       }



   }



   public void parseData(JSONObject objects)
   {





       try {


           String mfg = objects.getString("heading_title");

           String brand = objects.getString("manufacturer");

           String price = objects.getString("price");

           String sharelink = objects.getString("share");

           reviewsCount = objects.getInt("rating");

           shareProductID = objects.getString("product_id");;
           productShareTitle = objects.getString("heading_title");

           String stock = objects.getString("stock");

           Log.e("Product Fragment > Stock",stock);

           String descps = objects.getString("description");

           String description = objects.getString("description");

           String reviewTabtxt = objects.getString("tab_review");

           JSONArray review_data = objects.getJSONArray("reviews");

           String image = objects.getString("thumb");

           String pid = objects.getString("product_id");

           String cutPrice = objects.getString("special");

           String minimum = objects.getString("minimum");

           JSONArray imagesArray = objects.getJSONArray("images");

           limit = Integer.parseInt(minimum);


           setReviewData(review_data);

           setDescriptions(descps);

           setTabTexts("loooooo");


           //photoView = view.findViewById(R.id.previewer);



           textView = view.findViewById(R.id.display);

           fullscreenBtn = view.findViewById(R.id.fullscreenB);

           priceView = view.findViewById(R.id.priceView);

           stockView = view.findViewById(R.id.stock);


           cid = view.findViewById(R.id.cid);

           offer = view.findViewById(R.id.special);

           productsrecyclerviw = view.findViewById(R.id.thumb_recyclerview);

            ArrayList<String> imageData = new ArrayList<>();

           sendImage = new ArrayList<>();


           imageData.add(image);
           sendImage.add(image);

           for (int k=0; k < imagesArray.length(); k++ )
           {
               JSONObject imageObject = imagesArray.getJSONObject(k);

               String popups = imageObject.getString("popup");
               imageData.add(popups);
               sendImage.add(popups);

           }

           slider_listener = this;

           productsrecyclerviw.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
           previewproducts_adapter adapter = new previewproducts_adapter(getContext(),imageData,this);
           productsrecyclerviw.setAdapter(adapter);

           viewPager.setAdapter(new slide_adapter_product(getContext(),imageData,slider_listener));


           fullscreenBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   FullScreenSlider(sendImage);
               }
           });


           //photoView.setImageResource(R.drawable.logo);

           //Picasso.get().load(image).into(photoView);


           cid.setText(pid);

           textView.setText(mfg);

           if (cutPrice == "false"){

               priceView.setText(price);

               offer.setVisibility(View.GONE);

           }else {

               priceView.setText(price);
               offer.setText(cutPrice);

               Paint paint = new Paint();
               paint.setColor(Color.parseColor("#ff0000"));
               paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

               priceView.setPaintFlags(paint.getFlags());

           }




           cartBtn = getView().findViewById(R.id.addToCart);


           if (stock.matches("Out Of Stock"))
           {

               stockView.setText("Not Available");
               stockView.setTextColor(getResources().getColor(R.color.red));
               cartBtn.setEnabled(false);
               cartBtn.setBackgroundColor(getResources().getColor(R.color.grey));
               cartBtn.setText(stock);
               cartBtn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Toast.makeText(getContext(),"Not available",Toast.LENGTH_LONG).show();
                   }
               });

           }else {

               stockView.setText(stock);
               stockView.setTextColor(getResources().getColor(R.color.green));
           }



          if (descps.contains("&nbsp;"))
          {
              descps = descps.replace("&nbsp;"," ");
          }



           radioGroup = view.findViewById(R.id.radios);

           radioOptionsview(objects);



           //Product options here
           //show random options required for a product
           //all items should  atleast be checked at index 0 (Checked / selected )


           linearLayout = view.findViewById(R.id.uploadbox);
           JSONObject optb = objects.getJSONObject("upload");


          for (int i=0; i < objects.length(); i++)
          {

              if (objects.has("upload"))
              {

                  Log.e("extra_option",optb.toString());

                  if (optb.has("file"))
                  {
                      JSONArray array = optb.getJSONArray("file");
                      for (int k=0; k<array.length(); k++)
                      {
                          JSONObject jsonObject = array.getJSONObject(k);
                          String type = jsonObject.getString("type");
                          String uploadId = jsonObject.getString("product_option_id");
                          String upload_title = jsonObject.getString("name");
                          int upload_id = Integer.parseInt(uploadId);
                          uploadKeys.put(upload_id,upload_title);
                          linearLayout.setVisibility(View.VISIBLE);
                          isFileRequired = true;
                          product_options_id = Integer.parseInt(uploadId);
                      }
                  }

                  if (optb.has("textarea"))
                  {
                      JSONArray textArea = optb.getJSONArray("textarea");

                      for (int k=0; k<textArea.length(); k++)
                      {
                          JSONObject jsonObject = textArea.getJSONObject(k);
                          String type = jsonObject.getString("type");
                          String option_text_id = jsonObject.getString("product_option_id");
                          String textTitle = jsonObject.getString("name");
                          int textID = Integer.parseInt(option_text_id);
                          textKeys.put(textID,textTitle);
                          Textboxlayout.setVisibility(View.VISIBLE);
                          isTextRequired = true;
                          textArea_product_id = Integer.parseInt(option_text_id);

                      }
                  }


                  if (optb.has("date"))
                  {
                      JSONArray dateOption = optb.getJSONArray("date");

                      for (int k=0; k<dateOption.length(); k++)
                      {
                          JSONObject jsonObject = dateOption.getJSONObject(k);
                          String option_text_id = jsonObject.getString("product_option_id");
                          String textTitle = jsonObject.getString("name");
                          int textID = Integer.parseInt(option_text_id);
                          dateKeys.put(textID,textTitle);
                          datebox.setVisibility(View.VISIBLE);
                          dateOption_id = Integer.parseInt(option_text_id);
                          datebox.setVisibility(View.VISIBLE);

                      }
                  }


                  if (optb.has("time"))
                  {
                      JSONArray dateOption = optb.getJSONArray("time");

                      for (int k=0; k<dateOption.length(); k++)
                      {
                          JSONObject jsonObject = dateOption.getJSONObject(k);
                          String option_text_id = jsonObject.getString("product_option_id");
                          String textTitle = jsonObject.getString("name");
                          int textID = Integer.parseInt(option_text_id);
                          timeKeys.put(textID,textTitle);
                          timebox.setVisibility(View.VISIBLE);
                          timeOption_id = Integer.parseInt(option_text_id);

                      }
                  }



                  if (optb.has("datetime"))
                  {
                      JSONArray dateOption = optb.getJSONArray("datetime");

                      for (int k=0; k<dateOption.length(); k++)
                      {
                          JSONObject jsonObject = dateOption.getJSONObject(k);
                          String option_text_id = jsonObject.getString("product_option_id");
                          String textTitle = jsonObject.getString("name");
                          int textID = Integer.parseInt(option_text_id);
                          datetimeKeys.put(textID,textTitle);
                          datetimebox.setVisibility(View.VISIBLE);
                          datetimeOption_id = Integer.parseInt(option_text_id);
                      }
                  }


              }

          }

          if (objects.has("upload"))
          {
              if (optb.has("file"))
              {
                  JSONArray array = optb.getJSONArray("file");
                  uploadFileView(array.length());
              }

              if (optb.has("textarea"))
              {
                  JSONArray textArea = optb.getJSONArray("textarea");
                  TextArea(textArea.length());
              }

              if (optb.has("date"))
              {
                  JSONArray textArea = optb.getJSONArray("date");
                  setdeliveryDate(textArea.length());
              }

              if (optb.has("time"))
              {
                  JSONArray textArea = optb.getJSONArray("time");
                  setdeliveryTime(textArea.length());
              }

              if (optb.has("datetime"))
              {
                  JSONArray textArea = optb.getJSONArray("datetime");
                  setDateTime(textArea.length());
              }

          }


       }catch (Exception e)
       {
           e.printStackTrace();
       }



   }



   public void radioOptionsview(JSONObject objects)
   {
       try {

           JSONArray optionsArray = objects.getJSONArray("options");


           Map<String,String> radiokeys = new HashMap<>();

           LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,

                   LinearLayout.LayoutParams.WRAP_CONTENT);


           LinearLayout.LayoutParams textviewparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

           textviewparam.setMargins(20,20,0,0);




           for (int l=0; l < optionsArray.length(); l++)
           {
               JSONObject optionObj = optionsArray.getJSONObject(l);

               String type = optionObj.getString("type");

               String title = optionObj.getString("title");

               String optionId = optionObj.getString("id");

               radiotxt = view.findViewById(R.id.radioid);

               radiotxt.setText(optionId);

               radioHeader[l] = new TextView(getContext());
               radioHeader[l].setText(title);
               radioHeader[l].setLayoutParams(textviewparam);
               radioHeader[l].setTextColor(getResources().getColor(R.color.black));



               radioGroup.addView(radioHeader[l]);



               radioGroups[l] = new RadioGroup(getContext());
               radioGroups[l].setTag(title);
               radioGroups[l].setId(Integer.parseInt(optionId));
               radioGroups[l].setOrientation(LinearLayout.HORIZONTAL);

               radioGroup.addView(radioGroups[l]);




               LinearLayout.LayoutParams radioparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

               radioparams.setMargins(20,20,0,0);



               JSONArray items = optionObj.getJSONArray("items");

               for (int p=0; p < items.length(); p++)
               {
                   JSONObject itemObj = items.getJSONObject(p);

                   String id = itemObj.getString("id");

                   String value = itemObj.getString("value");


                   radioButtons[p] = new RadioButton(getContext());
                   radioButtons[p].setButtonDrawable(android.R.color.transparent);
                   radioButtons[p].setBackgroundResource(R.drawable.radio_background);
                   radioButtons[p].setLayoutParams(params);
                   radioButtons[p].setPadding(20,20,20,20);

                   radioButtons[p].setId(Integer.parseInt(id));
                   radioButtons[p].setTag(id);
                   radioButtons[p].setText(value);
                   radioButtons[0].setChecked(true);
                   radioButtons[p].setLayoutParams(radioparams);
                   radioGroups[l].addView(radioButtons[p]);
                   radioButtons[p].setGravity(Gravity.CENTER);


                   int key = radioGroups[l].getId();

                   String values = String.valueOf(radioGroups[l].getCheckedRadioButtonId());


                   keyValue.put(key,values);

                   isChecked = true;


                   radioGroups[l].setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                       @Override
                       public void onCheckedChanged(RadioGroup group, int checkedId) {

                           int who = group.getId();
                           String rbId = String.valueOf(checkedId);
                           keyValue.put(who,rbId);
                           isChecked = true;

                       }
                   });

               }

               if (type.equals("radio"))
               {
                   int total = radiokeys.size();
                   isCheckRequired = true;
                   Iterator it = radiokeys.entrySet().iterator();
               }

           }

       }catch (Exception e)
       {
           e.printStackTrace();
       }
   }


   public void uploadFileView(int length)
   {
       LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
       params1.setMargins(5,5,5,5);
       Iterator iterator = uploadKeys.entrySet().iterator();

       int index =-1;

       TextView[] texts = new TextView[length];
       texts[0] = new TextView(getContext());
       texts[0].setText("File Upload :");
       uploadlayout.addView(texts[0]);


       while (iterator.hasNext())
       {

           index++;

           Map.Entry pair = (Map.Entry) iterator.next();

           String value = pair.getValue().toString();

           int id = Integer.parseInt(pair.getKey().toString());

           linearLayouts[index] = new LinearLayout(getContext());
           linearLayouts[index].setLayoutParams(params1);
           linearLayouts[index].setId(id);

           uploadlayout.addView(linearLayouts[index]);


           uploadButton[index] = new Button(getContext());
           uploadButton[index].setTag(id);
           uploadButton[index].setText(value);
           uploadButton[index].setBackgroundResource(R.drawable.upload_active);


           String codes = String.valueOf(id);

           rules.put(codes,true);
           text_error.put(id,language_text.TEXT_UPLOAD);

           uploadlayout.addView(uploadButton[index]);
           uploadButton[index].setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   String opt = v.getTag().toString();
                   int code = Integer.parseInt(opt);
                   fileChooser(code);
               }
           });


           String error_tag = "error_"+id;
           TextView[] text_warning = new TextView[length];
           text_warning[0] = new TextView(getContext());
           text_warning[0].setText(language_text.TEXT_UPLOAD);
           text_warning[0].setTag(error_tag);
           text_warning[0].setVisibility(View.INVISIBLE);
           text_warning[0].setTextColor(getResources().getColor(R.color.red));
           uploadlayout.addView(text_warning[0]);

       }



   }



   public void TextArea(int length)
   {
       Iterator iterator = textKeys.entrySet().iterator();
       TextView[] texts = new TextView[length];
       EditText[] editTexts = new EditText[length];

       int index = -1;

       while (iterator.hasNext())
       {

           index++;

           Map.Entry pair = (Map.Entry) iterator.next();
           String value = pair.getValue().toString();
           final int id = Integer.parseInt(pair.getKey().toString());
           final String codes = String.valueOf(id);


           texts[index] = new TextView(getContext());
           texts[index].setText(value);
           Textboxlayout.addView(texts[index]);

           editTexts[index] = new EditText(getContext());
           editTexts[index].setLines(4);
           editTexts[index].setTag(codes);
           Textboxlayout.addView(editTexts[index]);


           rules.put(codes,true);
           text_error.put(id,language_text.TEXT_TEXTAREA);



           String error_tag = "error_"+id;
           TextView[] text_warning = new TextView[length];
           text_warning[index] = new TextView(getContext());
           text_warning[index].setText(language_text.TEXT_TEXTAREA);
           text_warning[index].setTag(error_tag);
           text_warning[index].setVisibility(View.INVISIBLE);
           text_warning[index].setTextColor(getResources().getColor(R.color.red));
           Textboxlayout.addView(text_warning[index]);



           editTexts[index].addTextChangedListener(new TextWatcher() {
               @Override
               public void beforeTextChanged(CharSequence s, int start, int count, int after) {

               }

               @Override
               public void onTextChanged(CharSequence s, int start, int before, int count) {

                   int textlength = count;
                   String chars = s.toString();

                   String error_tag = "error_"+id;
                   TextView textAreatext = Textboxlayout.findViewWithTag(error_tag);
                   TextView dateText = datebox.findViewWithTag(error_tag);


                   if (count > 0)
                   {
                       keyValue.put(id,chars);
                       rules.put(codes,false);
                       text_error.remove(id);

                       if (textAreatext != null)
                       {
                           textAreatext.setVisibility(View.INVISIBLE);
                       }


                   }else {

                       keyValue.remove(id);
                       rules.put(codes,true);
                       text_error.put(id,language_text.TEXT_TEXTAREA);

                       if (textAreatext != null)
                       {
                           textAreatext.setVisibility(View.VISIBLE);
                       }

                   }

               }

               @Override
               public void afterTextChanged(Editable s) {


               }
           });

       }


   }



    public void setdeliveryDate(int length)
    {
        Iterator iterator = dateKeys.entrySet().iterator();
        TextView[] texts = new TextView[length];
        EditText[] editTexts = new EditText[length];

        int index = -1;


        while (iterator.hasNext()) {

            index++;

            Map.Entry pair = (Map.Entry) iterator.next();
            String value = pair.getValue().toString();
            final int id = Integer.parseInt(pair.getKey().toString());
            final String codes = String.valueOf(id);


            texts[index] = new TextView(getContext());
            texts[index].setText(value);
            datebox.addView(texts[index]);

            editTexts[index] = new EditText(getContext());
            editTexts[index].setLines(1);
            editTexts[index].setFocusable(false);
            editTexts[index].setTag(codes);
            datebox.addView(editTexts[index]);


            String error_tag = "error_"+id;
            TextView[] text_warning = new TextView[length];
            text_warning[index] = new TextView(getContext());
            text_warning[index].setText(language_text.TEXT_DATE);
            text_warning[index].setTag(error_tag);
            text_warning[index].setVisibility(View.INVISIBLE);
            text_warning[index].setTextColor(getResources().getColor(R.color.red));
            datebox.addView(text_warning[index]);



            rules.put(codes, true);
            text_error.put(id,language_text.TEXT_DATE);


            editTexts[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText editText = (EditText) v;

                    updateDate(editText);

                }
            });


            editTexts[index].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    int textlength = count;
                    String chars = s.toString();


                    String error_tag = "error_"+id;
                    TextView dateText = datebox.findViewWithTag(error_tag);

                    if (count > 0)
                    {
                        keyValue.put(id,chars);
                        rules.put(codes,false);
                        text_error.remove(id);

                        if (dateText != null)
                        {
                            dateText.setVisibility(View.INVISIBLE);
                        }

                    }else {

                        keyValue.remove(id);
                        rules.put(codes,true);
                        text_error.put(id,language_text.TEXT_DATE);

                         if (dateText != null)
                        {
                            dateText.setVisibility(View.VISIBLE);
                        }

                    }

                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            });



        }

    }




    public void setdeliveryTime(int length)
    {
        Iterator iterator = timeKeys.entrySet().iterator();
        TextView[] texts = new TextView[length];
        EditText[] editTexts = new EditText[length];

        int index = -1;


        while (iterator.hasNext()) {

            index++;

            Map.Entry pair = (Map.Entry) iterator.next();
            String value = pair.getValue().toString();
            final int id = Integer.parseInt(pair.getKey().toString());
            final String codes = String.valueOf(id);


            texts[index] = new TextView(getContext());
            texts[index].setText(value);
            timebox.addView(texts[index]);

            editTexts[index] = new EditText(getContext());
            editTexts[index].setLines(1);
            editTexts[index].setFocusable(false);
            editTexts[index].setTag(codes);
            timebox.addView(editTexts[index]);


            String error_tag = "error_"+id;
            TextView[] text_warning = new TextView[length];
            text_warning[index] = new TextView(getContext());
            text_warning[index].setText(language_text.TEXT_TIME);
            text_warning[index].setTag(error_tag);
            text_warning[index].setVisibility(View.INVISIBLE);
            text_warning[index].setTextColor(getResources().getColor(R.color.red));
            timebox.addView(text_warning[index]);



            rules.put(codes, true);
            text_error.put(id,language_text.TEXT_DATE);


            editTexts[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText editText = (EditText) v;

                    updateTime(editText);

                }
            });


            editTexts[index].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    int textlength = count;
                    String chars = s.toString();


                    String error_tag = "error_"+id;
                    TextView dateText = timebox.findViewWithTag(error_tag);

                    if (count > 0)
                    {
                        keyValue.put(id,chars);
                        rules.put(codes,false);
                        text_error.remove(id);

                        if (dateText != null)
                        {
                            dateText.setVisibility(View.INVISIBLE);
                        }

                    }else {

                        keyValue.remove(id);
                        rules.put(codes,true);
                        text_error.put(id,language_text.TEXT_DATE);

                        if (dateText != null)
                        {
                            dateText.setVisibility(View.VISIBLE);
                        }

                    }

                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            });



        }

    }


    public void setDateTime(int length)
    {
        Iterator iterator = datetimeKeys.entrySet().iterator();
        TextView[] texts = new TextView[length];
        EditText[] editTexts = new EditText[length];

        int index = -1;


        while (iterator.hasNext()) {

            index++;

            Map.Entry pair = (Map.Entry) iterator.next();
            String value = pair.getValue().toString();
            final int id = Integer.parseInt(pair.getKey().toString());
            final String codes = String.valueOf(id);


            texts[index] = new TextView(getContext());
            texts[index].setText(value);
            datetimebox.addView(texts[index]);

            editTexts[index] = new EditText(getContext());
            editTexts[index].setLines(1);
            editTexts[index].setFocusable(false);
            editTexts[index].setTag(codes);
            datetimebox.addView(editTexts[index]);


            String error_tag = "error_"+id;
            TextView[] text_warning = new TextView[length];
            text_warning[index] = new TextView(getContext());
            text_warning[index].setText(language_text.TEXT_TIME_DATE);
            text_warning[index].setTag(error_tag);
            text_warning[index].setVisibility(View.INVISIBLE);
            text_warning[index].setTextColor(getResources().getColor(R.color.red));
            datetimebox.addView(text_warning[index]);



            rules.put(codes, true);
            text_error.put(id,language_text.TEXT_DATE);


            editTexts[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText editText = (EditText) v;

                    updateTime(editText);

                }
            });


            editTexts[index].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    int textlength = count;
                    String chars = s.toString();


                    String error_tag = "error_"+id;
                    TextView dateText = datetimebox.findViewWithTag(error_tag);

                    if (count > 0)
                    {
                        keyValue.put(id,chars);
                        rules.put(codes,false);
                        text_error.remove(id);

                        if (dateText != null)
                        {
                            dateText.setVisibility(View.INVISIBLE);
                        }

                    }else {

                        keyValue.remove(id);
                        rules.put(codes,true);
                        text_error.put(id,language_text.TEXT_DATE);

                        if (dateText != null)
                        {
                            dateText.setVisibility(View.VISIBLE);
                        }

                    }

                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            });



        }

    }



    private void updateTime(final EditText v)
    {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.e("onTimeSet",hourOfDay+":"+minute);

                String times = String.valueOf(hourOfDay)+":"+String.valueOf(minute);
                v.setText(times);
            }
        },hour,minute,false);

        timePickerDialog.show();

    }



    private void updateDate(final EditText v)
    {

        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                Log.e("dateset",sdf.format(calendar.getTime()));

                String dateset = sdf.format(calendar.getTime());
                v.setText(dateset);

            }
        };

        new DatePickerDialog(getContext(),dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))
                .show();


    }



    public void FullScreenSlider(List<String> images)
   {
       int position = viewPager.getCurrentItem();
       Intent intent = new Intent(getContext(),FullscreenSlider.class);
       intent.putStringArrayListExtra("image", (ArrayList<String>) images);
       intent.putExtra("position",position);
       startActivity(intent);
   }



   public void setUpCart(final JSONObject data, final View view)

   {

       view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               entireView = getView();
               qtext = entireView.findViewById(R.id.quantity);
               Log.e("Cart ",keyValue.toString());
               Log.e("rules",rules.toString());

               int cart_pro_id = 0;
               int quantity = Integer.parseInt(qtext.getText().toString());
               radiotxt = entireView.findViewById(R.id.radioid);
               String id = radiotxt.getText().toString();


              if (!rules.containsValue(true))
                 {
                     error = false;
                }


               try {
                      cart_pro_id = data.getInt("product_id");
                      StringBuilder stringBuilder = new StringBuilder();
                      Iterator iterator = keyValue.entrySet().iterator();


                      while (iterator.hasNext())
                      {
                          Map.Entry pair = (Map.Entry) iterator.next();
                          int key = Integer.parseInt( pair.getKey().toString());
                          String values = pair.getValue().toString();
                          keyValue.put(key,values);
                          String params = "option["+key+"]="+values+"&";
                          stringBuilder.append(params);
                      }

                      options = stringBuilder.toString();
                      options += "product_id="+cart_pro_id+"&quantity="+quantity;

                      Log.e("Selected Option",options);


               }catch (Exception e)
               {
                   e.printStackTrace();
               }


               Log.e("isChecked",String.valueOf(isChecked));
               Log.e("chech required",String.valueOf(isCheckRequired));


               if (error != true)

               {
                   new syncCartAdd(getContext(),options, new addedToCart() {
                       @Override
                       public void loadCarts(String data) {

                           Log.e("loadCarts",data);

                           try {

                               JSONObject messageobj = new JSONObject(data);

                               if (messageobj.has("error"))
                               {
                                   success = false;
                                   showError(messageobj.getJSONObject("error"));
                               }

                               if (messageobj.has("status"))
                               {
                                   String message = messageobj.getString("status");
                                   Log.e("Server Response",message);
                                   if (message.equals("success"))
                                   {
                                       success = true;
                                       redirectHome();
                                   }
                               }

                           }catch (Exception e)
                           {
                               e.printStackTrace();
                           }
                       }
                   }).execute();

                   setHasOptionsMenu(true);

               }

           }

       });



   }




   public void validateError()
   {

       Iterator iterator = text_error.entrySet().iterator();

       while (iterator.hasNext())
       {
           Map.Entry keys = (Map.Entry) iterator.next();

           Log.e("validateError",""+keys.getKey());

           String error_tag = "error_"+keys.getKey();
           TextView uploadtext = uploadlayout.findViewWithTag(error_tag);
           TextView textAreatext = Textboxlayout.findViewWithTag(error_tag);
           TextView dateText = datebox.findViewWithTag(error_tag);
           TextView timeText = timebox.findViewWithTag(error_tag);
           TextView datetimeText = datetimebox.findViewWithTag(error_tag);

           if (uploadtext != null)
           {
               uploadtext.setVisibility(View.VISIBLE);
           }

           if (textAreatext != null)
           {
               textAreatext.setVisibility(View.VISIBLE);
           }

           if (dateText != null)
           {
               dateText.setVisibility(View.VISIBLE);
           }

           if (timeText != null)
           {
               timeText.setVisibility(View.VISIBLE);
           }

           if (datetimeText != null)
           {
               datetimeText.setVisibility(View.VISIBLE);
           }
       }

   }


    public void showErrorByCode(int code)
    {

        Log.e("showErrorByCode",""+code);
        String error_tag = "error_"+code;
        TextView uploadtext = uploadlayout.findViewWithTag(error_tag);
        TextView textAreatext = Textboxlayout.findViewWithTag(error_tag);
        TextView dateText = datebox.findViewWithTag(error_tag);
        TextView timeText = timebox.findViewWithTag(error_tag);
        TextView datetimeText = datetimebox.findViewWithTag(error_tag);

        if (uploadtext != null)
        {
            uploadtext.setVisibility(View.VISIBLE);
        }

        if (textAreatext != null)
        {
            textAreatext.setVisibility(View.VISIBLE);
        }

        if (dateText != null)
        {
            dateText.setVisibility(View.VISIBLE);
        }

        if (timeText != null)
        {
            timeText.setVisibility(View.VISIBLE);
        }

        if (datetimeText != null)
        {
            datetimeText.setVisibility(View.VISIBLE);
        }

    }



   public void showError(JSONObject errors)
   {
       try {

          if (errors.has("option"))
          {
              JSONObject object = errors.getJSONObject("option");
              Iterator<?> keys = object.keys();
              while (keys.hasNext())
              {
                  String objectKey = (String) keys.next();
                  Log.e("objectKey",objectKey);
                  String text_warning = object.getString(objectKey);

                  showErrorByCode(Integer.parseInt(objectKey));

                  EditText dates = datebox.findViewWithTag(objectKey);
                  EditText textareas = Textboxlayout.findViewWithTag(objectKey);

                  if (textareas.findViewWithTag(objectKey) != null)
                  {
                      textareas.setError(text_warning);
                  }else if (dates.findViewWithTag(objectKey) != null)
                  {
                      dates.setError(text_warning);
                  }

              }
          }

       }catch (Exception e)
       {
           e.printStackTrace();
       }
   }


   public void redirectHome()

   {
       Fragment fragment = new fragment_main();
       FragmentManager manager = getFragmentManager();
       FragmentTransaction transaction = manager.beginTransaction();
       transaction.replace(R.id.mainframeL,fragment);
       transaction.commit();

       reloadScreen();

       Snackbar.make(view,"Item added to cart",Snackbar.LENGTH_SHORT).show();


   }


   public void reloadScreen()
   {
       new syncInfo(getContext(), new info() {
           @Override
           public void getInfo(String data) {

               try {

                   JSONObject jsonObject = new JSONObject(data);
                   String items = jsonObject.getString("text_items");

                   //Textview here to set count to cart basket
                   notify = menutabs.findViewById(R.id.notify_badge);

                   notify.setText(items);

                   Log.e("total",items);

               }catch (Exception e)
               {
                   e.printStackTrace();
               }

           }
       }).execute();
   }



   public void quanityButton(View views)

   {


       plus = views.findViewById(R.id.plus);

       minus = views.findViewById(R.id.minus);

       qtext = views.findViewById(R.id.quantity);

       qtext.setText(String.valueOf(1));


       plus.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               num++;

               setPlus();

           }
       });


       minus.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               setMinus();
           }
       });

   }






   public void setPlus()

   {

       entireView = getView();

       qtext = entireView.findViewById(R.id.quantity);

       if (num > 0)
       {
           num = Integer.parseInt(qtext.getText().toString());

           num++;
       }

       if (num > 10)
       {
           num = 10;

           Toast.makeText(getContext(),"You have reached quanity limit",Toast.LENGTH_SHORT).show();
       }

       qtext.setText(String.valueOf(num));

   }




    public void setMinus()

    {

        entireView = getView();

        qtext = entireView.findViewById(R.id.quantity);

        int currentValue = Integer.parseInt(qtext.getText().toString());

        int subs = currentValue - 1;

        if (subs < 1)
        {
            subs = limit;

            //set to 1 if a user is going to decrement less than 1
        }

        qtext.setText(String.valueOf(subs));

    }


    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            return result == PackageManager.PERMISSION_GRANTED;
        }

        return false;
    }


    public void requestPermissionForReadExtertalStorage() throws Exception {

        try {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }



     public void shareProduct()
     {

         share.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent sharing = new Intent(Intent.ACTION_SEND);
                 sharing.setType("text/plain");
                 sharing.putExtra(Intent.EXTRA_SUBJECT,productShareTitle);
                 sharing.putExtra(Intent.EXTRA_TEXT,shareProductID);
                 startActivity(Intent.createChooser(sharing,"Share via"));
             }
         });

     }


    @Override
    public void recyclerViewListClicked(View v, int position) {
        viewPager.setCurrentItem(position);

    }


    @Override
    public void isClicked(boolean clicked, int position) {

        FullScreenSlider(sendImage);

    }


}

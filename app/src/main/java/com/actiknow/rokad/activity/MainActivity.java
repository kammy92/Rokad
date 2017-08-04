package com.actiknow.rokad.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actiknow.rokad.R;
import com.actiknow.rokad.utils.AppConfigTags;
import com.actiknow.rokad.utils.AppConfigURL;
import com.actiknow.rokad.utils.Constants;
import com.actiknow.rokad.utils.NetworkConnection;
import com.actiknow.rokad.utils.SetTypeFace;
import com.actiknow.rokad.utils.UserDetailsPref;
import com.actiknow.rokad.utils.Utils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    TextView tvTruckEntry;
    TextView tvReceiving;
    TextView tvRokad;
    ImageView ivNavigation;
    UserDetailsPref userDetailsPref;
    Bundle savedInstanceState;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        this.savedInstanceState = savedInstanceState;
        initView ();
        initData ();
        initListener ();
        initDrawer ();
        isLogin ();
        initApplication ();
    }
    
    private void isLogin () {
        if (userDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.USER_LOGIN_KEY) == "") {
            Intent myIntent = new Intent (this, LoginActivity.class);
            startActivity (myIntent);
            finish ();
        }
    }
    
    private void initListener () {
        ivNavigation.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                result.openDrawer ();
            }
        });
        
        tvTruckEntry.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent intentTruckEntry = new Intent (MainActivity.this, TruckEntryActivity.class);
                startActivity (intentTruckEntry);
                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        
        tvReceiving.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent intentTruckEntry = new Intent (MainActivity.this, ReceivingActivity.class);
                startActivity (intentTruckEntry);
                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        
        tvRokad.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                
            }
        });
    }
    
    private void initData () {
        userDetailsPref = UserDetailsPref.getInstance ();
        Utils.setTypefaceToAllViews (this, tvTruckEntry);
    }
    
    private void initView () {
        tvTruckEntry = (TextView) findViewById (R.id.tvTruckEntry);
        tvReceiving = (TextView) findViewById (R.id.tvReceiving);
        tvRokad = (TextView) findViewById (R.id.tvRokad);
        ivNavigation = (ImageView) findViewById (R.id.ivNavigation);
    }
    
    private void showLogOutDialog () {
        MaterialDialog dialog = new MaterialDialog.Builder (this)
                .limitIconToDefaultSize ()
                .content ("Do you wish to Sign Out?")
                .positiveText ("Yes")
                .negativeText ("No")
                .typeface (SetTypeFace.getTypeface (MainActivity.this), SetTypeFace.getTypeface (MainActivity.this))
                .onPositive (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_NAME, "");
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_MOBILE, "");
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_EMAIL, "");
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_TYPE, "");
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_LOGIN_KEY, "");
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_PARTY_ID, "");
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_PARTY_NAME, "");
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_COMPANY_ID, "");
                        userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_COMPANY_NAME, "");
                        Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity (intent);
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }).build ();
        dialog.show ();
    }
    
    private void initDrawer () {
        IProfile profile = new IProfile () {
            @Override
            public Object withName (String name) {
                return null;
            }
    
            @Override
            public StringHolder getName () {
                return null;
            }
    
            @Override
            public Object withEmail (String email) {
                return null;
            }
    
            @Override
            public StringHolder getEmail () {
                return null;
            }
    
            @Override
            public Object withIcon (Drawable icon) {
                return null;
            }
    
            @Override
            public Object withIcon (Bitmap bitmap) {
                return null;
            }
    
            @Override
            public Object withIcon (@DrawableRes int iconRes) {
                return null;
            }
    
            @Override
            public Object withIcon (String url) {
                return null;
            }
    
            @Override
            public Object withIcon (Uri uri) {
                return null;
            }
    
            @Override
            public Object withIcon (IIcon icon) {
                return null;
            }
    
            @Override
            public ImageHolder getIcon () {
                return null;
            }
    
            @Override
            public Object withSelectable (boolean selectable) {
                return null;
            }
    
            @Override
            public boolean isSelectable () {
                return false;
            }
    
            @Override
            public Object withIdentifier (long identifier) {
                return null;
            }
    
            @Override
            public long getIdentifier () {
                return 0;
            }
        };
        
        DrawerImageLoader.init (new AbstractDrawerImageLoader () {
            @Override
            public void set (ImageView imageView, Uri uri, Drawable placeholder) {
                if (uri != null) {
                    Glide.with (imageView.getContext ()).load (uri).placeholder (placeholder).into (imageView);
                }
            }
            
            @Override
            public void cancel (ImageView imageView) {
                Glide.clear (imageView);
            }
            
            @Override
            public Drawable placeholder (Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name ().equals (tag)) {
                    return DrawerUIUtils.getPlaceHolder (ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name ().equals (tag)) {
                    return new IconicsDrawable (ctx).iconText (" ").backgroundColorRes (com.mikepenz.materialdrawer.R.color.colorPrimary).sizeDp (56);
                } else if ("customUrlItem".equals (tag)) {
                    return new IconicsDrawable (ctx).iconText (" ").backgroundColorRes (R.color.md_white_1000);
                }
                
                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()
                
                return super.placeholder (ctx, tag);
            }
        });
        
        
        headerResult = new AccountHeaderBuilder ()
                .withActivity (this)
                .withCompactStyle (false)
                .withTypeface (SetTypeFace.getTypeface (MainActivity.this))
                .withPaddingBelowHeader (false)
                .withSelectionListEnabled (false)
                .withSelectionListEnabledForSingleProfile (false)
                .withProfileImagesVisible (false)
                .withOnlyMainProfileImageVisible (false)
                .withDividerBelowHeader (true)
                .withHeaderBackground (R.color.primary)
                .withSavedInstance (savedInstanceState)
                .withOnAccountHeaderListener (new AccountHeader.OnAccountHeaderListener () {
                    @Override
                    public boolean onProfileChanged (View view, IProfile profile, boolean currentProfile) {
                        Intent intent = new Intent (MainActivity.this, MainActivity.class);
                        startActivity (intent);
                        return false;
                    }
                })
                .build ();
        headerResult.addProfiles (new ProfileDrawerItem ()
                .withName (userDetailsPref.getStringPref (this, UserDetailsPref.USER_NAME))
                .withEmail (userDetailsPref.getStringPref (this, UserDetailsPref.USER_EMAIL)));

      /*   .withIcon (buyerDetailsPref.getStringPref (MainActivity.this, BuyerDetailsPref.BUYER_IMAGE))
                .withName (buyerDetailsPref.getStringPref (MainActivity.this, BuyerDetailsPref.BUYER_NAME))
                .withEmail (buyerDetailsPref.getStringPref (MainActivity.this, BuyerDetailsPref.BUYER_EMAIL)));*/
        
        
        result = new DrawerBuilder ()
                .withActivity (this)
                .withAccountHeader (headerResult)
//                .withToolbar (toolbar)
//                .withItemAnimator (new AlphaCrossFadeAnimator ())
                .addDrawerItems (
                        new PrimaryDrawerItem ().withName ("My Home").withIcon (FontAwesome.Icon.faw_home).withIdentifier (1).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Sign Out").withIcon (FontAwesome.Icon.faw_sign_out).withIdentifier (2).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this))
                )
                .withSavedInstance (savedInstanceState)
                .withOnDrawerItemClickListener (new Drawer.OnDrawerItemClickListener () {
                    @Override
                    public boolean onItemClick (View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier ()) {
                            case 2:
                                showLogOutDialog ();
                                break;
                          /*  case 3:
                                Intent intent3 = new Intent (MainActivity.this, RecommendedJobActivity.class);
                                startActivity (intent3);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;

                            case 5:
                                Intent intent5 = new Intent (MainActivity.this, FeedbackActivity.class);
                                startActivity (intent5);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;

                            case 7:
                                showLogOutDialog ();
                                break;
                            case 8:
                            */
    
                        }
                        return false;
                    }
                })
                .build ();
//        result.getActionBarDrawerToggle ().setDrawerIndicatorEnabled (false);
    }
    
    private void initApplication () {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager ().getPackageInfo (getPackageName (), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace ();
        }
        if (NetworkConnection.isNetworkAvailable (this)) {
            Utils.showLog (Log.INFO, AppConfigTags.URL, AppConfigURL.URL_INIT, true);
            final PackageInfo finalPInfo = pInfo;
            StringRequest strRequest = new StringRequest (Request.Method.POST, AppConfigURL.URL_INIT,
                    new Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    final JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        if (jsonObj.getBoolean (AppConfigTags.VERSION_UPDATE)) {
                                            if (jsonObj.getInt (AppConfigTags.VERSION_ACTIVE) > 0) {
                                                if (jsonObj.getInt (AppConfigTags.VERSION_CRITICAL) > 0) {
                                                    new MaterialDialog.Builder (MainActivity.this)
                                                            .content (R.string.dialog_text_new_version_available)
                                                            .positiveColor (getResources ().getColor (R.color.accent))
                                                            .contentColor (getResources ().getColor (R.color.primary_text))
                                                            .negativeColor (getResources ().getColor (R.color.accent))
                                                            .typeface (SetTypeFace.getTypeface (MainActivity.this), SetTypeFace.getTypeface (MainActivity.this))
                                                            .canceledOnTouchOutside (false)
                                                            .cancelable (false)
                                                            .autoDismiss (false)
                                                            .positiveText (R.string.dialog_action_update)
                                                            .onPositive (new MaterialDialog.SingleButtonCallback () {
                                                                @Override
                                                                public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                    final String appPackageName = getPackageName ();
                                                                    try {
                                                                        startActivity (new Intent (Intent.ACTION_VIEW, Uri.parse ("market://details?id=" + appPackageName)));
                                                                    } catch (android.content.ActivityNotFoundException anfe) {
                                                                        startActivity (new Intent (Intent.ACTION_VIEW, Uri.parse ("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                                    }
                                                                }
                                                            }).show ();
                                                } else {
                                                    new MaterialDialog.Builder (MainActivity.this)
                                                            .content (R.string.dialog_text_new_version_available)
                                                            .positiveColor (getResources ().getColor (R.color.accent))
                                                            .contentColor (getResources ().getColor (R.color.primary_text))
                                                            .negativeColor (getResources ().getColor (R.color.accent))
                                                            .typeface (SetTypeFace.getTypeface (MainActivity.this), SetTypeFace.getTypeface (MainActivity.this))
                                                            .canceledOnTouchOutside (false)
                                                            .cancelable (false)
                                                            .autoDismiss (false)
                                                            .positiveText (R.string.dialog_action_update)
                                                            .negativeText (R.string.dialog_action_ignore)
                                                            .onPositive (new MaterialDialog.SingleButtonCallback () {
                                                                @Override
                                                                public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                    dialog.dismiss ();
                                                                    final String appPackageName = getPackageName ();
                                                                    try {
                                                                        startActivity (new Intent (Intent.ACTION_VIEW, Uri.parse ("market://details?id=" + appPackageName)));
                                                                    } catch (android.content.ActivityNotFoundException anfe) {
                                                                        startActivity (new Intent (Intent.ACTION_VIEW, Uri.parse ("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                                    }
                                                                }
                                                            })
                                                            .onNegative (new MaterialDialog.SingleButtonCallback () {
                                                                @Override
                                                                public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                    dialog.dismiss ();
                                                                }
                                                            }).show ();
                                                }
                                            }
                                        }
                                        if (jsonObj.getBoolean (AppConfigTags.SUBSCRIPTION_EXPIRED)) {
                                            new MaterialDialog.Builder (MainActivity.this)
                                                    .content (R.string.dialog_text_subscription_expired)
                                                    .positiveColor (getResources ().getColor (R.color.accent))
                                                    .contentColor (getResources ().getColor (R.color.primary_text))
                                                    .typeface (SetTypeFace.getTypeface (MainActivity.this), SetTypeFace.getTypeface (MainActivity.this))
                                                    .canceledOnTouchOutside (false)
                                                    .cancelable (false)
                                                    .positiveText (R.string.dialog_action_ok)
                                                    .onPositive (new MaterialDialog.SingleButtonCallback () {
                                                        @Override
                                                        public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_NAME, "");
                                                            userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_MOBILE, "");
                                                            userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_EMAIL, "");
                                                            userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_TYPE, "");
                                                            userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_LOGIN_KEY, "");
                                                            userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_PARTY_ID, "");
                                                            userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_PARTY_NAME, "");
                                                            userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_COMPANY_ID, "");
                                                            userDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_COMPANY_NAME, "");
                                                            Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                                                            intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity (intent);
                                                            overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                                                        }
                                                    })
                                                    .show ();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                        }
                    },
                    new Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                        }
                    }) {
                
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<> ();
                    params.put ("app_version", String.valueOf (finalPInfo.versionCode));
                    params.put ("device", "ANDROID");
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    params.put (AppConfigTags.HEADER_USER_LOGIN_KEY, userDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.USER_LOGIN_KEY));
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            strRequest.setRetryPolicy (new DefaultRetryPolicy (DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Utils.sendRequest (strRequest, 30);
        } else {
//            initApplication ();
        }
    }
}
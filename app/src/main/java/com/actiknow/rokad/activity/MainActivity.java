package com.actiknow.rokad.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actiknow.rokad.R;
import com.actiknow.rokad.utils.SetTypeFace;
import com.actiknow.rokad.utils.UserDetailsPref;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    TextView tvTruckEntry;
    TextView tvReceiving;
    TextView tvRokad;
    TextView tvLogout;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    ImageView ivNavigation;
    UserDetailsPref userDetailsPref;
    Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.savedInstanceState = savedInstanceState;
        initView();
        initData();
        initListener();
        initDrawer();
        isLogin();
    }

    private void isLogin() {
        if (userDetailsPref.getStringPref(MainActivity.this, UserDetailsPref.USER_LOGIN_KEY) == "") {
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
            finish();
        }
    }

    private void initListener() {
        ivNavigation.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                result.openDrawer ();
            }
        });



        tvTruckEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTruckEntry = new Intent(MainActivity.this, TruckEntryActivity.class);
                startActivity(intentTruckEntry);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        tvReceiving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTruckEntry = new Intent(MainActivity.this, ReceivingActivity.class);
                startActivity(intentTruckEntry);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        tvRokad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogOutDialog();
            }
        });
    }

    private void initData() {
        userDetailsPref = UserDetailsPref.getInstance();
    }

    private void initView() {
        tvTruckEntry = (TextView) findViewById(R.id.tvTruckEntry);
        tvReceiving = (TextView) findViewById(R.id.tvReceiving);
        tvRokad = (TextView) findViewById(R.id.tvRokad);
        tvLogout = (TextView) findViewById(R.id.tvLogout);
        ivNavigation = (ImageView) findViewById(R.id.ivNavigation);
    }

    private void showLogOutDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .limitIconToDefaultSize()
                .content("Do you wish to Sign Out?")
                .positiveText("Yes")
                .negativeText("No")
                .typeface(SetTypeFace.getTypeface(MainActivity.this), SetTypeFace.getTypeface(MainActivity.this))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        userDetailsPref.putStringPref(MainActivity.this, UserDetailsPref.USER_MOBILE, "");
                        userDetailsPref.putStringPref(MainActivity.this, UserDetailsPref.USER_LOGIN_KEY, "");
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }).build();
        dialog.show();
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

        DrawerImageLoader.init (new AbstractDrawerImageLoader() {
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
                    return new IconicsDrawable(ctx).iconText (" ").backgroundColorRes (com.mikepenz.materialdrawer.R.color.colorPrimary).sizeDp (56);
                } else if ("customUrlItem".equals (tag)) {
                    return new IconicsDrawable (ctx).iconText (" ").backgroundColorRes (R.color.md_white_1000);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder (ctx, tag);
            }
        });



        headerResult = new AccountHeaderBuilder()
                .withActivity (this)
                .withCompactStyle (false)
                .withTypeface (SetTypeFace.getTypeface (MainActivity.this))
                .withTypeface (SetTypeFace.getTypeface (this))
                .withPaddingBelowHeader (false)
                .withSelectionListEnabled (false)
                .withSelectionListEnabledForSingleProfile (false)
                .withProfileImagesVisible (false)
                .withOnlyMainProfileImageVisible (true)
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
        headerResult.addProfiles (new ProfileDrawerItem()
                .withIcon (R.drawable.doctor)
                .withName ("Rahul jain")
                .withEmail ("Rahul.jain@actiknowbi.com"));

      /*   .withIcon (buyerDetailsPref.getStringPref (MainActivity.this, BuyerDetailsPref.BUYER_IMAGE))
                .withName (buyerDetailsPref.getStringPref (MainActivity.this, BuyerDetailsPref.BUYER_NAME))
                .withEmail (buyerDetailsPref.getStringPref (MainActivity.this, BuyerDetailsPref.BUYER_EMAIL)));*/


        result = new DrawerBuilder()
                .withActivity (this)
                .withAccountHeader (headerResult)
//                .withToolbar (toolbar)
//                .withItemAnimator (new AlphaCrossFadeAnimator ())
                .addDrawerItems (
                        new PrimaryDrawerItem().withName ("My Home").withIcon (FontAwesome.Icon.faw_home).withIdentifier (1).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Search job").withIcon (FontAwesome.Icon.faw_heart).withIdentifier (2).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Recommended Job").withIcon (FontAwesome.Icon.faw_handshake_o).withIdentifier (3).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Bookmarked Job").withIcon (FontAwesome.Icon.faw_info).withIdentifier (4).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Feedback").withIcon (FontAwesome.Icon.faw_comments).withIdentifier (5).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Promote this app").withIcon (FontAwesome.Icon.faw_phone).withIdentifier (6).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Sign Out").withIcon (FontAwesome.Icon.faw_sign_out).withIdentifier (7).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this))
                       /* new PrimaryDrawerItem ().withName ("FAQ").withIcon (FontAwesome.Icon.faw_question).withIdentifier (7).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("My Profile").withIcon (FontAwesome.Icon.faw_user).withIdentifier (8).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Change Password").withIcon (FontAwesome.Icon.faw_key).withIdentifier (9).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Sign Out").withIcon (FontAwesome.Icon.faw_sign_out).withIdentifier (10).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this))*/
                )
                .withSavedInstance (savedInstanceState)
                .withOnDrawerItemClickListener (new Drawer.OnDrawerItemClickListener () {
                    @Override
                    public boolean onItemClick (View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier ()) {
                          /*  case 2:
                                Intent intent2 = new Intent (MainActivity.this, SearchJobActivity.class);
                                startActivity (intent2);
                                overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case 3:
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
}

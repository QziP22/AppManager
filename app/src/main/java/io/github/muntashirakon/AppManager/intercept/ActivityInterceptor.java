/*
 * Copyright (C) 2020 Muntashir Al-Islam
 * Copyright (C) 2012-2014 Intrications
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.muntashirakon.AppManager.intercept;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.collection.SparseArrayCompat;
import androidx.core.util.Pair;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import io.github.muntashirakon.AppManager.R;
import io.github.muntashirakon.AppManager.types.IconLoaderThread;

public class ActivityInterceptor extends AppCompatActivity {
    private static final String INTENT_EDITED = "intent_edited";
    private static final String NEWLINE = "\n<br>";
    private static final String BLANK = " ";

    private static final SparseArrayCompat<String> INTENT_FLAG_TO_STRING = new SparseArrayCompat<String>() {
        {
            put(Intent.FLAG_GRANT_READ_URI_PERMISSION, "FLAG_GRANT_READ_URI_PERMISSION");
            put(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, "FLAG_GRANT_WRITE_URI_PERMISSION");
            put(Intent.FLAG_FROM_BACKGROUND, "FLAG_FROM_BACKGROUND");
            put(Intent.FLAG_DEBUG_LOG_RESOLUTION, "FLAG_DEBUG_LOG_RESOLUTION");
            put(Intent.FLAG_EXCLUDE_STOPPED_PACKAGES, "FLAG_EXCLUDE_STOPPED_PACKAGES");
            put(Intent.FLAG_INCLUDE_STOPPED_PACKAGES, "FLAG_INCLUDE_STOPPED_PACKAGES");
            put(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION, "FLAG_GRANT_PERSISTABLE_URI_PERMISSION");
            put(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION, "FLAG_GRANT_PREFIX_URI_PERMISSION");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(Intent.FLAG_DIRECT_BOOT_AUTO, "FLAG_DIRECT_BOOT_AUTO");
            }
            put(0x00000200, "FLAG_IGNORE_EPHEMERAL");
            put(Intent.FLAG_ACTIVITY_NO_HISTORY, "FLAG_ACTIVITY_NO_HISTORY");
            put(Intent.FLAG_ACTIVITY_SINGLE_TOP, "FLAG_ACTIVITY_SINGLE_TOP");
            put(Intent.FLAG_ACTIVITY_NEW_TASK, "FLAG_ACTIVITY_NEW_TASK");
            put(Intent.FLAG_ACTIVITY_MULTIPLE_TASK, "FLAG_ACTIVITY_MULTIPLE_TASK");
            put(Intent.FLAG_ACTIVITY_CLEAR_TOP, "FLAG_ACTIVITY_CLEAR_TOP");
            put(Intent.FLAG_ACTIVITY_FORWARD_RESULT, "FLAG_ACTIVITY_FORWARD_RESULT");
            put(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP, "FLAG_ACTIVITY_PREVIOUS_IS_TOP");
            put(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS, "FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS");
            put(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT, "FLAG_ACTIVITY_BROUGHT_TO_FRONT");
            put(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED, "FLAG_ACTIVITY_RESET_TASK_IF_NEEDED");
            put(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY, "FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY");
            put(Intent.FLAG_ACTIVITY_NEW_DOCUMENT, "FLAG_ACTIVITY_NEW_DOCUMENT");
            put(Intent.FLAG_ACTIVITY_NO_USER_ACTION, "FLAG_ACTIVITY_NO_USER_ACTION");
            put(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT, "FLAG_ACTIVITY_REORDER_TO_FRONT");
            put(Intent.FLAG_ACTIVITY_NO_ANIMATION, "FLAG_ACTIVITY_NO_ANIMATION");
            put(Intent.FLAG_ACTIVITY_CLEAR_TASK, "FLAG_ACTIVITY_CLEAR_TASK");
            put(Intent.FLAG_ACTIVITY_TASK_ON_HOME, "FLAG_ACTIVITY_TASK_ON_HOME");
            put(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS, "FLAG_ACTIVITY_RETAIN_IN_RECENTS");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                put(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT, "FLAG_ACTIVITY_LAUNCH_ADJACENT");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                put(Intent.FLAG_ACTIVITY_MATCH_EXTERNAL, "FLAG_ACTIVITY_MATCH_EXTERNAL");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                put(Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER, "FLAG_ACTIVITY_REQUIRE_NON_BROWSER");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                put(Intent.FLAG_ACTIVITY_REQUIRE_DEFAULT, "FLAG_ACTIVITY_REQUIRE_DEFAULT");
            }
            put(Intent.FLAG_RECEIVER_REGISTERED_ONLY, "FLAG_RECEIVER_REGISTERED_ONLY");
            put(Intent.FLAG_RECEIVER_REPLACE_PENDING, "FLAG_RECEIVER_REPLACE_PENDING");
            put(Intent.FLAG_RECEIVER_FOREGROUND, "FLAG_RECEIVER_FOREGROUND");
            put(0x80000000, "FLAG_RECEIVER_OFFLOAD");
            put(Intent.FLAG_RECEIVER_NO_ABORT, "FLAG_RECEIVER_NO_ABORT");
            put(0x04000000, "FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT");
            put(0x02000000, "FLAG_RECEIVER_BOOT_UPGRADE");
            put(0x01000000, "FLAG_RECEIVER_INCLUDE_BACKGROUND");
            put(0x00800000, "FLAG_RECEIVER_EXCLUDE_BACKGROUND");
            put(0x00400000, "FLAG_RECEIVER_FROM_SHELL");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                put(Intent.FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS, "FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS");
            }
        }
    };

    private static final String NEWSEGMENT = NEWLINE + "------------" + NEWLINE;

    private static final String BOLD_START = "<b><u>";
    private static final String BOLD_END_BLANK = "</u></b>" + BLANK;
    private static final String BOLD_END_NL = "</u></b>" + NEWLINE;

    private abstract class IntentUpdateTextWatcher implements TextWatcher {
        private final TextView textView;

        IntentUpdateTextWatcher(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (areTextWatchersActive) {
                try {
                    String modifiedContent = textView.getText().toString();
                    onUpdateIntent(modifiedContent);
                    showTextViewIntentData(textView);
                    showResetIntentButton(true);
                    refreshUI();
                } catch (Exception e) {
                    Toast.makeText(ActivityInterceptor.this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }

        abstract protected void onUpdateIntent(String modifiedContent);

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private ShareActionProvider shareActionProvider;

    private MaterialAutoCompleteTextView action;
    private MaterialAutoCompleteTextView data;
    private MaterialAutoCompleteTextView type;
    private MaterialAutoCompleteTextView uri;

    private HistoryEditText mHistory = null;

    private TextView categoriesHeader;
    private CategoriesRecyclerViewAdapter categoriesAdapter;
    private CategoriesRecyclerViewAdapter flagsAdapter;
    private ExtrasRecyclerViewAdapter extrasAdapter;
    private TextView activitiesHeader;
    private MatchingActivitiesRecyclerViewAdapter matchingActivitiesAdapter;
    private Button resendIntentButton;
    private Button resetIntentButton;

    /**
     * String representation of intent as URI
     */
    private String originalIntent;

    /**
     * Extras that are lost during intent to string conversion
     */
    private Bundle additionalExtras;

    private Intent mutableIntent;

    private Integer lastResultCode = null;
    private Intent lastResultIntent = null;

    private boolean areTextWatchersActive;

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                Intent data = result.getData();
                this.lastResultCode = result.getResultCode();
                this.lastResultIntent = result.getData();

                // Forward the result of the activity to the caller activity
                setResult(result.getResultCode(), data);

                refreshUI();
                Uri uri = data == null ? null : data.getData();
                Toast.makeText(ActivityInterceptor.this,
                        String.format("%s: (%s)", getString(R.string.activity_result), uri),
                        Toast.LENGTH_LONG).show();
            });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interceptor);
        setSupportActionBar(findViewById(R.id.toolbar));
        findViewById(R.id.progress_linear).setVisibility(View.GONE);

        rememberIntent(getIntent());

        final boolean isVisible = savedInstanceState != null && savedInstanceState.getBoolean(INTENT_EDITED);
        showInitialIntent(isVisible);

        if (mHistory != null) mHistory.saveHistory();
    }

    private void rememberIntent(Intent original) {
        this.originalIntent = getUri(original);

        Intent copy = cloneIntent(this.originalIntent);

        final Bundle originalExtras = original.getExtras();

        if (originalExtras != null) {
            // Collect extras that are lost in the intent to string conversion
            Bundle additionalExtrasBundle = new Bundle(originalExtras);
            for (String key : originalExtras.keySet()) {
                if (copy.hasExtra(key)) {
                    additionalExtrasBundle.remove(key);
                }
            }

            if (!additionalExtrasBundle.isEmpty()) {
                additionalExtras = additionalExtrasBundle;
            }
        }
    }

    /**
     * creates a clone of originalIntent and displays it for editing
     */
    private void showInitialIntent(boolean isVisible) {
        mutableIntent = cloneIntent(this.originalIntent);

        mutableIntent.setComponent(null);

        setupVariables();

        setupTextWatchers();

        showAllIntentData(null);

        showResetIntentButton(isVisible);
    }

    /**
     * textViewToIgnore is not updated so current selected char in that textview will not change
     */
    private void showAllIntentData(TextView textViewToIgnore) {
        showTextViewIntentData(textViewToIgnore);

        // Display categories
        Set<String> categories = mutableIntent.getCategories();
        if (categories != null) {
            categoriesHeader.setVisibility(View.VISIBLE);
        } else {
            categoriesHeader.setVisibility(View.GONE);
        }
        categoriesAdapter.setDefaultList(categories);

        // Display flags
        ArrayList<String> flagsStrings = getFlags();
        flagsAdapter.setDefaultList(flagsStrings);

        // Display extras
        List<Pair<String, Object>> extras = new ArrayList<>();
        Bundle intentBundle = mutableIntent.getExtras();
        if (intentBundle != null) {
            Set<String> extraKeys = intentBundle.keySet();
            for (String extraKey : extraKeys) {
                Object extraValue = intentBundle.get(extraKey);
                if (extraValue == null) continue;
                extras.add(new Pair<>(extraKey, extraValue));
            }
        }
        extrasAdapter.setDefaultList(extras);
        refreshUI();
    }

    /**
     * textViewToIgnore is not updated so current selected char in that textview will not change
     */
    private void showTextViewIntentData(TextView textViewToIgnore) {
        areTextWatchersActive = false;
        if (textViewToIgnore != action) action.setText(mutableIntent.getAction());
        if ((textViewToIgnore != data) && (mutableIntent.getDataString() != null)) {
            data.setText(mutableIntent.getDataString());
        }
        if (textViewToIgnore != type) type.setText(mutableIntent.getType());
        if (textViewToIgnore != uri) uri.setText(getUri(mutableIntent));
        areTextWatchersActive = true;
    }

    @NonNull
    private ArrayList<String> getFlags() {
        ArrayList<String> flagsStrings = new ArrayList<>();
        int flags = mutableIntent.getFlags();
        for (int i = 0; i < INTENT_FLAG_TO_STRING.size(); ++i) {
            if ((flags & INTENT_FLAG_TO_STRING.keyAt(i)) != 0) {
                flagsStrings.add(INTENT_FLAG_TO_STRING.valueAt(i));
            }
        }
        return flagsStrings;
    }

    private void checkAndShowMatchingActivities() {
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentActivities(mutableIntent, 0);
        if (resolveInfo.size() < 1) {
            resendIntentButton.setEnabled(false);
            activitiesHeader.setVisibility(View.GONE);
        } else {
            resendIntentButton.setEnabled(true);
            activitiesHeader.setVisibility(View.VISIBLE);
        }
        activitiesHeader.setText(getString(R.string.matching_activities));
        matchingActivitiesAdapter.setDefaultList(resolveInfo);
    }

    private void setupVariables() {
        action = findViewById(R.id.action_edit);
        data = findViewById(R.id.data_edit);
        type = findViewById(R.id.type_edit);
        uri = findViewById(R.id.uri_edit);

        mHistory = new HistoryEditText(this, action, data, type, uri);

        // Setup categories
        categoriesHeader = findViewById(R.id.intent_categories_header);
        RecyclerView categoriesRecyclerView = findViewById(R.id.intent_categories);
        categoriesRecyclerView.setHasFixedSize(true);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoriesAdapter = new CategoriesRecyclerViewAdapter();
        categoriesRecyclerView.setAdapter(categoriesAdapter);

        // Setup flags
        RecyclerView flagsRecyclerView = findViewById(R.id.intent_flags);
        flagsRecyclerView.setHasFixedSize(true);
        flagsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        flagsAdapter = new CategoriesRecyclerViewAdapter();
        flagsRecyclerView.setAdapter(flagsAdapter);

        // Setup extras
        RecyclerView extrasRecyclerView = findViewById(R.id.intent_extras);
        extrasRecyclerView.setHasFixedSize(true);
        extrasRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        extrasAdapter = new ExtrasRecyclerViewAdapter();
        extrasRecyclerView.setAdapter(extrasAdapter);

        // Setup matching activities
        activitiesHeader = findViewById(R.id.intent_matching_activities_header);
        RecyclerView matchingActivitiesRecyclerView = findViewById(R.id.intent_matching_activities);
        matchingActivitiesRecyclerView.setHasFixedSize(true);
        matchingActivitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        matchingActivitiesAdapter = new MatchingActivitiesRecyclerViewAdapter(this);
        matchingActivitiesRecyclerView.setAdapter(matchingActivitiesAdapter);

        resendIntentButton = findViewById(R.id.resend_intent_button);
        resetIntentButton = findViewById(R.id.reset_intent_button);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        // Send Intent on clicking the resend intent button
        resendIntentButton.setOnClickListener(v -> launcher.launch(Intent
                .createChooser(mutableIntent, resendIntentButton.getText())));
        // Reset Intent data on clicking the reset intent button
        resetIntentButton.setOnClickListener(v -> {
            areTextWatchersActive = false;
            showInitialIntent(false);
            areTextWatchersActive = true;
            refreshUI();
        });
    }

    private void setupTextWatchers() {
        action.addTextChangedListener(new IntentUpdateTextWatcher(action) {
            @Override
            protected void onUpdateIntent(String modifiedContent) {
                mutableIntent.setAction(modifiedContent);
            }
        });
        data.addTextChangedListener(new IntentUpdateTextWatcher(data) {
            @Override
            protected void onUpdateIntent(String modifiedContent) {
                // setData clears type so we save it
                String savedType = mutableIntent.getType();
                mutableIntent.setDataAndType(Uri.parse(modifiedContent), savedType);
            }
        });
        type.addTextChangedListener(new IntentUpdateTextWatcher(type) {
            @Override
            protected void onUpdateIntent(String modifiedContent) {
                // setData clears type so we save it
                String dataString = mutableIntent.getDataString();
                mutableIntent.setDataAndType(Uri.parse(dataString), modifiedContent);
            }
        });
        uri.addTextChangedListener(new IntentUpdateTextWatcher(uri) {
            @Override
            protected void onUpdateIntent(String modifiedContent) {
                // no error yet so continue
                mutableIntent = cloneIntent(modifiedContent);
                // this time must update all content since extras/flags may have been changed
                showAllIntentData(uri);
            }
        });
    }

    private void showResetIntentButton(boolean visible) {
        resendIntentButton.setText(R.string.send_edited_intent);
        resetIntentButton.setVisibility((visible) ? View.VISIBLE : View.GONE);
    }

    private void copyIntentDetails() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("Intent Details", getIntentDetailsString()));
    }

    private void refreshUI() {
        checkAndShowMatchingActivities();
        if (shareActionProvider != null) {
            Intent share = createShareIntent();
            shareActionProvider.setShareIntent(share);
        }
    }

    @NonNull
    private Intent createShareIntent() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, getIntentDetailsString());
        return share;
    }

    private Spanned getIntentDetailsString() {
        StringBuilder result = new StringBuilder();

        // k3b so intent can be reloaded using
        // Intent.parseUri("Intent:....", Intent.URI_INTENT_SCHEME)
        result.append(getUri(mutableIntent)).append(NEWSEGMENT);

        appendIntentDetails(result, mutableIntent, true).append(NEWSEGMENT);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentActivities(mutableIntent, 0);

        // Remove Intent Intercept from matching activities
        int numberOfMatchingActivities = resolveInfo.size() - 1;

        appendHeader(result, R.string.matching_activities);
        if (numberOfMatchingActivities < 1) {
            appendHeader(result, R.string.no_content);
        } else {
            for (int i = 0; i <= numberOfMatchingActivities; i++) {
                ResolveInfo info = resolveInfo.get(i);
                ActivityInfo activityinfo = info.activityInfo;
                if (!activityinfo.packageName.equals(getPackageName())) {
                    result.append(BOLD_START).append(activityinfo.loadLabel(pm))
                            .append(BOLD_END_BLANK).append(" (")
                            .append(activityinfo.packageName)
                            .append(" - ")
                            .append(activityinfo.name)
                            .append(")").append(NEWLINE);
                }
            }
        }

        // Add activity results
        if (this.lastResultCode != null) {
            result.append(NEWSEGMENT);
            appendHeader(result, R.string.activity_result);
            appendNameValue(result, R.string.result_code, this.lastResultCode);

            if (this.lastResultIntent != null) {
                appendIntentDetails(result, lastResultIntent, false);
            }
        }

        return Html.fromHtml(result.toString());
    }

    private StringBuilder appendIntentDetails(StringBuilder result, Intent intent, boolean detailed) {
        if (detailed) appendNameValue(result, R.string.action, intent.getAction());

        appendNameValue(result, R.string.data, intent.getData());
        appendNameValue(result, R.string.mime_type, intent.getType());
        appendNameValue(result, R.string.uri, getUri(intent));

        Set<String> categories = intent.getCategories();
        if ((categories != null) && (categories.size() > 0)) {
            appendHeader(result, R.string.category);
            for (String category : categories) {
                result.append(category).append(NEWLINE);
            }
        }

        if (detailed) {
            appendHeader(result, R.string.flags);
            ArrayList<String> flagsStrings = getFlags();
            if (!flagsStrings.isEmpty()) {
                for (String thisFlagString : flagsStrings) {
                    result.append(thisFlagString).append(NEWLINE);
                }
            } else {
                result.append(getString(R.string.none)).append(NEWLINE);
            }
        }

        try {
            Bundle intentBundle = intent.getExtras();
            if (intentBundle != null) {
                Set<String> keySet = intentBundle.keySet();
                appendHeader(result, R.string.extras);
                int count = 0;

                for (String key : keySet) {
                    count++;
                    Object thisObject = intentBundle.get(key);
                    if (thisObject == null) continue;
                    result.append(BOLD_START).append(count).append(BOLD_END_BLANK);
                    String thisClass = thisObject.getClass().getName();
                    result.append(getString(R.string.class_name)).append(BLANK)
                            .append(thisClass).append(NEWLINE);
                    result.append(getString(R.string.key_name)).append(BLANK)
                            .append(key).append(NEWLINE);

                    if (thisObject instanceof String || thisObject instanceof Long
                            || thisObject instanceof Integer
                            || thisObject instanceof Boolean
                            || thisObject instanceof Uri) {
                        result.append(getString(R.string.value)).append(BLANK)
                                .append(thisObject.toString())
                                .append(NEWLINE);
                    } else if (thisObject instanceof ArrayList) {
                        result.append(getString(R.string.value)).append(NEWLINE);
                        ArrayList<?> thisArrayList = (ArrayList<?>) thisObject;
                        for (Object thisArrayListObject : thisArrayList) {
                            result.append(thisArrayListObject.toString()).append(NEWLINE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            appendHeader(result, R.string.extras);
            result.append("<font color='red'>").append(getString(R.string.error)).append("</font>").append(NEWLINE);
            e.printStackTrace();
        }
        return result;
    }

    private void appendNameValue(StringBuilder result, int keyId, Object value) {
        if (value != null) {
            result.append(BOLD_START).append(getString(keyId)).append(BOLD_END_BLANK)
                    .append(value).append(NEWLINE);
        }
    }

    private void appendHeader(@NonNull StringBuilder result, int keyId) {
        result.append(BOLD_START).append(getString(keyId)).append(BOLD_END_NL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_activity_interceptor_actions, menu);
        MenuItem actionItem = menu.findItem(R.id.action_share);

        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(actionItem);

        if (shareActionProvider == null) {
            shareActionProvider = new ShareActionProvider(this);
            MenuItemCompat.setActionProvider(actionItem, shareActionProvider);
        }

        shareActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        refreshUI();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_copy) {
            copyIntentDetails();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        areTextWatchersActive = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // inhibit new activity animation when resetting intent details
        overridePendingTransition(0, 0);
        areTextWatchersActive = true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(INTENT_EDITED,
                resetIntentButton.getVisibility() == View.VISIBLE);
        if (mHistory != null) mHistory.saveHistory();
    }

    private static String getUri(Intent src) {
        return (src != null) ? src.toUri(Intent.URI_INTENT_SCHEME) : null;
    }

    private Intent cloneIntent(String intentUri) {
        if (intentUri != null) {
            try {
                Intent clone = Intent.parseUri(intentUri, Intent.URI_INTENT_SCHEME);

                // bugfix #14: restore extras that are lost in the intent <-> string conversion
                if (additionalExtras != null) {
                    clone.putExtras(additionalExtras);
                }

                return clone;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static class CategoriesRecyclerViewAdapter extends RecyclerView.Adapter<CategoriesRecyclerViewAdapter.ViewHolder> {
        private final List<String> categories = new ArrayList<>();

        public void setDefaultList(@Nullable Collection<String> categories) {
            this.categories.clear();
            if (categories != null) this.categories.addAll(categories);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TextView tv = (TextView) holder.itemView;
            tv.setText(categories.get(position));
            tv.setTextIsSelectable(true);
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

    private static class ExtrasRecyclerViewAdapter extends RecyclerView.Adapter<ExtrasRecyclerViewAdapter.ViewHolder> {
        private final List<Pair<String, Object>> matchingActivities = new ArrayList<>();

        public void setDefaultList(@Nullable List<Pair<String, Object>> matchingActivities) {
            this.matchingActivities.clear();
            if (matchingActivities != null) this.matchingActivities.addAll(matchingActivities);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_icon_title_subtitle, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (holder.iconLoader != null) holder.iconLoader.interrupt();
            Pair<String, Object> extraItem = matchingActivities.get(position);
            holder.title.setText(extraItem.first);
            holder.title.setTextIsSelectable(true);
            holder.subtitle.setText(extraItem.second.toString());
            holder.subtitle.setTextIsSelectable(true);
        }

        @Override
        public int getItemCount() {
            return matchingActivities.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView subtitle;
            ImageView icon;
            ImageView actionIcon;
            IconLoaderThread iconLoader;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.item_title);
                subtitle = itemView.findViewById(R.id.item_subtitle);
                actionIcon = itemView.findViewById(R.id.item_open);
                icon = itemView.findViewById(R.id.item_icon);
                actionIcon.setVisibility(View.GONE);
                icon.setVisibility(View.GONE);
            }
        }
    }

    private static class MatchingActivitiesRecyclerViewAdapter extends RecyclerView.Adapter<MatchingActivitiesRecyclerViewAdapter.ViewHolder> {
        private final List<ResolveInfo> matchingActivities = new ArrayList<>();
        private final PackageManager pm;
        private final ActivityInterceptor activity;

        public MatchingActivitiesRecyclerViewAdapter(ActivityInterceptor activity) {
            this.activity = activity;
            pm = activity.getPackageManager();
        }

        public void setDefaultList(@Nullable List<ResolveInfo> matchingActivities) {
            this.matchingActivities.clear();
            if (matchingActivities != null) this.matchingActivities.addAll(matchingActivities);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_icon_title_subtitle, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (holder.iconLoader != null) holder.iconLoader.interrupt();
            ResolveInfo resolveInfo = matchingActivities.get(position);
            ActivityInfo info = resolveInfo.activityInfo;
            holder.title.setText(info.loadLabel(pm));
            String activityName = info.targetActivity != null ? info.targetActivity : info.name;
            String name = info.packageName + "\n" + activityName;
            holder.subtitle.setText(name);
            holder.subtitle.setTextIsSelectable(true);
            holder.iconLoader = new IconLoaderThread(holder.icon, info);
            holder.iconLoader.start();
            holder.actionIcon.setOnClickListener(v -> {
                Intent intent = new Intent(activity.mutableIntent);
                intent.setClassName(info.packageName, activityName);
                activity.launcher.launch(intent);
            });
        }

        @Override
        public int getItemCount() {
            return matchingActivities.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView subtitle;
            ImageView icon;
            ImageView actionIcon;
            IconLoaderThread iconLoader;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.item_title);
                subtitle = itemView.findViewById(R.id.item_subtitle);
                actionIcon = itemView.findViewById(R.id.item_open);
                icon = itemView.findViewById(R.id.item_icon);
            }
        }
    }
}
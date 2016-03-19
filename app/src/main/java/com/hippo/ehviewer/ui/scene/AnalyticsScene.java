/*
 * Copyright 2016 Hippo Seven
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hippo.ehviewer.ui.scene;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hippo.ehviewer.Analytics;
import com.hippo.ehviewer.R;
import com.hippo.ehviewer.Settings;
import com.hippo.ehviewer.ui.MainActivity;
import com.hippo.ehviewer.ui.annotation.ViewLifeCircle;
import com.hippo.rippleold.RippleSalon;
import com.hippo.text.Html;
import com.hippo.text.LinkMovementMethod2;
import com.hippo.yorozuya.ViewUtils;

public class AnalyticsScene extends BaseScene implements View.OnClickListener {

    @Nullable
    @ViewLifeCircle
    private View mCancel;
    @Nullable
    @ViewLifeCircle
    private View mOk;

    @Override
    public boolean needShowLeftDrawer() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scene_analytics, container, false);

        mCancel = ViewUtils.$$(view, R.id.cancel);
        mOk = ViewUtils.$$(view, R.id.ok);
        TextView text = (TextView) ViewUtils.$$(view, R.id.text);

        text.setText(Html.fromHtml(getString(R.string.analytics_explain)));
        text.setMovementMethod(LinkMovementMethod2.getInstance());

        mCancel.setOnClickListener(this);
        mOk.setOnClickListener(this);

        RippleSalon.addRipple(mCancel, true);
        RippleSalon.addRipple(mOk, true);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mCancel = null;
        mOk = null;
    }

    @Override
    public void onClick(View v) {
        Context context = getContext2();
        if (null == context) {
            return;
        }

        if (mCancel == v) {
            Settings.putEnableAnalytics(false);
        } else if (mOk == v) {
            Settings.putEnableAnalytics(true);
            // Start Analytics
            Analytics.start(context);
        }
        Settings.putAskAnalytics(false);

        // Start new scene and finish it self
        MainActivity activity = getActivity2();
        if (null != activity) {
            activity.startSceneForCheckStep(MainActivity.CHECK_STEP_ANALYTICS, getArguments());
        }
        finish();
    }
}

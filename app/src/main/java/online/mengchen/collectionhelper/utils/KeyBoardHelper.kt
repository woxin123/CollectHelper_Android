package online.mengchen.collectionhelper.utils

import com.qmuiteam.qmui.util.QMUIActivityLifecycleCallbacks
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIViewHelper
import kotlin.math.roundToInt

/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */



import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author cginechen
 * @date 2016-11-07
 * <p>
 * https://github.com/yshrsmz/KeyboardVisibilityEvent/blob/master/keyboardvisibilityevent/src/main/java/net/yslibrary/android/keyboardvisibilityevent/KeyboardVisibilityEvent.java
 */

class KeyBoardHelper {
    /**
     * 显示软键盘的延迟时间
     */

    companion object {
        const val SHOW_KEYBOARD_DELAY_TIME = 200
        const val TAG = "KeyBoardHelper"
        const val KEYBOARD_VISIBLE_THRESHOLD_DP = 60
        fun setVisibilityEventListener(
            activity: Activity,
            keyBoardListener: (isOpen: Boolean, heightDiff: Int) -> Boolean
        ) {
            val activityRoot = QMUIViewHelper.getActivityRoot(activity)
            val layoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {

                private val r = Rect()
                val visibleThreshold: Int = QMUIDisplayHelper.dp2px(
                    activity,
                    KEYBOARD_VISIBLE_THRESHOLD_DP
                ).toDouble().roundToInt()
                private var wasOpened = false

                override fun onGlobalLayout() {
                    activityRoot.getWindowVisibleDisplayFrame(r)

                    val heightDiff: Int = activityRoot.rootView.height - 80

                    val isOpen = heightDiff > visibleThreshold;

                    if (isOpen == wasOpened) {
                        // keyboard state has not changed
                        return;
                    }

                    wasOpened = isOpen

                    val removeListener = keyBoardListener.invoke(isOpen, heightDiff)
                    if (removeListener) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            activityRoot.viewTreeObserver
                                .removeOnGlobalLayoutListener(this);
                        } else {
                            activityRoot.viewTreeObserver
                                .removeGlobalOnLayoutListener(this);
                        }
                    }
                }
            }
            activityRoot.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
            activity.application.registerActivityLifecycleCallbacks(object :
                    QMUIActivityLifecycleCallbacks(activity) {
                    override fun onTargetActivityDestroyed() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            activityRoot.viewTreeObserver
                                .removeOnGlobalLayoutListener(layoutListener);
                        } else {
                            activityRoot.viewTreeObserver
                                .removeGlobalOnLayoutListener(layoutListener);
                        }
                    }
                })
        }
    }

}

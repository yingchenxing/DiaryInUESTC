package edu.uestc.diaryinuestc.ui.me;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.preference.DialogPreference;

import android.text.TextUtils;
import android.util.AttributeSet;

import edu.uestc.diaryinuestc.R;

public class DatePreference extends DialogPreference {
    private String dateStr;

    public DatePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPositiveButtonText("设置");
        setNegativeButtonText("取消");
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(Object defaultValue) {
        setDate(getPersistedString((String) defaultValue));
    }

    /**
     * Saves the date as a string in the current data storage.
     *
     * @param text string representation of the date to save.
     */
    public void setDate(String text) {
        final boolean wasBlocking = shouldDisableDependents();

        dateStr = text;

        persistString(text);

        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }

        notifyChanged();
    }

    public String getDate() {
        return dateStr;
    }

    public static final class DateSummaryProvider implements SummaryProvider<DatePreference> {

        private static DateSummaryProvider sDateSummaryProvider;

        private DateSummaryProvider() {
        }

        public static DateSummaryProvider getInstance() {
            if (sDateSummaryProvider == null) {
                sDateSummaryProvider = new DateSummaryProvider();
            }
            return sDateSummaryProvider;
        }

        @Override
        public CharSequence provideSummary(DatePreference preference) {
            if (TextUtils.isEmpty(preference.getDate())) {
                return (preference.getContext().getString(R.string.not_set));
            } else {
                return preference.getDate();
            }
        }
    }
}

package edu.uestc.diaryinuestc;

import android.view.View;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

public class MRadioGroup {

    List<RadioButton> radios = new ArrayList<>();

    public MRadioGroup(RadioButton... radios) {
        super();
        for (RadioButton rb : radios) {
            if (rb != null) {
                rb.setOnClickListener(onClick);
                this.radios.add(rb);
            }
        }
    }

    View.OnClickListener onClick = v -> {
        clearCheck();
        RadioButton rb = (RadioButton) v;
        rb.setChecked(true);
    };

    public void clearCheck() {
        for (RadioButton rb : radios) {
            rb.setChecked(false);
        }
    }

    public void setCheck(int id) {
        if (id - 1 >= 0)
            radios.get(id - 1).setChecked(true);
    }


    public int getType() {
        int i;
        for (i = 0; i < radios.size(); i++) {
            if (radios.get(i).isChecked())
                return i + 1;
        }
        return 0;
    }
}

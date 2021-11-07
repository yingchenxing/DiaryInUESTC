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
            this.radios.add(rb);
            rb.setOnClickListener(onClick);
        }
    }

    View.OnClickListener onClick = v -> {
        clearCheck();
        RadioButton rb = (RadioButton) v;
        rb.setChecked(true);
    };

    private void clearCheck() {
        for (RadioButton rb : radios) {
            rb.setChecked(false);
        }
    }

    public int getType() {
        int i;
        for( i =0;i<radios.size();i++){
            if(radios.get(i).isChecked())
                return i+1;
        }
        return 0;
    }
}

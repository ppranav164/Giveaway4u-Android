package Model;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shopping.gway_4u.R;

import ernestoyaquello.com.verticalstepperform.Step;

public class secondtStep extends Step<String> {

    private TextView stepView;

    String subTitle;

    public secondtStep(String stepTitle , String subTitle) {
        super(stepTitle);

        this.subTitle = subTitle;

    }

    @Override
    public String getStepData() {
        return null;
    }

    @Override
    protected View createStepContentLayout() {
        // Here we generate the view that will be used by the library as the content of the step.
        // In this case we do it programmatically, but we could also do it by inflating an XML layout.
        stepView = new TextView(getContext());
        stepView.setText(subTitle);
        stepView.setTextColor(getContext().getResources().getColor(R.color.darkbl));

        onStepMarkedAsCompleted(false);

        return stepView;
    }




    @Override
    public String getStepDataAsHumanReadableString() {
        // Because the step's data is already a human-readable string, we don't need to convert it.
        // However, we return "(Empty)" if the text is empty to avoid not having any text to display.
        // This string will be displayed in the subtitle of the step whenever the step gets closed.
        String userName = getStepData();
        return  "";
    }

    @Override
    protected void onStepOpened(boolean animated) {
        // This will be called automatically whenever the step gets opened.

        Log.e("onStepOpened",String.valueOf(animated));
    }

    @Override
    protected void onStepClosed(boolean animated) {
        // This will be called automatically whenever the step gets closed.

        Log.e("onStepClosed",String.valueOf(animated));
    }


    @Override
    protected void onStepMarkedAsCompleted(boolean animated) {
        // This will be called automatically whenever the step is marked as completed.

        Log.e("onStepMarkedAsCompleted",String.valueOf(animated));

    }

    @Override
    protected void onStepMarkedAsUncompleted(boolean animated) {
        // This will be called automatically whenever the step is marked as uncompleted.

        Log.e("onStepMarkedAsUncomp",String.valueOf(animated));

    }

    @Override
    public void restoreStepData(String stepData) {
        // To restore the step after a configuration change, we restore the text of its EditText view.
        stepView.setText(stepData);
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        return null;
    }



}

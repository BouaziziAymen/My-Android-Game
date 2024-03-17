package com.evolgames.activity.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.evolgames.activity.GameActivity;
import com.evolgames.activity.ResourceManager;
import com.evolgames.gameengine.R;
import com.evolgames.helpers.ItemMetaData;
import com.evolgames.userinterface.model.ItemCategory;

import org.andengine.BuildConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EditItemDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater.
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog.
        // Pass null as the parent view because it's going in the dialog layout.
        View dialogLayout = inflater.inflate(R.layout.edit_item_dialog, null);


        TextView linkTextView = dialogLayout.findViewById(R.id.linkTextView);
        String linkText = "Or create New";
        SpannableString spannableString = new SpannableString(linkText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                ((GameActivity) requireActivity()).showCreateItemDialog();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true); // Set underline
                ds.setFakeBoldText(true); // Set bold
            }
        };
        spannableString.setSpan(clickableSpan, 0, linkText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        linkTextView.setText(spannableString);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        linkTextView.setHighlightColor(Color.TRANSPARENT); // Remove highlight color

        CheckBox checkBox = dialogLayout.findViewById(R.id.continueCheckbox);


        // Set listener for checkbox state change


        Button shareButton = dialogLayout.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(v -> ((GameActivity) requireActivity()).sendEmailWithAttachment(""));


        builder.setView(dialogLayout)
                // Add action buttons
                .setPositiveButton(R.string.go_edit, (d, id) -> {
                })
                .setNegativeButton(R.string.cancel, (d, id) -> {
                    EditItemDialog.this.getDialog().cancel();
                    ((GameActivity) requireActivity()).installMenuUi();
                });

        AutoCompleteTextView itemNameAutoComplete = dialogLayout.findViewById(R.id.itemNameAutoComplete);
        Map<ItemCategory, List<ItemMetaData>> map = ResourceManager.getInstance().getItemsMap();
        List<ItemMetaData> items = map.values().stream().flatMap(Collection::stream).filter(e -> BuildConfig.DEBUG || e.isUserCreated()).collect(Collectors.toList());
        String[] userItemNames = items.stream().map(ItemMetaData::getName).toArray(String[]::new);
        ArrayAdapter<String> userItemNamesAdapter = new ArrayAdapter<>(this.requireActivity(), android.R.layout.simple_dropdown_item_1line, userItemNames);
        itemNameAutoComplete.setAdapter(userItemNamesAdapter);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Enable or disable EditText based on checkbox state
            itemNameAutoComplete.setEnabled(!isChecked);
            if (isChecked) {
                itemNameAutoComplete.setText("Latest Item");
            } else {
                itemNameAutoComplete.setText("");
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            // Perform input validation
            String itemNameText = itemNameAutoComplete.getText().toString().trim();
            boolean invalidName = !checkBox.isChecked() && Arrays.stream(userItemNames).map(String::toLowerCase).noneMatch(e -> e.equals(itemNameText.toLowerCase()));
            boolean isNameEmpty = itemNameText.isEmpty();
            if (invalidName || isNameEmpty) {
                // Show error message if any field is empty
                List<String> list = new ArrayList<>();
                if (isNameEmpty) {
                    list.add("Choose a name.");
                } else {
                    list.add("Item doesn't exist.");
                }
                String msg = String.join(" ", list);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            } else {
                // Valid inputs, dismiss the dialog
                alertDialog.dismiss();
                ((GameActivity) requireActivity()).getUiController().onProceedToEdit(itemNameText);
            }
        });


        return alertDialog;
    }

}
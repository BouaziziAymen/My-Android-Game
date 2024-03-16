package com.evolgames.activity.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.evolgames.activity.GameActivity;
import com.evolgames.activity.ResourceManager;
import com.evolgames.gameengine.R;
import com.evolgames.helpers.ItemMetaData;
import com.evolgames.userinterface.model.ItemCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CreateItemDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater.
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog.
        // Pass null as the parent view because it's going in the dialog layout.
        View dialogLayout = inflater.inflate(R.layout.new_item_dialog, null);



        AutoCompleteTextView itemTypeAutoComplete = dialogLayout.findViewById(R.id.itemTypeAutoComplete);
        String[] types = Arrays.stream(ItemCategory.values()).map(Enum::name).toArray(String[]::new);
        ArrayAdapter<String> itemTypesAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, types);
        itemTypeAutoComplete.setAdapter(itemTypesAdapter);

        AutoCompleteTextView itemTemplateAutoComplete = dialogLayout.findViewById(R.id.itemTemplateAutoComplete);
        Map<ItemCategory, List<ItemMetaData>> map = ResourceManager.getInstance().getItemsMap();
        List<ItemMetaData> items = map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        String[] itemNames = items.stream().map(ItemMetaData::getName).toArray(String[]::new);
        ArrayAdapter<String> userItemNamesAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, itemNames);
        itemTemplateAutoComplete.setAdapter(userItemNamesAdapter);




        TextView linkTextView = dialogLayout.findViewById(R.id.linkTextView);
        String linkText = "Or edit existing";
        SpannableString spannableString = new SpannableString(linkText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                ((GameActivity) getActivity()).showEditItemDialog();
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


        EditText itemNameEditText = dialogLayout.findViewById(R.id.itemName);
        builder.setView(dialogLayout)
                // Add action buttons
                .setPositiveButton(R.string.go_create,(d,id)->{})
                .setNegativeButton(R.string.cancel, (d, id) -> {
                    CreateItemDialog.this.getDialog().cancel();
                    ((GameActivity) getActivity()).installMenuUi();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);


        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            // Perform input validation
            String itemTypeText = itemTypeAutoComplete.getText().toString().trim();
            String itemNameText = itemNameEditText.getText().toString();
           boolean isTypeInvalid = (itemTypeText.isEmpty()|| Arrays.stream(ItemCategory.values()).map(Enum::name).noneMatch(e-> e.equals(itemTypeText)));

           boolean nameExists = Arrays.stream(itemNames).map(String::toLowerCase).anyMatch(e->e.equals(itemNameText.toLowerCase()));
            boolean isNameEmpty = itemNameText.isEmpty();
            if (isTypeInvalid||isNameEmpty||nameExists) {
                // Show error message if any field is empty
                List<String> list = new ArrayList<>();
                if(isNameEmpty){
                    list.add("Choose a name.");
                }
                else if(nameExists){
                    list.add("Name already exists.");
                }
                if(isTypeInvalid){list.add("Choose a valid type");}
                String msg = String.join(" ", list);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            } else {
                // Valid inputs, dismiss the dialog
                alertDialog.dismiss();
                // Or perform other actions here
            }
        });
        return alertDialog;
    }

}

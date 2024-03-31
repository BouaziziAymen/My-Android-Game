package com.evolgames.activity.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.evolgames.activity.GameActivity;
import com.evolgames.activity.ResourceManager;
import com.evolgames.gameengine.BuildConfig;
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

    private AutoCompleteTextView itemTemplateAutoComplete;
    private String[] itemNames;

    private void setupTemplatesAutoComplete(ItemCategory category) {
        Map<ItemCategory, List<ItemMetaData>> map = ResourceManager.getInstance().getItemsMap();
        List<ItemMetaData> items = map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        itemNames = items.stream().filter(e -> e.getItemCategory() == category).map(ItemMetaData::getName).toArray(String[]::new);
        ArrayAdapter<String> userItemNamesAdapter = new ArrayAdapter<>(this.requireActivity(), android.R.layout.simple_dropdown_item_1line, itemNames);
        itemTemplateAutoComplete.setAdapter(userItemNamesAdapter);
        itemTemplateAutoComplete.setEnabled(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater.
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog.
        // Pass null as the parent view because it's going in the dialog layout.
        View dialogLayout = inflater.inflate(R.layout.new_item_dialog, null);
        itemNames = new String[0];


        AutoCompleteTextView itemTypeAutoComplete = dialogLayout.findViewById(R.id.itemTypeAutoComplete);
        String[] types = Arrays.stream(ItemCategory.values()).map(Enum::name).toArray(String[]::new);
        ArrayAdapter<String> itemTypesAdapter = new ArrayAdapter<>(this.requireActivity(), android.R.layout.simple_dropdown_item_1line, types);
        itemTypeAutoComplete.setAdapter(itemTypesAdapter);
        itemTypeAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String type = (String) parent.getItemAtPosition(position);
            ItemCategory category = ItemCategory.fromName(type);
            setupTemplatesAutoComplete(category);
        });

        itemTemplateAutoComplete = dialogLayout.findViewById(R.id.itemTemplateAutoComplete);
        itemTemplateAutoComplete.setEnabled(false);
        itemTypeAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called before the text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemTemplateAutoComplete.setEnabled(false);
                itemTemplateAutoComplete.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called after the text is changed
                // You can perform your desired action here
                // For example, filter the suggestions based on the new text
            }
        });

        TextView linkTextView = dialogLayout.findViewById(R.id.linkTextView);
        String linkText = "Or Edit Existing";
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
        itemNameEditText.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            StringBuilder filteredStringBuilder = new StringBuilder();
            for (int i = start; i < end; i++) {
                char currentChar = source.charAt(i);
                if (Character.isLetterOrDigit(currentChar) || (BuildConfig.DEBUG && currentChar == '#') || currentChar == ' ' || currentChar == '-' || currentChar == '.' || currentChar == '_') {
                    filteredStringBuilder.append(currentChar);
                }
            }
            return filteredStringBuilder.toString();
        }});


        builder.setView(dialogLayout)
                // Add action buttons
                .setPositiveButton(R.string.go_create, (d, id) -> {
                })
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
            String itemNameText = itemNameEditText.getText().toString().trim();
            String itemTemplateText = itemTemplateAutoComplete.getText().toString().trim();

            boolean isTypeInvalid = (itemTypeText.isEmpty() || Arrays.stream(ItemCategory.values()).map(Enum::name).noneMatch(e -> e.equals(itemTypeText)));
            boolean isTemplateInvalid = !itemTemplateText.isEmpty() && Arrays.stream(itemNames).map(String::toLowerCase).noneMatch(e -> e.equals(itemTemplateText.toLowerCase()));
            boolean nameExists = Arrays.stream(itemNames).map(String::toLowerCase).anyMatch(e -> e.equals(itemNameText.toLowerCase()));
            boolean isNameEmpty = itemNameText.isEmpty();
            if (isTypeInvalid || isNameEmpty || nameExists || isTemplateInvalid) {
                // Show error message if any field is empty
                List<String> list = new ArrayList<>();
                if (isNameEmpty) {
                    list.add("Choose a name.");
                } else if (nameExists) {
                    list.add("Name already exists.");
                }
                if (isTypeInvalid) {
                    list.add("Choose a valid type");
                }
                if (isTemplateInvalid) {
                    list.add("Choose a valid template.");
                }
                String msg = String.join(" ", list);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            } else {
                // Valid inputs, dismiss the dialog
                alertDialog.dismiss();
                ((GameActivity) requireActivity()).getUiController().onProceedToCreate(itemNameText, itemTypeText, itemTemplateText);
                // Or perform other actions here
            }
        });
        return alertDialog;
    }

}

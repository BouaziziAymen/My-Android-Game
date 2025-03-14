package com.evolgames.dollmutilationgame.activity.components;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.evolgames.dollmutilationgame.activity.GameActivity;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.helpers.ItemMetaData;
import com.evolgames.dollmutilationgame.BuildConfig;
import com.evolgames.dollmutilationgame.R;
import com.evolgames.dollmutilationgame.userinterface.model.ItemCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CreateItemDialog extends DialogFragment {

    private AutoCompleteTextView itemTemplateAutoComplete;
    private Item[] items;
    private String selectedType;
    private String selectedTemplate;

    private void setupTemplatesAutoComplete(ItemCategory category) {
        Map<ItemCategory, List<ItemMetaData>> map = ResourceManager.getInstance().getItemsMap();
        List<ItemMetaData> items = map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());

        this.items = items.stream().filter(e -> e.getItemCategory() == category).map(e->{
            int itemNameTranslationId = ResourceManager.getInstance().getTranslatedItemStringId(e.getName());
            String itemDisplayedName;
            if (itemNameTranslationId != -1) {
                itemDisplayedName = requireContext().getString(itemNameTranslationId);
            } else {
                itemDisplayedName = e.getName();
            }
            return new Item(itemDisplayedName,e.getName());
        }).toArray(Item[]::new);
        ArrayAdapter<Item> userItemNamesAdapter = new ArrayAdapter<>(this.requireActivity(), android.R.layout.simple_dropdown_item_1line, this.items);
        itemTemplateAutoComplete.setAdapter(userItemNamesAdapter);
        itemTemplateAutoComplete.setEnabled(true);

        itemTemplateAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            Item item = userItemNamesAdapter.getItem(position);
            assert item != null;
            this.selectedTemplate = item.getTitle();
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater.
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog.
        // Pass null as the parent view because it's going in the dialog layout.
        View dialogLayout = inflater.inflate(R.layout.new_item_dialog, null);

        AutoCompleteTextView itemTypeAutoComplete = dialogLayout.findViewById(R.id.itemTypeAutoComplete);
        Item[] types = Arrays.stream(ItemCategory.values()).filter(e->!e.nonCreatable||BuildConfig.DEBUG).map(e -> new Item(requireContext().getString(e.nameId), e.name())).toArray(Item[]::new);
        ArrayAdapter<Item> itemTypesAdapter = new ArrayAdapter<>(this.requireActivity(), android.R.layout.simple_dropdown_item_1line, types);

        itemTypeAutoComplete.setAdapter(itemTypesAdapter);


        itemTypeAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            Item item = itemTypesAdapter.getItem(position);
            assert item != null;
            ItemCategory category = ItemCategory.fromName(item.getTitle());
            setupTemplatesAutoComplete(category);
            this.selectedType = category.name();
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
        String linkText = requireActivity().getString(R.string.edit_existing);
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
            String itemNameText = itemNameEditText.getText().toString().trim();
            String typeText = itemTypeAutoComplete.getText().toString().trim();

            boolean isTypeInvalid = typeText.isEmpty() || Arrays.stream(ItemCategory.values()).map(Enum::name).noneMatch(e -> e.equals(selectedType));
            boolean isTemplateInvalid = selectedTemplate!=null&&!selectedTemplate.isEmpty() && Arrays.stream(items).map(Item::getTitle).noneMatch(e -> e.equals(selectedTemplate));
            boolean nameExists = items!=null&&Arrays.stream(items).map(Item::getTitle).anyMatch(e -> e.equals(itemNameText));
            boolean isNameEmpty = itemNameText.isEmpty();
            if (isTypeInvalid || isNameEmpty || nameExists || isTemplateInvalid) {
                // Show error message if any field is empty
                List<String> list = new ArrayList<>();
                if (isNameEmpty) {
                    list.add(getString(R.string.choose_a_name_validation));
                } else if (nameExists) {
                    list.add(getString(R.string.name_already_exists_validation));
                }
                if (isTypeInvalid) {
                    list.add(getString(R.string.choose_a_valid_type_validation));
                }
                if (isTemplateInvalid) {
                    list.add(getString(R.string.choose_a_valid_template_validation));
                }
                String msg = String.join(" ", list);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            } else {
                // Valid inputs, dismiss the dialog
                alertDialog.dismiss();
                ((GameActivity) requireActivity()).getUiController().onProceedToCreate(itemNameText, this.selectedType, this.selectedTemplate);
                // Or perform other actions here
            }
        });



        alertDialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                CreateItemDialog.this.getDialog().cancel();
                ((GameActivity) getActivity()).installMenuUi();
                return true;
            }
            return false;
        });

        return alertDialog;
    }


    }

package com.example.kitchencompanion;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Shopping tab
public class Tab3 extends Fragment {

    private ListView shopListView;
    private View view;
    private ShopListAdapter adapter;
    private List<ShopListItem> shopList;
    private Tab2 tab2;
    // Reference to the set of all FoodType objects in the app.
    // Maps FoodType.getID() to FoodType
    HashMap<Integer, FoodType> foodDictionary;

    // Used in FoodType selector
    private ArrayList<FoodType> foodSelectorList;
    private HashMap<String, List<ShopListItem>> ShopListPresets;
    private FoodTypeSelectorAdapter foodSelectorListAdapter;
    private Dialog foodTypeSelector;
    // THIS IS WHERE YOU ACCESS THE FOOD SELECTED BY THE FOODTYPE SELECTOR
    private FoodType selectedFood;
    private String selectedKey = "";

    private boolean shoppingMode;
    public Tab3(HashMap<Integer, FoodType> foodDictionary, Tab2 tab2){
        this.foodDictionary = foodDictionary;

        shopList = new ArrayList<ShopListItem>();
        ShopListPresets = new HashMap<String, List<ShopListItem>>();
        shopList.add(new ShopListItem(foodDictionary.get(1),5));
        shopList.add(new ShopListItem(foodDictionary.get(2),3));
        shopList.add(new ShopListItem(foodDictionary.get(3),1));

        ArrayList<ShopListItem> test = new ArrayList<ShopListItem>();
        test.add(new ShopListItem(foodDictionary.get(2),10));
        ShopListPresets.put("10 Oranges", test);

        ArrayList<ShopListItem> test2 = new ArrayList<ShopListItem>();
        test2.add(new ShopListItem(foodDictionary.get(16),4));
        test2.add(new ShopListItem(foodDictionary.get(24),3));
        test2.add(new ShopListItem(foodDictionary.get(39),2));
        test2.add(new ShopListItem(foodDictionary.get(55),1));
        test2.add(new ShopListItem(foodDictionary.get(63),2));
        test2.add(new ShopListItem(foodDictionary.get(87),1));
        test2.add(new ShopListItem(foodDictionary.get(119),2));
        ShopListPresets.put("Favorite Foods", test2);

        ArrayList<ShopListItem> test3 = new ArrayList<ShopListItem>();
        test3.add(new ShopListItem(foodDictionary.get(4),2));
        test3.add(new ShopListItem(foodDictionary.get(8),4));
        test3.add(new ShopListItem(foodDictionary.get(16),3));
        test3.add(new ShopListItem(foodDictionary.get(20),12));
        test3.add(new ShopListItem(foodDictionary.get(54),2));
        test3.add(new ShopListItem(foodDictionary.get(67),7));
        test3.add(new ShopListItem(foodDictionary.get(53),4));
        test3.add(new ShopListItem(foodDictionary.get(75),2));
        test3.add(new ShopListItem(foodDictionary.get(48),1));
        ShopListPresets.put("New Shopping List", test3);

        shoppingMode = false;
        this.tab2 = tab2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab3, container, false);

        shopListView = view.findViewById(R.id.shopListView);
        adapter = new ShopListAdapter(getContext(), shopList, shoppingMode);
        shopListView.setAdapter(adapter);
        Button submitShop = view.findViewById(R.id.submitListButton);
        Button shopMode = view.findViewById(R.id.buyModeButton);
        FloatingActionButton addButton = view.findViewById(R.id.addShopListFoodButton);
        FloatingActionButton addPreset = view.findViewById(R.id.addPresetShopListButton);
        FloatingActionButton pushPreset = view.findViewById(R.id.pushPresetShopListButton);
        if (shoppingMode) {
            shopMode.setText("Open Edit Mode");
            submitShop.setVisibility(View.VISIBLE);
        } else {
            shopMode.setText("Open Shopping Mode");
            submitShop.setVisibility(View.GONE);
        }
        shopMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!adapter.anySelected()) {
                    shoppingMode = !shoppingMode;
                    if (shoppingMode) {
                        shopMode.setText("Open Edit Mode");
                        submitShop.setVisibility(View.VISIBLE);
                    } else {
                        shopMode.setText("Open Shopping Mode");
                        submitShop.setVisibility(View.GONE);
                    }
                    adapter.changeMode();
                    adapter.notifyDataSetChanged();
                }
                else{
                    new AlertDialog.Builder(getContext())
                            .setTitle("Warning")
                            .setMessage("Selected items must be bought using the Buy button before returning to edit mode.")
                            .setPositiveButton(android.R.string.ok, null)
                            .setIcon(R.drawable.warning)
                            .show();
                }
            }
        });
        submitShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ShopListItem> temp = adapter.getSelectedItems();
                if (tab2.getAdapter() == null) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Error")
                            .setMessage("The pantry has not been loaded. Open the pantry tab and try again.")
                            .setPositiveButton(android.R.string.ok, null)
                            .setIcon(R.drawable.warning)
                            .show();
                }
                for(ShopListItem item: temp){
                    if (tab2.addItems(item.getFood(), item.getAmount())) {
                        adapter.removeSelectedItems(temp);
                    }
                }
            }
        });
        /*CODE FOR ADDING ITEMS TO THE SHOPPING LIST*/
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.push_shoplist_dialog, null);

                // Find and set up the views inside the dialog layout
                // ...
                Button cancelButton = dialogView.findViewById(R.id.shopListAddFoodCancelButton);
                Button addButton = dialogView.findViewById(R.id.shopListAddFoodCreateButton);
                EditText amount = dialogView.findViewById(R.id.shopListAddFoodCountInput);

                /*
                    ########################################################
                    ######## FOODTYPE SELECTOR CODE, DO NOT CHANGE! ########
                    ########################################################
                 */
                final TextView foodTypeSelector = dialogView.findViewById(R.id.shopListAddFoodFoodTypeSelector);

                // Code adapted from: https://www.youtube.com/watch?v=5iIXg4-Iw3U
                // This initialization must stay here to ensure it remains up to date with foodDictionary
                foodSelectorList = new ArrayList<FoodType>(foodDictionary.values());
                foodTypeSelector.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Tab3.this.foodTypeSelector = new Dialog(requireContext());
                        Tab3.this.foodTypeSelector.setContentView(R.layout.food_type_selector);
                        Tab3.this.foodTypeSelector.getWindow().setLayout(650, 800);
                        Tab3.this.foodTypeSelector.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        Tab3.this.foodTypeSelector.show();

                        EditText foodSelectorSearchBar = Tab3.this.foodTypeSelector.findViewById(R.id.foodSelectorSearchBar);
                        ListView foodSelectorListView = Tab3.this.foodTypeSelector.findViewById(R.id.foodSelectorListView);

                        foodSelectorListAdapter = new FoodTypeSelectorAdapter(getContext(), foodSelectorList);
                        foodSelectorListView.setAdapter(foodSelectorListAdapter);

                        foodSelectorSearchBar.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                foodSelectorListAdapter.getFilter().filter(s);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

                        foodSelectorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // Pass the selected food
                                selectedFood = foodSelectorListAdapter.getItem(position);
                                // Update visuals
                                foodTypeSelector.setText(selectedFood.getItemName());
                                // Dismiss dialog
                                Tab3.this.foodTypeSelector.dismiss();
                            }
                        });

                        ImageButton foodSelectorExitButton = Tab3.this.foodTypeSelector.findViewById(R.id.foodSelectorCloseButton);
                        foodSelectorExitButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Tab3.this.foodTypeSelector.dismiss();
                            }
                        });

                        // FOODTYPE SELECTOR ADD BUTTON
                        Button foodSelectorAddButton = Tab3.this.foodTypeSelector.findViewById(R.id.foodSelectorAddButton);
                        foodSelectorAddButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Dialog dialog = new Dialog(requireContext());
                                dialog.setContentView(R.layout.add_foodtype_dialog);

                                Button addFoodTypeCancelButton = dialog.findViewById(R.id.addFoodTypeCancelButton);
                                addFoodTypeCancelButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                Button addFoodTypeCreateButton = dialog.findViewById(R.id.addFoodTypeCreateButton);
                                addFoodTypeCreateButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Elements
                                        EditText addFoodNameEditText = dialog.findViewById(R.id.addFoodNameEditText);
                                        EditText addFoodExpirationEditText = dialog.findViewById(R.id.addFoodExpirationEditText);
                                        Spinner addFoodGroupSpinner = dialog.findViewById(R.id.addFoodGroupSpinner);

                                        // Data
                                        String name = addFoodNameEditText.getText().toString();
                                        String expirationPeriodString = addFoodExpirationEditText.getText().toString().trim();
                                        Enums.FoodGroup foodGroup = Enums.FoodGroup.values()[addFoodGroupSpinner.getSelectedItemPosition()];

                                        boolean isValid = true;

                                        // Ensure name field is filled in
                                        if (name.isEmpty()) {
                                            addFoodNameEditText.setError("Please enter a name");
                                            isValid = false;
                                        } else {
                                            addFoodNameEditText.setError(null);
                                        }

                                        // Ensure expiration date field is filled in
                                        if (expirationPeriodString.isEmpty()) {
                                            addFoodExpirationEditText.setError("Please enter an expiration period");
                                            isValid = false;
                                        } else {
                                            addFoodExpirationEditText.setError(null);
                                        }

                                        if (isValid) {
                                            int expirationPeriod = Integer.parseInt(expirationPeriodString);
                                            FoodType newFoodType = new FoodType(name, foodGroup, expirationPeriod);

                                            boolean exists = false;
                                            for (Map.Entry<Integer, FoodType> entry : foodDictionary.entrySet()) {
                                                FoodType existingFoodType = entry.getValue();
                                                if (existingFoodType.compareTo(newFoodType) == 0) {
                                                    exists = true;
                                                    addFoodNameEditText.setError("This item already exists");
                                                    break;
                                                }
                                            }

                                            if (!exists) {
                                                Tab3.this.foodDictionary.put(newFoodType.getID(), newFoodType);
                                                Tab3.this.foodSelectorList.add(newFoodType);

                                                Tab3.this.foodSelectorListAdapter.notifyDataSetChanged();

                                                dialog.dismiss();
                                            }
                                        }
                                    }
                                });

                                dialog.show();
                            }
                        });
                    }
                });
                /*
                    ############################################
                    ######## FOODTYPE SELECTOR CODE END ########
                    ############################################
                 */

                builder.setView(dialogView);

                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                // Set the dialog to occupy approximately 75% of the screen
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = (int) (displayMetrics.widthPixels * 0.75);
                int height = (int) (displayMetrics.heightPixels * 0.75);
                dialog.getWindow().setLayout(width, height);

                // Dim the background
                dialog.getWindow().setDimAmount(0.5f);

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isValid = true;
                        String countString = amount.getText().toString();

                        if (selectedFood == null) {
                            foodTypeSelector.setError("Please select a food");
                            isValid = false;
                        } else {
                            foodTypeSelector.setError(null);
                        }

                        if (countString.isEmpty()) {
                            amount.setError("Please enter a number of items");
                            isValid = false;
                        } else {
                            amount.setError(null);
                        }

                        if (isValid) {
                            adapter.addShopListItem(new ShopListItem(selectedFood, Integer.parseInt(amount.getText().toString())));
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
        /*END OF CODE FOR THE ADD BUTTON*/
        /*Code for adding presets to the library of presets*/
        addPreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_shoplist_preset_dialog, null);
                // Find and set up the views inside the dialog layout
                // ...
                EditText listName = dialogView.findViewById(R.id.shopListAddPresetNameInput);
                Button presetAdd = dialogView.findViewById(R.id.shopListAddPresetCreateButton);
                Button presetCancel = dialogView.findViewById(R.id.shopListAddPresetCancelButton);
                ListView addPresetList = dialogView.findViewById(R.id.shopListAddPresetListView);
                Button presetAddFood = dialogView.findViewById(R.id.shopListAddPresetAddFoodButton);
                List<ShopListItem> presetList = new ArrayList<ShopListItem>();
                builder.setView(dialogView);

                ShopListAdapter presetAddAdapter = new ShopListAdapter(getContext(), presetList, false);
                addPresetList.setAdapter(presetAddAdapter);

                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                // Set the dialog to occupy approximately 75% of the screen
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = (int) (displayMetrics.widthPixels * 0.75);
                int height = (int) (displayMetrics.heightPixels * 0.75);
                dialog.getWindow().setLayout(width, height);

                // Dim the background
                dialog.getWindow().setDimAmount(0.5f);

                presetAddFood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        View addDialogView = LayoutInflater.from(getContext()).inflate(R.layout.push_shoplist_dialog, null);

                        // Find and set up the views inside the dialog layout
                        // ...
                        Button cancelButton = addDialogView.findViewById(R.id.shopListAddFoodCancelButton);
                        Button addButton = addDialogView.findViewById(R.id.shopListAddFoodCreateButton);
                        EditText amount = addDialogView.findViewById(R.id.shopListAddFoodCountInput);

                /*
                    ########################################################
                    ######## FOODTYPE SELECTOR CODE, DO NOT CHANGE! ########
                    ########################################################
                 */
                        final TextView foodTypeSelector = addDialogView.findViewById(R.id.shopListAddFoodFoodTypeSelector);

                        // Code adapted from: https://www.youtube.com/watch?v=5iIXg4-Iw3U
                        // This initialization must stay here to ensure it remains up to date with foodDictionary
                        foodSelectorList = new ArrayList<FoodType>(foodDictionary.values());
                        foodTypeSelector.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Tab3.this.foodTypeSelector = new Dialog(requireContext());
                                Tab3.this.foodTypeSelector.setContentView(R.layout.food_type_selector);
                                Tab3.this.foodTypeSelector.getWindow().setLayout(650, 800);
                                Tab3.this.foodTypeSelector.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                Tab3.this.foodTypeSelector.show();

                                EditText foodSelectorSearchBar = Tab3.this.foodTypeSelector.findViewById(R.id.foodSelectorSearchBar);
                                ListView foodSelectorListView = Tab3.this.foodTypeSelector.findViewById(R.id.foodSelectorListView);

                                foodSelectorListAdapter = new FoodTypeSelectorAdapter(getContext(), foodSelectorList);
                                foodSelectorListView.setAdapter(foodSelectorListAdapter);

                                foodSelectorSearchBar.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        foodSelectorListAdapter.getFilter().filter(s);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                                foodSelectorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        // Pass the selected food
                                        selectedFood = foodSelectorListAdapter.getItem(position);
                                        // Update visuals
                                        foodTypeSelector.setText(selectedFood.getItemName());
                                        // Dismiss dialog
                                        Tab3.this.foodTypeSelector.dismiss();
                                    }
                                });

                                ImageButton foodSelectorExitButton = Tab3.this.foodTypeSelector.findViewById(R.id.foodSelectorCloseButton);
                                foodSelectorExitButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Tab3.this.foodTypeSelector.dismiss();
                                    }
                                });

                                // FOODTYPE SELECTOR ADD BUTTON
                                Button foodSelectorAddButton = Tab3.this.foodTypeSelector.findViewById(R.id.foodSelectorAddButton);
                                foodSelectorAddButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Dialog dialog = new Dialog(requireContext());
                                        dialog.setContentView(R.layout.add_foodtype_dialog);

                                        Button addFoodTypeCancelButton = dialog.findViewById(R.id.addFoodTypeCancelButton);
                                        addFoodTypeCancelButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        });

                                        Button addFoodTypeCreateButton = dialog.findViewById(R.id.addFoodTypeCreateButton);
                                        addFoodTypeCreateButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Elements
                                                EditText addFoodNameEditText = dialog.findViewById(R.id.addFoodNameEditText);
                                                EditText addFoodExpirationEditText = dialog.findViewById(R.id.addFoodExpirationEditText);
                                                Spinner addFoodGroupSpinner = dialog.findViewById(R.id.addFoodGroupSpinner);

                                                // Data
                                                String name = addFoodNameEditText.getText().toString();
                                                String expirationPeriodString = addFoodExpirationEditText.getText().toString().trim();
                                                Enums.FoodGroup foodGroup = Enums.FoodGroup.values()[addFoodGroupSpinner.getSelectedItemPosition()];

                                                boolean isValid = true;

                                                // Ensure name field is filled in
                                                if (name.isEmpty()) {
                                                    addFoodNameEditText.setError("Please enter a name");
                                                    isValid = false;
                                                } else {
                                                    addFoodNameEditText.setError(null);
                                                }

                                                // Ensure expiration date field is filled in
                                                if (expirationPeriodString.isEmpty()) {
                                                    addFoodExpirationEditText.setError("Please enter an expiration period");
                                                    isValid = false;
                                                } else {
                                                    addFoodExpirationEditText.setError(null);
                                                }

                                                if (isValid) {
                                                    int expirationPeriod = Integer.parseInt(expirationPeriodString);
                                                    FoodType newFoodType = new FoodType(name, foodGroup, expirationPeriod);

                                                    boolean exists = false;
                                                    for (Map.Entry<Integer, FoodType> entry : foodDictionary.entrySet()) {
                                                        FoodType existingFoodType = entry.getValue();
                                                        if (existingFoodType.compareTo(newFoodType) == 0) {
                                                            exists = true;
                                                            addFoodNameEditText.setError("This item already exists");
                                                            break;
                                                        }
                                                    }

                                                    if (!exists) {
                                                        Tab3.this.foodDictionary.put(newFoodType.getID(), newFoodType);
                                                        Tab3.this.foodSelectorList.add(newFoodType);

                                                        Tab3.this.foodSelectorListAdapter.notifyDataSetChanged();

                                                        dialog.dismiss();
                                                    }
                                                }
                                            }
                                        });

                                        dialog.show();
                                    }
                                });
                            }
                        });
                /*
                    ############################################
                    ######## FOODTYPE SELECTOR CODE END ########
                    ############################################
                 */

                        builder.setView(addDialogView);

                        AlertDialog dialog = builder.create();
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                        // Set the dialog to occupy approximately 75% of the screen
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int width = (int) (displayMetrics.widthPixels * 0.75);
                        int height = (int) (displayMetrics.heightPixels * 0.75);
                        dialog.getWindow().setLayout(width, height);

                        // Dim the background
                        dialog.getWindow().setDimAmount(0.5f);

                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        addButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean isValid = true;
                                String countString = amount.getText().toString();

                                if (selectedFood == null) {
                                    foodTypeSelector.setError("Please select a food");
                                    isValid = false;
                                } else {
                                    foodTypeSelector.setError(null);
                                }

                                if (countString.isEmpty()) {
                                    amount.setError("Please enter a number of items");
                                    isValid = false;
                                } else {
                                    amount.setError(null);
                                }

                                if (isValid) {
                                    presetAddAdapter.addShopListItem(new ShopListItem(selectedFood, Integer.parseInt(amount.getText().toString())));
                                    dialog.dismiss();
                                }
                            }
                        });
                        dialog.show();
                    }
                });


                presetAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isValid = true;
                        String name = listName.getText().toString();

                        if (name.isEmpty()) {
                            listName.setError("Please enter a name");
                            isValid = false;
                        } else {
                            listName.setError(null);
                        }

                        if (isValid) {
                            ArrayList<ShopListItem> temp = new ArrayList<ShopListItem>();
                            for (ShopListItem i : presetList) {
                                temp.add(new ShopListItem(i.getFood(), i.getAmount()));
                            }
                            ShopListPresets.put(listName.getText().toString(), temp);
                            dialog.dismiss();
                        }
                    }
                });
                presetCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        /*Code for spinner adapted from https://developer.android.com/develop/ui/views/components/spinner#java*/
        pushPreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.shoplist_preset_dialog, null);

                // Find and set up the views inside the dialog layout
                // ...
                Button presetCancel = dialogView.findViewById(R.id.shopListPushPresetCancelButton);
                Button presetPush = dialogView.findViewById(R.id.shopListPushPresetCreateButton);
                Spinner presetList = dialogView.findViewById(R.id.shopListPushPresetSpinner);
                ListView presetPushView = dialogView.findViewById(R.id.shopListPushPresetList);

                ShopListPushPresetAdapter presetAdapter = new ShopListPushPresetAdapter(getContext());

                List<String> presetListKeys= new ArrayList<String>(ShopListPresets.keySet());
                ArrayAdapter<String> shopListAdapter = new ArrayAdapter<>(getContext(), R.layout.shoplist_spinner, presetListKeys);

                presetList.setAdapter(shopListAdapter);
                presetPushView.setAdapter(presetAdapter);

                builder.setView(dialogView);

                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                // Set the dialog to occupy approximately 75% of the screen
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = (int) (displayMetrics.widthPixels * 0.75);
                int height = (int) (displayMetrics.heightPixels * 0.75);
                dialog.getWindow().setLayout(width, height);

                // Dim the background
                dialog.getWindow().setDimAmount(0.5f);

                presetList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedKey = parent.getItemAtPosition(position).toString();
                        presetAdapter.changeList(ShopListPresets.get(selectedKey));
                        presetAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        selectedKey = "";
                    }
                });
                presetPush.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!selectedKey.isEmpty()) {
                            adapter.addShopListItemBatch(ShopListPresets.get(selectedKey));
                            dialog.dismiss();
                        }
                    }
                });

                presetCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return view;
    }

    public ShopListAdapter getAdapter() {
        return adapter;
    }
}
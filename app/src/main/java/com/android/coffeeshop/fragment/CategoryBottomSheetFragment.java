package com.android.coffeeshop.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.coffeeshop.R;
import com.android.coffeeshop.adapter.CategoryEmployeeAdapter;
import com.android.coffeeshop.adapter.ProductEmployeeAdapter;
import com.android.coffeeshop.entity.Category;
import com.android.coffeeshop.viewmodel.CategoryViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryBottomSheetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryBottomSheetFragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<Category> categories;
    BottomSheetDialog dialog;
    BottomSheetBehavior<View> bottomSheetBehavior;
    View rootView;
    TextView closeBottomSheet;
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    ProductEmployeeAdapter productAdapter;
    CategoryEmployeeAdapter categoryAdapter;
    CategoryViewModel categoryViewModel;

    public CategoryBottomSheetFragment(List<Category> categories) {
        // Required empty public constructor
        this.categories = categories;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryBottomSheetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryBottomSheetFragment newInstance(String param1, String param2, List<Category> categories) {
        CategoryBottomSheetFragment fragment = new CategoryBottomSheetFragment(categories);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.color.overlay);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_category_bottom_sheet, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        initData(view);

        addEvents();
        observeCategories();

        // Set min height to parent view
        CoordinatorLayout bottomSheetLayout = dialog.findViewById(R.id.categoryBottomSheetLayout);

        if (bottomSheetLayout != null) {
            // Set a temporary height for the BottomSheet
            View parentView = (View) view.getParent();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenHeight = displayMetrics.heightPixels;
            parentView.getLayoutParams().height = screenHeight / 2;
            parentView.requestLayout();
        }
    }

    private void initData(View view) {
        closeBottomSheet = (TextView) view.findViewById(R.id.closeBottomSheet);

        recyclerView = view.findViewById(R.id.categoryListView);
        recyclerView.hasFixedSize();
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        categoryViewModel = new CategoryViewModel(requireActivity().getApplication());
    }

    private void addEvents() {
        closeBottomSheetEvent();
    }

    private void closeBottomSheetEvent() {
        closeBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void observeCategories() {
        categoryViewModel.getCategorieList().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categoryList) {
                categories = new ArrayList<>(categoryList);
                renderCategories();
            }
        });
    }

    private void renderCategories() {
        categoryAdapter = new CategoryEmployeeAdapter(categories, 0);
        clickOnCategoryItem();
        recyclerView.setAdapter(categoryAdapter);


        // Update the height of the BottomSheet after data has been loaded
        View parentView = (View) rootView.getParent();
        parentView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        parentView.requestLayout();
    }

    private void clickOnCategoryItem() {
        categoryAdapter.setOnItemClickListener(new CategoryEmployeeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                categoryItemClickListener.onCategoryItemClick(category.getCategoryId());
                dismiss();
            }
        });

    }

    public interface OnCategoryItemClickListener {
        void onCategoryItemClick(int categoryId);
    }

    private OnCategoryItemClickListener categoryItemClickListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            categoryItemClickListener = (OnCategoryItemClickListener) getParentFragment();
            if (categoryItemClickListener == null) {
                throw new ClassCastException("Parent fragment must implement OnCategoryItemClickListener");
            }
//            categoryItemClickListener = (OnCategoryItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Parent fragment must implement OnItemClickListener");
        }
    }
}